<template>
  <div class="mypage-container">
    <!-- 4️⃣ 상단 영역: 나의 여행 기록 -->
    <header class="mypage-header">
      <div class="header-icon">🧭</div>
      <h1 class="header-title">나의 여행 기록</h1>
      <p class="header-subtitle">“일본어를 여행처럼 배우다”</p>
      <div class="gold-divider"></div>
    </header>

    <main class="mypage-content">
      
      <!-- 1️⃣ 🎫 여행자 프로필 카드 (Passport 컨셉) -->
      <section class="passport-section">
        <div class="passport-card">
          <!-- 배경 일본 도장 워터마크 -->
          <div class="passport-watermark">入国許可<br>旅Quiz</div>

          <div class="passport-header">
            <span class="passport-flag">🇯🇵</span>
            <span class="passport-title">TRAVELER PASSPORT</span>
          </div>

          <div class="passport-body">
            <div class="info-group">
              <span class="info-label">여권 이름 (닉네임)</span>
              <div class="info-input-row" v-if="!profileLoading && !profileErrorMessage">
                <input 
                  type="text" 
                  v-model="nickname" 
                  class="passport-input" 
                  placeholder="예: 타비타로"
                  @keyup.enter="saveProfile"
                />
                <button class="save-btn" @click="saveProfile" :disabled="savingProfile">
                  {{ savingProfile ? "갱신 중" : "수정" }}
                </button>
              </div>
              <div v-else class="info-value loading-text">불러오는 중...</div>
            </div>

            <div class="info-group">
              <span class="info-label">이메일 (연락처)</span>
              <span class="info-value">{{ profile.email || '불러오는 중...' }}</span>
            </div>

            <div class="info-group">
              <span class="info-label">여행 시작일</span>
              <span class="info-value date-value">2026.02.22</span>
            </div>
          </div>
          
          <div v-if="profileErrorMessage" class="msg error-msg">⚠ {{ profileErrorMessage }}</div>
          <div v-if="profileSuccessMessage" class="msg success-msg">✓ {{ profileSuccessMessage }}</div>
        </div>
      </section>

      <!-- 2️⃣ 🗺️ 나의 일본 여행 기록 (통계 4분할 그리드) -->
      <section class="record-section">
        <h2 class="section-title">🗺️ 나의 일본 여행 기록</h2>
        
        <div v-if="statsLoading" class="loading-state">기록을 조회하고 있습니다...</div>
        <div v-else-if="statsErrorMessage" class="msg error-msg">⚠ {{ statsErrorMessage }}</div>
        
        <div v-else class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon">✈️</div>
            <div class="stat-data">{{ stats.totalAttempts }}<span class="stat-unit">회</span></div>
            <div class="stat-label">방문한 여행 횟수</div>
          </div>
          
          <div class="stat-card">
            <div class="stat-icon">🏁</div>
            <div class="stat-data">{{ stats.completedAttempts }}<span class="stat-unit">회</span></div>
            <div class="stat-label">완주한 여행</div>
          </div>
          
          <div class="stat-card">
            <div class="stat-icon">🎯</div>
            <div class="stat-data">{{ stats.correctAnswers }}<span class="stat-unit">개</span></div>
            <div class="stat-label">성공한 미션</div>
          </div>
          
          <div class="stat-card">
            <div class="stat-icon">📊</div>
            <div class="stat-data">{{ stats.accuracyRate }}<span class="stat-unit">%</span></div>
            <div class="stat-label">여행 성공률</div>
          </div>
        </div>
      </section>

      <!-- ✨ 미친 디테일: Scene별 여행 지도 진행도 (UI 퍼블리싱) -->
      <section class="map-progress-section">
        <h2 class="section-title">🗾 여행 노선 달성도</h2>
        <div class="progress-list">
          <!-- 공항 -->
          <div class="progress-item completed">
            <div class="p-header">
              <span class="p-icon">✈️</span>
              <span class="p-name">공항 / 입국</span>
              <span class="p-status">✅</span>
            </div>
            <div class="p-bar-bg"><div class="p-bar-fill" style="width: 100%;"></div></div>
            <div class="p-text">100% 클리어</div>
          </div>
          <!-- 교통 -->
          <div class="progress-item in-progress">
            <div class="p-header">
              <span class="p-icon">🚉</span>
              <span class="p-name">교통 / 이동</span>
              <span class="p-status">🔄</span>
            </div>
            <div class="p-bar-bg"><div class="p-bar-fill" style="width: 65%;"></div></div>
            <div class="p-text">65% 진행 중</div>
          </div>
          <!-- 숙박 -->
          <div class="progress-item not-started">
            <div class="p-header">
              <span class="p-icon">🏨</span>
              <span class="p-name">숙박 / 호텔</span>
              <span class="p-status">❌</span>
            </div>
            <div class="p-bar-bg"><div class="p-bar-fill" style="width: 0%;"></div></div>
            <div class="p-text">미방문</div>
          </div>
        </div>
      </section>

      <!-- 3️⃣ 🧳 오답노트 / 즐겨찾기 / 개인 통계 (여행 가방 메뉴) -->
      <section class="baggage-section">
        <h2 class="section-title">🧳 나의 여행 가방</h2>
        <div class="menu-list">
          <RouterLink to="/quiz/wrong-answers" class="menu-card">
            <div class="menu-left">
              <div class="menu-icon-wrap">📓</div>
              <span class="menu-text">오답노트</span>
            </div>
            <div class="menu-right">
              <span class="menu-arrow">→</span>
            </div>
          </RouterLink>
          
          <RouterLink to="/quiz/favorites" class="menu-card">
            <div class="menu-left">
              <div class="menu-icon-wrap">⭐</div>
              <span class="menu-text">즐겨찾기</span>
            </div>
            <div class="menu-right">
              <span class="menu-arrow">→</span>
            </div>
          </RouterLink>
          
          <RouterLink to="/me/stats" class="menu-card">
            <div class="menu-left">
              <div class="menu-icon-wrap">📊</div>
              <span class="menu-text">개인 상세 통계</span>
            </div>
            <div class="menu-right">
              <span class="menu-arrow">→</span>
            </div>
          </RouterLink>
        </div>
      </section>

    </main>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { RouterLink } from "vue-router";
