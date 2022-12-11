package com.jongil.memo.domain.memo.service;

import com.jongil.memo.domain.analysis.service.AnalysisService;
import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import com.jongil.memo.domain.memo.dto.MemoId;
import com.jongil.memo.domain.memo.dto.MemoView;
import com.jongil.memo.domain.memo.exception.MemoNotFoundException;
import com.jongil.memo.domain.memo.repository.MemoRepository;
import com.jongil.memo.domain.user.User;
import com.jongil.memo.global.config.security.SecurityUtil;
import com.jongil.memo.global.config.websocket.SecuredStompSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class DefaultMemoService implements MemoService {
    private final MemoRepository memoRepository;

    private final SecurityUtil securityUtil;

    private final SecuredStompSender websocket;

    private final AnalysisService analysisService;

    private final String MESSAGE_PREFIX = "/memo";

    @Override
    public void create(String title, String content) {
        User writer = securityUtil.getUser();

        Memo memo = Memo.create(title, writer, content);

        memoRepository.save(memo);

        analysisService.analyze(memo.getContent());

        websocket.send(MESSAGE_PREFIX + "/create", memo);
    }

    @Override
    public void update(long id, String title, String content) {
        Memo memo = find(id);

        memo.update(title, content);

        analysisService.analyze(memo.getContent());

        websocket.send(MESSAGE_PREFIX + "/update", memo);
    }

    @Override
    public void delete(long id) {
        Memo memo = find(id);

        memoRepository.delete(memo);

        websocket.send(MESSAGE_PREFIX + "/delete", new MemoId(id, memo.getPrincipalName()));
    }

    @Transactional(readOnly = true)
    @Override
    public MemoData findDataById(long id) {
        return memoRepository.findDataById(id)
                .orElseThrow(MemoNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemoView> findAll() {
        User writer = securityUtil.getUser();

        return memoRepository.findAllByWriter(writer);
    }

    private Memo find(long id) {
        return memoRepository.findById(id)
                .orElseThrow(MemoNotFoundException::new);
    }
}
