<template>
  <section class="card">
    <h2>오답노트</h2>
    <ul>
      <li v-for="item in items" :key="`${item.questionId}-${item.lastWrongAt}`">
        문제ID: {{ item.questionId }} / 오답횟수: {{ item.wrongCount }}
      </li>
    </ul>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getWrongAnswers } from "../api/wrongAnswerApi";

const items = ref([]);

onMounted(async () => {
  const page = await getWrongAnswers();
  items.value = page.content || [];
});
</script>