import { getMyProfile, updateMyProfile } from "../api/memberApi";
import { getMyStats } from "../api/statsApi";

const profile = reactive({
  email: "",
  nickname: ""
});
const stats = reactive({
  totalAttempts: 0,
  completedAttempts: 0,
  totalAnswers: 0,
  correctAnswers: 0,
  accuracyRate: 0
});

const nickname = ref("");
const profileLoading = ref(false);
const profileErrorMessage = ref("");
const profileSuccessMessage = ref("");
const savingProfile = ref(false);

const statsLoading = ref(false);
const statsErrorMessage = ref("");

function toErrorMessage(error, defaultMessage) {
  const status = error?.response?.status;
  if (status === 401) return "로그인이 필요합니다.";
  if (status === 403) return "접근 권한이 없습니다.";
  if (status === 404) return "데이터를 찾을 수 없습니다.";
  if (status === 400) return error?.response?.data?.message || "요청 값이 올바르지 않습니다.";
  return defaultMessage;
}

async function loadProfile() {
  try {
    profileLoading.value = true;
    profileErrorMessage.value = "";
    const data = await getMyProfile();
    profile.email = data?.email || "";
    profile.nickname = data?.nickname || "";
    nickname.value = profile.nickname;
  } catch (error) {
    profileErrorMessage.value = toErrorMessage(error, "회원 정보를 불러오지 못했습니다.");
  } finally {
    profileLoading.value = false;
  }
}

async function saveProfile() {
  if (!nickname.value.trim()) {
    profileErrorMessage.value = "닉네임을 입력해 주세요.";
    return;
  }
  try {
    savingProfile.value = true;
    profileErrorMessage.value = "";
    profileSuccessMessage.value = "";
    await updateMyProfile({ nickname: nickname.value.trim() });
    profile.nickname = nickname.value.trim();
    profileSuccessMessage.value = "여권 이름이 성공적으로 갱신되었습니다.";
    setTimeout(() => { profileSuccessMessage.value = ""; }, 3000);
  } catch (error) {
    const status = error?.response?.status;
    const code = error?.response?.data?.code;
    if (status === 409 && code === "DUPLICATE_NICKNAME") {
      profileErrorMessage.value = "이미 다른 여행자가 사용 중인 이름입니다.";
      return;
    }
    profileErrorMessage.value = toErrorMessage(error, "정보 갱신 중 오류가 발생했습니다.");
  } finally {
    savingProfile.value = false;
  }
}

