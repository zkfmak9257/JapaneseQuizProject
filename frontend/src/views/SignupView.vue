<template>
  <section class="card">
    <h2>회원가입</h2>
    <form @submit.prevent="onSubmit" class="form">
      <input v-model="email" type="email" placeholder="email" required />
      <input v-model="nickname" type="text" placeholder="nickname" required />
      <input v-model="password" type="password" placeholder="password" required />
      <input v-model="passwordConfirm" type="password" placeholder="password confirm" required />
      <button type="submit" :disabled="loading">{{ loading ? "가입 중..." : "회원가입" }}</button>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p class="muted">
      이미 계정이 있으신가요?
      <RouterLink to="/login">로그인으로 이동</RouterLink>
    </p>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useAuthStore } from "../stores/authStore";

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const email = ref("");
const nickname = ref("");
const password = ref("");
const passwordConfirm = ref("");
const loading = ref(false);
const errorMessage = ref("");
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
const NICKNAME_REGEX = /^[가-힣a-zA-Z0-9_]+$/;

function validateForm() {
  if (!EMAIL_REGEX.test(email.value)) {
    return "이메일 형식이 올바르지 않습니다. 예: user@example.com";
  }
  if (nickname.value.length < 2 || nickname.value.length > 50) {
    return "닉네임은 2자 이상 50자 이하여야 합니다.";
  }
  if (!NICKNAME_REGEX.test(nickname.value)) {
    return "닉네임은 한글/영문/숫자/언더스코어(_)만 사용할 수 있습니다.";
  }
  if (password.value.length < 8 || password.value.length > 50) {
    return "비밀번호는 8자 이상 50자 이하여야 합니다.";
  }
  if (!PASSWORD_REGEX.test(password.value)) {
    return "비밀번호는 영문/숫자/특수문자를 각각 1개 이상 포함해야 합니다.";
  }
  if (password.value !== passwordConfirm.value) {
    return "비밀번호 확인이 일치하지 않습니다.";
  }
  return "";
}

async function onSubmit() {
  const validationMessage = validateForm();
  if (validationMessage) {
    errorMessage.value = validationMessage;
    return;
  }

  try {
    loading.value = true;
    errorMessage.value = "";
    await authStore.register(email.value, nickname.value, password.value);
    const redirect = route.query.redirect || "/quiz/start";
    router.push(String(redirect));
  } catch (error) {
    const status = error?.response?.status;
    const code = error?.response?.data?.code;
    const serverMessage = error?.response?.data?.message;

    if (status === 400) {
      if (code === "INVALID_REQUEST") {
        errorMessage.value = "입력값이 규칙에 맞지 않습니다. 이메일/닉네임/비밀번호 조건을 확인해 주세요.";
        return;
      }
      errorMessage.value = serverMessage || "요청 값이 올바르지 않습니다.";
      return;
    }

    if (status === 409) {
      if (code === "DUPLICATE_EMAIL") {
        errorMessage.value = "이미 사용 중인 이메일입니다. 다른 이메일을 입력해 주세요.";
        return;
      }
      if (code === "DUPLICATE_NICKNAME") {
        errorMessage.value = "이미 사용 중인 닉네임입니다. 다른 닉네임을 입력해 주세요.";
        return;
      }
      errorMessage.value = serverMessage || "이미 사용 중인 계정 정보입니다.";
      return;
    }

    if (status >= 500) {
      errorMessage.value = "서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.";
      return;
    }

    errorMessage.value = serverMessage || "회원가입에 실패했습니다.";
  } finally {
    loading.value = false;
  }
}
</script>
