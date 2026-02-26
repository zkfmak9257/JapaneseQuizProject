<!--
  QuizResultView.vue — 퀴즈 결과 페이지 (프리미엄 여행 UX)
  5-Layer 구조: Hero → Performance → Insight → Reward → CTA
-->
<template>
  <!-- ── 로딩 상태 ── -->
  <div v-if="loading" class="result-page">
    <div class="result-loading">
      <div class="loading-plane">✈️</div>
      <p>여행 기록을 정리하고 있습니다…</p>
    </div>
  </div>

  <!-- ── 에러 상태 ── -->
  <div v-else-if="errorMessage" class="result-page">
    <div class="result-error-card">
      <div class="error-icon">⚠️</div>
      <p class="error-text">{{ errorMessage }}</p>
      <div class="error-actions">
        <button class="btn-primary-sm" @click="loadResult">다시 시도</button>
        <RouterLink class="btn-text" to="/">홈으로</RouterLink>
      </div>
    </div>
  </div>

  <!-- ── 메인 결과 ── -->
  <div v-else-if="result" class="result-page" :class="tierClass">

    <!-- ① HERO — 감정 폭발 -->
    <section class="hero">
      <div class="hero__confetti" v-if="isPerfect">🎊</div>
      <p class="hero__mission">
        {{ sceneName }} 미션 {{ isPerfect ? '완료' : '종료' }}
        <span class="hero__plane">✈️</span>
      </p>

      <!-- 원형 게이지 -->
      <div class="gauge-wrap">
        <svg class="gauge" viewBox="0 0 120 120">
          <!-- 배경 원 -->
          <circle cx="60" cy="60" r="52" fill="none"
                  stroke="#e2e8f0" stroke-width="10" />
          <!-- 진행 원 (dasharray 애니메이션) -->
          <circle cx="60" cy="60" r="52" fill="none"
                  :stroke="gaugeColor" stroke-width="10"
                  stroke-linecap="round"
                  :stroke-dasharray="gaugeDash"
                  stroke-dashoffset="0"
                  class="gauge__fill"
                  transform="rotate(-90 60 60)" />
        </svg>
        <div class="gauge__label">
          <span class="gauge__pct">{{ animatedPct }}%</span>
          <span class="gauge__sub">여행 성공률</span>
        </div>
      </div>

      <p class="hero__mood">{{ moodText }}</p>
    </section>

    <!-- ② PERFORMANCE — 성적표 카드 -->
    <section class="perf-grid">
      <div class="perf-card">
        <span class="perf-card__icon">📝</span>
        <span class="perf-card__num">{{ result.totalQuestions }}</span>
        <span class="perf-card__label">총 문제</span>
      </div>
      <div class="perf-card perf-card--correct">
        <span class="perf-card__icon">✅</span>
        <span class="perf-card__num">{{ result.correctCount }}</span>
        <span class="perf-card__label">정답</span>
      </div>
      <div class="perf-card perf-card--wrong">
        <span class="perf-card__icon">❌</span>
        <span class="perf-card__num">{{ wrongCount }}</span>
        <span class="perf-card__label">오답</span>
      </div>
    </section>

    <!-- ③ INSIGHT — 학습 피드백 -->
    <section class="insight">
      <div class="insight__icon">{{ insightIcon }}</div>
      <p class="insight__text">{{ insightText }}</p>
    </section>

    <!-- ④ REWARD — 보상 배지 -->
    <section class="reward">
      <div class="reward__title">🎖 이번 여행 보상</div>
      <div class="reward__badges">
        <div class="badge" v-for="b in badges" :key="b.id">
          <span class="badge__icon">{{ b.icon }}</span>
          <span class="badge__name">{{ b.name }}</span>
        </div>
      </div>
    </section>

    <!-- ⑤ CTA — 다음 행동 -->
    <section class="cta">
      <button class="cta__primary" @click="startNextQuiz" :disabled="startingNextQuiz">
        {{ startingNextQuiz ? '출발 준비 중…' : '✈️ 다음 여행 떠나기' }}
      </button>
      <RouterLink v-if="wrongCount > 0" class="cta__secondary" to="/quiz/wrong-answers">
        🔥 오답 복습하기
      </RouterLink>
      <div class="cta__links">
        <RouterLink to="/quiz/start">다시 풀기</RouterLink>
        <span class="cta__dot">·</span>
        <RouterLink to="/">홈으로</RouterLink>
      </div>
      <p v-if="nextQuizErrorMessage" class="cta__error">{{ nextQuizErrorMessage }}</p>
    </section>

    <!-- 완료 시각 (아주 작게) -->
    <p class="result-timestamp" v-if="result.completedAt">
      {{ formatCompletedAt(result.completedAt) }}
    </p>
  </div>

  <!-- ── 게스트 모달 ── -->
  <div v-if="isGuest" class="modal-backdrop">
    <section class="guest-modal">
      <div class="guest-modal__icon">🛂</div>
      <h3>여행 기록을 저장하려면</h3>
      <p>로그인하면 오답노트·즐겨찾기·훈장을<br>모두 이용할 수 있습니다.</p>
      <div class="guest-modal__actions">
        <RouterLink class="btn-primary-sm" :to="{ path: '/login', query: { redirect: route.fullPath } }">
          로그인
        </RouterLink>
        <RouterLink class="btn-outline-sm" to="/signup">회원가입</RouterLink>
      </div>
      <RouterLink class="btn-text" to="/">홈으로 돌아가기</RouterLink>
    </section>
  </div>

  <!-- ── 빈 상태 ── -->
  <div v-else-if="!loading && !result && !errorMessage" class="result-page">
    <div class="result-error-card">
      <p class="error-text">표시할 결과가 없습니다.</p>
      <RouterLink class="btn-text" to="/">홈으로</RouterLink>
    </div>
  </div>
