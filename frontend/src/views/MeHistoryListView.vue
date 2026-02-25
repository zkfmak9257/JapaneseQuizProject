<template>
  <div class="journal-container">

    <!-- 헤더 -->
    <header class="journal-header">
<<<<<<< feat/pcj/history-page-and-quiz-category
      <div class="header-bg-overlay"></div>
      <div class="header-text">
        <h1 class="header-title">나의 여행 기록</h1>
        <p class="header-subtitle">일본어 여행을 통해 쌓아온 나의 발자취</p>
      </div>
=======
      <div class="header-icon">🗺️</div>
      <h1 class="header-title">나의 여행 기록</h1>
      <p class="header-subtitle">일본어 여행을 통해 쌓아온 나의 발자취</p>
      <div class="teal-divider"></div>
>>>>>>> main
    </header>

    <main class="journal-content">

      <!-- 로딩 -->
      <div v-if="loading" class="state-msg">여행 기록을 펼치는 중입니다...</div>

      <!-- 에러 -->
      <div v-else-if="errorMessage" class="state-msg error">{{ errorMessage }}</div>

      <!-- 데이터 없음 -->
      <div v-else-if="isEmpty" class="empty-state">
        <div class="empty-icon">✈️</div>
        <p>아직 시작된 여행이 없습니다.<br>첫 번째 퀴즈 여행을 떠나보세요!</p>
        <button class="action-btn primary" @click="router.push('/quiz/start')">첫 여행 시작하기</button>
      </div>

      <template v-else>

        <!-- 1. 핵심 통계 카드 -->
        <section class="stat-cards">
          <div class="stat-card">
            <div class="stat-icon">✈️</div>
            <div class="stat-body">
              <p class="stat-label">총 여행 횟수</p>
              <p class="stat-value">{{ stats.totalAttempts }}<span class="stat-unit">회</span></p>
            </div>
          </div>
          <div class="stat-card accent-green">
            <div class="stat-icon">📍</div>
            <div class="stat-body">
              <p class="stat-label">완료한 여행</p>
              <p class="stat-value">{{ stats.completedAttempts }}<span class="stat-unit">회</span></p>
            </div>
          </div>
          <div class="stat-card accent-blue">
            <div class="stat-icon">📝</div>
            <div class="stat-body">
              <p class="stat-label">총 답변 수</p>
              <p class="stat-value">{{ stats.totalAnswers }}<span class="stat-unit">개</span></p>
            </div>
          </div>
          <div class="stat-card accent-amber">
            <div class="stat-icon">🎯</div>
            <div class="stat-body">
              <p class="stat-label">최근 7일 답변</p>
              <p class="stat-value">{{ stats.recent7dAnswers }}<span class="stat-unit">개</span></p>
            </div>
          </div>
        </section>

        <!-- 2. 차트 섹션 -->
        <section class="charts-section">

          <!-- 정답률 도넛 차트 -->
          <div class="chart-card">
            <h3 class="chart-title">🎯 정답률</h3>
            <div class="chart-wrap">
              <canvas ref="accuracyChartRef"></canvas>
            </div>
            <div class="chart-legend">
              <span class="legend-dot correct"></span>정답 {{ stats.correctAnswers }}개
              <span class="legend-dot wrong"></span>오답 {{ wrongAnswers }}개
            </div>
            <p class="chart-highlight">{{ stats.accuracyRate }}<span class="highlight-unit">%</span></p>
          </div>

          <!-- 완료율 도넛 차트 -->
          <div class="chart-card">
            <h3 class="chart-title">📍 여행 완료율</h3>
            <div class="chart-wrap">
              <canvas ref="completionChartRef"></canvas>
            </div>
            <div class="chart-legend">
              <span class="legend-dot done"></span>완료 {{ stats.completedAttempts }}회
              <span class="legend-dot undone"></span>미완료 {{ incompletedAttempts }}회
            </div>
            <p class="chart-highlight">{{ stats.completionRate }}<span class="highlight-unit">%</span></p>
          </div>

        </section>

        <!-- 3. 달성 배지 섹션 -->
        <section class="badge-section">
          <h3 class="section-title">🏅 여행 훈장</h3>
          <div class="badge-grid">
            <div class="badge-item" :class="{ unlocked: stats.totalAttempts >= 1 }">
              <div class="badge-icon">🛫</div>
              <p class="badge-name">첫 출발</p>
              <p class="badge-desc">첫 퀴즈 도전</p>
            </div>
            <div class="badge-item" :class="{ unlocked: stats.completedAttempts >= 5 }">
              <div class="badge-icon">🗺️</div>
              <p class="badge-name">탐험가</p>
              <p class="badge-desc">5회 완료</p>
            </div>
            <div class="badge-item" :class="{ unlocked: stats.accuracyRate >= 80 }">
              <div class="badge-icon">⭐</div>
              <p class="badge-name">언어 마스터</p>
              <p class="badge-desc">정답률 80%↑</p>
            </div>
            <div class="badge-item" :class="{ unlocked: stats.recent7dAnswers >= 30 }">
              <div class="badge-icon">🔥</div>
              <p class="badge-name">열혈 여행자</p>
              <p class="badge-desc">7일 30답변↑</p>
            </div>
            <div class="badge-item" :class="{ unlocked: stats.totalAttempts >= 20 }">
              <div class="badge-icon">🏆</div>
              <p class="badge-name">베테랑</p>
              <p class="badge-desc">20회 도전</p>
            </div>
            <div class="badge-item" :class="{ unlocked: stats.completionRate >= 90 }">
              <div class="badge-icon">💎</div>
              <p class="badge-name">완벽주의자</p>
              <p class="badge-desc">완료율 90%↑</p>
            </div>
          </div>
        </section>

        <!-- 4. 빠른 이동 -->
        <section class="quick-nav">
          <h3 class="section-title">🧭 여행 이어가기</h3>
          <div class="nav-grid">
