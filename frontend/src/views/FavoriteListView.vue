<template>
  <div class="passport-container">
    <!-- 1️⃣ 상단 영역: 여권 컨셉 헤더 -->
    <header class="passport-header">
      <div class="header-icon">🛂</div>
      <h1 class="header-title">나의 여행 도장</h1>
      <p class="header-subtitle">마음에 든 표현을 모아둔 여권 페이지</p>
      <div class="gold-divider"></div>
    </header>

    <main class="passport-content">
      
      <!-- 2️⃣ 상단 요약: 여권 스탬프 컬렉션 (시각적 자극) -->
      <section class="stamps-overview" v-if="!loading && !errorMessage">
        <div class="overview-header">
          <h2 class="overview-title">🛂 모은 도장 <strong>{{ totalElements }}</strong>개</h2>
        </div>
        
        <div class="mini-stamps-box" v-if="items.length > 0">
          <div class="mini-stamps-grid">
            <div 
              v-for="(item, index) in items.slice(0, 10)" 
              :key="'stamp-'+item.questionId"
              class="mini-stamp"
              :class="[getRotationClass(index), getCategoryColorClass(item.category)]"
            >
              {{ shortenCategory(item.category) }}
            </div>
            <div v-if="totalElements > 10" class="mini-stamp-more">+{{ totalElements - 10 }}</div>
          </div>
        </div>
      </section>

      <!-- 기능 버튼 영역 (추후 복습 세트 생성 기능 확장 대비) -->
      <section class="action-bar" v-if="items.length > 0">
        <button class="review-btn" @click="startReviewSession" disabled>
          <span class="btn-icon">✈️</span>
          <span>이 도장들로 바로 복습하기 (준비중)</span>
        </button>
      </section>

      <!-- 로딩 및 에러 상태 처리 -->
      <div v-if="loading" class="state-msg">도장 기록을 펼치는 중입니다...</div>
      <div v-else-if="errorMessage" class="state-msg error">{{ errorMessage }}</div>
      
      <!-- 빈 여권 처리 -->
      <div v-else-if="items.length === 0" class="empty-state">
        <div class="empty-icon">📭</div>
        <p>아직 여권에 찍힌 도장이 없습니다.<br>퀴즈 여행을 떠나 유용한 표현을 스크랩해보세요!</p>
      </div>

      <!-- 3️⃣ 하단 리스트: 학습용 카드 상세 뷰 (문제 풀이/기능 중심) -->
      <template v-else>
        <div class="study-cards-list">
          <div 
            v-for="item in items" 
            :key="item.questionId"
            class="study-card"
            :class="{ 'is-deleting': deletingId === item.questionId }"
          >
            <!-- 카드 헤더 (카테고리 & 뱃지) -->
            <div class="card-header">
              <span class="cat-badge" :class="getCategoryColorClass(item.category).replace('stamp-', 'bg-')">
                {{ item.category || '기타' }}
              </span>
              <span class="date-text">{{ formatDate(item.createdAt) }} 저장</span>
            </div>

            <!-- 카드 본문 (문제 텍스트 전체 노출) -->
            <div class="card-body">
              <p class="question-text">{{ item.questionText }}</p>
            </div>

            <!-- 카드 하단 (액션 버튼) -->
            <div class="card-actions">
              <button class="action-btn primary" @click="solveAgain(item.questionId)" disabled>
                <span class="icon">🔄</span> 다시 풀기 (준비중)
              </button>
              <button class="action-btn danger" @click="removeStamp(item.questionId)">
                <span class="icon">🧽</span> 도장 지우기
              </button>
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
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { getFavorites, toggleFavorite } from "../api/favoriteApi";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from "lucide-vue-next";

const router = useRouter();
const items = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const page = ref(1);
const size = 10;
const totalPages = ref(1);
const totalElements = ref(0);

const deletingId = ref(null);

async function loadItems() {
  try {
    loading.value = true;
    errorMessage.value = "";
    const data = await getFavorites(page.value, size);
    items.value = data?.content || [];
    totalPages.value = Math.max(data?.totalPages || 1, 1);
    totalElements.value = data?.totalElements || items.value.length;
  } catch (error) {
    errorMessage.value = "도장을 불러오지 못했습니다.";
  } finally {
    loading.value = false;
  }
}

async function changePage(nextPage) {
  page.value = nextPage;
  await loadItems();
}

