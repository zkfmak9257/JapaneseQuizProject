package com.team.jpquiz.quiz.dto.response;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionResponse {

    private Long questionId; // 문제 PK
    private String questionText; // 문제 문장
    private Long sceneId; // 상황별

    // 이 문제의 보기 목록
    // null 방지 위해 빈 리스트로 ㅊ초기화
    private List<QuizChoiceResponse> choices = new ArrayList<>();
    public QuizQuestionResponse(){
    }
    public QuizQuestionResponse(Long questionId, String questionText, Long sceneId) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.sceneId = sceneId;
    }
    public Long getQuestionId() {
        return questionId;
    }
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText){
        this.questionText = questionText;
    }
    public Long getSceneId(){
        return sceneId;
    }
    public void setSceneId(Long sceneId){
        this.sceneId = sceneId;
    }
    public List<QuizChoiceResponse> getChoices(){
        return choices;
    }
    public void setChoices(List<QuizChoiceResponse> choices) {
        this.choices = choices;
    }
}
