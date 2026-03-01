<template>
  <div class="training-container">
    <!-- A. 상단: 상태 요약 + 1개 핵심 CTA -->
    <header class="training-header">
      <div class="header-icon">📚</div>
      <h1 class="header-title">오답노트</h1>
      <p class="header-subtitle">내가 약한 상황을 빠르게 발견하고 다시 풀어서 통과시키는 곳</p>
      <div class="red-divider"></div>
    </header>

    <main class="training-content">
      
      <section class="overview-section" v-if="!loading && !errorMessage">
        <div class="stats-row">
          <div class="stat-item">
             <span class="stat-label">발견된 미션 보류</span>
             <span class="stat-value">{{ totalElements }}개</span>
          </div>
          <!-- 위험 오답을 하드코딩해서 보여주거나 추후 API 응답으로 대체 -->
          <div class="stat-item danger">
             <span class="stat-label">🔴 위험 (3회 이상)</span>
             <span class="stat-value">우선순위 복습</span>
          </div>
        </div>

        <button class="cta-primary-btn" @click="startReviewSession" :disabled="items.length === 0 || reviewLoading">
          <span class="cta-icon">🔥</span>
          {{ reviewLoading ? '복습 세트 생성 중...' : reviewBtnLabel }}
        </button>
      </section>

      <!-- B. 중단: 약점 탐지 영역 (필터, 정렬) -->
      <section class="filter-section" v-if="items.length > 0 || categoryFilter !== 'ALL'">
        <div class="filter-row">
          <!-- 정렬 드롭다운 -->
          <div class="sort-toggle">
            <select v-model="sortType" @change="onFilterChange" class="sort-select">
              <option value="recent">최근 오답순</option>
              <option value="frequent">중복 오답순</option>
            </select>
            <span class="dropdown-arrow">▼</span>
          </div>
          
          <!-- 카테고리 탭 (스크롤 가능) -->
          <div class="category-tabs">
            <button class="tab-btn" :class="{ active: categoryFilter === 'ALL' }" @click="setCategory('ALL')">전체</button>
            <button class="tab-btn" :class="{ active: categoryFilter === '공항' }" @click="setCategory('공항')">✈️공항</button>
            <button class="tab-btn" :class="{ active: categoryFilter === '교통' }" @click="setCategory('교통')">🚉교통</button>
            <button class="tab-btn" :class="{ active: categoryFilter === '숙박' }" @click="setCategory('숙박')">🏨숙박</button>
            <button class="tab-btn" :class="{ active: categoryFilter === '음식' }" @click="setCategory('음식')">🍣음식</button>
            <button class="tab-btn" :class="{ active: categoryFilter === '쇼핑' }" @click="setCategory('쇼핑')">🏪쇼핑</button>
            <button class="tab-btn" :class="{ active: categoryFilter === '긴급' }" @click="setCategory('긴급')">🚨긴급</button>
            <button class="tab-btn" :class="{ active: categoryFilter === '관광' }" @click="setCategory('관광')">🏛️관광</button>
          </div>
        </div>
      </section>

      <!-- 로딩 및 상태 -->
      <div v-if="loading" class="state-msg">훈련 데이터를 불러옵니다...</div>
      <div v-else-if="errorMessage" class="state-msg error">{{ errorMessage }}</div>
      
      <!-- C. 하단: 개별 오답 카드 -->
      <template v-else>
        <div v-if="items.length === 0" class="empty-state">
           <div class="empty-icon">🏆</div>
           <p>다시 도전할 미션이 없습니다!<br>완벽하게 여행을 준비하셨네요.</p>
        </div>

        <div v-else class="mission-cards-list">
          <!-- 오답 카드 1개 -->
          <div 
            v-for="item in items" 
            :key="item.questionId"
            class="mission-card"
            :class="[getRiskCardClass(item.wrongCount), { 'is-hiding': hidingId === item.questionId }]"
          >
            <!-- 띠 디자인 (좌측 위험도 컬러 바) -->
            <div class="risk-bar"></div>
            
            <div class="card-inner">
              <div class="card-header">
                <span class="cat-badge" :class="getCategoryColorClass(item.category)">
                  {{ shortenCategory(item.category) }}
                </span>
                <span class="risk-badge" :class="getRiskBadgeClass(item.wrongCount)">
                  {{ getRiskIcon(item.wrongCount) }} 실수 {{ item.wrongCount }}회
                </span>
              </div>

              <div class="card-body">
                <p class="question-text">{{ item.questionText }}</p>
                <!-- 개발자 정보 (문제ID 등)을 빼고, 사람 형태의 시간 텍스트만 표시 -->
                <p class="recent-time">최근 실수: {{ getHumanReadableTime(item.lastWrongAt) }}</p>
              </div>

              <div class="card-actions">
                <button
                  class="mission-btn primary"
                  @click="solveAgain(item.questionId)"
                  :disabled="solveAgainLoadingId !== null"
                >
                  <span class="icon">🚀</span>
                  {{ solveAgainLoadingId === item.questionId ? '세션 생성 중...' : '미션 다시 도전' }}
                </button>
                <button class="mission-btn ghost" @click="hideMission(item.questionId)">
                  <span class="icon">👁️‍🗨️</span> 미션 제거
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 페이지네이션 -->
        <div class="pager" v-if="totalPages > 1">
          <button class="pager-btn pager-edge" :disabled="page <= 1" @click="changePage(1)" title="첫 페이지">
            <ChevronsLeft :size="22" :stroke-width="2.5" />
          </button>
          <button class="pager-btn" :disabled="page <= 1" @click="changePage(page - 1)" title="이전">
            <ChevronLeft :size="22" :stroke-width="2.5" />
          </button>
          <span class="pager-text">{{ page }} / {{ totalPages }}</span>
          <button class="pager-btn" :disabled="page >= totalPages" @click="changePage(page + 1)" title="다음">
            <ChevronRight :size="22" :stroke-width="2.5" />
          </button>
          <button class="pager-btn pager-edge" :disabled="page >= totalPages" @click="changePage(totalPages)" title="마지막 페이지">
            <ChevronsRight :size="22" :stroke-width="2.5" />
          </button>
        </div>
      </template>

    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { getWrongAnswers, removeWrongAnswer, createReviewSet, createSingleQuestionAttempt } from "../api/wrongAnswerApi";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from "lucide-vue-next";
