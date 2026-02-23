<template>
  <!-- layout: 전체 페이지를 감싸는 최상위 컨테이너 -->
  <div class="layout">

    <!-- ── 헤더 — SaaS 스타일 내비게이션 ────────────────
         디자인 포인트:
         - 깔끔한 흰색 배경 + 얇은 border-bottom
         - 로고: 旅(인디고) + Quiz(슬레이트) + 부제
         - 메뉴: hover 시 인디고 underline 애니메이션
         - 프로필: 드롭다운 (마이페이지/로그아웃) -->
    <header class="topbar" v-if="!isAuthPage">
      <div class="topbar-inner">

        <!-- ── 로고 영역 ──────────────────────────────── 
             旅(인디고) + Quiz(슬레이트)로 브랜드 개성 표현.
             아래에 '일본어를 여행처럼' 부제 -->
        <RouterLink class="brand" to="/">
          <span class="brand-title">
            <span class="brand-tabi">旅</span><span class="brand-quiz">Quiz</span>
          </span>
          <span class="brand-subtitle">일본어를 여행처럼</span>
        </RouterLink>

        <!-- ── 데스크탑 네비게이션 ─────────────────────── 
             아이콘 + 텍스트 중심의 명확한 네비게이션 -->
        <nav class="topbar-nav" v-if="isLoggedIn">
          <RouterLink to="/me/history" class="nav-text-link">
            <span class="nav-icon">✈️</span> 여행 기록
          </RouterLink>
          <RouterLink to="/quiz/wrong-answers" class="nav-text-link">
            <span class="nav-icon">🔥</span> 미션 훈련소
          </RouterLink>
          <RouterLink to="/quiz/favorites" class="nav-text-link">
            <span class="nav-icon">🛂</span> 나의 여행 도장
          </RouterLink>
          <RouterLink v-if="isAdmin" to="/admin" class="nav-text-link">
            <span class="nav-icon">⚙️</span> 관리자
          </RouterLink>
        </nav>

        <!-- ── 오른쪽 영역: 인증 상태별 UI ────────────── -->
        <div class="topbar-actions">

          <!-- 비로그인 상태: 로그인/회원가입 버튼 -->
          <template v-if="!isLoggedIn">
            <RouterLink to="/login" class="btn-auth">로그인</RouterLink>
            <RouterLink to="/signup" class="btn-auth btn-auth-primary">회원가입</RouterLink>
          </template>

          <!-- 로그인 상태: 프로필 드롭다운 -->
          <div v-else class="profile-dropdown" @click="toggleProfileMenu">
            <!-- 프로필 버튼 — 닉네임 + 드롭다운 화살표 -->
            <button class="profile-trigger">
              <span class="profile-avatar">👤</span>
              <span class="profile-name">{{ displayName }}</span>
              <span class="profile-arrow" :class="{ open: showProfileMenu }">▾</span>
            </button>

            <!-- 드롭다운 메뉴 — 클릭 시 열림/닫힘 -->
            <div v-if="showProfileMenu" class="profile-menu">
              <RouterLink to="/mypage" class="profile-menu-item" @click="closeProfileMenu">
                마이페이지
              </RouterLink>
              <button class="profile-menu-item profile-menu-logout" @click="onLogout">
                로그아웃
              </button>
            </div>
          </div>
        </div>

        <!-- ── 모바일 햄버거 메뉴 ─────────────────────── -->
        <button class="mobile-menu-btn" @click="toggleMobileMenu" aria-label="메뉴">
          <span class="hamburger-line" :class="{ open: showMobileMenu }"></span>
        </button>
      </div>

      <!-- ── 모바일 메뉴 패널 ──────────────────────────
           햄버거 클릭 시 슬라이드 다운으로 열림 -->
      <div v-if="showMobileMenu" class="mobile-menu">
        <template v-if="isLoggedIn">
          <div class="mobile-menu-section">내 여권</div>
          <RouterLink to="/me/history" class="mobile-menu-link" @click="closeMobileMenu">✈️ 여행 기록</RouterLink>
          <RouterLink to="/quiz/wrong-answers" class="mobile-menu-link" @click="closeMobileMenu">🔥 미션 훈련소</RouterLink>
          <RouterLink to="/quiz/favorites" class="mobile-menu-link" @click="closeMobileMenu">🛂 나의 여행 도장</RouterLink>
        </template>
        <RouterLink v-if="isAdmin" to="/admin" class="mobile-menu-link" @click="closeMobileMenu">⚙️ 관리자</RouterLink>
        <template v-if="!isLoggedIn">
          <RouterLink to="/login" class="mobile-menu-link" @click="closeMobileMenu">로그인</RouterLink>
          <RouterLink to="/signup" class="mobile-menu-link" @click="closeMobileMenu">회원가입</RouterLink>
        </template>
        <template v-else>
          <RouterLink to="/mypage" class="mobile-menu-link" @click="closeMobileMenu">마이페이지</RouterLink>
          <button class="mobile-menu-link mobile-menu-logout" @click="onLogout">로그아웃</button>
        </template>
      </div>
    </header>

    <!-- 메인 콘텐츠 영역 — RouterView로 페이지 컴포넌트 렌더링 -->
    <main class="content">
      <RouterView />
    </main>

    <!-- ── 푸터 ────────────────────────────────────── -->
    <footer class="site-footer">
      <p>© 2026 旅Quiz — 일본어를 여행처럼 배우다</p>
    </footer>
  </div>

  <!-- 프로필 드롭다운 외부 클릭 시 닫기 -->
  <div v-if="showProfileMenu" class="profile-overlay" @click="closeProfileMenu"></div>
