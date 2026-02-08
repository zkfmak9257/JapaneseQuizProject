package com.team.jpquiz.quiz.dto.response;

import java.util.ArrayList;
import java.util.List;

public class QuizAttemptQuestionResponse {

    // attempt의 몇 번째 문제인지 식별
    private Long attemptId;
    private int seq;
    private int totalQuestions;

    // 실제 문제 정보
    private Long questionId;
    private String questionText;

    // 상황(카테고리) 정보
    private QuizSceneResponse scene;

    // 보기 목록 (order 순서대로 채워질 예정)
    private List<QuizChoiceResponse> choices = new ArrayList<>();

    public QuizAttemptQuestionResponse() {
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
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

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuizSceneResponse getScene() {
        return scene;
    }

    public void setScene(QuizSceneResponse scene) {
        this.scene = scene;
    }

    public List<QuizChoiceResponse> getChoices() {
        return choices;
    }

    public void setChoices(List<QuizChoiceResponse> choices) {
        this.choices = choices;
    }

}
