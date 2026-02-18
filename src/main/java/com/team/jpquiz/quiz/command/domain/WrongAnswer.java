package com.team.jpquiz.quiz.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wrong_answers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "question_id"})
})
// 회원별 문제 오답 이력과 누적 횟수를 저장하는 오답노트 엔티티입니다.
public class WrongAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "question_id", nullable = false)
  private Long questionId;

  @Column(name = "wrong_count", nullable = false)
  private int wrongCount;

  @LastModifiedDate
  @Column(name = "last_wrong_at")
  private LocalDateTime lastWrongAt;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Builder
  public WrongAnswer(Long memberId, Long questionId) {
    this.memberId = memberId;
    this.questionId = questionId;
    this.wrongCount = 1; // 처음 생성될때는 1회 틀린 것
  }

  // 비지니스 로직 : 또 틀렸을 때 카운트 증가 및 시간 갱신
  public void increaseWrongCount() {
    this.wrongCount++;
    this.lastWrongAt = LocalDateTime.now();
  }

  public Long getQuestionId() {
    return questionId;
  }

  public int getWrongCount() {
    return wrongCount;
  }

  public LocalDateTime getLastWrongAt() {
    return lastWrongAt;
  }
}
