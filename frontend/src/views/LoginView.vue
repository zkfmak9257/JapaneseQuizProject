<template>
  <!-- ── 로그인 페이지 — 여행 출발 게이트 ──────────────
       앱의 하늘/바다 테마와 통일.
       글래스모피즘 카드 + 입장 애니메이션 + 플로팅 장식 -->
  <div class="login-page">

    <!-- ── 로그인 카드 ──────────────────────────────── -->
    <div class="login-card">

      <!-- 카드 상단 장식선 — 골드 그라데이션 -->
      <div class="card-topline"></div>

      <!-- 일본 국장(16국화문) 엠블럼 -->
      <div class="login-emblem">
        <div class="emblem-glow"></div>
        <div class="emblem-ring">
          <!-- 16국화문(Kikumon) SVG - 원본 이미지 기반의 황금색 + 테두리 국장 스타일 -->
          <svg class="emblem-pattern" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <defs>
              <!-- 끝이 둥글고 중간이 살짝 볼록한 전통 국화 꽃잎 모양 -->
              <path id="petal" 
                    d="M 50 40 
                       C 45 35, 42 22, 44 9 
                       C 45 4, 47.5 2, 50 2 
                       C 52.5 2, 55 4, 56 9 
                       C 58 22, 55 35, 50 40 Z" 
                    fill="#FEE685" 
                    stroke="#B47927" 
                    stroke-width="1.5" 
                    stroke-linejoin="round" />
            </defs>
            <g>
              <!-- 하단 겹꽃 (16잎, 11.25도 엇갈림 배치) -->
              <use v-for="i in 16" :key="'bg-'+i" href="#petal" :transform="`rotate(${i * 22.5 + 11.25}, 50, 50)`" />
              <!-- 상단 주꽃잎 (16잎, 정방향 배치) -->
              <use v-for="i in 16" :key="'fg-'+i" href="#petal" :transform="`rotate(${i * 22.5}, 50, 50)`" />
            </g>
            <!-- 중앙 꽃심: 겹치는 안쪽 선들을 깔끔하게 덮고 명확한 테두리 형성 -->
            <circle cx="50" cy="50" r="12" fill="#FEE685" stroke="#B47927" stroke-width="1.5" />
          </svg>
        </div>
        <p class="emblem-tag">TABIQ PASSPORT</p>
      </div>

      <!-- 구분선 -->
      <div class="login-divider"></div>

      <!-- 브랜드 -->
      <h2 class="login-title">
        <span class="title-tabi">旅</span>Quiz에 오신 것을 환영합니다
      </h2>
      <p class="login-desc">로그인하고 일본어 여행을 이어가세요</p>

      <!-- 폼 -->
      <form @submit.prevent="onSubmit" class="login-form">
        <div class="login-field">
          <label for="login-email">이메일</label>
          <div class="input-wrap">
            <svg class="input-icon svg-icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
              <polyline points="22,6 12,13 2,6"></polyline>
            </svg>
            <input
              id="login-email"
              v-model="email"
              type="email"
              placeholder="you@example.com"
              required
              autocomplete="email"
            />
          </div>
        </div>

        <div class="login-field">
          <label for="login-pw">비밀번호</label>
          <div class="input-wrap">
            <svg class="input-icon svg-icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
            </svg>
            <input
              id="login-pw"
              v-model="password"
              type="password"
              placeholder="••••••••"
              required
              autocomplete="current-password"
            />
          </div>
        </div>

        <button type="submit" class="login-btn" :disabled="loading">
          <span v-if="loading" class="btn-loader"></span>
          <span v-else class="btn-text">
            로그인
            <span class="btn-arrow">→</span>
          </span>
          <span v-if="loading">인증 중...</span>
        </button>
      </form>

      <p v-if="errorMessage" class="login-error">{{ errorMessage }}</p>

      <!-- 하단 구분선 -->
      <div class="login-divider divider-light"></div>

      <div class="login-links">
        <RouterLink to="/quiz/start" class="login-link link-guest">
          게스트로 시작하기
        </RouterLink>
        <span class="link-dot">·</span>
        <RouterLink to="/signup" class="login-link">회원가입</RouterLink>
      </div>

      <!-- 스탬프 장식 -->
      <div class="login-stamp">
        <span class="stamp-top">TABIQ</span>
        <span class="stamp-mid">✦</span>
        <span class="stamp-bot">ENTRY 2026</span>
      </div>
    </div>
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
    const redirect = route.query.redirect || "/";
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
   로그인 — Travel Gate (v3 — 디자인 개선)
   
   문제 분석 결과 반영:
   1. 카드-배경 대비 강화 (더 강한 그림자 + 배경 패턴)
   2. 입체감 강화 (다중 레이어 그림자 + 카드 상단 장식선)
   3. 요소 간격 개선 (라벨↔입력 간격 넓힘)
   4. 패스포트 테마 강화 (구분선, 스탬프 개선)
   5. 버튼 프리미엄화 (화살표 + hovr 인터랙션)
   ============================================================ */