import { useModal } from "../composables/useModal";

const { showAlert, showConfirm } = useModal();

const router = useRouter();

const items = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const page = ref(1);
const size = 5;
const totalPages = ref(1);
const totalElements = ref(0);
const hidingId = ref(null);

const categoryFilter = ref('ALL');
const sortType = ref('recent');

async function loadItems() {
  try {
    loading.value = true;
    errorMessage.value = "";
    // 정렬, 필터를 백엔드로 전달 (미지원 시 백엔드가 무시하더라도 구조는 맞춰둠)
    const data = await getWrongAnswers(page.value, size, sortType.value, categoryFilter.value);
    
    // 임시: 백엔드 정렬 미지원 시 프론트엔드에서 수동 정렬
    let resultItems = data?.content || [];
    if (sortType.value === 'frequent') {
      resultItems.sort((a, b) => b.wrongCount - a.wrongCount);
    } else {
      // 최근 순 정렬 (lastWrongAt 기준)
      resultItems.sort((a, b) => new Date(b.lastWrongAt) - new Date(a.lastWrongAt));
    }
    
    items.value = resultItems;
    totalPages.value = Math.max(data?.totalPages || 1, 1);
    totalElements.value = data?.totalElements || items.value.length;
  } catch (error) {
    const status = error?.response?.status;
    if (status === 401) {
      errorMessage.value = "로그인이 필요합니다.";
    } else {
      errorMessage.value = "오답 기록을 불러오는 중 문제가 발생했습니다.";
    }
  } finally {
    loading.value = false;
  }
}

function onFilterChange() {
  page.value = 1;
  loadItems();
}

function setCategory(cat) {
  categoryFilter.value = cat;
  page.value = 1;
  loadItems();
}

async function changePage(nextPage) {
  page.value = nextPage;
  await loadItems();
}

// 미션 재도전(개별)
const solveAgainLoadingId = ref(null);