async function loadStats() {
  try {
    statsLoading.value = true;
    statsErrorMessage.value = "";
    const data = await getMyStats();
    stats.totalAttempts = data?.totalAttempts ?? 0;
    stats.completedAttempts = data?.completedAttempts ?? 0;
    stats.totalAnswers = data?.totalAnswers ?? 0;
    stats.correctAnswers = data?.correctAnswers ?? 0;
    stats.accuracyRate = data?.accuracyRate ?? 0;
  } catch (error) {
    statsErrorMessage.value = toErrorMessage(error, "여행 기록을 불러오지 못했습니다.");
  } finally {
    statsLoading.value = false;
  }
}

onMounted(async () => {
  await Promise.all([loadProfile(), loadStats()]);
});
</script>

<style scoped>
/* ── 배경 및 전체 레이아웃 (오프화이트 & 골드네이비 톤) ── */
.mypage-container {
  min-height: 100vh;
  /* 아주 연한 베이지에 미세한 도트 패턴을 넣어 종이 질감 연출 */
  background-color: #fdfbf7;
  background-image: radial-gradient(#e2e8f0 1px, transparent 1px);
  background-size: 20px 20px;
  padding: 40px 16px 80px;
  font-family: Pretendard, "Noto Sans KR", serif, sans-serif;
  color: #1e293b;
}

/* 4️⃣ 상단 영역: 나의 여행 기록 */
.mypage-header {
  text-align: center;
  margin-bottom: 40px;
}
.header-icon {
  font-size: 36px;
  margin-bottom: 8px;
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-4px); }
}
.header-title {
  font-size: 26px;
  font-weight: 800;
  color: #0f172a;
  letter-spacing: 1px;
  margin: 0 0 8px;
  font-family: "Noto Serif", serif;
}
.header-subtitle {
  font-size: 15px;
  font-weight: 600;
  color: #64748b;
  margin: 0 0 16px;
}
.gold-divider {
  width: 60px;
  height: 3px;
  background: #eab308; /* yellow-500 */
  margin: 0 auto;
  border-radius: 2px;
}

.mypage-content {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 48px;
}
.section-title {
  font-size: 19px;
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 1️⃣ 여행자 프로필 카드 (Passport 컨셉) */
.passport-card {
  position: relative;
  background: #0f172a; /* navy-900 */
  border: 2px solid #eab308; /* yellow-500 */
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 20px 25px -5px rgba(15, 23, 42, 0.2), 0 8px 10px -6px rgba(15, 23, 42, 0.1);
  overflow: hidden;
  color: #f8fafc;
  /* 부드러운 블러 효과 */
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
}
.passport-watermark {
  position: absolute;
  top: 15%;
  right: -5%;
  font-size: 40px;
  font-weight: 900;
  color: rgba(234, 179, 8, 0.08); /* 금색 도장 워터마크 */
  transform: rotate(-15deg);
  pointer-events: none;
  font-family: "Noto Serif JP", serif;
  border: 3px double rgba(234, 179, 8, 0.08);
  border-radius: 50%;
  width: 140px;
  height: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  line-height: 1.2;
}
.passport-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 28px;
  border-bottom: 1px solid rgba(234, 179, 8, 0.3);
  padding-bottom: 16px;
}
.passport-flag {
  font-size: 26px;
}
.passport-title {
  font-size: 18px;
  font-weight: 800;
  color: #eab308;
  letter-spacing: 2px;
  font-family: ui-monospace, sans-serif;
}

