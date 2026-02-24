<template>
  <div class="reports-container">
    <!-- 헤더 -->
    <header class="reports-header">
      <div class="header-icon">🚨</div>
      <h1 class="header-title">신고 관리</h1>
      <p class="header-subtitle">여행자가 발견한 오류 신고를 검토합니다</p>
      <div class="blue-divider"></div>
    </header>

    <main class="reports-content">

      <!-- 상태 필터 탭 -->
      <div class="filter-tabs">
        <button
          v-for="tab in STATUS_TABS"
          :key="tab.value"
          class="tab-btn"
          :class="{ active: statusFilter === tab.value }"
          @click="setStatus(tab.value)"
        >
          {{ tab.label }}
        </button>
      </div>

      <!-- 로딩/에러/빈 상태 -->
      <div v-if="loading" class="state-msg">신고 목록을 불러오는 중...</div>
      <div v-else-if="errorMessage" class="state-msg error">⚠ {{ errorMessage }}</div>

      <template v-else>
        <div v-if="items.length === 0" class="empty-state">
          <div class="empty-icon">📭</div>
          <p>해당 상태의 신고가 없습니다.</p>
        </div>

        <!-- 신고 카드 리스트 -->
        <div v-else class="report-list">
          <div v-for="item in items" :key="item.reportId" class="report-card">

            <!-- 카드 헤더: 배지 + 날짜 -->
            <div class="card-header">
              <div class="badge-row">
                <span class="status-badge" :class="getStatusClass(item.status)">
                  {{ getStatusLabel(item.status) }}
                </span>
                <span class="type-badge">
                  {{ getTypeLabel(item.reportType) }}
                </span>
              </div>
              <span class="card-date">{{ formatDate(item.createdAt) }}</span>
            </div>

            <!-- 카드 본문 -->
            <div class="card-body">
              <p class="card-meta">
                문제 ID: <strong>#{{ item.questionId }}</strong>
                &nbsp;·&nbsp;
                신고자 ID: <strong>#{{ item.reporterId }}</strong>
              </p>
              <p class="card-content">{{ item.content || '(신고 내용 없음)' }}</p>
            </div>

            <!-- 처리 내용 -->
            <div v-if="item.action" class="action-box">
              <span class="action-label">처리 내용</span>
              <p class="action-text">{{ item.action }}</p>
            </div>
            <div v-else-if="item.status === 'RESOLVED' || item.status === 'REJECTED'" class="action-box empty">
              <p class="action-text muted">처리 내용 없음</p>
            </div>

            <!-- 상태 변경 버튼 (PENDING / IN_REVIEW만 표시) -->
            <div v-if="item.status === 'PENDING' || item.status === 'IN_REVIEW'" class="card-footer">
              <button
                class="update-toggle-btn"
                :class="{ open: openPanelId === item.reportId }"
                @click="togglePanel(item.reportId)"
              >
                {{ openPanelId === item.reportId ? '▲ 닫기' : '✏️ 상태 변경' }}
              </button>
            </div>

            <!-- 인라인 상태 변경 패널 -->
            <transition name="slide-down">
              <div v-if="openPanelId === item.reportId" class="update-panel">
                <div class="panel-field">
                  <label class="panel-label">변경할 상태</label>
                  <select v-model="editForm.status" class="panel-select">
                    <option v-for="opt in getAvailableStatuses(item.status)" :key="opt.value" :value="opt.value">
                      {{ opt.label }}
                    </option>
                  </select>
                </div>
                <div class="panel-field">
                  <label class="panel-label">처리 내용 <span class="required">*</span></label>
                  <textarea
                    v-model="editForm.action"
                    class="panel-textarea"
                    placeholder="처리 내용을 입력하세요 (최대 500자)"
                    maxlength="500"
                    rows="3"
                  ></textarea>
                  <span class="char-count">{{ editForm.action.length }} / 500</span>
                </div>
                <div v-if="updateError" class="panel-error">⚠ {{ updateError }}</div>
                <button
                  class="panel-submit-btn"
                  :disabled="!editForm.action.trim() || updating"
                  @click="submitUpdate(item.reportId)"
                >
                  {{ updating ? '저장 중...' : '저장' }}
                </button>
              </div>
            </transition>

          </div>
        </div>

        <!-- 페이지네이션 -->
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

    </main>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { getAdminReports, updateReportStatus } from "../api/reportApi";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from "lucide-vue-next";