async function solveAgain(questionId) {
  if (solveAgainLoadingId.value !== null) return;
  try {
    solveAgainLoadingId.value = questionId;
    const data = await createSingleQuestionAttempt(questionId);
    router.push(`/quiz/attempts/${data.attemptId}/questions/1`);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 401) {
      errorMessage.value = "로그인이 필요합니다.";
    } else if (status === 403) {
      errorMessage.value = "내 오답노트에 없는 문제입니다.";
    } else if (status === 404) {
      errorMessage.value = "문제를 찾을 수 없습니다.";
    } else {
      errorMessage.value = "재도전 세션 생성 중 문제가 발생했습니다. 다시 시도해 주세요.";
    }
  } finally {
    solveAgainLoadingId.value = null;
  }
}

// 오답 복습 시작 - 가장 강력한 CTA
const reviewLoading = ref(false);

const reviewBtnLabel = computed(() => {
  if (categoryFilter.value === 'ALL') return '오답 복습 시작 (10문제)';
  return `${categoryFilter.value} 오답 전체 복습`;
});

async function startReviewSession() {
  if (reviewLoading.value) return;
  try {
    reviewLoading.value = true;
    const data = await createReviewSet(categoryFilter.value);
    router.push(`/quiz/attempts/${data.attemptId}/questions/1`);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 401) {
      errorMessage.value = "로그인이 필요합니다.";
    } else if (status === 400) {
      errorMessage.value = "해당 카테고리에 복습할 오답이 없습니다.";
    } else {
      errorMessage.value = "복습 세트 생성 중 문제가 발생했습니다. 다시 시도해 주세요.";
    }
  } finally {
    reviewLoading.value = false;
  }
}

// 오답 리스트에서 숨기기
async function hideMission(questionId) {
  const ok = await showConfirm("이 미션을 리스트에서 뺄까요?", "미션 제외");
  if (!ok) return;

  try {
    hidingId.value = questionId;
    await removeWrongAnswer(questionId);

    setTimeout(() => {
      items.value = items.value.filter(item => item.questionId !== questionId);
      totalElements.value = Math.max(0, totalElements.value - 1);
      hidingId.value = null;
    }, 300);

  } catch (error) {
    await showAlert("오답노트 제외 기능이 아직 백엔드에 구현되지 않았을 수 있습니다. 화면에서만 임시로 제거합니다.", "안내");
    setTimeout(() => {
      items.value = items.value.filter(item => item.questionId !== questionId);
      totalElements.value = Math.max(0, totalElements.value - 1);
      hidingId.value = null;
    }, 300);
  }
}

/* ── UI 보조 함수 ──────────────────────────────────── */