/* ── 페이지 ──────────────────────────────────────── */
.login-page {
  min-height: 100vh;
  min-height: 100dvh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 16px;
  background: transparent;
  position: relative;
  /* overflow: hidden 제거하여 필요 없는 제약 해제 */
}

/* ── 로그인 카드 ─────────────────────────────────── */
.login-card {
  position: relative;
  width: min(400px, 100%);
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 44px 36px 40px;
  border: 1px solid rgba(255, 255, 255, 0.7);
  /* 강화된 다중 그림자 — 입체감 핵심 */
  box-shadow:
    0 20px 60px rgba(15, 23, 42, 0.08),  /* 넓은 그림자 */
    0 8px 24px rgba(79, 70, 229, 0.06),  /* 인디고 서브 */
    0 2px 6px rgba(0, 0, 0, 0.04),       /* 가장자리 */
    inset 0 1px 0 rgba(255, 255, 255, 0.9); /* 상단 하이라이트 */
  z-index: 1;
  overflow: hidden;
  animation: cardEntrance 0.7s cubic-bezier(0.16, 1, 0.3, 1) both;
}

/* 카드 상단 장식선 — 인디고→스카이 그라데이션 */
.card-topline {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #4f46e5, #7EC8E3, #4f46e5);
  border-radius: 24px 24px 0 0;
}

@keyframes cardEntrance {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.96);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* ── 엠블럼 ──────────────────────────────────────── */
.login-emblem {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 16px;
  position: relative;
  animation: cardEntrance 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.1s both;
}

/* 엠블럼 뒤의 은은한 glow */
.emblem-glow {
  position: absolute;
  width: 100px; height: 100px;
  border-radius: 50%;
  /* 국장 골드 색상의 은은한 빛 */
  background: radial-gradient(circle, rgba(202, 138, 4, 0.15) 0%, transparent 70%);
  top: -10px;
  pointer-events: none;
}

.emblem-ring {
  width: 80px; height: 80px;
  border-radius: 50%;
  border: 2px solid rgba(202, 138, 4, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: rgba(202, 138, 4, 0.05);
  box-shadow:
    0 4px 20px rgba(202, 138, 4, 0.12),
    inset 0 0 0 4px rgba(202, 138, 4, 0.06);
  animation: emblemSpin 60s linear infinite;
}

@keyframes emblemSpin {
  to { transform: rotate(360deg); }
}

/* 국화문양 SVG 자체 스타일 */
.emblem-pattern {
  width: 48px; 
  height: 48px;
  z-index: 1;
  /* 국화문이 천천히 반시계방향으로 회전 */
  animation: emblemSpin 80s linear infinite reverse;
}

.emblem-tag {
  margin-top: 10px;
  font-size: 10px;
  font-weight: 800;
  letter-spacing: 3.5px;
  text-transform: uppercase;
  color: #B45309;
  opacity: 0.8;
}

/* ── 구분선 ──────────────────────────────────────── */
.login-divider {
  height: 1px;
  margin: 16px 0;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(79, 70, 229, 0.12) 20%,
    rgba(79, 70, 229, 0.18) 50%,
    rgba(79, 70, 229, 0.12) 80%,
    transparent 100%
  );
}

.divider-light {
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(0, 0, 0, 0.04) 20%,
    rgba(0, 0, 0, 0.06) 50%,
    rgba(0, 0, 0, 0.04) 80%,
    transparent 100%
  );
}

/* ── 브랜드 텍스트 ───────────────────────────────── */
.login-title {
  text-align: center;
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 8px;
  animation: cardEntrance 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.15s both;
}

.title-tabi {
  color: #4f46e5;
  font-size: 24px;
}

.login-desc {
  text-align: center;
  font-size: 14px;
  color: #94a3b8;
  margin-bottom: 28px;
  animation: cardEntrance 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.2s both;
}

