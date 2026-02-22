<template>
  <!-- ── 로그인 페이지 — Passport Theme ─────────────────
       디자인 컨셉:
       - 딥 네이비 배경 위에 패스포트 커버 느낌의 카드
       - 골드(#F4C27A) 엠블럼 + 라인 장식
       - "여행의 시작점" = 로그인
       - 미니멀하지만 고급스러운 톤 -->
  <div class="passport-page">

    <!-- ── 패스포트 카드 ──────────────────────────────── -->
    <div class="passport-card">

      <!-- ── 상단: 커스텀 엠블럼 ────────────────────────
           실제 여권 문장이 아닌 완전 커스텀 디자인.
           CSS로 방사형 패턴 + 골드 라인 구현 -->
      <div class="passport-emblem">
        <!-- 외곽 원형 테두리 -->
        <div class="emblem-ring">
          <!-- 방사형 패턴 라인들 (CSS 기반) -->
          <div class="emblem-rays">
            <span v-for="i in 12" :key="i" class="emblem-ray"></span>
          </div>
          <!-- 중앙 텍스트 -->
          <span class="emblem-center">旅</span>
        </div>
        <!-- 엠블럼 하단 텍스트 -->
        <p class="emblem-label">TABIQ PASSPORT</p>
      </div>

      <!-- ── 골드 구분선 ─────────────────────────────── -->
      <div class="passport-divider"></div>

      <!-- ── 브랜드 메시지 ───────────────────────────── -->
      <div class="passport-brand">
        <h2 class="passport-title">
          <span class="passport-title-tabi">旅</span>Quiz Passport
        </h2>
        <p class="passport-subtitle">
          일본어를 여행처럼 — 로그인하고 내 학습을 이어가세요.
        </p>
      </div>

      <!-- ── 로그인 폼 ──────────────────────────────── -->
      <form @submit.prevent="onSubmit" class="passport-form">
        <div class="passport-field">
          <label class="passport-label" for="login-email">이메일</label>
          <input
            id="login-email"
            v-model="email"
            type="email"
            placeholder="you@example.com"
            required
            class="passport-input"
            autocomplete="email"
          />
        </div>

        <div class="passport-field">
          <label class="passport-label" for="login-password">비밀번호</label>
          <input
            id="login-password"
            v-model="password"
            type="password"
            placeholder="••••••••"
            required
            class="passport-input"
            autocomplete="current-password"
          />
        </div>

        <!-- 로그인 버튼 — 골드 강조, 신뢰감 있는 디자인 -->
        <button type="submit" class="passport-btn" :disabled="loading">
          {{ loading ? "인증 중..." : "로그인" }}
        </button>
      </form>

      <!-- 에러 메시지 -->
      <p v-if="errorMessage" class="passport-error">{{ errorMessage }}</p>

      <!-- ── 하단 링크 ──────────────────────────────── -->
      <div class="passport-links">
        <RouterLink to="/quiz/start" class="passport-link passport-link-guest">
          게스트로 시작하기
        </RouterLink>
        <RouterLink to="/signup" class="passport-link">
          회원가입
        </RouterLink>
      </div>

      <!-- ── 스탬프 장식 (우하단) ────────────────────── 
           패스포트의 입국 스탬프 느낌.
           실제 행정 명칭 사용 없이 커스텀 문구만 사용 -->
      <div class="passport-stamp">
        <span class="stamp-text">TABIQ</span>
        <span class="stamp-date">ENTRY 2026</span>
      </div>

      <!-- ── 카드 하단 장식선 ────────────────────────── -->
      <div class="passport-bottom-line"></div>
    </div>

    <!-- ── 배경 장식: 미세한 격자 패턴 ─────────────────── -->
    <div class="passport-bg-pattern"></div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { RouterLink } from "vue-router";
import { useAuthStore } from "../stores/authStore";

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const email = ref("");
const password = ref("");
const loading = ref(false);
const errorMessage = ref("");

async function onSubmit() {
  try {
    loading.value = true;
    errorMessage.value = "";
    await authStore.login(email.value, password.value);
    const redirect = route.query.redirect || "/quiz/start";
    router.push(String(redirect));
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || "로그인에 실패했습니다.";
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
/* ============================================================
   로그인 — Passport Theme (Light Edition)
   
   전체 앱의 밝은 SaaS 톤과 통일하면서
   패스포트 감성(엠블럼, 스탬프, 구분선)은 유지.
   
   컬러 시스템:
   - 배경: 연한 슬레이트 그라데이션 (#f8fafc → #eef2f7)
   - 카드: 흰색 (#fff) + 부드러운 그림자
   - 포인트: 인디고 (#4f46e5) — 헤더와 동일
   - 텍스트: 슬레이트 다크 (#1e293b)
   - 보조: 슬레이트 (#64748b)
   - 스탬프: 인디고 계열 (브랜드 통일)
   ============================================================ */

/* ── 전체 페이지 — 밝은 배경 ─────────────────────────── */
.passport-page {
  min-height: 100vh;
  min-height: 100dvh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
  /* 앱 전체와 통일된 밝은 그라데이션 */
  background: linear-gradient(
    160deg,
    #f8fafc 0%,
    #eef2f7 50%,
    #f1f5f9 100%
  );
  position: relative;
  overflow: hidden;
}

/* ── 배경 격자 패턴 — 여권 느낌의 미세 패턴 ──────────── */
.passport-bg-pattern {
  position: absolute;
  inset: 0;
  opacity: 0.4;
  /* 인디고 톤의 미세 격자 */
  background-image:
    linear-gradient(rgba(79, 70, 229, 0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(79, 70, 229, 0.04) 1px, transparent 1px);
  background-size: 40px 40px;
  pointer-events: none;
}

/* ── 패스포트 카드 ───────────────────────────────────── */
.passport-card {
  position: relative;
  width: min(420px, 100%);
  background: #fff;
  border-radius: 16px;
  padding: 40px 32px 36px;
  border: 1px solid #e2e8f0;
  /* 부드러운 그림자 — 카드가 떠있는 느낌 */
  box-shadow:
    0 4px 24px rgba(0, 0, 0, 0.06),
    0 1px 4px rgba(0, 0, 0, 0.04);
  z-index: 1;
}

/* ── 엠블럼 영역 ─────────────────────────────────────── */
.passport-emblem {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

/* 외곽 원형 링 — 인디고 이중 테두리 */
.emblem-ring {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  border: 2px solid rgba(79, 70, 229, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  /* 이중 테두리 효과 */
  box-shadow:
    inset 0 0 0 3px transparent,
    inset 0 0 0 5px rgba(79, 70, 229, 0.08);
  background: rgba(79, 70, 229, 0.02);
}

/* 방사형 패턴 — 얇은 인디고 라인들 */
.emblem-rays {
  position: absolute;
  inset: 4px;
  border-radius: 50%;
  overflow: hidden;
}

.emblem-ray {
  position: absolute;
  width: 1px;
  height: 100%;
  left: 50%;
  top: 0;
  background: rgba(79, 70, 229, 0.06);
  transform-origin: center center;
}

/* 각 선을 회전 (12개) */
.emblem-ray:nth-child(1)  { transform: rotate(0deg); }
.emblem-ray:nth-child(2)  { transform: rotate(30deg); }
.emblem-ray:nth-child(3)  { transform: rotate(60deg); }
.emblem-ray:nth-child(4)  { transform: rotate(90deg); }
.emblem-ray:nth-child(5)  { transform: rotate(120deg); }
.emblem-ray:nth-child(6)  { transform: rotate(150deg); }
.emblem-ray:nth-child(7)  { transform: rotate(15deg); }
.emblem-ray:nth-child(8)  { transform: rotate(45deg); }
.emblem-ray:nth-child(9)  { transform: rotate(75deg); }
.emblem-ray:nth-child(10) { transform: rotate(105deg); }
.emblem-ray:nth-child(11) { transform: rotate(135deg); }
.emblem-ray:nth-child(12) { transform: rotate(165deg); }

/* 중앙 旅 텍스트 — 인디고 */
.emblem-center {
  font-family: var(--font-display);
  font-size: 32px;
  font-weight: 800;
  color: #4f46e5;
  z-index: 1;
}

/* 엠블럼 하단 라벨 */
.emblem-label {
  margin-top: 10px;
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 3px;
  text-transform: uppercase;
  color: rgba(79, 70, 229, 0.4);
}

/* ── 구분선 — 인디고 그라데이션 ──────────────────────── */
.passport-divider {
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(79, 70, 229, 0.1) 20%,
    rgba(79, 70, 229, 0.2) 50%,
    rgba(79, 70, 229, 0.1) 80%,
    transparent 100%
  );
  margin: 0 0 24px;
}

/* ── 브랜드 메시지 ───────────────────────────────────── */
.passport-brand {
  text-align: center;
  margin-bottom: 28px;
}

.passport-title {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  letter-spacing: -0.5px;
  margin-bottom: 8px;
}

.passport-title-tabi {
  color: #4f46e5;                  /* 旅만 인디고로 강조 */
}

.passport-subtitle {
  font-size: 13px;
  color: #64748b;
  line-height: 1.5;
}

/* ── 로그인 폼 ───────────────────────────────────────── */
.passport-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 16px;
}

.passport-field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.passport-label {
  font-size: 12px;
  font-weight: 600;
  color: #64748b;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

/* 입력 필드 — 밝은 배경 + 인디고 포커스 */
.passport-input {
  padding: 12px 14px;
  border: 1.5px solid #e2e8f0;
  border-radius: 8px;
  background: #f8fafc;
  color: #1e293b;
  font-size: 15px;
  font-family: var(--font-body);
  transition: all 0.25s ease;
}

.passport-input::placeholder {
  color: #94a3b8;
}

.passport-input:focus {
  outline: none;
  border-color: #4f46e5;
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
  background: #fff;
}

/* 로그인 버튼 — 인디고 (헤더 CTA와 통일) */
.passport-btn {
  margin-top: 4px;
  padding: 14px;
  border: none;
  border-radius: 10px;
  background: #4f46e5;
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.5px;
  cursor: pointer;
  transition: all 0.25s ease;
  min-height: 48px;
}

.passport-btn:hover:not(:disabled) {
  background: #4338ca;
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(79, 70, 229, 0.3);
}

.passport-btn:active:not(:disabled) {
  transform: translateY(0);
}

.passport-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* ── 에러 메시지 ─────────────────────────────────────── */
.passport-error {
  text-align: center;
  color: #dc3545;
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 8px;
  padding: 8px 12px;
  border-radius: 6px;
  background: rgba(220, 53, 69, 0.06);
  border: 1px solid rgba(220, 53, 69, 0.12);
}

/* ── 하단 링크 ───────────────────────────────────────── */
.passport-links {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 4px;
}

.passport-link {
  font-size: 13px;
  color: #64748b;
  text-decoration: none;
  transition: color 0.2s ease;
  font-weight: 500;
}

.passport-link:hover {
  color: #4f46e5;
}

.passport-link-guest {
  color: #4f46e5;
  opacity: 0.7;
}

.passport-link-guest:hover {
  opacity: 1;
}

/* ── 스탬프 장식 (우하단) ─────────────────────────────── 
   인디고 톤으로 브랜드 통일 */
.passport-stamp {
  position: absolute;
  bottom: 20px;
  right: 20px;
  width: 56px;
  height: 56px;
  border: 2px solid rgba(79, 70, 229, 0.15);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transform: rotate(-12deg);
  opacity: 0.5;
}

.stamp-text {
  font-size: 8px;
  font-weight: 800;
  color: rgba(79, 70, 229, 0.4);
  letter-spacing: 1.5px;
  text-transform: uppercase;
  line-height: 1;
}

.stamp-date {
  font-size: 6px;
  font-weight: 600;
  color: rgba(79, 70, 229, 0.3);
  letter-spacing: 0.5px;
  margin-top: 2px;
  line-height: 1;
}

/* ── 카드 하단 장식선 ─────────────────────────────────── */
.passport-bottom-line {
  margin-top: 28px;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(79, 70, 229, 0.08) 30%,
    rgba(79, 70, 229, 0.15) 50%,
    rgba(79, 70, 229, 0.08) 70%,
    transparent 100%
  );
}

/* ── 반응형 ──────────────────────────────────────────── */
@media (max-width: 480px) {
  .passport-card {
    padding: 32px 24px 28px;
  }

  .emblem-ring {
    width: 72px;
    height: 72px;
  }

  .emblem-center {
    font-size: 26px;
  }

  .passport-title {
    font-size: 18px;
  }
}
</style>
