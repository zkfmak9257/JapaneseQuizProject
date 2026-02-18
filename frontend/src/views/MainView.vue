<template>
  <section class="card">
    <h2>카테고리 선택</h2>
    <div class="categories">
      <button
        v-for="category in categories"
        :key="category"
        :class="['chip', { active: selectedCategory === category }]"
        @click="selectedCategory = category"
      >
        {{ category }}
      </button>
    </div>
    <div class="actions">
      <button @click="onStart">퀴즈 시작</button>
    </div>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const categories = ["일상회화", "비즈니스", "여행", "식당", "쇼핑"];
const selectedCategory = ref(categories[0]);

function onStart() {
  router.push("/quiz/start");
}
</script>
