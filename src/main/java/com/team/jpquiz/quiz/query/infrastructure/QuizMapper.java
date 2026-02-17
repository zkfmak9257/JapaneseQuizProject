package com.team.jpquiz.quiz.query.infrastructure;

import com.team.jpquiz.quiz.dto.response.QuizAttemptQuestionResponse;
import com.team.jpquiz.quiz.dto.response.QuizChoiceResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuizMapper {

    // attempt의 seq 문제 1개(문제 + scene + totalQuestions) 조회
    QuizAttemptQuestionResponse findAttemptQuestion(
            @Param("attemptId") Long attemptId,
            @Param("seq") int seq
    );

    // 위 문제의 보기 목록 조회 (choice_order 순서 반영)
    List<QuizChoiceResponse> findAttemptQuestionChoices(
            @Param("attemptId") Long attemptId,
            @Param("seq") int seq
    );
}
