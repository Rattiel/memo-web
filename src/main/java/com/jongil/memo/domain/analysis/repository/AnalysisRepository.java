package com.jongil.memo.domain.analysis.repository;

import com.jongil.memo.domain.analysis.Analysis;
import com.jongil.memo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    Optional<Analysis> findByOwner(User owner);
}