</template>

<script setup>
/**
 * QuizResultView — 퀴즈 완료 후 "여행 세션 종료 경험" 제공
 *
 * 데이터 흐름:
 * 1) onMounted → loadResult → API 호출
 * 2) result 수신 → computed로 게이지/감성 문구/배지 생성
 * 3) 정답률 카운트업 애니메이션 (requestAnimationFrame)
 *
 * quizStore.selectedSceneId → 씬 이름 매핑 (여행 감성 유지)
 */
import { computed, onMounted, ref, watch } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { getResult, startQuiz } from "../api/quizApi";
import { useAuthStore } from "../stores/authStore";
import { useQuizStore } from "../stores/quizStore";

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const quizStore = useQuizStore();
const { isLoggedIn } = storeToRefs(authStore);

// 상태 관리
const result = ref(null);
const isGuest = ref(false);
const loading = ref(false);
const errorMessage = ref("");
const startingNextQuiz = ref(false);
const nextQuizErrorMessage = ref("");
const animatedPct = ref(0); // 카운트업 애니메이션용

// ── 씬 이름 매핑 (여행 컨셉 유지) ──
const sceneMap = {
  1: "🛫 공항",
  2: "🏨 호텔",
  3: "🚃 교통",
  4: "🍱 식당",
  5: "🛍️ 쇼핑",
  6: "🏥 긴급 상황",
  7: "📅 일상생활",
  8: "🏛️ 관광지"
};

// ── 씬 이름 (quizStore에서 참조) ──
const sceneName = computed(() => {
  const sid = quizStore.selectedSceneId;
  if (sid && sceneMap[sid]) return sceneMap[sid];
  return "여행";
});

// ── 오답 수 ──
const wrongCount = computed(() => {
  if (!result.value) return 0;
  return result.value.totalQuestions - result.value.correctCount;
});

// ── 정답률 (0~100) ──
const accuracy = computed(() => result.value?.accuracy ?? 0);

// ── 퍼펙트 여부 ──
const isPerfect = computed(() => accuracy.value === 100);

