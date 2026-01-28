package com.team.jpquiz.common.presentation;

import com.team.jpquiz.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ApiResponse<Void> health() {
        return ApiResponse.ok(); // 공통 응답 포맷 사용 예시
    }
}
