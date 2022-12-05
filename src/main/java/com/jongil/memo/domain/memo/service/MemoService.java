package com.jongil.memo.domain.memo.service;

import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import com.jongil.memo.domain.memo.dto.MemoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemoService {
    Memo create(String content);

    Memo update(long id, String content);

    void delete(long id);

    Page<MemoPreview> list(Pageable pageable);

    MemoData findDataById(long id);

    Memo findById(long id);
}
