package com.team.jpquiz.quiz.query.application;

import com.team.jpquiz.common.dto.PageResponse;
import com.team.jpquiz.quiz.dto.response.WrongAnswerResponse;
import com.team.jpquiz.quiz.query.infrastructure.WrongAnswerMapper; // 패키지 경로 주의 (infrastructure 바로 아래인지, mapper 폴더 안인지)
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WrongAnswerQueryService {

  private final WrongAnswerMapper wrongAnswerMapper;

  public PageResponse<WrongAnswerResponse> getWrongAnswerList(Long memberId, int page, int size) {
    int offset = (page - 1) * size; // 1-based page -> 0-based offset

    List<WrongAnswerResponse> content = wrongAnswerMapper.findWrongAnswers(memberId, offset, size);
    long totalElements = wrongAnswerMapper.countWrongAnswers(memberId);

    return PageResponse.of(content, page, size, totalElements);
  }
}