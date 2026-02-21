<template>
  <section class="card">
    <h2>개인 통계</h2>

    <LoadingState v-if="loading" message="개인 통계를 불러오는 중입니다..." />

    <ErrorState
      v-else-if="errorMessage"
      :message="errorMessage"
      :show-retry="true"
      @retry="loadStats"
    />

    <EmptyState v-else-if="isEmpty" message="아직 통계 데이터가 없습니다." />

    <div v-else class="stats-grid">
      <article class="stat-card">
        <p class="stat-label">총 시도</p>
        <p class="stat-value">{{ stats.totalAttempts }}</p>
      </article>
      <article class="stat-card">
        <p class="stat-label">완료 시도</p>
        <p class="stat-value">{{ stats.completedAttempts }}</p>
      </article>
      <article class="stat-card">
        <p class="stat-label">총 제출</p>
        <p class="stat-value">{{ stats.totalAnswers }}</p>
      </article>
      <article class="stat-card">
        <p class="stat-label">정답 수</p>
        <p class="stat-value">{{ stats.correctAnswers }}</p>
      </article>
      <article class="stat-card">
        <p class="stat-label">완료율</p>
        <p class="stat-value">{{ stats.completionRate }}%</p>
      </article>
      <article class="stat-card">
        <p class="stat-label">정답률</p>
        <p class="stat-value">{{ stats.accuracyRate }}%</p>
      </article>
      <article class="stat-card">
        <p class="stat-label">최근 7일 제출</p>
        <p class="stat-value">{{ stats.recent7dAnswers }}</p>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { getMyStats } from "../api/statsApi";
import LoadingState from "../components/ui/LoadingState.vue";
import ErrorState from "../components/ui/ErrorState.vue";
import EmptyState from "../components/ui/EmptyState.vue";

const stats = reactive({
  totalAttempts: 0,
  completedAttempts: 0,
  totalAnswers: 0,
  correctAnswers: 0,
  recent7dAnswers: 0,
  completionRate: 0,
  accuracyRate: 0
});

const loading = ref(false);
const errorMessage = ref("");

const isEmpty = computed(() => {
  return (
    stats.totalAttempts === 0 &&
    stats.completedAttempts === 0 &&
    stats.totalAnswers === 0 &&
    stats.correctAnswers === 0 &&
    stats.recent7dAnswers === 0
  );
});

function toErrorMessage(error) {
  const status = error?.response?.status;
  if (status === 400) {
    return "요청 값이 올바르지 않습니다.";
  }
  if (status === 401) {
    return "로그인이 필요합니다.";
  }
  if (status === 403) {
    return "접근 권한이 없습니다.";
  }
  return "개인 통계를 불러오지 못했습니다.";
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
  } catch (error) {
    errorMessage.value = toErrorMessage(error);
  } finally {
    loading.value = false;
  }
}

onMounted(loadStats);
</script>
