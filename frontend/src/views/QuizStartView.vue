<template>
  <div class="boarding-page" :class="{ 'is-departing': isDeparting }">

    <!-- 페이지 단독 풀스크린 느낌을 위한 헤더 (상단 내비게이션을 덮는 느낌 연출) -->
    <div class="airport-header">
      <!-- 로고 제거 요청 -->
      <div></div>
      <button class="back-btn" @click="router.back()">← 돌아가기</button>
    </div>

    <!-- 비행기 이륙 애니메이션 오버레이 -->
    <div v-if="isDeparting" class="takeoff-overlay">
      <div class="airplane">✈︎</div>
      <div class="clouds-fx">☁ ☁ ☁</div>
    </div>

    <div class="ticket-card">
      <div class="ticket-top">
        <h1 class="gate-title">BOARDING GATE</h1>
        <div class="gate-subtitle">타비퀴즈 출발 게이트</div>
      </div>

      <div class="ticket-middle">
        <!-- 10이라는 숫자 극대화 -->
        <div class="question-count-block">
          <span class="q-num">10</span>
          <span class="q-text">QUESTIONS</span>
        </div>

        <div class="dotted-line"></div>

        <!-- 3️⃣ 테이블이 아닌 카드식 정보 구조 -->
        <div class="flight-info-grid">
          <div class="info-item full-width">
            <span class="i-label">모드</span>
            <span class="i-value">{{ modeLabel }}</span>
          </div>
          <div class="info-item full-width">
            <span class="i-label">상황</span>
            <span class="i-value">{{ sceneLabel }}</span>
          </div>
          <div class="info-item full-width">
            <span class="i-label">예상 소요 시간</span>
            <span class="i-value time-val">약 3~5분</span>
          </div>
        </div>

        <div class="dotted-line"></div>

        <!-- 4️⃣ 오늘의 미션 박스화 -->
        <div class="mission-box">
          <div class="m-badge">오늘의 미션 미리보기</div>
          <div class="m-text">“{{ randomMission }}”</div>
        </div>

        <div v-if="errorMessage" class="error-msg">
          ⚠ {{ errorMessage }}
        </div>
      </div>

      <!-- 5️⃣ 가장 강렬한 하단 분리형 CTA 영역 -->
      <div class="ticket-bottom">
        <button class="depart-btn" @click="startBoarding" :disabled="loading || isDeparting">
          <span v-if="loading" class="btn-loader"></span>
          <span v-else class="btn-text-group">
            <span class="btn-main">✈ 탑승 시작하기</span>
            <span class="btn-sub">START BOARDING</span>
          </span>
        </button>
        <div class="footer-wish">안전하고 즐거운 여행 되세요.</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { startQuiz } from "../api/quizApi";
import { useQuizStore } from "../stores/quizStore";

const router = useRouter();
const route = useRoute();
const quizStore = useQuizStore();

const loading = ref(false);
const errorMessage = ref("");
const isDeparting = ref(false);

const selectedQuestionType = computed(() => {
  const value = String(route.query.questionType || "").toUpperCase();
  if (value === "WORD" || value === "SENTENCE") return value;
  return null;
});

const selectedSceneId = computed(() => {
  const raw = route.query.sceneId;
  if (!raw) return null;
  const parsed = Number(raw);
  return Number.isNaN(parsed) || parsed <= 0 ? null : parsed;
});

const modeLabel = computed(() => {
  return selectedQuestionType.value === "WORD" ? "📝 단어" :
         selectedQuestionType.value === "SENTENCE" ? "💬 문장" : "단어";
});

const sceneMap = {
  1: "✈️ 공항 / 입출국",
  2: "🚉 교통 / 이동",
  3: "🏨 숙박",
  4: "🍣 음식 / 술",
  5: "🏪 쇼핑 / 상점",
  6: "🌙 야간 / 즐길거리",
  7: "🚨 긴급 상황",
  8: "🏛️ 관광지 / 명소"
};

const sceneLabel = computed(() => {
  if (selectedSceneId.value && sceneMap[selectedSceneId.value]) {
    return sceneMap[selectedSceneId.value];
  }
  return "🗺️ 전체 (랜덤 출제)";
});

const missions = [
  "수하물을 맡기고 싶습니다.",
  "이 근처에 역이 있나요?",
  "체크인 부탁드립니다.",
  "메뉴판 좀 주시겠어요?",
  "이거 얼마인가요?",
  "사진 좀 찍어주시겠어요?",
  "따뜻한 커피 한 잔 주세요.",
  "오늘의 추천 메뉴는 뭔가요?",
  "여기서 유명한 곳이 어딘가요?"
];
const randomMission = ref("");

onMounted(() => {
  // 실제 앱 레이아웃(헤더 등)을 덮어버리기 위한 임시 꼼수 (CSS fixed)
  const idx = Math.floor(Math.random() * missions.length);
  randomMission.value = missions[idx];
});

