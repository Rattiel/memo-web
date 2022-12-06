package com.jongil.memo.domain.memo.service;

import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import com.jongil.memo.domain.memo.exception.MemoNotFoundException;
import com.jongil.memo.domain.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultMemoService implements MemoService {
    private final MemoRepository postRepository;

    @Transactional
    @Override
    public Memo create(String content) {
        Memo post = Memo.create(content);

        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Memo update(long id, String content) {
        Memo post = find(id);

        return post.update(content);
    }

    @Transactional
    @Override
    public void delete(long id) {
        Memo post = find(id);

        postRepository.delete(post);
    }

    @Transactional
    @Override
    public MemoData findDataById(long id) {
        return postRepository.findDataById(id)
                .orElseThrow(MemoNotFoundException::new);
    }

    @Transactional
    @Override
    public Memo findById(long id) {
        return find(id);
    }

    private Memo find(long id) {
        return postRepository.findById(id)
                .orElseThrow(MemoNotFoundException::new);
    }
}
