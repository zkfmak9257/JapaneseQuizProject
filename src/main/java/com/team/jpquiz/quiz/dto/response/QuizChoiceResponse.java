package com.team.jpquiz.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class QuizChoiceResponse {

    @JsonIgnore
    private Long questionId; // 이 보기가 어느 문제 소속인지 연결키
    private Long choiceId; // 보기 Pk
    private String choiceText; // 보기 문장

    // Mybatis가 객체 생성할 때 기본 생성자가 필요
    public QuizChoiceResponse(){

    }
    public QuizChoiceResponse(Long questionId, Long choiceId, String choiceText) {
        this.questionId = questionId;
        this.choiceId = choiceId;
        this.choiceText = choiceText;
    }

    public Long getQuestionId(){
        return questionId;
    }
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    public Long getChoiceId() {
        return choiceId;
    }
    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }
    public String getChoiceText() {
        return choiceText;
    }
    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

}