// ── 성적 등급 (CSS 클래스) ──
const tierClass = computed(() => {
  const a = accuracy.value;
  if (a === 100) return 'tier-perfect';
  if (a >= 80) return 'tier-great';
  if (a >= 60) return 'tier-good';
  return 'tier-try';
});

// ── 원형 게이지 계산 ──
// 원 둘레 = 2πr = 2 × π × 52 ≈ 326.7
const circumference = 2 * Math.PI * 52;
const gaugeDash = computed(() => {
  const filled = (accuracy.value / 100) * circumference;
  return `${filled} ${circumference}`;
});
const gaugeColor = computed(() => {
  const a = accuracy.value;
  if (a === 100) return '#16a34a';
  if (a >= 80) return '#0ea5e9';
  if (a >= 60) return '#f59e0b';
  return '#ef4444';
});

// ── 감성 문구 (점수 구간별) ──
const moodText = computed(() => {
  const a = accuracy.value;
  if (a === 100) return '완벽한 여행이었어요! 🌟';
  if (a >= 90) return '거의 완벽! 아주 훌륭해요.';
  if (a >= 80) return '좋은 여행이었어요! 조금만 더 다듬으면 완벽.';
  if (a >= 70) return '괜찮은 여정이에요. 복습하면 금방 올라갈 거예요.';
  if (a >= 50) return '도전은 성장의 시작! 다시 한번 떠나볼까요?';
  return '이번 여행은 좀 험난했네요. 오답 복습이 도움될 거예요.';
});

// ── Insight 영역 ──
const insightIcon = computed(() => {
  if (isPerfect.value) return '🏆';
  if (accuracy.value >= 80) return '📈';
  if (accuracy.value >= 60) return '💡';
  return '📖';
});
const insightText = computed(() => {
  const w = wrongCount.value;
  if (isPerfect.value) return '모든 표현을 정확하게 사용했어요! 실전에서도 자신감을 가져도 좋습니다.';
  if (accuracy.value >= 80) return `${w}문제만 아쉬웠지만, 전체적으로 실력이 탄탄해요.`;
  if (accuracy.value >= 60) return `${w}개 표현이 헷갈렸어요. 오답 복습 한 번이면 확 달라질 거예요.`;
  return `${w}개 표현을 놓쳤어요. 해당 씬의 핵심 표현을 다시 확인해보세요.`;
});

// ── 보상 배지 (프론트 전용, 정답률 기반) ──
const badges = computed(() => {
  const b = [];
  const a = accuracy.value;
  // 씬 클리어 도장 (항상)
  b.push({ id: 'scene', icon: '🗺️', name: `${sceneName.value} 도전 완료` });
  // 정답률 기반 배지
  if (a === 100) b.push({ id: 'perfect', icon: '💎', name: '퍼펙트 클리어!' });
  else if (a >= 80) b.push({ id: 'great', icon: '🌟', name: '우수 여행자' });
  else if (a >= 60) b.push({ id: 'good', icon: '✨', name: '성장 중인 여행자' });
  return b;
});

// ── 시각 포맷 ──
function formatCompletedAt(value) {
  if (!value) return "";
  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return String(value);
  return d.toLocaleString("ko-KR");
}

// ── 카운트업 애니메이션 ──
function animateCount(target) {
  const duration = 1200; // ms
  const start = performance.now();
  function tick(now) {
    const elapsed = now - start;
    const progress = Math.min(elapsed / duration, 1);
    // easeOutCubic
    const eased = 1 - Math.pow(1 - progress, 3);
    animatedPct.value = Math.round(eased * target);
    if (progress < 1) requestAnimationFrame(tick);
  }
  requestAnimationFrame(tick);
}

// ── 결과 로드 ──
async function loadResult() {
  if (!isLoggedIn.value) {
    isGuest.value = true;
    result.value = null;
    errorMessage.value = "";
    return;
  }
  try {
    isGuest.value = false;
    loading.value = true;
    errorMessage.value = "";
    result.value = await getResult(Number(route.params.attemptId));
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) { errorMessage.value = "아직 완료되지 않은 퀴즈입니다."; return; }
    if (status === 401) { errorMessage.value = "로그인이 필요합니다."; return; }
    if (status === 403) { errorMessage.value = "다른 사용자의 결과에는 접근할 수 없습니다."; return; }
    if (status === 404) { errorMessage.value = "결과 정보를 찾을 수 없습니다."; return; }
    errorMessage.value = "결과 조회 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
}

