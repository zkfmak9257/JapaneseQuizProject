<template>
  <section
    v-if="visible"
    class="card unified-feedback-card"
    :class="isCorrect ? 'is-correct' : 'is-wrong'"
  >
    <!-- 스텝 인디케이터 -->
    <div class="feedback-stepper">
      <span class="step-pill" :class="{ active: step >= 1 }">1. 결과 확인</span>
      <span class="step-pill" :class="{ active: step >= 2 }">2. 정답 확인</span>
      <span class="step-pill" :class="{ active: step >= 3 }">3. 해설 학습</span>
    </div>

    <!-- 가이드 노트 -->
    <div class="feedback-context guide-note">
      <div class="guide-character">👩‍✈️</div>
      <div class="guide-speech">
        <p class="context-title">여행 가이드 노트</p>
        <p class="context-desc">{{ guideText }}</p>
      </div>
    </div>

    <!-- 결과 헤더 -->
    <div class="feedback-header ticket-header-result">
      <div class="feedback-status-badge" :class="isCorrect ? 'badge-clear' : 'badge-fail'">
        {{ isCorrect ? '🎉 MISSION CLEAR' : '⚠️ MISSION FAILED' }}
      </div>
      <div class="feedback-title">
        <h3 class="feedback-title-text" :class="isCorrect ? 'text-correct' : 'text-wrong'">
          {{ isCorrect ? '정답입니다!' : '오답입니다' }}
        </h3>
        <p class="feedback-subtitle">
          {{ isCorrect ? '실전에서 더 강해집니다!' : '해설을 확인하고 다시 도전하세요.' }}
        </p>
      </div>
    </div>

    <!-- 에러 메시지 -->
    <div v-if="serverError" class="feedback-error-banner">
      ⚠️ {{ serverError }}
      <button class="error-dismiss" @click="emit('dismiss-error')">✕</button>
    </div>

    <!-- 정답/오답 확인 (step 2) -->
    <div v-if="step >= 2" class="feedback-answer-reveal">
      <div class="answer-box">
        <div class="answer-label">
          <span class="answer-check-icon">✔️</span> 정답
        </div>
        <h2 class="answer-jp-text">
          {{ correct.kanji }}<span v-if="correct.kana && correct.kana !== '-'" class="answer-kana">（{{ correct.kana }}）</span>
        </h2>
        <p class="answer-ko-meaning">{{ correct.meaning }}</p>
      </div>

      <div v-if="!isCorrect" class="wrong-box">
        <div class="wrong-label">
          <span class="wrong-cross-icon">❌</span> 내가 고른 답
        </div>
        <p class="wrong-jp-text">{{ selected.kanji }}<span v-if="selected.kana && selected.kana !== '-'" class="wrong-kana">（{{ selected.kana }}）</span></p>
        <p class="wrong-ko-meaning">{{ selected.meaning }}</p>
      </div>
    </div>

    <!-- 해설 (step 3) -->
    <div v-if="step >= 3" class="feedback-explanation">
      <button class="explanation-toggle-btn" @click="showExplanation = !showExplanation">
        📘 해설 및 보기 뜻 {{ showExplanation ? "접기" : "펼치기" }}
        <span class="toggle-arrow">{{ showExplanation ? '▴' : '▾' }}</span>
      </button>

      <div v-show="showExplanation" class="explanation-content">
        <div v-if="keyPoint" class="explanation-section">
          <h4 class="explanation-heading">📌 핵심 포인트</h4>
          <div class="explanation-highlight-box" style="margin-bottom:1.5rem; padding:1rem; border-radius:8px; background:rgba(52,152,219,0.05); border-left:3px solid var(--primary-color);">
            <p class="explanation-highlight">💡 {{ keyPoint }}</p>
          </div>
        </div>

        <div v-if="choices.length > 0" class="explanation-section">
          <h4 class="explanation-heading">📚 다른 보기 의미</h4>
          <div class="vocab-comparison-grid">
            <div class="vocab-comparison-header">
              <span>보기</span>
              <span>뜻</span>
            </div>
            <div v-for="(choice, idx) in choices" :key="idx" class="vocab-item">
              <div class="vocab-jp">{{ choice.kanji }}<span v-if="choice.kana && choice.kana !== '-'" class="vocab-kana">（{{ choice.kana }}）</span></div>
              <div class="vocab-ko">{{ choice.meaning || '-' }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- CTA 버튼 -->
    <div class="feedback-cta">
      <div class="feedback-cta-secondary">
        <button class="btn-action-secondary" type="button" @click="onToggleFavorite">
          {{ localFavorited ? '★ 표현 저장됨' : '☆ 이 표현 저장하기' }}
        </button>
        <button class="btn-action-secondary ghost" type="button" @click="emit('go-wrong-note')">
          🧠 오답노트로 이어서 복습
        </button>
      </div>

      <button
        v-if="step === 1"
        class="btn-action-secondary"
        type="button"
        @click="step = 2"
      >
        {{ isCorrect ? '정답 다시 확인하기' : '정답 확인하기' }}
      </button>

      <button
        v-else-if="step === 2"
        class="btn-action-secondary"
        type="button"
        @click="step = 3; showExplanation = true"
      >
        왜 틀렸는지 해설 보기
      </button>

      <button
        class="btn-action-primary btn-next-mission"
        @click="emit('next')"
      >
        {{ nextLabel }}
      </button>
    </div>
  </section>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  visible:     { type: Boolean, default: false },
  isCorrect:   { type: Boolean, default: false },
  correct:     { type: Object, default: () => ({ kanji: "-", kana: "-", meaning: "-" }) },
  selected:    { type: Object, default: () => ({ kanji: "-", kana: "-", meaning: "-" }) },
  choices:     { type: Array,  default: () => [] },
  serverError: { type: String, default: "" },
  guideText:   { type: String, default: "" },
  keyPoint:    { type: String, default: "" },
  nextLabel:   { type: String, default: "다음 여행지로 이동 ✈️" },
});

const emit = defineEmits(["next", "toggle-favorite", "go-wrong-note", "retry-submit", "dismiss-error"]);

const step = ref(1);
const showExplanation = ref(false);
const localFavorited = ref(false);

// visible이 바뀔 때(새 문제) 상태 초기화
watch(() => props.visible, (val) => {
  if (val) {
    step.value = 1;
    showExplanation.value = false;
    localFavorited.value = false;
  }
});

function onToggleFavorite() {
  localFavorited.value = !localFavorited.value;
  emit("toggle-favorite");
}
</script>