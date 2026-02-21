<template>
  <!-- layout: 전체 페이지를 감싸는 최상위 컨테이너 -->
  <div class="layout">
    <!-- ── 네비게이션 바 ──────────────────────────────
         글라스모피즘 스타일의 상단 고정 네비게이션
         스크롤해도 항상 보임 (sticky) -->
    <header class="topbar">
      <!-- 로고 — 클릭 시 메인 페이지로 이동 -->
      <h1>
        <RouterLink class="brand" to="/">
          <!-- 🗾는 일본 지도 이모지 — 브랜드 아이덴티티 강화 -->
          <span class="brand-icon">🗾</span>
          <span class="brand-text">타비퀴즈</span>
        </RouterLink>
      </h1>

      <!-- 네비게이션 링크들 -->
      <nav>
        <RouterLink to="/quiz/start">퀴즈 시작</RouterLink>
        <!-- v-if로 로그인 상태에 따라 조건부 렌더링 -->
        <RouterLink v-if="isLoggedIn" to="/mypage">마이페이지</RouterLink>
        <RouterLink v-if="isLoggedIn" to="/me/history">학습기록</RouterLink>
        <RouterLink v-if="isLoggedIn" to="/quiz/favorites">즐겨찾기</RouterLink>
        <RouterLink v-if="isLoggedIn" to="/quiz/wrong-answers">오답노트</RouterLink>
        <RouterLink v-if="isAdmin" to="/admin">관리자</RouterLink>
        <!-- 비로그인 상태: 로그인/회원가입 링크 표시 -->
        <template v-if="!isLoggedIn">
          <RouterLink to="/login">로그인</RouterLink>
          <RouterLink to="/signup">회원가입</RouterLink>
        </template>
        <!-- 로그인 상태: 로그아웃 버튼 표시 -->
        <button v-else class="logout" @click="onLogout">로그아웃</button>
      </nav>
    </header>

    <!-- 메인 콘텐츠 영역 — RouterView로 페이지 컴포넌트 렌더링 -->
    <main class="content">
      <RouterView />
    </main>

    <!-- ── 푸터 ────────────────────────────────────── -->
    <footer class="site-footer">
      <p>© 2026 타비퀴즈 — 일본어를 여행처럼 배우다</p>
    </footer>
  </div>
</template>

<script setup>
// Vue 라이프사이클 훅: 컴포넌트 마운트 시 실행
import { onMounted } from "vue";
// Pinia의 storeToRefs: store의 state를 반응형으로 구독
import { storeToRefs } from "pinia";
// Vue Router 컴포넌트
import { RouterLink, RouterView } from "vue-router";
// 인증 상태 관리 스토어
import { useAuthStore } from "./stores/authStore";

const authStore = useAuthStore();
// storeToRefs: 반응형(ref)으로 변환하여 template에서 사용
const { isLoggedIn, isAdmin } = storeToRefs(authStore);

// 마운트 시: 로그인 상태지만 프로필이 없으면 서버에서 가져오기
onMounted(async () => {
  if (isLoggedIn.value && !authStore.profile) {
    await authStore.fetchMe();
  }
});

// 로그아웃 핸들러
function onLogout() {
  authStore.logout();
}
</script>

<style scoped>
/* ── 푸터 스타일 ──────────────────────────────────── */
.site-footer {
  /* 바다색 배경의 깔끔한 푸터 */
  background: var(--dark);
  color: rgba(255, 255, 255, 0.6);
  text-align: center;
  padding: 20px;
  font-size: 13px;
  font-family: var(--font-body);
}

/* 브랜드 아이콘 + 텍스트 조합 */
.brand-icon {
  font-size: 22px;
  margin-right: 6px;
  vertical-align: middle;
}

.brand-text {
  font-weight: 800;
  font-size: 20px;
  letter-spacing: -0.5px;
  vertical-align: middle;
}
</style>
