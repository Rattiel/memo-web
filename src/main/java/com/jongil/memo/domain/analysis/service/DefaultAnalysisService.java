package com.jongil.memo.domain.analysis.service;

import com.jongil.memo.domain.analysis.Analysis;
import com.jongil.memo.domain.analysis.dto.AnalysisMessage;
import com.jongil.memo.domain.analysis.repository.AnalysisRepository;
import com.jongil.memo.domain.user.User;
import com.jongil.memo.domain.user.repository.UserRepository;
import com.jongil.memo.global.config.security.SecurityUtil;
import com.jongil.memo.global.config.websocket.SecuredStompSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class DefaultAnalysisService implements AnalysisService {
    private final AnalysisRepository analysisRepository;

    private final SecuredStompSender websocket;

    private final SecurityUtil securityUtil;

    private final UserRepository userRepository;

    private final static String[] KEYWORDS = {"약속", "이벤트", "하기"};

    @Override
    public void analyze(String content) {
        User user = securityUtil.getUser();

        Analysis analysis = findOrSave(user);

        int score = checkKeywords(content);

        analysis.update(score);
    }

    @Override
    public void check(String username) {
        User user = findUser(username);

        Optional<Analysis> analysisOptional = analysisRepository.findByOwner(user);

        if (analysisOptional.isPresent()) {
            Analysis analysis = analysisOptional.get();

            if (analysis.getScore() >= 1) {
                AnalysisMessage message = AnalysisMessage.builder()
                        .principalName(user.getUsername())
                        .message("오늘 하루도 힘내세요.")
                        .build();

                websocket.send("/message", message);

                analysis.reset();
            }
        }
    }

    private int checkKeywords(String content) {
        int score = 0;

        for (String keyword: KEYWORDS) {
            if (content.contains(keyword)) {
                score++;
            }
        }

        return score;
    }

    private Analysis findOrSave(User user) {
        Optional<Analysis> analysisOptional = analysisRepository.findByOwner(user);

        if (analysisOptional.isEmpty()) {
            Analysis analysis = Analysis.create(user);

            return analysisRepository.save(analysis);
        } else {
            return analysisOptional.get();
        }
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("사용자 찾기 실패(존재하지 않는 아이디)"));
    }
}
