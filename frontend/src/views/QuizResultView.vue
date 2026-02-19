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
      <RouterLink class="btn-link" to="/quiz/start">다시 풀기</RouterLink>
      <RouterLink class="btn-link secondary" to="/quiz/wrong-answers">오답노트 보기</RouterLink>
      <RouterLink class="btn-link secondary" to="/">홈으로</RouterLink>
    </div>
  </section>

  <div v-if="isGuest" class="modal-backdrop">
    <section class="modal-card">
      <h3>퀴즈 완료</h3>
      <p class="muted">비회원은 결과 확인과 오답노트 이용을 위해 로그인이 필요합니다.</p>
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
import { RouterLink, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { getResult } from "../api/quizApi";
import { useAuthStore } from "../stores/authStore";

const route = useRoute();
const authStore = useAuthStore();
const { isLoggedIn } = storeToRefs(authStore);
const result = ref(null);
const isGuest = ref(false);
const loading = ref(false);
const errorMessage = ref("");

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

onMounted(loadResult);
</script>
