<template>
  <div class="stats-container">
    <!-- 헤더 -->
    <header class="stats-header">
      <div class="header-icon">📊</div>
      <h1 class="header-title">통계 분석</h1>
      <p class="header-subtitle">문항별·카테고리별 데이터를 기반으로 여행 품질을 점검합니다</p>
      <div class="blue-divider"></div>
    </header>

    <main class="stats-content">

      <!-- 필터 바 -->
      <div class="filter-bar">
        <div class="filter-item">
          <span class="filter-label">풀이 기준</span>
          <div class="select-wrap">
            <select v-model="basis" @change="reloadAll" class="filter-select">
              <option value="latest">최근 풀이</option>
              <option value="first">첫 풀이</option>
            </select>
            <span class="select-arrow">▼</span>
          </div>
        </div>
        <div class="filter-item">
          <span class="filter-label">페이지당 문항</span>
          <div class="select-wrap">
            <select v-model.number="size" @change="onSizeChange" class="filter-select">
              <option :value="10">10개</option>
              <option :value="20">20개</option>
              <option :value="50">50개</option>
            </select>
            <span class="select-arrow">▼</span>
          </div>
        </div>
        <div class="filter-item">
          <span class="filter-label">오답 TOP</span>
          <div class="select-wrap">
            <select v-model.number="limit" @change="loadTopWrong" class="filter-select">
              <option :value="5">TOP 5</option>
              <option :value="10">TOP 10</option>
              <option :value="20">TOP 20</option>
            </select>
            <span class="select-arrow">▼</span>
          </div>
        </div>
      </div>

      <!-- ① 개요 -->
      <section class="panel">
        <h2 class="panel-title">📡 운항 개요</h2>
        <div v-if="overviewLoading" class="state-msg">불러오는 중...</div>
        <div v-else-if="overviewError" class="state-msg error">⚠ {{ overviewError }}</div>
        <div v-else class="overview-grid">
          <article class="ov-card">
            <div class="ov-icon">✈️</div>
            <div class="ov-value">{{ overview.totalAttempts }}</div>
            <div class="ov-label">총 시도</div>
          </article>
          <article class="ov-card">
            <div class="ov-icon">🏁</div>
            <div class="ov-value">{{ overview.completedAttempts }}</div>
            <div class="ov-label">완료 시도</div>
          </article>
          <article class="ov-card">
            <div class="ov-icon">📝</div>
            <div class="ov-value">{{ overview.totalAnswers }}</div>
            <div class="ov-label">총 제출</div>
          </article>
          <article class="ov-card">
            <div class="ov-icon">🎯</div>
            <div class="ov-value">{{ overview.correctAnswers }}</div>
            <div class="ov-label">정답 수</div>
          </article>
          <article class="ov-card highlight">
            <div class="ov-icon">📈</div>
            <div class="ov-value">{{ overview.completionRate }}<span class="unit">%</span></div>
            <div class="ov-label">완료율</div>
          </article>
          <article class="ov-card highlight">
            <div class="ov-icon">✅</div>
            <div class="ov-value">{{ overview.accuracyRate }}<span class="unit">%</span></div>
            <div class="ov-label">정답률</div>
          </article>
          <article class="ov-card">
            <div class="ov-icon">👥</div>
            <div class="ov-value">{{ overview.activeUsers7d }}</div>
            <div class="ov-label">최근 7일 활성 유저</div>
          </article>
        </div>
      </section>

      <!-- ② 카테고리 정확도 -->
      <section class="panel">
        <h2 class="panel-title">🗂 카테고리 정확도
          <span class="basis-tag">{{ basis === 'latest' ? '최근 풀이 기준' : '첫 풀이 기준' }}</span>
        </h2>
        <div v-if="categoriesLoading" class="state-msg">불러오는 중...</div>
        <div v-else-if="categoriesError" class="state-msg error">⚠ {{ categoriesError }}</div>
        <div v-else-if="categories.length === 0" class="state-msg">데이터가 없습니다.</div>
        <div v-else class="category-list">
          <div v-for="item in categories" :key="item.category" class="category-row">
            <div class="cat-info">
              <span class="cat-name">{{ item.category }}</span>
              <span class="cat-counts">{{ item.correctAnswers }} / {{ item.totalAnswers }}</span>
            </div>
            <div class="cat-bar-wrap">
              <div class="cat-bar" :style="{ width: item.accuracyRate + '%' }" :class="getAccuracyClass(item.accuracyRate)"></div>
            </div>
            <span class="cat-rate" :class="getAccuracyClass(item.accuracyRate)">{{ item.accuracyRate }}%</span>
          </div>
        </div>
      </section>

      <!-- ③ 문항 통계 -->
      <section class="panel">
        <h2 class="panel-title">📋 문항 통계
          <span class="basis-tag">{{ basis === 'latest' ? '최근 풀이 기준' : '첫 풀이 기준' }}</span>
        </h2>
        <div v-if="questionsLoading" class="state-msg">불러오는 중...</div>
        <div v-else-if="questionsError" class="state-msg error">⚠ {{ questionsError }}</div>
        <div v-else-if="questionPage.content.length === 0" class="state-msg">데이터가 없습니다.</div>
        <template v-else>
          <div class="question-list">
            <div v-for="item in questionPage.content" :key="item.questionId" class="question-row">
              <div class="q-left">
                <span class="q-id">#{{ item.questionId }}</span>
                <span class="q-cat-badge">{{ item.category || '-' }}</span>
              </div>
              <div class="q-body">
                <p class="q-text">{{ item.questionText }}</p>
                <div class="q-meta-row">
                  <span class="q-meta">총 {{ item.totalAnswers }}회</span>
                  <span class="q-meta correct">정답 {{ item.correctAnswers }}</span>
                  <span class="q-meta wrong">오답 {{ item.wrongAnswers }}</span>
                </div>
              </div>
              <div class="q-right">
                <span class="difficulty-badge" :class="getDifficultyClass(item.difficultyLevel)">
                  {{ getDifficultyLabel(item.difficultyLevel) }}
                </span>
                <span class="q-accuracy" :class="getAccuracyClass(item.accuracyRate)">{{ item.accuracyRate }}%</span>
              </div>
            </div>
          </div>
          <div v-if="totalPages > 1" class="pager">
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
      </section>

      <!-- ④ 오답 TOP -->
      <section class="panel">
        <h2 class="panel-title">🔴 오답 TOP {{ limit }}</h2>
        <div v-if="topWrongLoading" class="state-msg">불러오는 중...</div>
        <div v-else-if="topWrongError" class="state-msg error">⚠ {{ topWrongError }}</div>
        <div v-else-if="topWrongItems.length === 0" class="state-msg">데이터가 없습니다.</div>
        <div v-else class="top-wrong-list">
          <div v-for="(item, idx) in topWrongItems" :key="item.questionId" class="top-wrong-row">
            <div class="rank-badge" :class="getRankClass(idx)">{{ idx + 1 }}</div>
            <div class="tw-body">
              <p class="tw-text">{{ item.questionText }}</p>
              <div class="tw-meta-row">
                <span class="tw-cat">{{ item.category || '-' }}</span>
                <span class="tw-counts">오답 {{ item.wrongAnswers }} / 총 {{ item.totalAnswers }}</span>
              </div>
              <div class="tw-bar-wrap">
                <div class="tw-bar" :style="{ width: item.wrongRate + '%' }"></div>
              </div>
            </div>
            <div class="tw-rate">{{ item.wrongRate }}<span class="tw-unit">%</span></div>
          </div>
        </div>
      </section>

    </main>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import {
  getAdminOverview,
  getCategoryAccuracy,
  getQuestionStats,
  getTopWrongQuestions
} from "../api/statsApi";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from "lucide-vue-next";