// ── 다음 퀴즈 시작 ──
async function startNextQuiz() {
  if (!isLoggedIn.value) {
    router.push({ path: "/login", query: { redirect: "/quiz/start" } });
    return;
  }
  try {
    startingNextQuiz.value = true;
    nextQuizErrorMessage.value = "";
    const res = await startQuiz();
    router.push(`/quiz/attempts/${res.attemptId}/questions/1`);
  } catch (error) {
    const status = error?.response?.status;
    const code = error?.response?.data?.code;
    if (status === 403 && code === "GUEST_QUIZ_LIMIT_EXCEEDED") {
      nextQuizErrorMessage.value = "비회원은 추가 퀴즈가 제한됩니다. 로그인 후 이용해주세요.";
      return;
    }
    if (status === 401) { nextQuizErrorMessage.value = "로그인이 필요합니다."; return; }
    nextQuizErrorMessage.value = "다음 퀴즈 시작 중 오류가 발생했습니다.";
  } finally {
    startingNextQuiz.value = false;
  }
}

// result가 로드되면 카운트업 애니메이션 시작
watch(result, (val) => {
  if (val) animateCount(val.accuracy ?? 0);
});

onMounted(loadResult);
</script>

<style scoped>
/* ═══════════════════════════════════════════════
   QuizResultView — 여행 세션 종료 경험
   ═══════════════════════════════════════════════ */

