package com.jongil.memo.domain.memo.service;

import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import com.jongil.memo.domain.memo.dto.MemoView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemoService {
    void create(String title, String content);

    void update(long id, String title, String content);

    void delete(long id);

    MemoData findDataById(long id);

    List<MemoView> findAll();
}