const basis = ref("latest");
const page = ref(1);
const size = ref(10);
const limit = ref(10);

const overview = reactive({
  totalAttempts: 0,
  completedAttempts: 0,
  totalAnswers: 0,
  correctAnswers: 0,
  activeUsers7d: 0,
  completionRate: 0,
  accuracyRate: 0
});
const categories = ref([]);
const questionPage = reactive({ content: [], totalPages: 1 });
const topWrongItems = ref([]);

const overviewLoading = ref(false);
const categoriesLoading = ref(false);
const questionsLoading = ref(false);
const topWrongLoading = ref(false);

const overviewError = ref("");
const categoriesError = ref("");
const questionsError = ref("");
const topWrongError = ref("");

const totalPages = ref(1);

function toErrorMessage(error, defaultMessage) {
  const status = error?.response?.status;
  if (status === 400) return "요청 값이 올바르지 않습니다.";
  if (status === 401) return "로그인이 필요합니다.";
  if (status === 403) return "접근 권한이 없습니다.";
  return defaultMessage;
}

async function loadOverview() {
  try {
    overviewLoading.value = true;
    overviewError.value = "";
    const data = await getAdminOverview();
    overview.totalAttempts = data?.totalAttempts ?? 0;
    overview.completedAttempts = data?.completedAttempts ?? 0;
    overview.totalAnswers = data?.totalAnswers ?? 0;
    overview.correctAnswers = data?.correctAnswers ?? 0;
    overview.activeUsers7d = data?.activeUsers7d ?? 0;
    overview.completionRate = data?.completionRate ?? 0;
    overview.accuracyRate = data?.accuracyRate ?? 0;
  } catch (error) {
    overviewError.value = toErrorMessage(error, "개요 통계를 불러오지 못했습니다.");
  } finally {
    overviewLoading.value = false;
  }
}

