package com.jongil.memo.domain.memo.repository;

import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import com.jongil.memo.domain.memo.dto.MemoView;
import com.jongil.memo.domain.user.User;
import com.jongil.memo.global.config.security.access.AccessOnlyOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    @AccessOnlyOwner
    @Override
    Optional<Memo> findById(Long id);

    @AccessOnlyOwner
    Optional<MemoData> findDataById(Long id);

    @SuppressWarnings("unchecked")
    @AccessOnlyOwner
    @Override
    Memo save(Memo entity);

    @AccessOnlyOwner
    List<MemoView> findAllByWriter(User writer);
}