</template>

<script setup>
// Vue 반응형/라이프사이클 API
import { computed, onMounted, ref } from "vue";
// Pinia의 storeToRefs: store state를 반응형으로 구독
import { storeToRefs } from "pinia";
// Vue Router 컴포넌트
import { RouterLink, RouterView, useRoute } from "vue-router";
// 인증 상태 관리 스토어
import { useAuthStore } from "./stores/authStore";

const authStore = useAuthStore();
const route = useRoute();
// storeToRefs: 반응형(ref)으로 변환하여 template에서 사용
const { isLoggedIn, isAdmin } = storeToRefs(authStore);

// 로그인/회원가입 페이지인지 확인하여 헤더 숨김 처리
const isAuthPage = computed(() => {
  return ["/login", "/signup"].includes(route.path);
});

// 프로필 드롭다운 열림/닫힘 상태
const showProfileMenu = ref(false);
// 모바일 메뉴 열림/닫힘 상태
const showMobileMenu = ref(false);

/**
 * displayName — 프로필에 표시할 이름
 * 닉네임이 있으면 닉네임, 없으면 이메일 앞부분
 */
const displayName = computed(() => {
  const p = authStore.profile;
  if (!p) {
    return "사용자";
  }
  if (p.nickname) {
    return p.nickname;
  }
  if (p.email) {
    return p.email.split("@")[0];
  }
  return "사용자";
});


// 마운트 시: 로그인 상태지만 프로필이 없으면 서버에서 가져오기
onMounted(async () => {
  if (isLoggedIn.value && !authStore.profile) {
    await authStore.fetchMe();
  }
});

// 프로필 드롭다운 토글
function toggleProfileMenu() {
  showProfileMenu.value = !showProfileMenu.value;
}

function closeProfileMenu() {
  showProfileMenu.value = false;
}

// 모바일 메뉴 토글
function toggleMobileMenu() {
  showMobileMenu.value = !showMobileMenu.value;
}

function closeMobileMenu() {
  showMobileMenu.value = false;
}

// 로그아웃 핸들러
function onLogout() {
  closeProfileMenu();
  closeMobileMenu();
  authStore.logout();
}
</script>

<style scoped>
/* ── 푸터 스타일 ──────────────────────────────────── */
.site-footer {
  /* 배경 투명 → body의 그라데이션이 이어짐 */
  background: transparent;
  color: #64748b;                 /* 슬레이트 500 */
  text-align: center;
  padding: 24px 20px;
  font-size: 13px;
  font-family: var(--font-body);
  border-top: 1px solid rgba(79, 70, 229, 0.08);
}

/* ── 프로필 드롭다운 오버레이 — 외부 클릭 감지 ────── */
/* ── 데스크탑 네비게이션 (텍스트+아이콘) ────── */
.nav-text-link {
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
  color: var(--text-muted);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 8px;
}

.nav-icon {
  font-size: 16px;
}

.nav-text-link:hover {
  color: var(--ocean);
  background: rgba(126, 200, 227, 0.1);
}

.nav-text-link.router-link-active {
  color: var(--ocean);
  font-weight: 800;
  background: rgba(126, 200, 227, 0.15);
}
</style>
