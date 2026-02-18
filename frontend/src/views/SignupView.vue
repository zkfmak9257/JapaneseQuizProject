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

async function onSubmit() {
  if (password.value !== passwordConfirm.value) {
    errorMessage.value = "비밀번호 확인이 일치하지 않습니다.";
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
    if (status === 400) {
      errorMessage.value = error?.response?.data?.message || "입력값을 확인해 주세요.";
      return;
    }
    if (status === 409) {
      errorMessage.value = error?.response?.data?.message || "이미 사용 중인 이메일 또는 닉네임입니다.";
      return;
    }
    errorMessage.value = "회원가입에 실패했습니다.";
  } finally {
    loading.value = false;
  }
}
</script>