<<<<<<< feat/pcj/history-page-and-quiz-category
            <button class="nav-card" @click="router.push('/quiz/start?category=true')">
=======
            <button class="nav-card" @click="router.push('/quiz/start')">
>>>>>>> main
              <span class="nav-icon">✈️</span>
              <span class="nav-label">새 퀴즈 출발</span>
              <span class="nav-arrow">→</span>
            </button>
            <button class="nav-card" @click="router.push('/quiz/wrong-answers')">
              <span class="nav-icon">🔥</span>
              <span class="nav-label">미션 훈련소</span>
              <span class="nav-arrow">→</span>
            </button>
            <button class="nav-card" @click="router.push('/quiz/favorites')">
              <span class="nav-icon">🛂</span>
              <span class="nav-label">나의 도장첩</span>
              <span class="nav-arrow">→</span>
            </button>
            <button class="nav-card" @click="router.push('/me/stats')">
              <span class="nav-icon">📊</span>
              <span class="nav-label">개인 통계</span>
              <span class="nav-arrow">→</span>
            </button>
          </div>
        </section>

      </template>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { Chart, DoughnutController, ArcElement, Tooltip, Legend } from "chart.js";
import { getMyStats } from "../api/statsApi";

Chart.register(DoughnutController, ArcElement, Tooltip, Legend);

const router = useRouter();

const stats = reactive({
  totalAttempts: 0,
  completedAttempts: 0,
  totalAnswers: 0,
  correctAnswers: 0,
  recent7dAnswers: 0,
  completionRate: 0,
  accuracyRate: 0,
});

const loading = ref(false);
const errorMessage = ref("");

const accuracyChartRef = ref(null);
const completionChartRef = ref(null);
let accuracyChart = null;
let completionChart = null;

const wrongAnswers = computed(() =>
  Math.max(0, stats.totalAnswers - stats.correctAnswers)
);
const incompletedAttempts = computed(() =>
  Math.max(0, stats.totalAttempts - stats.completedAttempts)
);
const isEmpty = computed(
  () => stats.totalAttempts === 0 && stats.totalAnswers === 0
);

function buildCharts() {
  const PALETTE = {
    correct: "#22c55e",
    wrong: "#f87171",
    done: "#3b82f6",
    undone: "#e2e8f0",
    correctBg: "rgba(34,197,94,0.15)",
    wrongBg: "rgba(248,113,113,0.15)",
  };

  const tooltipPlugin = {
    backgroundColor: "#1e293b",
    titleColor: "#f8fafc",
    bodyColor: "#cbd5e1",
    padding: 10,
    cornerRadius: 8,
  };

  if (accuracyChart) accuracyChart.destroy();
  if (completionChart) completionChart.destroy();

  accuracyChart = new Chart(accuracyChartRef.value, {
    type: "doughnut",
    data: {
      labels: ["정답", "오답"],
      datasets: [{
        data: [stats.correctAnswers || 0, wrongAnswers.value || 0],
        backgroundColor: [PALETTE.correct, PALETTE.wrong],
        borderColor: ["#fff", "#fff"],
        borderWidth: 3,
        hoverOffset: 6,
      }],
    },
    options: {
      cutout: "72%",
      plugins: {
        legend: { display: false },
        tooltip: tooltipPlugin,
      },
      animation: { animateRotate: true, duration: 900 },
    },
  });

  completionChart = new Chart(completionChartRef.value, {
    type: "doughnut",
    data: {
      labels: ["완료", "미완료"],
      datasets: [{
        data: [stats.completedAttempts || 0, incompletedAttempts.value || 0],
        backgroundColor: [PALETTE.done, PALETTE.undone],
        borderColor: ["#fff", "#fff"],
        borderWidth: 3,
        hoverOffset: 6,
      }],
    },
    options: {
      cutout: "72%",
      plugins: {
        legend: { display: false },
        tooltip: tooltipPlugin,
      },
      animation: { animateRotate: true, duration: 900 },
    },
  });
}