/* ── 페이지 전체 ── */
.result-page {
  min-height: 100vh;
  padding: 2rem 1.2rem 3rem;
  display: flex; flex-direction: column; align-items: center; gap: 1.5rem;
  /* 여권 질감 배경 */
  background:
    radial-gradient(ellipse at 30% 20%, rgba(251,191,36,0.06) 0%, transparent 60%),
    linear-gradient(180deg, #fefce8 0%, #fffdf7 40%, #f0f9ff 100%);
  position: relative;
  max-width: 540px; margin: 0 auto;
}

/* ── 로딩 ── */
.result-loading {
  display: flex; flex-direction: column; align-items: center; gap: 1rem;
  padding-top: 30vh; color: #64748b; font-size: 0.95rem;
}
.loading-plane {
  font-size: 3rem;
  animation: planeFloat 2s ease-in-out infinite;
}
@keyframes planeFloat {
  0%,100% { transform: translateY(0); }
  50% { transform: translateY(-12px); }
}

/* ── 에러 ── */
.result-error-card {
  margin-top: 15vh; padding: 2rem; border-radius: 16px;
  background: white; box-shadow: 0 4px 20px rgba(0,0,0,0.06);
  text-align: center; display: flex; flex-direction: column; align-items: center; gap: 1rem;
}
.error-icon { font-size: 2.5rem; }
.error-text { color: #dc2626; font-size: 0.95rem; font-weight: 600; margin: 0; }
.error-actions { display: flex; gap: 0.8rem; }

/* ── ① HERO ── */
.hero {
  text-align: center; padding: 1.5rem 0 0; position: relative;
  animation: heroFadeIn 0.6s ease-out;
}
@keyframes heroFadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.hero__confetti {
  position: absolute; top: -8px; right: 10%;
  font-size: 2rem;
  animation: confettiPop 0.8s cubic-bezier(0.34,1.56,0.64,1) both;
}
@keyframes confettiPop {
  0% { transform: scale(0) rotate(-20deg); opacity: 0; }
  100% { transform: scale(1) rotate(0); opacity: 1; }
}
.hero__mission {
  font-size: 1.4rem; font-weight: 800; color: #0f172a;
  margin: 0 0 1.5rem; line-height: 1.4;
}
.hero__plane {
  display: inline-block;
  animation: planeTakeoff 1.5s ease-out 0.5s both;
}
@keyframes planeTakeoff {
  0% { transform: translateX(-10px) rotate(0); opacity: 0; }
  50% { opacity: 1; }
  100% { transform: translateX(0) rotate(-5deg); opacity: 1; }
}

/* 원형 게이지 */
.gauge-wrap {
  position: relative; width: 160px; height: 160px; margin: 0 auto 1rem;
}
.gauge { width: 100%; height: 100%; }
.gauge__fill {
  transition: stroke-dasharray 1.2s cubic-bezier(0.4,0,0.2,1);
}
.gauge__label {
  position: absolute; inset: 0; display: flex; flex-direction: column;
  align-items: center; justify-content: center;
}
.gauge__pct {
  font-size: 2.8rem; font-weight: 900; line-height: 1;
  color: #0f172a;
}
.gauge__sub {
  font-size: 0.75rem; font-weight: 600; color: #94a3b8;
  letter-spacing: 0.06em; margin-top: 4px;
}
.hero__mood {
  font-size: 1rem; color: #475569; font-weight: 600; margin: 0;
  animation: moodFadeIn 0.8s ease-out 0.6s both;
}
@keyframes moodFadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* ── ② PERFORMANCE 카드 그리드 ── */
.perf-grid {
  display: grid; grid-template-columns: repeat(3, 1fr); gap: 0.8rem;
  width: 100%;
  animation: perfSlideUp 0.5s ease-out 0.3s both;
}
@keyframes perfSlideUp {
  from { opacity: 0; transform: translateY(15px); }
  to { opacity: 1; transform: translateY(0); }
}
.perf-card {
  padding: 1rem 0.5rem; border-radius: 14px;
  background: white; border: 1px solid #e2e8f0;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04);
  text-align: center; display: flex; flex-direction: column; align-items: center; gap: 0.3rem;
}
.perf-card--correct { border-color: #86efac; background: #f0fdf4; }
.perf-card--wrong { border-color: #fca5a5; background: #fef2f2; }
.perf-card__icon { font-size: 1.3rem; }
.perf-card__num { font-size: 1.8rem; font-weight: 900; color: #0f172a; line-height: 1; }
.perf-card__label { font-size: 0.72rem; font-weight: 700; color: #94a3b8; letter-spacing: 0.04em; }

/* ── ③ INSIGHT ── */
.insight {
  width: 100%; padding: 1.2rem; border-radius: 14px;
  background: white; border: 1px solid #e0f2fe;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04);
  display: flex; align-items: flex-start; gap: 0.8rem;
  animation: perfSlideUp 0.5s ease-out 0.5s both;
}
.insight__icon { font-size: 1.5rem; flex-shrink: 0; }
.insight__text { font-size: 0.92rem; color: #334155; line-height: 1.6; margin: 0; font-weight: 500; }

/* ── ④ REWARD ── */
.reward {
  width: 100%; padding: 1.2rem; border-radius: 14px;
  background: linear-gradient(135deg, #fffbeb, #fef3c7);
  border: 1px solid #fde68a;
  animation: perfSlideUp 0.5s ease-out 0.7s both;
}
.reward__title {
  font-size: 0.82rem; font-weight: 800; color: #92400e;
  letter-spacing: 0.04em; margin-bottom: 0.8rem;
}
.reward__badges { display: flex; flex-wrap: wrap; gap: 0.6rem; }
.badge {
  display: flex; align-items: center; gap: 0.4rem;
  padding: 0.4rem 0.8rem; border-radius: 999px;
  background: rgba(255,255,255,0.7); border: 1px solid #fde68a;
  font-size: 0.85rem; font-weight: 700; color: #78350f;
}
.badge__icon { font-size: 1.1rem; }

/* ── ⑤ CTA ── */
.cta {
  width: 100%; display: flex; flex-direction: column; align-items: center; gap: 0.8rem;
  padding-top: 0.5rem;
  animation: perfSlideUp 0.5s ease-out 0.9s both;
}
.cta__primary {
  width: 100%; padding: 1.1rem; font-size: 1.1rem; font-weight: 800;
  border: none; border-radius: 14px;
  background: #0f172a; color: #fbbf24;
  box-shadow: 0 6px 20px rgba(15,23,42,0.25);
  cursor: pointer; transition: all 0.2s cubic-bezier(0.34,1.56,0.64,1);
}
.cta__primary:hover:not(:disabled) {
  background: #1e293b; transform: translateY(-3px) scale(1.01);
  box-shadow: 0 8px 25px rgba(15,23,42,0.35);
}
.cta__primary:active { transform: translateY(0) scale(0.99); }
.cta__primary:disabled { opacity: 0.6; cursor: not-allowed; }
.cta__secondary {
  width: 100%; padding: 0.9rem; font-size: 0.95rem; font-weight: 700;
  text-align: center; text-decoration: none;
  border: 2px solid #ef4444; border-radius: 14px;
  color: #ef4444; background: white;
  transition: all 0.15s;
}
.cta__secondary:hover { background: #fef2f2; }
.cta__links {
  display: flex; gap: 0.5rem; align-items: center;
  font-size: 0.85rem; color: #94a3b8;
}
.cta__links a { color: #64748b; text-decoration: none; font-weight: 600; }
.cta__links a:hover { color: #0f172a; text-decoration: underline; }
.cta__dot { color: #cbd5e1; }
.cta__error { font-size: 0.82rem; color: #dc2626; text-align: center; margin: 0; }

/* ── 타임스탬프 ── */
.result-timestamp {
  font-size: 0.72rem; color: #cbd5e1; margin: 0; text-align: center;
}

/* ── 게스트 모달 ── */
.modal-backdrop {
  position: fixed; inset: 0; z-index: 100;
  background: rgba(15,23,42,0.5); backdrop-filter: blur(4px);
  display: flex; align-items: center; justify-content: center; padding: 1rem;
}
.guest-modal {
  background: white; border-radius: 20px; padding: 2.5rem 2rem;
  max-width: 380px; width: 100%; text-align: center;
  box-shadow: 0 20px 50px rgba(0,0,0,0.15);
  animation: modalPop 0.35s ease-out;
}
@keyframes modalPop {
  from { opacity: 0; transform: scale(0.92) translateY(10px); }
  to { opacity: 1; transform: scale(1) translateY(0); }
}
.guest-modal__icon { font-size: 2.5rem; margin-bottom: 0.8rem; }
.guest-modal h3 { margin: 0 0 0.5rem; font-size: 1.2rem; color: #0f172a; }
.guest-modal p { margin: 0 0 1.5rem; font-size: 0.9rem; color: #64748b; line-height: 1.6; }
.guest-modal__actions { display: flex; gap: 0.6rem; justify-content: center; margin-bottom: 1rem; }

/* ── 공통 버튼 ── */
.btn-primary-sm {
  padding: 0.6rem 1.5rem; border-radius: 10px; font-size: 0.9rem; font-weight: 700;
  background: #0f172a; color: #fbbf24; border: none; cursor: pointer;
  text-decoration: none; transition: all 0.15s;
}
.btn-primary-sm:hover { background: #1e293b; }
.btn-outline-sm {
  padding: 0.6rem 1.5rem; border-radius: 10px; font-size: 0.9rem; font-weight: 700;
  background: white; color: #334155; border: 1px solid #cbd5e1; cursor: pointer;
  text-decoration: none; transition: all 0.15s;
}
.btn-outline-sm:hover { border-color: #94a3b8; }
.btn-text {
  font-size: 0.82rem; color: #94a3b8; text-decoration: none; font-weight: 600;
}
.btn-text:hover { color: #0f172a; text-decoration: underline; }

/* ── 등급별 미세 조정 ── */
.tier-perfect .hero__pct { color: #16a34a; }
.tier-great .hero__pct { color: #0ea5e9; }
.tier-try .insight { border-color: #fca5a5; background: #fef2f2; }
</style>