// 즐겨찾기 해제 (도장 지우기)
async function removeStamp(questionId) {
  if (!confirm("이 표현을 도장 목록에서 지우시겠습니까?")) return;
  
  try {
    deletingId.value = questionId; 
    await toggleFavorite(questionId);
    
    // 애니메이션 후 리스트 반영
    setTimeout(() => {
      items.value = items.value.filter(item => item.questionId !== questionId);
      totalElements.value = Math.max(0, totalElements.value - 1);
      deletingId.value = null;
      // 목록이 비면 이전 페이지로 돌리기 로직 생략 (단순화)
    }, 300);
    
  } catch (error) {
    alert("도장을 지우는 데 실패했습니다.");
    deletingId.value = null;
  }
}

function solveAgain(questionId) {
  // TODO: 단일 문제 풀이 페이지 or scene 기반 퀴즈로 라우팅 연동
  alert("해당 문제 다시 풀기 모드로 이동합니다. (API 및 라우팅 연결 필요)");
}

function startReviewSession() {
  // TODO: 즐겨찾기 전체 리스트를 attempt 생성 API로 말아보내는 로직
  alert("수집한 도장들을 모아 새로운 여행(복습) 코스를 만듭니다. (API 연결 필요)");
}

/* ── UI 보조 함수 ──────────────────────────────────── */