async function loadStats() {
  try {
    loading.value = true;
    errorMessage.value = "";
    const data = await getMyStats();
    stats.totalAttempts = data?.totalAttempts ?? 0;
    stats.completedAttempts = data?.completedAttempts ?? 0;
    stats.totalAnswers = data?.totalAnswers ?? 0;
    stats.correctAnswers = data?.correctAnswers ?? 0;
    stats.recent7dAnswers = data?.recent7dAnswers ?? 0;
    stats.completionRate = data?.completionRate ?? 0;
    stats.accuracyRate = data?.accuracyRate ?? 0;
  } catch (e) {
    const status = e?.response?.status;
    errorMessage.value =
      status === 401 ? "로그인이 필요합니다." :
      status === 403 ? "접근 권한이 없습니다." :
      "여행 기록을 불러오지 못했습니다.";
  } finally {
    loading.value = false;
    if (!errorMessage.value && !isEmpty.value) {
      // DOM 렌더링 후 차트 생성
      setTimeout(buildCharts, 50);
    }
  }
}

onMounted(loadStats);
onUnmounted(() => {
  accuracyChart?.destroy();
  completionChart?.destroy();
});
</script>

<style scoped>
/* ── 기본 레이아웃 ── */
.journal-container {
  min-height: 100vh;
  background-color: #f8fafc;
  padding: 40px 16px 80px;
  font-family: Pretendard, "Noto Sans KR", sans-serif;
  color: #1e293b;
}

