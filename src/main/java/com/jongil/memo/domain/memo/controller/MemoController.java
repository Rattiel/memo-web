package com.jongil.memo.domain.memo.controller;

import com.jongil.memo.domain.memo.dto.MemoData;
import com.jongil.memo.domain.memo.dto.MemoForm;
import com.jongil.memo.domain.memo.dto.MemoView;
import com.jongil.memo.domain.memo.exception.MemoNotFoundException;
import com.jongil.memo.domain.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/memo")
@Controller
public class MemoController {
    private final MemoService memoService;

    @ExceptionHandler({MemoNotFoundException.class})
    public String redirectIndex() {
        return redirectList();
    }

    private String redirectList() {
        return "redirect:/memo";
    }

    @ModelAttribute
    public void bindYesterday(Model model) {
        LocalDateTime yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        model.addAttribute("yesterday", yesterday);
    }

    @GetMapping
    public String renderListView() {
        return "memo/list";
    }

    @PostMapping("/list")
    @ResponseBody
    public List<MemoView> requestList() {
        return memoService.findAll();
    }

    @GetMapping("/new/create")
    public String renderCreatePage(Model model) {
        bindCreateForm(model);

        return "memo/create";
    }

    @PostMapping("/new/create")
    public String requestCreate(
            @Validated @ModelAttribute("form") MemoForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "memo/create";
        }

        memoService.create(form.getTitle(), form.getContent());

        return redirectList();
    }

    @GetMapping("/{id}/update")
    public String renderUpdatePage(
            @PathVariable Long id,
            Model model
    ) {
        MemoData data = memoService.findDataById(id);
        bindUpdateForm(data, model);

        return "memo/update";
    }

    @PostMapping("/{id}/update")
    public String requestUpdate(
            @PathVariable Long id,
            @Validated @ModelAttribute("form") MemoForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "memo/update";
        }

        memoService.update(id, form.getTitle(), form.getContent());

        return redirectList();
    }

    @PostMapping("/{id}/delete")
    public String requestDelete(
            @PathVariable Long id
    ) {
        memoService.delete(id);

        return redirectList();
    }

    private void bindCreateForm(Model model) {
        MemoForm form = new MemoForm();
        model.addAttribute("form", form);
    }

    private void bindUpdateForm(MemoData data, Model model) {
        MemoForm form = MemoForm.from(data);
        model.addAttribute("form", form);
    }
}
