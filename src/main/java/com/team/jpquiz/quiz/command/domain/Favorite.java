package com.team.jpquiz.quiz.command.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/*
* - @NoArgsConstructor(PROTECTED)로 JPA(자바 영속성 API) 규칙 준수
  - createdAt은 수정 불가(updatable = false)로 운영 이력 안정화
  - DB 객체명은 snake_case(스네이크 표기)로 컨벤션 유지
  * */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // Auditing(감사 추적)
@Table(
    name = "favorites",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_favorites_member_question",
            columnNames = {"member_id", "question_id"}
        )
    }
)
// 회원별 문제 즐겨찾기 관계를 저장하는 엔티티입니다.
public class Favorite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "question_id", nullable = false)
  private Long questionId;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  public Favorite(Long memberId, Long questionId) {
    this.memberId = memberId;
    this.questionId = questionId;
  }
}
