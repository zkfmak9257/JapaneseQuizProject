<template>
  <section class="card" v-if="loading">
    <h2>퀴즈 결과</h2>
    <p>결과를 불러오는 중입니다...</p>
  </section>

  <section class="card" v-else-if="errorMessage">
    <h2>퀴즈 결과</h2>
    <p class="error">{{ errorMessage }}</p>
    <div class="actions actions-left">
      <button class="ghost" @click="loadResult">다시 시도</button>
      <RouterLink class="btn-link secondary" to="/">홈으로</RouterLink>
      <RouterLink class="btn-link" to="/quiz/start">다시 풀기</RouterLink>
    </div>
  </section>

  <section class="card" v-else-if="result">
    <h2>퀴즈 결과</h2>
    <ul class="result-list">
      <li>총 문제 수: {{ result.totalQuestions }}</li>
      <li>제출 수: {{ result.solvedCount }}</li>
      <li>정답 수: {{ result.correctCount }}</li>
      <li>정답률: {{ formatAccuracy(result.accuracy) }}</li>
      <li>완료 시각: {{ formatCompletedAt(result.completedAt) }}</li>
    </ul>

    <div class="actions actions-left">
      <button class="btn-link" @click="startNextQuiz" :disabled="startingNextQuiz">
        {{ startingNextQuiz ? "시작 중..." : "다음 퀴즈" }}
      </button>
      <RouterLink class="btn-link secondary" to="/quiz/start">한번 더 풀기</RouterLink>
      <RouterLink class="btn-link secondary" to="/quiz/wrong-answers">오답노트 보기</RouterLink>
      <RouterLink class="btn-link secondary" to="/">홈으로</RouterLink>
    </div>

    <p v-if="nextQuizErrorMessage" class="error">{{ nextQuizErrorMessage }}</p>
  </section>

  <div v-if="isGuest" class="modal-backdrop">
    <section class="modal-card">
      <h3>퀴즈 완료</h3>
      <p class="muted">기록/오답노트/즐겨찾기는 로그인 후 이용할 수 있습니다.</p>
      <div class="actions actions-left">
        <RouterLink class="btn-link" :to="{ path: '/login', query: { redirect: route.fullPath } }">로그인</RouterLink>
        <RouterLink class="btn-link secondary" to="/signup">회원가입</RouterLink>
        <RouterLink class="btn-link secondary" to="/">홈으로</RouterLink>
      </div>
    </section>
  </div>

  <section class="card" v-else-if="!loading && !result && !errorMessage">
    <h2>퀴즈 결과</h2>
    <p class="muted">표시할 결과가 없습니다.</p>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { getResult, startQuiz } from "../api/quizApi";
import { useAuthStore } from "../stores/authStore";

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const { isLoggedIn } = storeToRefs(authStore);
const result = ref(null);
const isGuest = ref(false);
const loading = ref(false);
const errorMessage = ref("");
const startingNextQuiz = ref(false);
const nextQuizErrorMessage = ref("");

function formatAccuracy(value) {
  if (typeof value !== "number") {
    return "-";
  }
  return `${value}%`;
}

function formatCompletedAt(value) {
  if (!value) {
    return "-";
  }
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return String(value);
  }
  return date.toLocaleString("ko-KR");
}

async function loadResult() {
  if (!isLoggedIn.value) {
    isGuest.value = true;
    result.value = null;
    errorMessage.value = "";
    return;
  }

  try {
    isGuest.value = false;
    loading.value = true;
    errorMessage.value = "";
    result.value = await getResult(Number(route.params.attemptId));
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      errorMessage.value = "아직 완료되지 않은 퀴즈입니다. 모든 문제를 제출한 뒤 다시 시도해 주세요.";
      return;
    }
    if (status === 401) {
      errorMessage.value = "로그인이 필요합니다. 다시 로그인해 주세요.";
      return;
    }
    if (status === 403) {
      errorMessage.value = "다른 사용자의 결과에는 접근할 수 없습니다.";
      return;
    }
    if (status === 404) {
      errorMessage.value = "결과 정보를 찾을 수 없습니다.";
      return;
    }
    errorMessage.value = "결과 조회 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
}

async function startNextQuiz() {
  if (!isLoggedIn.value) {
    router.push({ path: "/login", query: { redirect: "/quiz/start" } });
    return;
  }

  try {
    startingNextQuiz.value = true;
    nextQuizErrorMessage.value = "";
    const res = await startQuiz();
    router.push(`/quiz/attempts/${res.attemptId}/questions/1`);
  } catch (error) {
    const status = error?.response?.status;
    const code = error?.response?.data?.code;
    if (status === 403 && code === "GUEST_QUIZ_LIMIT_EXCEEDED") {
      nextQuizErrorMessage.value = "비회원은 추가 퀴즈 진행이 제한됩니다. 로그인 후 이용해 주세요.";
      return;
    }
    if (status === 401) {
      nextQuizErrorMessage.value = "다음 퀴즈를 시작하려면 로그인이 필요합니다.";
      return;
    }
    nextQuizErrorMessage.value = "다음 퀴즈 시작 중 오류가 발생했습니다.";
  } finally {
    startingNextQuiz.value = false;
  }
}

onMounted(loadResult);
</script>