async function loadCategories() {
  try {
    categoriesLoading.value = true;
    categoriesError.value = "";
    categories.value = await getCategoryAccuracy(basis.value);
  } catch (error) {
    categoriesError.value = toErrorMessage(error, "카테고리 통계를 불러오지 못했습니다.");
  } finally {
    categoriesLoading.value = false;
  }
}

async function loadQuestions() {
  try {
    questionsLoading.value = true;
    questionsError.value = "";
    const data = await getQuestionStats({ basis: basis.value, page: page.value, size: size.value });
    questionPage.content = data?.content || [];
    questionPage.totalPages = Math.max(data?.totalPages || 1, 1);
    totalPages.value = questionPage.totalPages;
  } catch (error) {
    questionsError.value = toErrorMessage(error, "문항 통계를 불러오지 못했습니다.");
  } finally {
    questionsLoading.value = false;
  }
}

async function loadTopWrong() {
  try {
    topWrongLoading.value = true;
    topWrongError.value = "";
    topWrongItems.value = await getTopWrongQuestions(limit.value);
  } catch (error) {
    topWrongError.value = toErrorMessage(error, "오답 TOP 통계를 불러오지 못했습니다.");
  } finally {
    topWrongLoading.value = false;
  }
}

async function changePage(nextPage) {
  page.value = nextPage;
  await loadQuestions();
}

async function onSizeChange() {
  page.value = 1;
  await loadQuestions();
}

async function reloadAll() {
  page.value = 1;
  await Promise.all([loadCategories(), loadQuestions()]);
}

/* ── UI 보조 함수 ── */
function getAccuracyClass(rate) {
  if (rate >= 70) return "acc-high";
  if (rate >= 40) return "acc-mid";
  return "acc-low";
}
function getDifficultyClass(level) {
  if (level === "HARD")   return "diff-hard";
  if (level === "NORMAL") return "diff-normal";
  return "diff-easy";
}
function getDifficultyLabel(level) {
  if (level === "HARD")   return "어려움";
  if (level === "NORMAL") return "보통";
  return "쉬움";
}
function getRankClass(idx) {
  if (idx === 0) return "rank-gold";
  if (idx === 1) return "rank-silver";
  if (idx === 2) return "rank-bronze";
  return "rank-default";
}

onMounted(() => {
  Promise.all([loadOverview(), loadCategories(), loadQuestions(), loadTopWrong()]);
});
</script>

<style scoped>
.stats-container {
  min-height: 100vh;
  background-color: #f8fafc;
  padding: 40px 16px 80px;
  font-family: Pretendard, "Noto Sans KR", sans-serif;
  color: #1e293b;
}

/* 헤더 */
.stats-header {
  text-align: center;
  margin-bottom: 32px;
}
.header-icon {
  font-size: 40px;
  margin-bottom: 8px;
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50%       { transform: translateY(-5px); }
}
.header-title {
  font-size: 26px; font-weight: 900; color: #0f172a;
  margin: 0 0 8px; letter-spacing: -0.5px;
}
.header-subtitle {
  font-size: 15px; font-weight: 500; color: #64748b; margin: 0 0 16px;
}
.blue-divider {
  width: 60px; height: 4px; background: #2563eb; margin: 0 auto; border-radius: 2px;
}

/* 본문 */
.stats-content {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 필터 바 */
.filter-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  background: white;
  border-radius: 16px;
  padding: 16px 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.04);
  border: 1px solid #e2e8f0;
}
.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}
.filter-label {
  font-size: 13px;
  font-weight: 700;
  color: #64748b;
  white-space: nowrap;
}
.select-wrap {
  position: relative;
}
.filter-select {
  appearance: none;
  padding: 7px 28px 7px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: #f8fafc;
  font-size: 13px;
  font-weight: 700;
  color: #334155;
  cursor: pointer;
  outline: none;
}
.filter-select:focus { border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }
.select-arrow {
  position: absolute; right: 9px; top: 50%; transform: translateY(-50%);
  font-size: 9px; color: #94a3b8; pointer-events: none;
}