function formatDate(dateString) {
  if (!dateString) return "2026.02";
  const d = new Date(dateString);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}.${month}.${day}`;
}

function shortenCategory(category) {
  if (!category) return "기타";
  if (category.includes("공항") || category.includes("입국")) return "공항";
  if (category.includes("교통") || category.includes("이동")) return "교통";
  if (category.includes("숙박") || category.includes("호텔")) return "숙박";
  if (category.includes("음식") || category.includes("식당")) return "음식";
  if (category.includes("쇼핑") || category.includes("상점")) return "쇼핑";
  if (category.includes("긴급") || category.includes("경찰")) return "긴급";
  if (category.includes("관광") || category.includes("야간")) return "관광";
  return category;
}

function getCategoryColorClass(category) {
  const shortLine = shortenCategory(category);
  switch (shortLine) {
    case "공항": return "stamp-red";
    case "교통": return "stamp-blue";
    case "숙박": return "stamp-green";
    case "음식": return "stamp-orange";
    case "쇼핑": return "stamp-purple";
    case "긴급": return "stamp-gray";
    default: return "stamp-teal";
  }
}

function getRotationClass(index) {
  const rotationClasses = ['rot-1', 'rot-2', 'rot-3', 'rot-4', 'rot-5'];
  return rotationClasses[index % rotationClasses.length];
}

onMounted(loadItems);
</script>

<style scoped>
/* ── 배경 및 헤더 (여권 테마 유지) ── */
.passport-container {
  min-height: 100vh;
  background-color: #fefce8; 
  background-image: radial-gradient(rgba(214, 211, 209, 0.2) 1px, transparent 1px);
  background-size: 20px 20px;
  padding: 40px 16px 80px;
  font-family: Pretendard, "Noto Sans KR", serif, sans-serif;
  color: #1e293b;
}

.passport-header { text-align: center; margin-bottom: 32px; }
.header-icon { font-size: 36px; margin-bottom: 8px; }
.header-title {
  font-size: 26px; font-weight: 900; color: #0f172a;
  letter-spacing: 2px; margin: 0 0 8px; font-family: "Noto Serif", serif;
}
.header-subtitle { font-size: 14px; font-weight: 600; color: #64748b; margin: 0 0 16px; }
.gold-divider { width: 80px; height: 3px; background: #eab308; margin: 0 auto; border-radius: 2px; }

.passport-content {
  max-width: 680px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* ── 2️⃣ 상단 요약 (미니 스탬프 컬렉션 표면화) ── */
.stamps-overview {
  background: white;
  border-radius: 20px;
  padding: 24px;
  border: 1px dashed #cbd5e1;
  box-shadow: inset 0 2px 4px rgba(0,0,0,0.02);
  display: flex;
  flex-direction: column;
  align-items: center;
}
.overview-title {
  font-size: 16px;
  font-weight: 600;
  color: #334155;
  margin: 0 0 20px;
}
.overview-title strong {
  font-size: 22px;
  color: #0f172a;
  margin: 0 4px;
}
.mini-stamps-box {
  width: 100%;
}
.mini-stamps-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 12px;
}
/* 디자인 핵심: 진짜 미니 도장 느낌 */
.mini-stamp {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 2px solid currentColor;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 900;
  opacity: 0.8;
  mix-blend-mode: multiply;
  box-shadow: 0 1px 2px currentColor, inset 0 0 4px rgba(255,255,255,0.5);
  cursor: default;
}
.mini-stamp-more {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: #f1f5f9;
  color: #64748b;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
}

/* 색상 및 회전용 */
.stamp-red    { color: #ef4444; } 
.stamp-blue   { color: #3b82f6; } 
.stamp-green  { color: #22c55e; } 
.stamp-orange { color: #f97316; } 
.stamp-purple { color: #a855f7; } 
.stamp-gray   { color: #475569; } 
.stamp-teal   { color: #14b8a6; } 

.bg-red    { background: #fee2e2; color: #dc2626; border: 1px solid #fca5a5; } 
.bg-blue   { background: #dbeafe; color: #2563eb; border: 1px solid #93c5fd; } 
.bg-green  { background: #dcfce7; color: #16a34a; border: 1px solid #86efac; } 
.bg-orange { background: #ffedd5; color: #ea580c; border: 1px solid #fdba74; } 
.bg-purple { background: #f3e8ff; color: #9333ea; border: 1px solid #d8b4fe; } 
.bg-gray   { background: #f1f5f9; color: #475569; border: 1px solid #cbd5e1; } 
.bg-teal   { background: #ccfbf1; color: #0d9488; border: 1px solid #5eead4; } 

.rot-1 { transform: rotate(-6deg); }
.rot-2 { transform: rotate(8deg); }
.rot-3 { transform: rotate(-2deg); }
.rot-4 { transform: rotate(5deg); }
.rot-5 { transform: rotate(-8deg); }

/* ── 기능 버튼 ──────────────────────── */
.action-bar {
  display: flex;
  justify-content: center;
}
.review-btn {
  width: 100%;
  max-width: 320px;
  background: #0f172a;
  color: white;
  border: none;
  border-radius: 12px;
  padding: 16px 20px;
  font-size: 16px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 4px 6px rgba(15, 23, 42, 0.2);
}
.review-btn:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 8px 12px rgba(15, 23, 42, 0.3); }
.review-btn:disabled { background: #94a3b8; cursor: not-allowed; box-shadow: none; }

/* ── 3️⃣ 하단 리스트: 실용적 학습 카드 ── */
.study-cards-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.study-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  border: 1px solid #f1f5f9;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.study-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.08);
  border-color: #e2e8f0;
}
.study-card.is-deleting {
  opacity: 0;
  transform: scale(0.95);
  pointer-events: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.cat-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 1px;
}
.date-text {
  font-size: 13px;
  color: #94a3b8;
  font-family: ui-monospace, sans-serif;
}

.card-body {
  margin-bottom: 24px;
}
.question-text {
  font-size: 17px;
  font-weight: 600;
  color: #1e293b;
  line-height: 1.5;
  margin: 0;
  word-break: keep-all;
}

.card-actions {
  display: flex;
  gap: 12px;
  border-top: 1px solid #f1f5f9;
  padding-top: 16px;
}
.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
}
.action-btn.primary {
  background: #f0fdf4;
  color: #16a34a;
  border: 1px solid #bbf7d0;
}
.action-btn.primary:hover:not(:disabled) { background: #dcfce7; }
.action-btn.danger {
  background: transparent;
  color: #94a3b8;
  border: 1px solid #cbd5e1;
}
.action-btn.danger:hover { background: #fee2e2; color: #ef4444; border-color: #fca5a5; }
.action-btn:disabled { opacity: 0.5; cursor: not-allowed; }

/* 상태 메시지 */
.state-msg { text-align: center; padding: 40px; color: #64748b; font-weight: 600; }
.state-msg.error { color: #ef4444; }
.empty-state { text-align: center; padding: 60px 20px; background: rgba(255,255,255,0.5); border: 2px dashed #e2e8f0; border-radius: 16px; color: #64748b; line-height: 1.6; }
.empty-icon { font-size: 48px; margin-bottom: 16px; opacity: 0.8; }

/* 페이지네이션 */
.pager { display: flex; align-items: center; justify-content: center; gap: 4px; margin-top: 40px; }
.pager-btn {
  width: 40px; height: 40px;
  border-radius: 8px;
  border: 1px solid #e9d97a;
  background: white;
  color: #92740a;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer;
  transition: all 0.18s;
}
.pager-btn:hover:not(:disabled) { background: #fefce8; color: #713f12; border-color: #ca8a04; }
.pager-btn:disabled { opacity: 0.35; cursor: not-allowed; }
.pager-btn.pager-edge {
  background: #eab308;
  border-color: #eab308;
  color: white;
}
.pager-btn.pager-edge:hover:not(:disabled) { background: #ca8a04; border-color: #ca8a04; }
.pager-text {
  font-size: 14px; font-weight: 800;
  color: #92740a;
  padding: 0 12px;
  font-family: "Noto Serif", serif;
  letter-spacing: 0.5px;
}
</style>