const STATUS_TABS = [
  { value: null,        label: "전체" },
  { value: "PENDING",   label: "대기" },
  { value: "IN_REVIEW", label: "검토중" },
  { value: "RESOLVED",  label: "해결" },
  { value: "REJECTED",  label: "거절" },
];

const STATUS_TRANSITIONS = {
  PENDING:   [{ value: "IN_REVIEW", label: "검토 시작" }, { value: "RESOLVED", label: "해결" }, { value: "REJECTED", label: "거절" }],
  IN_REVIEW: [{ value: "RESOLVED",  label: "해결" },      { value: "REJECTED", label: "거절" }],
};

const items = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const page = ref(1);
const size = 10;
const totalPages = ref(1);
const statusFilter = ref(null);

const openPanelId = ref(null);
const editForm = reactive({ status: "", action: "" });
const updating = ref(false);
const updateError = ref("");

async function loadItems() {
  try {
    loading.value = true;
    errorMessage.value = "";
    openPanelId.value = null;
    const data = await getAdminReports(page.value, size, statusFilter.value);
    items.value = data?.content || [];
    totalPages.value = Math.max(data?.totalPages || 1, 1);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 403) {
      errorMessage.value = "접근 권한이 없습니다.";
    } else {
      errorMessage.value = "신고 목록을 불러오지 못했습니다.";
    }
  } finally {
    loading.value = false;
  }
}

function setStatus(value) {
  statusFilter.value = value;
  page.value = 1;
  loadItems();
}

async function changePage(next) {
  page.value = next;
  await loadItems();
}

function togglePanel(reportId) {
  if (openPanelId.value === reportId) {
    openPanelId.value = null;
  } else {
    openPanelId.value = reportId;
    editForm.status = "";
    editForm.action = "";
    updateError.value = "";
  }
}

function getAvailableStatuses(currentStatus) {
  return STATUS_TRANSITIONS[currentStatus] || [];
}

async function submitUpdate(reportId) {
  if (!editForm.action.trim() || !editForm.status) return;
  try {
    updating.value = true;
    updateError.value = "";
    const result = await updateReportStatus(reportId, editForm.status, editForm.action.trim());
    // 해당 카드 상태/처리 내용 즉시 반영
    const target = items.value.find(i => i.reportId === reportId);
    if (target) {
      target.status = result?.status ?? editForm.status;
      target.action = result?.action ?? editForm.action.trim();
      target.updatedAt = result?.updatedAt ?? target.updatedAt;
    }
    openPanelId.value = null;
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      updateError.value = "올바르지 않은 상태 전환입니다.";
    } else if (status === 404) {
      updateError.value = "해당 신고를 찾을 수 없습니다.";
    } else {
      updateError.value = "상태 변경 중 오류가 발생했습니다.";
    }
  } finally {
    updating.value = false;
  }
}