/* 패널 공통 */
.panel {
  background: white;
  border-radius: 20px;
  padding: 28px;
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
  border: 1px solid #f1f5f9;
}
.panel-title {
  font-size: 17px;
  font-weight: 800;
  color: #0f172a;
  margin: 0 0 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.basis-tag {
  font-size: 12px;
  font-weight: 600;
  color: #2563eb;
  background: #eff6ff;
  padding: 3px 8px;
  border-radius: 20px;
}

/* ① 개요 그리드 */
.overview-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
.ov-card {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  transition: transform 0.2s;
}
.ov-card:hover { transform: translateY(-2px); }
.ov-card.highlight { background: #eff6ff; border-color: #bfdbfe; }
.ov-icon { font-size: 20px; }
.ov-value {
  font-size: 22px; font-weight: 900; color: #0f172a; line-height: 1;
}
.ov-value .unit { font-size: 14px; font-weight: 700; color: #2563eb; }
.ov-label { font-size: 11px; font-weight: 600; color: #64748b; text-align: center; }

/* ② 카테고리 정확도 */
.category-list { display: flex; flex-direction: column; gap: 14px; }
.category-row {
  display: grid;
  grid-template-columns: 1fr 1fr auto;
  align-items: center;
  gap: 12px;
}
.cat-info { display: flex; flex-direction: column; gap: 2px; }
.cat-name { font-size: 14px; font-weight: 800; color: #1e293b; }
.cat-counts { font-size: 12px; font-weight: 600; color: #94a3b8; }
.cat-bar-wrap {
  height: 8px;
  background: #f1f5f9;
  border-radius: 4px;
  overflow: hidden;
}
.cat-bar {
  height: 100%;
  border-radius: 4px;
  transition: width 0.6s ease;
}
.cat-rate {
  font-size: 14px; font-weight: 800;
  min-width: 44px; text-align: right;
}

/* 정확도 색상 */
.acc-high { color: #16a34a; }
.acc-high.cat-bar { background: #22c55e; }
.acc-mid  { color: #d97706; }
.acc-mid.cat-bar  { background: #f59e0b; }
.acc-low  { color: #dc2626; }
.acc-low.cat-bar  { background: #ef4444; }

/* ③ 문항 통계 */
.question-list { display: flex; flex-direction: column; gap: 10px; }
.question-row {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  transition: box-shadow 0.2s;
}
.question-row:hover { box-shadow: 0 4px 8px rgba(0,0,0,0.05); }

.q-left {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex-shrink: 0;
}
.q-id {
  font-size: 11px; font-weight: 700; color: #94a3b8;
}
.q-cat-badge {
  font-size: 11px; font-weight: 700;
  background: #e2e8f0; color: #475569;
  padding: 3px 7px; border-radius: 6px;
  white-space: nowrap;
}

.q-body { flex: 1; }
.q-text {
  font-size: 14px; font-weight: 700; color: #1e293b;
  margin: 0 0 8px; line-height: 1.5; word-break: keep-all;
}
.q-meta-row { display: flex; gap: 10px; }
.q-meta { font-size: 12px; font-weight: 600; color: #64748b; }
.q-meta.correct { color: #16a34a; }
.q-meta.wrong   { color: #dc2626; }

.q-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 6px;
  flex-shrink: 0;
}
.difficulty-badge {
  font-size: 11px; font-weight: 800;
  padding: 3px 8px; border-radius: 6px;
}
.diff-hard   { background: #fee2e2; color: #dc2626; }
.diff-normal { background: #fef3c7; color: #d97706; }
.diff-easy   { background: #dcfce7; color: #16a34a; }
.q-accuracy  { font-size: 15px; font-weight: 900; }

/* ④ 오답 TOP */
.top-wrong-list { display: flex; flex-direction: column; gap: 14px; }
.top-wrong-row {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}
.rank-badge {
  width: 32px; height: 32px;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 14px; font-weight: 900;
  flex-shrink: 0;
}
.rank-gold   { background: #fef3c7; color: #b45309; }
.rank-silver { background: #f1f5f9; color: #475569; }
.rank-bronze { background: #ffedd5; color: #c2410c; }
.rank-default { background: #f8fafc; color: #94a3b8; border: 1px solid #e2e8f0; }

.tw-body { flex: 1; }
.tw-text {
  font-size: 14px; font-weight: 700; color: #1e293b;
  margin: 0 0 6px; line-height: 1.5; word-break: keep-all;
}
.tw-meta-row { display: flex; gap: 10px; margin-bottom: 8px; }
.tw-cat { font-size: 12px; font-weight: 700; color: #64748b; }
.tw-counts { font-size: 12px; font-weight: 600; color: #94a3b8; }
.tw-bar-wrap {
  height: 6px; background: #fee2e2; border-radius: 3px; overflow: hidden;
}
.tw-bar {
  height: 100%; background: #ef4444; border-radius: 3px;
  transition: width 0.6s ease;
}

.tw-rate {
  font-size: 18px; font-weight: 900; color: #dc2626;
  flex-shrink: 0; text-align: right; min-width: 50px;
}
.tw-unit { font-size: 13px; font-weight: 700; }

/* 페이지네이션 */
.pager {
  display: flex; align-items: center; justify-content: center;
  gap: 4px; margin-top: 16px;
}
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
.pager-text { font-size: 14px; font-weight: 800; color: #334155; padding: 0 10px; }

/* 상태 메시지 */
.state-msg { text-align: center; padding: 32px; color: #64748b; font-weight: 600; }
.state-msg.error { color: #ef4444; }
</style>