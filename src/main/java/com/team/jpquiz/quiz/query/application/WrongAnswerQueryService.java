package com.team.jpquiz.quiz.query.application;

import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.quiz.dto.response.WrongAnswerResponse;
import com.team.jpquiz.quiz.query.infrastructure.WrongAnswerMapper; // 패키지 경로 주의 (infrastructure 바로 아래인지, mapper 폴더 안인지)
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
// 오답노트 목록 조회용 쿼리 로직을 처리하는 서비스입니다.
public class WrongAnswerQueryService {

  private final WrongAnswerMapper wrongAnswerMapper;

  // 회원별 오답노트 목록과 전체 개수를 조회해 페이지 응답으로 반환합니다.
  public PageResponse<WrongAnswerResponse> getWrongAnswerList(
      Long memberId,
      int page,
      int size,
      LocalDate fromDate
  ) {
    int offset = (page - 1) * size; // 1-based page -> 0-based offset
    LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;

    List<WrongAnswerResponse> content =
        wrongAnswerMapper.findWrongAnswers(memberId, offset, size, fromDateTime);
    long totalElements = wrongAnswerMapper.countWrongAnswers(memberId, fromDateTime);

    return PageResponse.of(content, page, size, totalElements);
  }
}