/* ── 폼 ──────────────────────────────────────────── */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-bottom: 16px;
  animation: cardEntrance 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.25s both;
}

.login-field label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  margin-bottom: 8px;
}

.input-wrap {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  pointer-events: none;
  z-index: 1;
  opacity: 0.5;
  color: #64748b;
  display: flex;
  align-items: center;
  justify-content: center;
}

.svg-icon {
  width: 18px;
  height: 18px;
}

.input-wrap input {
  width: 100%;
  padding: 16px 16px 16px 44px;
  border: 1.5px solid #e2e8f0;
  border-radius: 12px;
  background: rgba(248, 250, 252, 0.8);
  color: #1e293b;
  font-size: 16px;
  font-family: var(--font-body);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.input-wrap input::placeholder {
  color: #cbd5e1;
}

.input-wrap input:focus {
  outline: none;
  border-color: #6366f1;
  box-shadow:
    0 0 0 3px rgba(79, 70, 229, 0.08),
    0 4px 16px rgba(79, 70, 229, 0.06);
  background: #fff;
  transform: translateY(-1px);
}

/* 포커스 시 아이콘 색상 변화 */
.input-wrap:focus-within .input-icon {
  opacity: 1;
}

/* ── 로그인 버튼 ─────────────────────────────────── */
.login-btn {
  margin-top: 6px;
  padding: 16px 20px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #4f46e5 0%, #6366f1 50%, #818cf8 100%);
  background-size: 200% 200%;
  background-position: 0% 50%;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  min-height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow:
    0 4px 14px rgba(79, 70, 229, 0.25),
    0 1px 3px rgba(79, 70, 229, 0.15);
}

.btn-text {
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-arrow {
  transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  font-size: 16px;
}

.login-btn:hover:not(:disabled) {
  background-position: 100% 50%;
  transform: translateY(-2px);
  box-shadow:
    0 8px 24px rgba(79, 70, 229, 0.3),
    0 2px 8px rgba(79, 70, 229, 0.2);
}

.login-btn:hover:not(:disabled) .btn-arrow {
  transform: translateX(4px);
}

.login-btn:active:not(:disabled) {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(79, 70, 229, 0.15);
}

.login-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn-loader {
  width: 18px; height: 18px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ── 에러 ─────────────────────────────────────────── */
.login-error {
  text-align: center;
  color: #dc3545;
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 8px;
  padding: 10px 14px;
  border-radius: 10px;
  background: rgba(220, 53, 69, 0.05);
  border: 1px solid rgba(220, 53, 69, 0.1);
  animation: shake 0.4s ease-in-out;
}

/* ── 하단 링크 ────────────────────────────────────── */
.login-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 2px;
  animation: cardEntrance 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.35s both;
}

.login-link {
  font-size: 14px;
  color: #94a3b8;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s ease;
}

.login-link:hover { color: #4f46e5; }

.link-guest {
  color: #6366f1;
}

.link-guest:hover { color: #4338ca; }

.link-dot {
  color: #e2e8f0;
  font-size: 14px;
}

/* ── 스탬프 장식 ──────────────────────────────────── */
.login-stamp {
  position: absolute;
  bottom: 20px; right: 20px;
  width: 66px; height: 66px;
  /* 명확하게 잘 보이는 진한 빨간색 테두리 */
  border: 2px solid rgba(220, 53, 69, 0.8);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transform: rotate(-12deg);
  /* 호버 없이도 항상 진하게 표시 */
  opacity: 0.85;
  gap: 0;
}

.stamp-top {
  font-size: 8px; font-weight: 900;
  color: #dc3545; /* 완전한 레드 컬러 지정 */
  letter-spacing: 2px;
  line-height: 1;
}

.stamp-mid {
  font-size: 9px;
  color: rgba(220, 53, 69, 0.7);
  line-height: 1.2;
}

.stamp-bot {
  font-size: 6px; font-weight: 800;
  color: #dc3545;
  letter-spacing: 0.5px;
  line-height: 1;
}

/* ── 반응형 ──────────────────────────────────────── */
@media (max-width: 480px) {
  .login-card {
    padding: 36px 24px 32px;
    border-radius: 20px;
  }
  .emblem-ring { width: 68px; height: 68px; }
  .emblem-pattern { width: 40px; height: 40px; }
  .login-title { font-size: 16px; }
  .title-tabi { font-size: 18px; }
  .login-stamp { display: none; }
}
</style>
