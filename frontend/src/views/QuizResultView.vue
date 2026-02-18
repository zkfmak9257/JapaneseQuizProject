<template>
  <section class="card" v-if="isGuest">
    <h2>퀴즈 완료</h2>
    <p class="muted">비회원은 결과 저장/오답노트 이용을 위해 로그인이 필요합니다.</p>
    <div class="actions">
      <RouterLink class="btn-link" :to="{ path: '/login', query: { redirect: route.fullPath } }">로그인</RouterLink>
      <RouterLink class="btn-link secondary" to="/signup">회원가입</RouterLink>
    </div>
  </section>
  <section class="card" v-else-if="result">
    <h2>퀴즈 결과</h2>
    <ul>
      <li>총 문제 수: {{ result.totalQuestions }}</li>
      <li>제출 수: {{ result.solvedCount }}</li>
      <li>정답 수: {{ result.correctCount }}</li>
      <li>정답률: {{ result.accuracy }}%</li>
      <li>완료 시각: {{ result.completedAt }}</li>
    </ul>
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

onMounted(async () => {
  if (!isLoggedIn.value) {
    isGuest.value = true;
    return;
  }
  result.value = await getResult(Number(route.params.attemptId));
});
</script>
