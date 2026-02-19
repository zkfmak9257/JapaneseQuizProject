<template>
  <div class="layout">
    <header class="topbar">
      <h1><RouterLink class="brand" to="/">Japanese Quiz</RouterLink></h1>
      <nav>
        <RouterLink to="/quiz/start">퀴즈 시작</RouterLink>
        <RouterLink v-if="isLoggedIn" to="/mypage">마이페이지</RouterLink>
        <RouterLink v-if="isLoggedIn" to="/me/history">학습기록</RouterLink>
        <RouterLink v-if="isLoggedIn" to="/quiz/favorites">즐겨찾기</RouterLink>
        <RouterLink v-if="isLoggedIn" to="/quiz/wrong-answers">오답노트</RouterLink>
        <RouterLink v-if="isAdmin" to="/admin">관리자</RouterLink>
        <template v-if="!isLoggedIn">
          <RouterLink to="/login">로그인</RouterLink>
          <RouterLink to="/signup">회원가입</RouterLink>
        </template>
        <button v-else class="logout" @click="onLogout">로그아웃</button>
      </nav>
    </header>
    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { storeToRefs } from "pinia";
import { RouterLink, RouterView } from "vue-router";
import { useAuthStore } from "./stores/authStore";

const authStore = useAuthStore();
const { isLoggedIn, isAdmin } = storeToRefs(authStore);

onMounted(async () => {
  if (isLoggedIn.value && !authStore.profile) {
    await authStore.fetchMe();
  }
});

function onLogout() {
  authStore.logout();
}
</script>