.passport-body {
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: relative;
  z-index: 2;
}
.info-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.info-label {
  font-size: 12px;
  font-weight: 700;
  color: #94a3b8;
  letter-spacing: 1px;
}
.info-value {
  font-size: 17px;
  font-weight: 700;
  color: #f1f5f9;
}
.date-value {
  color: #cbd5e1;
  font-family: ui-monospace, monospace;
}
.info-input-row {
  display: flex;
  gap: 12px;
  align-items: center;
}
.passport-input {
  flex: 1;
  background: transparent;
  border: none;
  border-bottom: 2px solid #475569;
  color: #f8fafc;
  font-size: 18px;
  font-weight: 700;
  padding: 8px 4px 4px;
  outline: none;
  transition: border-color 0.2s;
  font-family: inherit;
}
.passport-input:focus {
  border-bottom-color: #eab308;
}
.passport-input::placeholder {
  color: #475569;
  font-weight: 500;
}
.save-btn {
  background: #eab308;
  color: #0f172a;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
  transition: all 0.2s;
}
.save-btn:hover:not(:disabled) { background: #fde047; transform: translateY(-1px); }
.save-btn:disabled { opacity: 0.5; cursor: not-allowed; }

.msg {
  font-size: 13px;
  font-weight: 600;
  margin-top: 20px;
  padding: 12px;
  border-radius: 6px;
  text-align: center;
}
.error-msg { background: rgba(248, 113, 113, 0.1); color: #fca5a5; }
.success-msg { background: rgba(74, 222, 128, 0.1); color: #86efac; }


/* 2️⃣ 🗺️ 나의 일본 여행 기록 (통계 그리드) */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}
@media (min-width: 600px) {
  .stats-grid { grid-template-columns: repeat(4, 1fr); }
}
.stat-card {
  background: #ffffff;
  border-radius: 16px;
  padding: 24px 16px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.2s;
  border: 1px solid #f1f5f9;
}
.stat-card:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  border-color: #e2e8f0;
}
.stat-icon {
  font-size: 32px;
  margin-bottom: 12px;
}
.stat-data {
  font-size: 24px;
  font-weight: 900;
  color: #0f172a;
  margin-bottom: 4px;
}
.stat-unit {
  font-size: 14px;
  font-weight: 600;
  color: #64748b;
  margin-left: 2px;
}
.stat-label {
  font-size: 13px;
  font-weight: 700;
  color: #94a3b8;
}


/* ✨ 미친 디테일: 여행 노선 달성도 progress UI */
.progress-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.progress-item {
  background: #ffffff;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #f1f5f9;
  box-shadow: 0 2px 4px rgba(0,0,0,0.02);
}
.p-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}
.p-icon { font-size: 20px; }
.p-name { font-size: 15px; font-weight: 700; color: #1e293b; flex: 1; }
.p-status { font-size: 16px; }

.p-bar-bg {
  height: 8px;
  background: #f1f5f9;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}
.p-bar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 1s ease-out;
}
/* 상태별 프로그레스바 컬러 */
.completed .p-bar-fill { background: #10b981; } /* 초록 */
.completed .p-text { color: #10b981; }
.in-progress .p-bar-fill { background: #3b82f6; } /* 파랑 */
.in-progress .p-text { color: #3b82f6; }
.not-started .p-bar-fill { background: #cbd5e1; } /* 회색 */
.not-started .p-text { color: #94a3b8; }

.p-text {
  font-size: 12px;
  font-weight: 700;
  text-align: right;
}


/* 3️⃣ 🧳 가방 메뉴 리스트 */
.menu-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.menu-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  text-decoration: none;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
  border: 1px solid #f1f5f9;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}
.menu-card:hover {
  transform: translateY(-4px) scale(1.01);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.08);
  background: #f8fafc; /* 연한 블루그레이 빛 */
  border-color: #e2e8f0;
}
.menu-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.menu-icon-wrap {
  width: 48px;
  height: 48px;
  background: #f1f5f9;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  transition: background 0.2s;
}
.menu-card:hover .menu-icon-wrap {
  background: #e2e8f0;
}
.menu-text {
  font-size: 17px;
  font-weight: 800;
  color: #0f172a;
}

/* 화살표 애니메이션 등장 */
.menu-arrow {
  color: #94a3b8;
  font-size: 24px;
  font-weight: bold;
  opacity: 0;
  transform: translateX(-15px);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.menu-card:hover .menu-arrow {
  opacity: 1;
  transform: translateX(0);
  color: #3b82f6; /* 파란색 화살표 포인트 */
}

/* 로딩 상태 */
.loading-state {
  text-align: center;
  padding: 40px;
  color: #64748b;
  font-weight: 600;
  font-size: 15px;
}
</style>
