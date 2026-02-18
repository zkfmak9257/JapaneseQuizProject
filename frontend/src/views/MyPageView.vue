<template>
  <section class="card">
    <h2>마이페이지</h2>

    <section class="mypage-panel">
      <h3>회원 정보</h3>
      <p v-if="profileLoading" class="muted">회원 정보를 불러오는 중입니다...</p>
      <p v-else-if="profileErrorMessage" class="error">{{ profileErrorMessage }}</p>
      <form v-else class="form" @submit.prevent="saveProfile">
        <input :value="profile.email" type="email" disabled />
        <input v-model="nickname" type="text" placeholder="닉네임" />
        <button type="submit" :disabled="savingProfile">
          {{ savingProfile ? "저장 중..." : "닉네임 저장" }}
        </button>
      </form>
      <p v-if="profileSuccessMessage" class="muted">{{ profileSuccessMessage }}</p>
    </section>

    <section class="mypage-panel">
      <h3>학습 바로가기</h3>
      <div class="actions mypage-actions">
        <RouterLink class="btn-link" to="/quiz/wrong-answers">오답노트</RouterLink>
        <RouterLink class="btn-link secondary" to="/quiz/favorites">즐겨찾기</RouterLink>
      </div>
    </section>

    <section class="mypage-panel">
      <h3>학습 통계</h3>
      <p v-if="statsLoading" class="muted">통계를 불러오는 중입니다...</p>
      <p v-else-if="statsErrorMessage" class="error">{{ statsErrorMessage }}</p>
      <ul v-else class="stats-list">
        <li>총 시도 수: {{ stats.totalAttempts }}</li>
        <li>완료 시도 수: {{ stats.completedAttempts }}</li>
        <li>총 제출 수: {{ stats.totalAnswers }}</li>
        <li>정답 수: {{ stats.correctAnswers }}</li>
        <li>정답률: {{ stats.accuracyRate }}%</li>
      </ul>
    </section>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { RouterLink } from "vue-router";
import { getMyProfile, updateMyProfile } from "../api/memberApi";
import { getMyStats } from "../api/statsApi";

const profile = reactive({
  email: "",
  nickname: ""
});
const stats = reactive({
  totalAttempts: 0,
  completedAttempts: 0,
  totalAnswers: 0,
  correctAnswers: 0,
  accuracyRate: 0
});

const nickname = ref("");
const profileLoading = ref(false);
const profileErrorMessage = ref("");
const profileSuccessMessage = ref("");
const savingProfile = ref(false);

const statsLoading = ref(false);
const statsErrorMessage = ref("");

function toErrorMessage(error, defaultMessage) {
  const status = error?.response?.status;
  if (status === 401) {
    return "로그인이 필요합니다.";
  }
  if (status === 403) {
    return "접근 권한이 없습니다.";
  }
  if (status === 404) {
    return "데이터를 찾을 수 없습니다.";
  }
  if (status === 400) {
    return error?.response?.data?.message || "요청 값이 올바르지 않습니다.";
  }
  return defaultMessage;
}

async function loadProfile() {
  try {
    profileLoading.value = true;
    profileErrorMessage.value = "";
    const data = await getMyProfile();
    profile.email = data?.email || "";
    profile.nickname = data?.nickname || "";
    nickname.value = profile.nickname;
  } catch (error) {
    profileErrorMessage.value = toErrorMessage(error, "회원 정보를 불러오지 못했습니다.");
  } finally {
    profileLoading.value = false;
  }
}

async function saveProfile() {
  if (!nickname.value.trim()) {
    profileErrorMessage.value = "닉네임을 입력해 주세요.";
    return;
  }

  try {
    savingProfile.value = true;
    profileErrorMessage.value = "";
    profileSuccessMessage.value = "";

    await updateMyProfile({ nickname: nickname.value.trim() });
    profile.nickname = nickname.value.trim();
    profileSuccessMessage.value = "닉네임이 수정되었습니다.";
  } catch (error) {
    const status = error?.response?.status;
    const code = error?.response?.data?.code;
    if (status === 409 && code === "DUPLICATE_NICKNAME") {
      profileErrorMessage.value = "이미 사용 중인 닉네임입니다.";
      return;
    }
    profileErrorMessage.value = toErrorMessage(error, "회원 정보 수정 중 오류가 발생했습니다.");
  } finally {
    savingProfile.value = false;
  }
}

async function loadStats() {
  try {
    statsLoading.value = true;
    statsErrorMessage.value = "";
    const data = await getMyStats();
    stats.totalAttempts = data?.totalAttempts ?? 0;
    stats.completedAttempts = data?.completedAttempts ?? 0;
    stats.totalAnswers = data?.totalAnswers ?? 0;
    stats.correctAnswers = data?.correctAnswers ?? 0;
    stats.accuracyRate = data?.accuracyRate ?? 0;
  } catch (error) {
    statsErrorMessage.value = toErrorMessage(error, "통계를 불러오지 못했습니다.");
  } finally {
    statsLoading.value = false;
  }
}

onMounted(async () => {
  await Promise.all([loadProfile(), loadStats()]);
});
</script>
