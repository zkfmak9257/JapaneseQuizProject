package com.team.jpquiz.quiz.query.application;

import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.global.error.CustomException;
import com.team.jpquiz.global.error.ErrorCode;
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
      LocalDate fromDate,
      String keyword,
      String category
  ) {
    validateInput(memberId, page, size);
    int offset = (page - 1) * size; // 1-based page -> 0-based offset
    LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;

    List<WrongAnswerResponse> content =
        wrongAnswerMapper.findWrongAnswers(memberId, offset, size, fromDateTime, keyword, category);
    long totalElements = wrongAnswerMapper.countWrongAnswers(memberId, fromDateTime, keyword, category);

    return PageResponse.of(content, page, size, totalElements);
  }

  private void validateInput(Long memberId, int page, int size) {
    if (memberId == null || memberId <= 0) {
      throw new CustomException(ErrorCode.UNAUTHORIZED);
    }
    if (page < 1 || size < 1 || size > 100) {
      throw new CustomException(ErrorCode.INVALID_REQUEST);
    }
  }
}