async function startBoarding() {
  try {
    loading.value = true;
    errorMessage.value = "";
    
    const res = await startQuiz({
      questionType: selectedQuestionType.value || undefined,
      sceneId: selectedSceneId.value || undefined
    });
    
    quizStore.setStartOptions({
      questionType: selectedQuestionType.value,
      sceneId: selectedSceneId.value
    });
    quizStore.setCurrentAttempt(res.attemptId);
    
    isDeparting.value = true;
    
    // 1.5초의 이륙 시간 대기 
    setTimeout(() => {
      router.push(`/quiz/attempts/${res.attemptId}/questions/1`);
    }, 1500);

  } catch (error) {
    loading.value = false;
    const status = error?.response?.status;
    const code = error?.response?.data?.code;
    
    if (status === 400) errorMessage.value = "이용할 수 없는 노선입니다.";
    else if (status === 401) errorMessage.value = "여권 등 인증이 만료되었습니다. 다시 로그인해 주세요.";
    else if (status === 403 && code === "GUEST_QUIZ_LIMIT_EXCEEDED") errorMessage.value = "여행 제한 구역입니다. 로그인 후 입장해주세요.";
    else if (status === 403) errorMessage.value = "접근 권한이 없습니다.";
    else if (status === 404) errorMessage.value = "게이트를 찾을 수 없습니다.";
    else errorMessage.value = "시스템 지연으로 출발 대기 중입니다.";
  }
}
</script>

<style scoped>
/* ── 공항 보딩 패스 느낌 극대화 ── */
.boarding-page {
  /* 글로벌 App.vue 헤더를 덮어버리는 효과를 주기 위해 화면 전체 고정 */
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  z-index: 1000; /* 네비게이션 무시 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #0f172a; /* 공항 밤하늘 네이비 */
  padding: 24px;
  overflow-y: auto;
  font-family: Pretendard, "Noto Sans KR", sans-serif;
}

