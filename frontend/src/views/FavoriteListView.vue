<template>
  <section class="card">
    <h2>즐겨찾기</h2>
    <p v-if="loading" class="muted">즐겨찾기를 불러오는 중입니다...</p>
    <p v-else-if="errorMessage" class="error">{{ errorMessage }}</p>
    <ul v-else>
      <li v-for="item in items" :key="`${item.questionId}-${item.createdAt}`">
        문제ID: {{ item.questionId }} / 등록일: {{ item.createdAt }}
      </li>
    </ul>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getFavorites } from "../api/favoriteApi";
import LoadingState from "../components/ui/LoadingState.vue";
import ErrorState from "../components/ui/ErrorState.vue";
import EmptyState from "../components/ui/EmptyState.vue";

const items = ref([]);
const loading = ref(false);
const errorMessage = ref("");

onMounted(async () => {
  try {
    loading.value = true;
    errorMessage.value = "";
    const page = await getFavorites();
    items.value = page.content || [];
  } catch (error) {
    const status = error?.response?.status;
    if (status === 401) {
      errorMessage.value = "로그인이 필요합니다.";
      return;
    }
    if (status === 403) {
      errorMessage.value = "접근 권한이 없습니다.";
      return;
    }
    if (status === 500) {
      errorMessage.value = "서버 오류로 즐겨찾기를 불러오지 못했습니다.";
      return;
    }
    errorMessage.value = "즐겨찾기 조회 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
});
</script>
