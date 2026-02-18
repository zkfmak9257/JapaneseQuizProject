<template>
  <section class="card">
    <h2>퀴즈 시작</h2>
    <p>문제 수는 10문제로 고정됩니다.</p>
    <button @click="start" :disabled="loading">{{ loading ? "시작 중..." : "퀴즈 시작" }}</button>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { startQuiz } from "../api/quizApi";

const router = useRouter();
const loading = ref(false);
const errorMessage = ref("");

async function start() {
  try {
    loading.value = true;
    errorMessage.value = "";
    const res = await startQuiz();
    router.push(`/quiz/attempts/${res.attemptId}/questions/1`);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      errorMessage.value = "퀴즈를 시작할 수 없습니다. 잠시 후 다시 시도해 주세요.";
      return;
    }
    if (status === 401) {
      errorMessage.value = "인증 상태가 만료되었습니다. 다시 로그인해 주세요.";
      return;
    }
    if (status === 403) {
      errorMessage.value = "해당 요청에 대한 권한이 없습니다.";
      return;
    }
    if (status === 404) {
      errorMessage.value = "요청한 경로를 찾을 수 없습니다.";
      return;
    }
    errorMessage.value = "퀴즈 시작 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
}
</script>