/* 단순화된 전용 헤더 (글로벌 덮음) */
.airport-header {
  position: absolute;
  top: 0; left: 0; right: 0;
  padding: 24px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 10;
}
/* ── 비행기 이륙 애니메이션 오버레이 ── */
.takeoff-overlay {
  position: fixed;
  inset: 0;
  z-index: 2000;
  background: rgba(15, 23, 42, 0);
  pointer-events: none;
  animation: bgFadeIn 1.5s ease-in forwards;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

@keyframes bgFadeIn {
  0% { background: rgba(15, 23, 42, 0); }
  50% { background: rgba(56, 189, 248, 0.4); } /* 구름층 파란 하늘 느낌 */
  100% { background: #0f172a; } /* 완전 암전 후 넘어감 */
}

/* 비행기 본체 */
.airplane {
  position: absolute;
  font-size: 120px;
  color: #fff;
  transform: translate(-100vw, 100vh) rotate(45deg);
  animation: flyoff 1.2s cubic-bezier(0.4, 0, 0.2, 1) forwards;
  filter: drop-shadow(0 10px 20px rgba(0,0,0,0.5));
}

@keyframes flyoff {
  0% { transform: translate(-100vw, 60vh) rotate(35deg) scale(0.5); opacity: 0; }
  30% { opacity: 1; transform: translate(-40vw, 20vh) rotate(40deg) scale(0.8); }
  100% { transform: translate(100vw, -80vh) rotate(45deg) scale(1.5); opacity: 0; }
}

/* 구름 효과 */
.clouds-fx {
  position: absolute;
  font-size: 200px;
  color: rgba(255,255,255,0.2);
  transform: scale(3);
  opacity: 0;
  animation: cloudZoom 1s ease-out 0.3s forwards;
  filter: blur(8px);
}

@keyframes cloudZoom {
  0% { transform: scale(3) translateY(20%); opacity: 0; }
  50% { opacity: 1; }
  100% { transform: scale(8) translateY(-20%); opacity: 0; }
}


/* ── 비행기 티켓/디스플레이 하이브리드 카드 ── */
.ticket-card {
  width: min(440px, 100%);
  background: #1e293b;
  border-radius: 16px;
  box-shadow: 0 32px 64px rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 10;
  overflow: hidden;
  animation: ticketIn 0.6s cubic-bezier(0.2, 0.8, 0.2, 1) both;
}

@keyframes ticketIn {
  0% { opacity: 0; transform: translateY(40px) scale(0.95); }
  100% { opacity: 1; transform: translateY(0) scale(1); }
}

/* 상단 타이틀 영역 (시각적 분리) */
.ticket-top {
  background: #0f172a;
  padding: 32px 24px 24px 24px;
  text-align: center;
  border-bottom: 2px dashed #475569;
  position: relative;
}
/* 티켓 잘린 느낌의 좌우 반원 홈 */
.ticket-top::before, .ticket-top::after {
  content: ''; position: absolute; bottom: -12px;
  width: 24px; height: 24px; background: #0f172a; border-radius: 50%;
}
.ticket-top::before { left: -12px; }
.ticket-top::after { right: -12px; }

.gate-title {
  font-size: 28px;
  font-weight: 900;
  color: #f8fafc;
  letter-spacing: 6px;
  margin: 0;
  line-height: 1.1;
  font-family: ui-monospace, monospace;
}
.gate-subtitle {
  font-size: 14px;
  font-weight: 600;
  color: #38bdf8;
  margin-top: 8px;
  letter-spacing: 2px;
}


/* 본문 (정보 및 미션 영역) */
.ticket-middle {
  padding: 32px 32px 24px;
  background: #1e293b;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 6️⃣ 문제 수 고정 -> 10을 거대하게 하이라이트 */
.question-count-block {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}
.q-num {
  font-size: 80px;
  font-weight: 900;
  line-height: 1;
  color: #fca5a5; /* 티켓의 붉은 스탬프 포인트 느낌 */
  text-shadow: 0 4px 20px rgba(248, 113, 113, 0.2);
  font-family: ui-monospace, sans-serif;
}
.q-text {
  font-size: 16px;
  font-weight: 800;
  letter-spacing: 4px;
  color: #94a3b8;
  margin-top: 4px;
}

/* 카드식 정보 그리드 */
.flight-info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  row-gap: 20px;
  column-gap: 16px;
}
.full-width { grid-column: 1 / -1; }
.info-item { display: flex; flex-direction: column; gap: 6px; }

.i-label {
  font-size: 12px;
  font-weight: 700;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 1px;
}
.i-value {
  font-size: 16px;
  font-weight: 700;
  color: #f1f5f9;
}
.time-val { color: #cbd5e1; font-weight: 500; }
.highlight { color: #fca5a5; font-size: 18px; }

.dotted-line {
  border-top: 2px dotted #334155;
  width: 100%;
}

/* 4️⃣ 오늘의 미션 박스화 강조 */
.mission-box {
  background: rgba(56, 189, 248, 0.05);
  border: 1px solid rgba(56, 189, 248, 0.2);
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}
.m-badge {
  display: inline-block;
  background: #0f172a;
  color: #38bdf8;
  font-size: 11px;
  font-weight: 800;
  padding: 4px 10px;
  border-radius: 12px;
  margin-bottom: 12px;
  letter-spacing: 1px;
}
.m-text {
  font-size: 20px;
  font-weight: 700;
  color: #f8fafc;
  line-height: 1.4;
  word-break: keep-all;
}

.error-msg {
  color: #fca5a5; font-size: 13px; text-align: center; font-weight: 600;
}


/* 5️⃣ 강렬한 CTA 구역 (하단 분리형) */
.ticket-bottom {
  background: #0f172a;
  padding: 24px 32px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.depart-btn {
  width: 100%;
  background: #fca5a5; /* 빨간색 포인트 유지 */
  color: #0f172a;
  border: none;
  padding: 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 8px 16px rgba(248, 113, 113, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
}
.btn-text-group { 
  display: flex; 
  flex-direction: column; 
  align-items: center; 
  justify-content: center;
  gap: 4px;
}
.btn-main { 
  font-size: 20px; 
  font-weight: 900; 
  letter-spacing: 1px; 
  white-space: nowrap; /* 줄바꿈 방지 */
}
.btn-sub { 
  font-size: 12px; 
  font-weight: 800; 
  letter-spacing: 3px; 
  opacity: 0.8; 
  white-space: nowrap; /* 줄바꿈 방지 */
}

.depart-btn:hover:not(:disabled) {
  background: #fecaca;
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 12px 24px rgba(248, 113, 113, 0.25);
}
.depart-btn:active:not(:disabled) {
  transform: translateY(2px) scale(0.98);
}
.depart-btn:disabled { opacity: 0.6; cursor: not-allowed; }

.btn-loader {
  width: 24px; height: 24px; border: 3px solid rgba(15, 23, 42, 0.2);
  border-top-color: #0f172a; border-radius: 50%;
  animation: spin 0.6s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.footer-wish {
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  margin-top: 20px;
  letter-spacing: 1px;
}

/* 반응형 모바일 */
@media (max-width: 600px) {
  .airport-header { padding: 16px 20px; }
  .boarding-page { padding: 80px 16px 24px; justify-content: flex-start; }
  .ticket-card { margin-top: 20px; }
  .ticket-top { padding: 24px 20px 16px; }
  .gate-title { font-size: 24px; }
  .ticket-middle { padding: 24px 20px; }
  .q-num { font-size: 64px; }
  .m-text { font-size: 18px; }
  .ticket-bottom { padding: 20px 24px 24px; }
  .btn-main { font-size: 18px; }
}
</style>