// 시간 포맷을 사람이 읽기 쉽게 변환 (최근 24시간, 몇 일 전 등)
function getHumanReadableTime(dateString) {
  if (!dateString) return "기록 없음";
  const date = new Date(dateString);
  const now = new Date();
  const diffMs = now - date;
  const diffMins = Math.floor(diffMs / 60000);
  
  if (diffMins < 60) return `${diffMins || 1}분 전`;
  
  const diffHrs = Math.floor(diffMins / 60);
  if (diffHrs < 24) return `${diffHrs}시간 전`;
  
  const diffDays = Math.floor(diffHrs / 24);
  if (diffDays === 1) return `어제 ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2,"0")}`;
  if (diffDays < 7) return `${diffDays}일 전`;
  
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}.${month}.${day}`;
}

// 오답 횟수에 따른 위험도 컬러 클래스
function getRiskClass(count) {
  const c = Number(count) || 1;
  if (c >= 3) return "risk-danger";  // 🔴 3회 이상
  if (c === 2) return "risk-warning"; // 🟡 2회
  return "risk-light";                // ⚪ 1회
}

function getRiskCardClass(count) {
  const c = Number(count) || 1;
  if (c >= 3) return "border-danger";
  if (c === 2) return "border-warning";
  return "border-light";
}

function getRiskBadgeClass(count) {
  const c = Number(count) || 1;
  if (c >= 3) return "badge-danger";
  if (c === 2) return "badge-warning";
  return "badge-light";
}

function getRiskText(count) {
  const c = Number(count) || 1;
  if (c >= 3) return "위험";
  if (c === 2) return "주의";
  return "가벼운 실수";
}

function getRiskIcon(count) {
  const c = Number(count) || 1;
  if (c >= 3) return "🔴";
  if (c === 2) return "🟡";
  return "⚪";
}

// 카테고리
function shortenCategory(category) {
  if (!category) return "기타";
  if (category.includes("공항") || category.includes("입국")) return "✈️공항";
  if (category.includes("교통") || category.includes("이동")) return "🚉교통";
  if (category.includes("숙박") || category.includes("호텔")) return "🏨숙박";
  if (category.includes("음식") || category.includes("식당")) return "🍣음식";
  if (category.includes("쇼핑") || category.includes("상점")) return "🏪쇼핑";
  if (category.includes("긴급") || category.includes("경찰")) return "🚨긴급";
  if (category.includes("관광") || category.includes("야간")) return "🏛️관광";
  return category;
}

function getCategoryColorClass(category) {
  const shortLine = shortenCategory(category);
  if (shortLine.includes("공항")) return "bg-red";
  if (shortLine.includes("교통")) return "bg-blue";
  if (shortLine.includes("숙박")) return "bg-green";
  if (shortLine.includes("음식")) return "bg-orange";
  if (shortLine.includes("쇼핑")) return "bg-purple";
  if (shortLine.includes("긴급")) return "bg-gray";
  return "bg-teal";
}

onMounted(loadItems);
</script>

<style scoped>
/* ── 전체 레이아웃 (훈련소 컨셉: 더 실용적이고 직관적인 톤) ── */
.training-container {
  min-height: 100vh;
  background-color: #f8fafc; /* 시원하고 깨끗한 slate-50 배경 */
  padding: 40px 16px 80px;
  font-family: Pretendard, "Noto Sans KR", sans-serif;
  color: #1e293b;
}

/* 헤더 */
.training-header {
  text-align: center;
  margin-bottom: 32px;
}
.header-icon {
  font-size: 40px;
  margin-bottom: 8px;
  animation: float 2s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-4px); }
}
.header-title {
  font-size: 26px; font-weight: 900; color: #0f172a;
  margin: 0 0 8px; letter-spacing: -0.5px;
}
.header-subtitle {
  font-size: 15px; font-weight: 500; color: #64748b; margin: 0 0 16px;
}
.red-divider {
  width: 60px; height: 4px; background: #ef4444; margin: 0 auto; border-radius: 2px;
}

.training-content {
  max-width: 680px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* ── 1. 상단 요약 및 메인 CTA (🔥) ── */
.overview-section {
  background: white;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.stats-row {
  display: flex;
  gap: 16px;
}
.stat-item {
  flex: 1;
  background: #f1f5f9;
  padding: 16px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.stat-item.danger {
  background: #fef2f2;
}
.stat-item.danger .stat-label { color: #dc2626; font-weight: 800; }
.stat-item.danger .stat-value { color: #991b1b; font-size: 14px; margin-top: 4px; }

.stat-label {
  font-size: 13px;
  font-weight: 700;
  color: #64748b;
  margin-bottom: 4px;
}
.stat-value {
  font-size: 22px;
  font-weight: 900;
  color: #0f172a;
}

.cta-primary-btn {
  width: 100%;
  padding: 18px;
  background: #ef4444; /* Alert Red */
  color: white;
  font-size: 18px;
  font-weight: 800;
  border: none;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 4px 14px rgba(239, 68, 68, 0.4);
}
.cta-primary-btn:hover:not(:disabled) {
  background: #dc2626;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(239, 68, 68, 0.5);
}
.cta-primary-btn:disabled {
  background: #cbd5e1;
  box-shadow: none;
  cursor: not-allowed;
}
.cta-icon {
  font-size: 22px;
}

/* ── 2. 중단 필터 영역 ── */
.filter-section {
  background: white;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 2px 4px -1px rgba(0,0,0,0.05);
}
.filter-row {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
/* 정렬 드롭다운 */
.sort-toggle {
  position: relative;
  width: 100%;
  max-width: 200px;
}
.sort-select {
  appearance: none;
  width: 100%;
  padding: 10px 32px 10px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  background: #f8fafc;
  font-size: 14px;
  font-weight: 700;
  color: #334155;
  cursor: pointer;
  outline: none;
}
.sort-select:hover { border-color: #cbd5e1; }
.sort-select:focus { border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
.dropdown-arrow {
  position: absolute; right: 12px; top: 50%; transform: translateY(-50%);
  font-size: 10px; color: #94a3b8; pointer-events: none;
}

/* 카테고리 탭 (가로 스크롤) */
.category-tabs {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  padding-bottom: 8px;
}
.category-tabs::-webkit-scrollbar { height: 4px; }
.category-tabs::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 4px; }
.tab-btn {
  white-space: nowrap;
  padding: 8px 16px;
  background: #f1f5f9;
  border: 1px solid transparent;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s;
}
.tab-btn:hover { background: #e2e8f0; }
.tab-btn.active {
  background: #0f172a;
  color: white;
  border-color: #0f172a;
}

/* ── 3. 개별 오답 카드 리스트 ── */
.mission-cards-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.mission-card {
  position: relative;
  display: flex;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,0.03);
  border: 1px solid #e2e8f0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.mission-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.06);
}
.mission-card.is-hiding {
  opacity: 0;
  transform: translateX(-20px);
  pointer-events: none;
}

/* 위험도 테두리 설정 */
.mission-card.border-danger { border-color: #fca5a5; }
.mission-card.border-warning { border-color: #fde047; }
.mission-card.border-light { border-color: #e2e8f0; }

/* 띠 (바) 왼쪽 포인트 컬러 */
.risk-bar { width: 6px; flex-shrink: 0; }
.risk-danger { background: #ef4444; }
.risk-warning { background: #eab308; }
.risk-light { background: #94a3b8; }

.card-inner {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.cat-badge {
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.5px;
}
.bg-red    { background: #fee2e2; color: #dc2626; } 
.bg-blue   { background: #dbeafe; color: #2563eb; } 
.bg-green  { background: #dcfce7; color: #16a34a; } 
.bg-orange { background: #ffedd5; color: #ea580c; } 
.bg-purple { background: #f3e8ff; color: #9333ea; } 
.bg-gray   { background: #f1f5f9; color: #475569; } 
.bg-teal   { background: #ccfbf1; color: #0d9488; } 

.risk-badge {
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 4px;
}
.badge-danger { background: #fef2f2; color: #dc2626; border: 1px solid #fecaca; }
.badge-warning { background: #fefce8; color: #ca8a04; border: 1px solid #fef08a; }
.badge-light { background: #f8fafc; color: #64748b; border: 1px solid #f1f5f9; }

.card-body {
  margin-bottom: 20px;
}
.question-text {
  font-size: 17px;
  font-weight: 700;
  color: #1e293b;
  line-height: 1.5;
  margin: 0 0 8px;
  word-break: keep-all;
}
.recent-time {
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-actions {
  display: flex;
  gap: 10px;
}
. mission-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 12px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
}
.mission-btn.primary {
  background: #f0fdf4;
  color: #16a34a;
  border: 1px solid #bbf7d0;
}
.mission-btn.primary:hover { background: #dcfce7; transform: translateY(-1px); }
.mission-btn.ghost {
  background: white;
  color: #64748b;
  border: 1px solid #cbd5e1;
}
.mission-btn.ghost:hover { background: #f1f5f9; color: #475569; border-color: #94a3b8; }

/* 상태 메시지 */
.state-msg { text-align: center; padding: 40px; color: #64748b; font-weight: 600; }
.state-msg.error { color: #ef4444; }
.empty-state { text-align: center; padding: 60px 20px; background: white; border: 2px dashed #cbd5e1; border-radius: 16px; color: #64748b; line-height: 1.6; }
.empty-icon { font-size: 48px; margin-bottom: 16px; }

/* 페이지네이션 */
.pager { display: flex; align-items: center; justify-content: center; gap: 4px; margin-top: 32px; }
.pager-btn {
  width: 40px; height: 40px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  color: #64748b;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer;
  transition: all 0.18s;
}
.pager-btn:hover:not(:disabled) { background: #f1f5f9; color: #0f172a; border-color: #cbd5e1; }
.pager-btn:disabled { opacity: 0.35; cursor: not-allowed; }
.pager-btn.pager-edge { background: #f8fafc; }
.pager-text { font-size: 14px; font-weight: 700; color: #334155; padding: 0 10px; }
</style>