/* ── 헤더 ── */
.journal-header {
<<<<<<< feat/pcj/history-page-and-quiz-category
  position: relative;
  width: calc(100% + 32px);
  margin: -40px -16px 36px;
  height: 200px;
  background-image: url('/bg/img.png');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: flex-end;
  overflow: hidden;
  border-radius: 0 0 24px 24px;
}
.header-bg-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    to bottom,
    rgba(15, 23, 42, 0.2) 0%,
    rgba(15, 23, 42, 0.75) 100%
  );
}
.header-text {
  position: relative;
  z-index: 1;
  padding: 24px 24px 28px;
}
.header-title {
  font-size: 24px; font-weight: 900; color: #fff;
  letter-spacing: 1px; margin: 0 0 6px;
  text-shadow: 0 2px 8px rgba(0,0,0,0.4);
}
.header-subtitle {
  font-size: 13px; font-weight: 600; color: rgba(255,255,255,0.8);
  margin: 0;
  text-shadow: 0 1px 4px rgba(0,0,0,0.4);
}
=======
  text-align: center;
  margin-bottom: 36px;
}
.header-icon { font-size: 36px; margin-bottom: 8px; }
.header-title {
  font-size: 26px; font-weight: 900; color: #0f172a;
  letter-spacing: 2px; margin: 0 0 8px;
}
.header-subtitle { font-size: 14px; font-weight: 600; color: #64748b; margin: 0 0 16px; }
.teal-divider { width: 80px; height: 3px; background: #14b8a6; margin: 0 auto; border-radius: 2px; }
>>>>>>> main

/* ── 콘텐츠 래퍼 ── */
.journal-content {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 28px;
}

/* ── 상태 메시지 ── */
.state-msg { text-align: center; padding: 60px; color: #64748b; font-weight: 600; }
.state-msg.error { color: #ef4444; }
.empty-state {
  text-align: center; padding: 60px 20px;
  background: white; border: 2px dashed #e2e8f0; border-radius: 20px;
  color: #64748b; line-height: 1.6;
  display: flex; flex-direction: column; align-items: center; gap: 16px;
}
.empty-icon { font-size: 52px; }
.action-btn.primary {
  padding: 12px 28px; border-radius: 12px; border: none;
  background: #0f172a; color: white;
  font-size: 15px; font-weight: 700; cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}
.action-btn.primary:hover { transform: translateY(-2px); box-shadow: 0 6px 12px rgba(15,23,42,0.25); }

/* ── 1. 통계 카드 ── */
.stat-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px;
}
@media (min-width: 540px) {
  .stat-cards { grid-template-columns: repeat(4, 1fr); }
}
.stat-card {
  background: white;
  border-radius: 16px;
  padding: 20px 16px;
  border: 1px solid #f1f5f9;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  display: flex;
  align-items: center;
  gap: 12px;
  transition: transform 0.2s, box-shadow 0.2s;
}
.stat-card:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,0.08); }
.stat-card.accent-green { border-top: 3px solid #22c55e; }
.stat-card.accent-blue  { border-top: 3px solid #3b82f6; }
.stat-card.accent-amber { border-top: 3px solid #f59e0b; }
.stat-icon { font-size: 22px; flex-shrink: 0; }
.stat-body { min-width: 0; }
.stat-label { font-size: 11px; font-weight: 600; color: #94a3b8; margin: 0 0 4px; letter-spacing: 0.5px; }
.stat-value { font-size: 22px; font-weight: 900; color: #0f172a; margin: 0; line-height: 1; }
.stat-unit { font-size: 13px; font-weight: 600; color: #64748b; margin-left: 2px; }

/* ── 2. 차트 섹션 ── */
.charts-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}
.chart-card {
  background: white;
  border-radius: 20px;
  padding: 24px 20px 20px;
  border: 1px solid #f1f5f9;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}
.chart-title {
  font-size: 14px; font-weight: 800; color: #334155;
  margin: 0; align-self: flex-start;
}
.chart-wrap {
  width: 150px; height: 150px;
  position: relative;
}
.chart-wrap canvas { width: 100% !important; height: 100% !important; }
.chart-legend {
  display: flex; align-items: center; gap: 8px;
  font-size: 12px; font-weight: 600; color: #475569;
  flex-wrap: wrap; justify-content: center;
}
.legend-dot {
  width: 10px; height: 10px; border-radius: 50%; display: inline-block; margin-left: 8px;
}
.legend-dot.correct { background: #22c55e; margin-left: 0; }
.legend-dot.wrong   { background: #f87171; }
.legend-dot.done    { background: #3b82f6; margin-left: 0; }
.legend-dot.undone  { background: #e2e8f0; }
.chart-highlight {
  font-size: 32px; font-weight: 900; color: #0f172a; margin: 0;
}
.highlight-unit { font-size: 16px; font-weight: 700; color: #64748b; }

/* ── 3. 배지 섹션 ── */
.badge-section {
  background: white;
  border-radius: 20px;
  padding: 24px;
  border: 1px solid #f1f5f9;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.section-title { font-size: 15px; font-weight: 800; color: #334155; margin: 0 0 18px; }
.badge-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}
@media (min-width: 540px) {
  .badge-grid { grid-template-columns: repeat(6, 1fr); }
}
.badge-item {
  display: flex; flex-direction: column; align-items: center; gap: 6px;
  padding: 14px 8px;
  border-radius: 14px;
  border: 1.5px dashed #e2e8f0;
  background: #f8fafc;
  opacity: 0.4;
  filter: grayscale(1);
  transition: all 0.3s;
}
.badge-item.unlocked {
  opacity: 1;
  filter: none;
  border-color: #fde68a;
  background: linear-gradient(135deg, #fefce8 0%, #fff 100%);
  box-shadow: 0 2px 8px rgba(234,179,8,0.15);
}
.badge-icon { font-size: 26px; }
.badge-name { font-size: 11px; font-weight: 800; color: #1e293b; margin: 0; text-align: center; }
.badge-desc { font-size: 10px; color: #94a3b8; margin: 0; text-align: center; }

/* ── 4. 빠른 이동 ── */
.quick-nav {
  background: white;
  border-radius: 20px;
  padding: 24px;
  border: 1px solid #f1f5f9;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.nav-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}
.nav-card {
  display: flex; align-items: center; gap: 10px;
  padding: 14px 16px;
  border-radius: 12px;
  border: 1.5px solid #e2e8f0;
  background: #f8fafc;
  cursor: pointer;
  font-size: 14px; font-weight: 700; color: #334155;
  transition: all 0.2s;
  text-align: left;
}
.nav-card:hover {
  background: #0f172a; color: white;
  border-color: #0f172a;
  transform: translateY(-1px);
  box-shadow: 0 4px 10px rgba(15,23,42,0.2);
}
.nav-icon { font-size: 20px; flex-shrink: 0; }
.nav-label { flex: 1; }
.nav-arrow { font-size: 16px; opacity: 0.5; }
</style>
