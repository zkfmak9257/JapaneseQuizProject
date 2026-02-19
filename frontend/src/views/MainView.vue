<template>
  <section class="card">
    <h2>퀴즈 시작</h2>
    <p class="muted">카테고리와 상황을 고른 뒤 퀴즈를 시작하세요.</p>

    <h3>카테고리</h3>
    <div class="categories">
      <button :class="['chip', { active: categoryType === 'WORD' }]" @click="categoryType = 'WORD'">단어</button>
      <button :class="['chip', { active: categoryType === 'SENTENCE' }]" @click="categoryType = 'SENTENCE'">
        문장
      </button>
    </div>

    <label class="field">
      상황(scene) 선택
      <select v-model="selectedSceneId">
        <option :value="null">전체(랜덤)</option>
        <option v-for="scene in scenes" :key="scene.id" :value="scene.id">{{ scene.name }}</option>
      </select>
    </label>

    <div class="actions">
      <button @click="onStart">퀴즈 시작</button>
    </div>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const categoryType = ref("WORD");
const scenes = [
  { id: 1, name: "일상회화" },
  { id: 2, name: "비즈니스" },
  { id: 3, name: "여행" },
  { id: 4, name: "식당" },
  { id: 5, name: "쇼핑" },
  { id: 6, name: "병원" },
  { id: 7, name: "학교" },
  { id: 8, name: "긴급상황" }
];
const selectedSceneId = ref(null);

function onStart() {
  router.push({
    path: "/quiz/start",
    query: {
      questionType: categoryType.value,
      sceneId: selectedSceneId.value == null ? undefined : String(selectedSceneId.value)
    }
  });
}
</script>
