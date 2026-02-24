<template>
  <div class="tower-container">
    <!-- 헤더 -->
    <header class="tower-header">
      <div class="header-icon">🗼</div>
      <h1 class="header-title">관제탑</h1>
      <p class="header-subtitle">일본 여행의 안전한 운항을 관리합니다</p>
      <div class="blue-divider"></div>
    </header>

    <main class="tower-content">
      <!-- 지표 개요 -->
      <section class="overview-section">
        <h2 class="section-title">📡 운항 현황</h2>

        <div v-if="loading" class="state-msg">데이터를 불러오는 중...</div>
        <div v-else-if="errorMessage" class="state-msg error">⚠ {{ errorMessage }}</div>

        <div v-else class="stats-grid">
          <article class="stat-card">
            <div class="stat-icon">✈️</div>
            <div class="stat-value">{{ overview.totalAttempts }}</div>
            <div class="stat-label">총 시도</div>
          </article>
          <article class="stat-card">
            <div class="stat-icon">🏁</div>
            <div class="stat-value">{{ overview.completedAttempts }}</div>
            <div class="stat-label">완료 시도</div>
          </article>
          <article class="stat-card">
            <div class="stat-icon">🎯</div>
            <div class="stat-value">{{ overview.correctAnswers }}</div>
            <div class="stat-label">정답 수</div>
          </article>
          <article class="stat-card highlight">
            <div class="stat-icon">📈</div>
            <div class="stat-value">{{ overview.completionRate }}<span class="unit">%</span></div>
            <div class="stat-label">완료율</div>
          </article>
          <article class="stat-card highlight">
            <div class="stat-icon">✅</div>
            <div class="stat-value">{{ overview.accuracyRate }}<span class="unit">%</span></div>
            <div class="stat-label">정답률</div>
          </article>
          <article class="stat-card">
            <div class="stat-icon">👥</div>
            <div class="stat-value">{{ overview.activeUsers7d }}</div>
            <div class="stat-label">최근 7일 활성 유저</div>
          </article>
        </div>
      </section>

      <!-- 빠른 이동 -->
      <section class="nav-section">
        <h2 class="section-title">🛂 관제 구역</h2>
        <div class="nav-grid">
          <button class="nav-card reports" @click="router.push('/admin/reports')">
            <div class="nav-icon">🚨</div>
            <div class="nav-body">
              <div class="nav-title">신고 관리</div>
              <div class="nav-desc">여행자가 제출한 오류 신고를 검토하고 처리합니다</div>
            </div>
            <div class="nav-arrow">→</div>
          </button>
          <button class="nav-card stats" @click="router.push('/admin/stats')">
            <div class="nav-icon">📊</div>
            <div class="nav-body">
              <div class="nav-title">통계 보기</div>
              <div class="nav-desc">카테고리별 정답률, 오답 TOP, 문항 분석 데이터를 확인합니다</div>
            </div>
            <div class="nav-arrow">→</div>
          </button>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { getAdminOverview } from "../api/statsApi";

const router = useRouter();
const loading = ref(false);
const errorMessage = ref("");

const overview = reactive({
  totalAttempts: 0,
  completedAttempts: 0,
  totalAnswers: 0,
  correctAnswers: 0,
  activeUsers7d: 0,
  completionRate: 0,
  accuracyRate: 0,
});

async function loadOverview() {
  try {
    loading.value = true;
    errorMessage.value = "";
    const data = await getAdminOverview();
    overview.totalAttempts = data?.totalAttempts ?? 0;
    overview.completedAttempts = data?.completedAttempts ?? 0;
    overview.totalAnswers = data?.totalAnswers ?? 0;
    overview.correctAnswers = data?.correctAnswers ?? 0;
    overview.activeUsers7d = data?.activeUsers7d ?? 0;
    overview.completionRate = data?.completionRate ?? 0;
    overview.accuracyRate = data?.accuracyRate ?? 0;
  } catch (error) {
    const status = error?.response?.status;
    if (status === 403) {
      errorMessage.value = "접근 권한이 없습니다.";
    } else {
      errorMessage.value = "운항 현황을 불러오지 못했습니다.";
    }
  } finally {
    loading.value = false;
  }
}

onMounted(loadOverview);
</script>

<style scoped>
.tower-container {
  min-height: 100vh;
  background-color: #f8fafc;
  padding: 40px 16px 80px;
  font-family: Pretendard, "Noto Sans KR", sans-serif;
  color: #1e293b;
}

.tower-header {
  text-align: center;
  margin-bottom: 40px;
}
.header-icon {
  font-size: 44px;
  margin-bottom: 8px;
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}
.header-title {
  font-size: 28px;
  font-weight: 900;
  color: #0f172a;
  margin: 0 0 8px;
  letter-spacing: -0.5px;
}
.header-subtitle {
  font-size: 15px;
  font-weight: 500;
  color: #64748b;
  margin: 0 0 16px;
}
.blue-divider {
  width: 60px;
  height: 4px;
  background: #2563eb;
  margin: 0 auto;
  border-radius: 2px;
}

.tower-content {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.section-title {
  font-size: 17px;
  font-weight: 800;
  color: #0f172a;
  margin: 0 0 16px;
}

.overview-section {
  background: white;
  border-radius: 20px;
  padding: 28px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.stat-card {
  background: #f8fafc;
  border-radius: 14px;
  padding: 20px 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  border: 1px solid #e2e8f0;
  transition: transform 0.2s;
}
.stat-card:hover { transform: translateY(-2px); }
.stat-card.highlight {
  background: #eff6ff;
  border-color: #bfdbfe;
}
.stat-icon { font-size: 22px; }
.stat-value {
  font-size: 26px;
  font-weight: 900;
  color: #0f172a;
  line-height: 1;
}
.stat-value .unit {
  font-size: 16px;
  font-weight: 700;
  color: #2563eb;
}
.stat-label {
  font-size: 12px;
  font-weight: 600;
  color: #64748b;
  text-align: center;
}

.nav-section { display: flex; flex-direction: column; }
.nav-grid { display: flex; flex-direction: column; gap: 12px; }

.nav-card {
  display: flex;
  align-items: center;
  gap: 20px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 24px;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);
}
.nav-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.06);
}
.nav-card.reports:hover { border-color: #fca5a5; }
.nav-card.stats:hover { border-color: #93c5fd; }

.nav-icon { font-size: 32px; flex-shrink: 0; }
.nav-body { flex: 1; }
.nav-title {
  font-size: 17px;
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 4px;
}
.nav-desc {
  font-size: 13px;
  font-weight: 500;
  color: #64748b;
  line-height: 1.5;
}
.nav-arrow {
  font-size: 20px;
  color: #94a3b8;
  flex-shrink: 0;
  transition: transform 0.2s, color 0.2s;
}
.nav-card:hover .nav-arrow {
  transform: translateX(4px);
  color: #2563eb;
}

.state-msg { text-align: center; padding: 32px; color: #64748b; font-weight: 600; }
.state-msg.error { color: #ef4444; }
</style>