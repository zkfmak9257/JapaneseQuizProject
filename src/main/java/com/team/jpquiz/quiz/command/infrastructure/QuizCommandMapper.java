package com.team.jpquiz.quiz.command.infrastructure;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuizCommandMapper {

    int countAllQuestions();

    List<Long> findRandomQuestionIds(@Param("count") int count);

    String findChoiceOrderCsv(@Param("questionId") Long questionId);

    void insertQuizAttempt(Map<String, Object> params);

    int insertQuizAttemptQuestion(
            @Param("attemptId") Long attemptId,
            @Param("seq") int seq,
            @Param("questionId") Long questionId,
            @Param("choiceOrder") String choiceOrder
    );
}
