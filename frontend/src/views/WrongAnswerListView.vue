<template>
  <section class="card">
    <h2>오답노트</h2>

    <LoadingState v-if="loading" message="오답노트를 불러오는 중입니다..." />

    <ErrorState
      v-else-if="errorMessage"
      :message="errorMessage"
      :show-retry="true"
      @retry="loadItems"
    />

    <template v-else>
      <EmptyState v-if="items.length === 0" message="아직 오답노트 데이터가 없습니다." />

      <template v-else>
        <ul class="list-view">
          <li v-for="item in items" :key="`${item.questionId}-${item.lastWrongAt}`" class="list-item">
            <p><strong>문제 ID:</strong> {{ item.questionId }}</p>
            <p><strong>오답 횟수:</strong> {{ item.wrongCount }}</p>
            <p><strong>최근 오답:</strong> {{ item.lastWrongAt }}</p>
          </li>
        </ul>

        <div class="actions actions-left pager">
          <button class="ghost" :disabled="page <= 1" @click="changePage(page - 1)">이전</button>
          <span>{{ page }} / {{ totalPages }}</span>
          <button class="ghost" :disabled="page >= totalPages" @click="changePage(page + 1)">다음</button>
        </div>
      </template>
    </template>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getWrongAnswers } from "../api/wrongAnswerApi";
import LoadingState from "../components/ui/LoadingState.vue";
import ErrorState from "../components/ui/ErrorState.vue";
import EmptyState from "../components/ui/EmptyState.vue";

const items = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const page = ref(1);
const size = 10;
const totalPages = ref(1);

async function loadItems() {
  try {
    loading.value = true;
    errorMessage.value = "";

    const data = await getWrongAnswers(page.value, size);
    items.value = data?.content || [];
    totalPages.value = Math.max(data?.totalPages || 1, 1);
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
    errorMessage.value = "오답노트 조회 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
}

async function changePage(nextPage) {
  page.value = nextPage;
  await loadItems();
}

onMounted(loadItems);
</script>
