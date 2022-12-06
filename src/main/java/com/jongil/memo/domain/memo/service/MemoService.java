package com.jongil.memo.domain.memo.service;

import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import org.springframework.data.domain.Pageable;

public interface MemoService {
    Memo create(String content);

    Memo update(long id, String content);

    void delete(long id);

    MemoData findDataById(long id);

    Memo findById(long id);
}
