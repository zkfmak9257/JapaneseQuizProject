<template>
  <section class="card">
    <h2>관리자 통계</h2>

    <div class="form admin-stats-filters">
      <div class="field-inline">
        <label for="basis">기준</label>
        <select id="basis" v-model="basis" @change="reloadAll">
          <option value="latest">latest</option>
          <option value="first">first</option>
        </select>
      </div>
      <div class="field-inline">
        <label for="size">문항 size</label>
        <select id="size" v-model.number="size" @change="onSizeChange">
          <option :value="10">10</option>
          <option :value="20">20</option>
          <option :value="50">50</option>
        </select>
      </div>
      <div class="field-inline">
        <label for="limit">오답 TOP</label>
        <select id="limit" v-model.number="limit" @change="loadTopWrong">
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select>
      </div>
    </div>

    <section class="admin-section">
      <h3>개요</h3>
      <LoadingState v-if="overviewLoading" message="개요를 불러오는 중입니다..." />
      <ErrorState
        v-else-if="overviewError"
        :message="overviewError"
        :show-retry="true"
        @retry="loadOverview"
      />
      <div v-else class="stats-grid">
        <article class="stat-card">
          <p class="stat-label">총 시도</p>
          <p class="stat-value">{{ overview.totalAttempts }}</p>
        </article>
        <article class="stat-card">
          <p class="stat-label">완료 시도</p>
          <p class="stat-value">{{ overview.completedAttempts }}</p>
        </article>
        <article class="stat-card">
          <p class="stat-label">총 제출</p>
          <p class="stat-value">{{ overview.totalAnswers }}</p>
        </article>
        <article class="stat-card">
          <p class="stat-label">정답 수</p>
          <p class="stat-value">{{ overview.correctAnswers }}</p>
        </article>
        <article class="stat-card">
          <p class="stat-label">완료율</p>
          <p class="stat-value">{{ overview.completionRate }}%</p>
        </article>
        <article class="stat-card">
          <p class="stat-label">정답률</p>
          <p class="stat-value">{{ overview.accuracyRate }}%</p>
        </article>
        <article class="stat-card">
          <p class="stat-label">최근 7일 활성 유저</p>
          <p class="stat-value">{{ overview.activeUsers7d }}</p>
        </article>
      </div>
    </section>

    <section class="admin-section">
      <h3>카테고리 정확도</h3>
      <LoadingState v-if="categoriesLoading" message="카테고리 통계를 불러오는 중입니다..." />
      <ErrorState
        v-else-if="categoriesError"
        :message="categoriesError"
        :show-retry="true"
        @retry="loadCategories"
      />
      <EmptyState v-else-if="categories.length === 0" message="카테고리 통계 데이터가 없습니다." />
      <ul v-else class="list-view">
        <li v-for="item in categories" :key="item.category" class="list-item">
          <p><strong>카테고리:</strong> {{ item.category }}</p>
          <p><strong>총 답변:</strong> {{ item.totalAnswers }}</p>
          <p><strong>정답:</strong> {{ item.correctAnswers }}</p>
          <p><strong>정답률:</strong> {{ item.accuracyRate }}%</p>
        </li>
      </ul>
    </section>

    <section class="admin-section">
      <h3>문항 통계</h3>
      <LoadingState v-if="questionsLoading" message="문항 통계를 불러오는 중입니다..." />
      <ErrorState
        v-else-if="questionsError"
        :message="questionsError"
        :show-retry="true"
        @retry="loadQuestions"
      />
      <EmptyState
        v-else-if="questionPage.content.length === 0"
        message="문항 통계 데이터가 없습니다."
      />
      <template v-else>
        <ul class="list-view">
          <li v-for="item in questionPage.content" :key="item.questionId" class="list-item">
            <p><strong>문제ID:</strong> {{ item.questionId }}</p>
            <p><strong>문장:</strong> {{ item.questionText }}</p>
            <p><strong>카테고리:</strong> {{ item.category || "-" }}</p>
            <p><strong>총 답변:</strong> {{ item.totalAnswers }}</p>
            <p><strong>정답/오답:</strong> {{ item.correctAnswers }} / {{ item.wrongAnswers }}</p>
            <p><strong>정답률:</strong> {{ item.accuracyRate }}%</p>
            <p><strong>난이도:</strong> {{ item.difficultyLevel }} ({{ item.difficultyScore }})</p>
          </li>
        </ul>
        <div class="actions actions-left pager">
          <button class="ghost" :disabled="page <= 1" @click="changePage(page - 1)">이전</button>
          <span>{{ page }} / {{ totalPages }}</span>
          <button class="ghost" :disabled="page >= totalPages" @click="changePage(page + 1)">다음</button>
        </div>
      </template>
    </section>

    <section class="admin-section">
      <h3>오답 TOP</h3>
      <LoadingState v-if="topWrongLoading" message="오답 TOP 통계를 불러오는 중입니다..." />
      <ErrorState
        v-else-if="topWrongError"
        :message="topWrongError"
        :show-retry="true"
        @retry="loadTopWrong"
      />
      <EmptyState v-else-if="topWrongItems.length === 0" message="오답 TOP 데이터가 없습니다." />
      <ul v-else class="list-view">
        <li v-for="item in topWrongItems" :key="item.questionId" class="list-item">
          <p><strong>문제ID:</strong> {{ item.questionId }}</p>
          <p><strong>문장:</strong> {{ item.questionText }}</p>
          <p><strong>카테고리:</strong> {{ item.category || "-" }}</p>
          <p><strong>오답/총답변:</strong> {{ item.wrongAnswers }} / {{ item.totalAnswers }}</p>
          <p><strong>오답률:</strong> {{ item.wrongRate }}%</p>
        </li>
      </ul>
    </section>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import {
  getAdminOverview,
  getCategoryAccuracy,
  getQuestionStats,
  getTopWrongQuestions
} from "../api/statsApi";
import LoadingState from "../components/ui/LoadingState.vue";
import ErrorState from "../components/ui/ErrorState.vue";
import EmptyState from "../components/ui/EmptyState.vue";

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
const questionPage = reactive({
  content: [],
  totalPages: 1
});
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
  if (status === 400) {
    return "요청 값이 올바르지 않습니다.";
  }
  if (status === 401) {
    return "로그인이 필요합니다.";
  }
  if (status === 403) {
    return "접근 권한이 없습니다.";
  }
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
    overviewError.value = toErrorMessage(error, "관리자 개요 통계를 불러오지 못했습니다.");
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
    const data = await getQuestionStats({
      basis: basis.value,
      page: page.value,
      size: size.value
    });
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

onMounted(() => {
  Promise.all([loadOverview(), loadCategories(), loadQuestions(), loadTopWrong()]);
});
</script>
