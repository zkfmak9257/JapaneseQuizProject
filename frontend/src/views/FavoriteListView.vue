<template>
  <section class="card">
    <h2>즐겨찾기</h2>
    <ul>
      <li v-for="item in items" :key="`${item.questionId}-${item.createdAt}`">
        문제ID: {{ item.questionId }} / 등록일: {{ item.createdAt }}
      </li>
    </ul>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getFavorites } from "../api/favoriteApi";

const items = ref([]);

onMounted(async () => {
  const page = await getFavorites();
  items.value = page.content || [];
});
</script>
