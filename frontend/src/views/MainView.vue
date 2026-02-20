<template>
  <section class="quiz-home">
    <header class="hero">
      <p class="eyebrow">JAPANESE QUIZ</p>
      <h1>카테고리와 상황을 고르고 바로 시작하세요</h1>
      <p class="hero-copy">문제 수는 10문제로 고정됩니다. 단어/문장 모드를 선택한 뒤 원하는 상황을 고르세요.</p>
    </header>

    <section class="control-panel">
      <div class="category-tabs">
        <button :class="['tab', { active: categoryType === 'WORD' }]" @click="categoryType = 'WORD'">단어</button>
        <button :class="['tab', { active: categoryType === 'SENTENCE' }]" @click="categoryType = 'SENTENCE'">문장</button>
      </div>

      <div class="control-row">
        <label class="field">
          상황(scene)
          <select v-model="selectedSceneId">
            <option :value="null">전체(랜덤)</option>
            <option v-for="scene in scenes" :key="scene.id" :value="scene.id">{{ scene.name }}</option>
          </select>
        </label>
        <button class="start-button" @click="onStart">퀴즈 시작</button>
      </div>
    </section>

    <section class="scene-grid">
      <button
        class="scene-card"
        :class="{ selected: selectedSceneId === scene.id }"
        v-for="scene in scenes"
        :key="scene.id"
        @click="selectedSceneId = scene.id"
      >
        <strong>{{ scene.name }}</strong>
        <span>scene {{ scene.id }}</span>
      </button>
    </section>
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

<style scoped>
.quiz-home {
  --c-primary: #102a43;
  --c-accent: #f0b429;
  --c-base: #f8fafc;
  display: grid;
  gap: 16px;
  background: var(--c-base);
  color: var(--c-primary);
  border: 2px solid var(--c-primary);
  border-radius: 14px;
  padding: 20px;
}

.hero {
  border: 2px solid var(--c-primary);
  background: linear-gradient(135deg, var(--c-primary) 0%, var(--c-primary) 70%, var(--c-accent) 100%);
  color: var(--c-base);
  border-radius: 12px;
  padding: 18px;
}

.eyebrow {
  margin: 0 0 8px;
  font-size: 12px;
  letter-spacing: 1px;
  opacity: 0.9;
}

h1 {
  margin: 0;
  line-height: 1.3;
}

.hero-copy {
  margin: 10px 0 0;
  opacity: 0.95;
}

.control-panel {
  border: 2px solid var(--c-primary);
  border-radius: 12px;
  padding: 14px;
  background: var(--c-base);
}

.category-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.tab {
  border: 2px solid var(--c-primary);
  border-radius: 8px;
  background: var(--c-base);
  color: var(--c-primary);
  padding: 8px 14px;
  cursor: pointer;
}

.tab.active {
  background: var(--c-accent);
}

.control-row {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: flex-end;
}

.field {
  display: grid;
  gap: 6px;
  min-width: 210px;
}

select {
  border: 2px solid var(--c-primary);
  border-radius: 8px;
  background: var(--c-base);
  color: var(--c-primary);
  padding: 8px 10px;
}

.start-button {
  border: 2px solid var(--c-primary);
  border-radius: 8px;
  background: var(--c-primary);
  color: var(--c-base);
  padding: 10px 16px;
  cursor: pointer;
}

.scene-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 10px;
}

.scene-card {
  border: 2px solid var(--c-primary);
  border-radius: 10px;
  background: var(--c-base);
  color: var(--c-primary);
  text-align: left;
  padding: 12px;
  display: grid;
  gap: 4px;
  cursor: pointer;
}

.scene-card span {
  opacity: 0.7;
  font-size: 12px;
}

.scene-card.selected {
  background: var(--c-accent);
}

@media (max-width: 640px) {
  .quiz-home {
    padding: 14px;
  }

  h1 {
    font-size: 20px;
  }
}
</style>
