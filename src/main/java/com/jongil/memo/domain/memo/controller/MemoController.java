package com.jongil.memo.domain.memo.controller;

import com.jongil.memo.domain.common.exception.Fieldable;
import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import com.jongil.memo.domain.memo.dto.MemoForm;
import com.jongil.memo.domain.memo.dto.MemoPreview;
import com.jongil.memo.domain.memo.exception.MemoNotFoundException;
import com.jongil.memo.domain.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@RequestMapping("/post")
@Controller
public class MemoController {
    private final static int MAX_NAV_ITEM = 5;

    private final MemoService memoService;

    @ExceptionHandler({MemoNotFoundException.class})
    public String redirectIndex() {
        return "redirect:/";
    }

    @ModelAttribute
    public void bindYesterday(Model model) {
        LocalDateTime yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        model.addAttribute("yesterday", yesterday);
    }

    @ModelAttribute
    public void bindPageable(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
    }

    @GetMapping("/new/create")
    public String renderCreatePage(Model model) {
        bindCreateForm(model);

        return "memo/create";
    }

    @PostMapping("/new/create")
    public String requestCreate(
            @Validated @ModelAttribute("form") MemoForm form,
            BindingResult bindingResult,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            return "memo/create";
        }

        Memo post = memoService.create(
                form.getContent()
        );

        return String.format(
                "redirect:/post/%d?page=%d&size=%d",
                post.getId(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
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
            BindingResult bindingResult,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            return "memo/update";
        }

        Memo post = memoService.update(
                id,
                form.getContent()
        );

        return String.format(
                "redirect:/post/%d?page=%d&size=%d",
                post.getId(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PostMapping("/{id}/delete")
    public String requestDelete(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        memoService.delete(id);

        return String.format(
                "redirect:/post?page=%d&size=%d",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @GetMapping
    public String renderList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<MemoPreview> postPreviewPage = memoService.list(pageable);
        bindPostPreviewPage(postPreviewPage, model);
        bindPageNavBar(postPreviewPage, model);

        return "memo/list";
    }

    @GetMapping("/{id}")
    public String renderView(
            @PathVariable Long id,
            Model model
    ) {
        Memo post = memoService.findById(id);
        bindPostView(post, model);

        return "memo/view";
    }

    private void bindCreateForm(Model model) {
        MemoForm form = new MemoForm();
        model.addAttribute("form", form);
    }

    private void bindUpdateForm(MemoData data, Model model) {
        MemoForm form = MemoForm.from(data);
        model.addAttribute("form", form);
    }

    private void bindPostPreviewPage(Page<MemoPreview> postPreviewPage, Model model) {
        model.addAttribute("postPreviewPage", postPreviewPage);
    }

    private void bindPageNavBar(Page<?> Page, Model model) {
        int navStartPage = Page.getNumber() - Page.getNumber() % MAX_NAV_ITEM;
        model.addAttribute("start", navStartPage);

        int navLastPage = Math.min(navStartPage + MAX_NAV_ITEM - 1, Page.getTotalPages() - 1);
        navLastPage = Math.max(navLastPage, 0);
        model.addAttribute("last", navLastPage);

        boolean showBeforeNav = navStartPage >= MAX_NAV_ITEM;
        model.addAttribute("showBeforeNav", showBeforeNav);

        boolean showNextNav = navLastPage < Page.getTotalPages() - 1;
        model.addAttribute("showNextNav", showNextNav);
    }

    private void bindPostView(Memo post, Model model) {
        model.addAttribute("post", post);
    }

    private void bindFieldError(BindingResult bindingResult, Fieldable e) {
        FieldError fieldError = new FieldError("form", e.getField(), e.getReason());
        bindingResult.addError(fieldError);
    }
}
