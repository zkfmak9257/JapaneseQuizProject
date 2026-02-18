package com.team.jpquiz.quiz.command.infrastructure;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuizCommandMapper {

    int countAllQuestions();

    List<Long> findRandomQuestionIds(@Param("count") int count);

    List<Long> findRecentWrongQuestionIds(
            @Param("memberId") Long memberId,
            @Param("limit") int limit
    );

    List<Long> findRandomQuestionIdsExcept(
            @Param("count") int count,
            @Param("excludeIds") List<Long> excludeIds
    );

    String findChoiceOrderCsv(@Param("questionId") Long questionId);

    void insertQuizAttempt(Map<String, Object> params);

    int insertQuizAttemptQuestion(
            @Param("attemptId") Long attemptId,
            @Param("seq") int seq,
            @Param("questionId") Long questionId,
            @Param("choiceOrder") String choiceOrder
    );

    int countAttemptById(@Param("attemptId") Long attemptId);

    Map<String, Object> findAttemptQuestionForSubmit(
            @Param("attemptId") Long attemptId,
            @Param("seq") int seq
    );

    Integer findChoiceCorrectFlag(
            @Param("questionId") Long questionId,
            @Param("choiceId") Long choiceId
    );

    int insertQuizAttemptAnswer(
            @Param("attemptId") Long attemptId,
            @Param("seq") int seq,
            @Param("questionId") Long questionId,
            @Param("selectedChoiceId") Long selectedChoiceId,
            @Param("correct") boolean correct
    );

    int countSubmittedAnswer(
            @Param("attemptId") Long attemptId,
            @Param("seq") int seq
    );

    int countSolvedQuestions(@Param("attemptId") Long attemptId);

    Map<String, Object> findAttemptForComplete(@Param("attemptId") Long attemptId);

    int completeAttempt(@Param("attemptId") Long attemptId);

    Map<String, Object> findAttemptForResult(@Param("attemptId") Long attemptId);

    int countCorrectAnswers(@Param("attemptId") Long attemptId);
}
