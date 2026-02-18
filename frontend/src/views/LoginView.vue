<template>
  <section class="card">
    <h2>로그인</h2>
    <form @submit.prevent="onSubmit" class="form">
      <input v-model="email" type="email" placeholder="email" required />
      <input v-model="password" type="password" placeholder="password" required />
      <button type="submit" :disabled="loading">{{ loading ? "로그인 중..." : "로그인" }}</button>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "../stores/authStore";

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const email = ref("");
const password = ref("");
const loading = ref(false);
const errorMessage = ref("");

async function onSubmit() {
  try {
    loading.value = true;
    errorMessage.value = "";
    await authStore.login(email.value, password.value);
    const redirect = route.query.redirect || "/quiz/start";
    router.push(String(redirect));
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || "로그인에 실패했습니다.";
  } finally {
    loading.value = false;
  }
}
</script>
