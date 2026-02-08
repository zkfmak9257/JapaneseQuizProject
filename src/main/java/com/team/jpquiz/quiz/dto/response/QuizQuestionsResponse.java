package com.team.jpquiz.quiz.dto.response;

import org.springframework.security.core.parameters.P;

import java.util.List;

public class QuizQuestionsResponse {
    // 응답 스펙 : questions[]
    private List<QuizQuestionResponse> questions;

    public QuizQuestionsResponse(){
    }

    public QuizQuestionsResponse(List<QuizQuestionResponse> questions) {
        this.questions = questions;
    }
    public List<QuizQuestionResponse> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuizQuestionResponse> questions) {
        this.questions = questions;
    }
}
