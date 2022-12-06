package com.jongil.memo.domain.memo.repository;

import com.jongil.memo.domain.memo.Memo;
import com.jongil.memo.domain.memo.dto.MemoData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    @EntityGraph(attributePaths = {"owner"}, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    Optional<Memo> findById(Long id);

    Optional<MemoData> findDataById(Long id);
}