/* ── UI 보조 함수 ── */
function getStatusLabel(status) {
  const map = { PENDING: "대기", IN_REVIEW: "검토중", RESOLVED: "해결", REJECTED: "거절" };
  return map[status] ?? status;
}
function getStatusClass(status) {
  const map = { PENDING: "badge-pending", IN_REVIEW: "badge-review", RESOLVED: "badge-resolved", REJECTED: "badge-rejected" };
  return map[status] ?? "";
}
function getTypeLabel(type) {
  const map = { WRONG_ANSWER: "오답 오류", WRONG_CONTENT: "내용 오류", ETC: "기타" };
  return map[type] ?? type;
}
function formatDate(dateStr) {
  if (!dateStr) return "-";
  const d = new Date(dateStr);
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, "0")}.${String(d.getDate()).padStart(2, "0")}`;
}

onMounted(loadItems);
</script>

<style scoped>
.reports-container {
  min-height: 100vh;
  background-color: #f8fafc;
  padding: 40px 16px 80px;
  font-family: Pretendard, "Noto Sans KR", sans-serif;
  color: #1e293b;
}

/* 헤더 */
.reports-header {
  text-align: center;
  margin-bottom: 32px;
}
.header-icon {
  font-size: 40px;
  margin-bottom: 8px;
  animation: float 2.5s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50%       { transform: translateY(-5px); }
}
.header-title {
  font-size: 26px;
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

.reports-content {
  max-width: 680px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 필터 탭 */
.filter-tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.tab-btn {
  padding: 8px 18px;
  border-radius: 20px;
  border: 1px solid #e2e8f0;
  background: white;
  font-size: 14px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s;
}
.tab-btn:hover { background: #f1f5f9; }
.tab-btn.active {
  background: #0f172a;
  color: white;
  border-color: #0f172a;
}

/* 신고 카드 */
.report-list { display: flex; flex-direction: column; gap: 12px; }

.report-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);
  transition: box-shadow 0.2s;
}
.report-card:hover { box-shadow: 0 6px 12px rgba(0, 0, 0, 0.06); }

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px 0;
}
.badge-row { display: flex; gap: 8px; align-items: center; }

/* 상태 배지 */
.status-badge {
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.3px;
}
.badge-pending  { background: #fef3c7; color: #d97706; }
.badge-review   { background: #dbeafe; color: #2563eb; }
.badge-resolved { background: #dcfce7; color: #16a34a; }
.badge-rejected { background: #fee2e2; color: #dc2626; }

.type-badge {
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  background: #f1f5f9;
  color: #475569;
}
.card-date {
  font-size: 12px;
  font-weight: 600;
  color: #94a3b8;
}

.card-body { padding: 12px 20px; }
.card-meta {
  font-size: 13px;
  color: #64748b;
  margin: 0 0 8px;
}
.card-content {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
  line-height: 1.6;
  margin: 0;
  word-break: keep-all;
}

/* 처리 내용 박스 */
.action-box {
  margin: 0 20px 12px;
  background: #f8fafc;
  border-left: 3px solid #2563eb;
  border-radius: 0 8px 8px 0;
  padding: 10px 14px;
}
.action-box.empty { border-left-color: #e2e8f0; }
.action-label {
  font-size: 11px;
  font-weight: 800;
  color: #2563eb;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  display: block;
  margin-bottom: 4px;
}
.action-text {
  font-size: 14px;
  font-weight: 500;
  color: #334155;
  margin: 0;
  line-height: 1.5;
}
.action-text.muted { color: #94a3b8; }

/* 카드 푸터: 상태 변경 버튼 */
.card-footer { padding: 0 20px 16px; }
.update-toggle-btn {
  padding: 8px 16px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  font-size: 13px;
  font-weight: 700;
  color: #475569;
  cursor: pointer;
  transition: all 0.2s;
}
.update-toggle-btn:hover { background: #f1f5f9; border-color: #94a3b8; }
.update-toggle-btn.open { background: #eff6ff; border-color: #93c5fd; color: #2563eb; }

/* 인라인 업데이트 패널 */
.update-panel {
  border-top: 1px solid #e2e8f0;
  background: #f8fafc;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

/* 슬라이드 애니메이션 */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.25s ease;
  overflow: hidden;
}
.slide-down-enter-from,
.slide-down-leave-to {
  opacity: 0;
  max-height: 0;
  padding-top: 0;
  padding-bottom: 0;
}
.slide-down-enter-to,
.slide-down-leave-from {
  opacity: 1;
  max-height: 300px;
}

.panel-field { display: flex; flex-direction: column; gap: 6px; }
.panel-label {
  font-size: 13px;
  font-weight: 700;
  color: #374151;
}
.required { color: #ef4444; }

.panel-select {
  padding: 10px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: white;
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  outline: none;
  cursor: pointer;
}
.panel-select:focus { border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }

.panel-textarea {
  padding: 10px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: white;
  font-size: 14px;
  font-weight: 500;
  color: #1e293b;
  outline: none;
  resize: vertical;
  font-family: inherit;
  line-height: 1.5;
}
.panel-textarea:focus { border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }

.char-count {
  font-size: 12px;
  color: #94a3b8;
  font-weight: 600;
  text-align: right;
}

.panel-error {
  font-size: 13px;
  font-weight: 600;
  color: #ef4444;
}

.panel-submit-btn {
  padding: 12px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 800;
  cursor: pointer;
  transition: all 0.2s;
}
.panel-submit-btn:hover:not(:disabled) {
  background: #1d4ed8;
  transform: translateY(-1px);
}
.panel-submit-btn:disabled {
  background: #cbd5e1;
  cursor: not-allowed;
}

/* 페이지네이션 */
.pager { display: flex; align-items: center; justify-content: center; gap: 4px; margin-top: 8px; }
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
.state-msg { text-align: center; padding: 40px; color: #64748b; font-weight: 600; }
.state-msg.error { color: #ef4444; }

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border: 2px dashed #e2e8f0;
  border-radius: 16px;
  color: #64748b;
}
.empty-icon { font-size: 48px; margin-bottom: 16px; }
</style>