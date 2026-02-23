<template>
  <section class="card" v-if="question">
    <div class="quiz-top">
      <div class="quiz-progress">
        <h2>문제 {{ question.seq }} / {{ totalQuestions }}</h2>
        <div class="progress-track">
          <div class="progress-fill" :style="{ width: `${progressPercent}%` }"></div>
        </div>
      </div>
      <div class="quiz-top-actions">
        <button
          class="favorite-button"
          :class="{ active: isFavorited }"
          @click="toggleFavoriteClick"
          :disabled="favoriteSubmitting"
        >
          {{ isFavorited ? "★ 즐겨찾기" : "☆ 즐겨찾기" }}
        </button>
        <button class="report-button" @click="openReportModal">신고하기</button>
      </div>
    </div>

    <!-- ── 장면 배지 ── 
         현재 어떤 장면(시나리오)에서 퀴즈를 풀고 있는지 표시.
         question.sceneName이 있으면 배지 노출 -->
    <div v-if="question.sceneName" class="scene-badge">
      <span class="scene-badge-icon">🗾</span>
      <span>{{ question.sceneName }}</span>
    </div>

    <!-- 유형 레이블 (알약형 배지) -->
    <span class="quiz-type-label">
      {{ isSentenceMode ? "✍️ 문장 조합" : "🎯 단어 선택" }}
    </span>

    <!-- 문제 텍스트 — 크고 굵게 표시하여 시선을 집중 -->
    <p class="quiz-question-text">{{ question.questionText }}</p>

    <!-- ── 문장 조합 모드 — 여행 경로 완성 스타일 ──────────
         디자인 컨셉:
         - 위: 슬롯형 조합 영역 (클릭하면 제거)
         - 아래: 토큰 보관함 (클릭하면 위로 추가)
         - 드래그 대신 클릭 기반 → 모바일 친화적
         
         UX 흐름:
         1️⃣ 토큰 클릭 → 위에 자동 추가 (tokenDropIn 애니메이션)
         2️⃣ 위 토큰 클릭 → 제거 (보관함으로 복귀)
         3️⃣ 선택 개수 자동 시각화
         4️⃣ 필요한 수만큼 채우면 제출 활성화
         5️⃣ 제출 → 정답(glow) / 오답(shake) 피드백 -->
    <template v-if="isSentenceMode">
      <div class="sentence-builder">

        <!-- ── 한국어 문장 안내 — "이걸 일본어로 만들어주세요" ── -->
        <div class="sentence-kr-guide">
          <span class="sentence-kr-guide-icon">🗣</span>
          <span class="sentence-kr-guide-text">
            아래 토큰을 조합하여 일본어 문장을 완성하세요
          </span>
        </div>

        <!-- ── 선택 개수 프로그레스 ─────────────────────────── 
             바(bar) + 동그라미 인디케이터로 진행감을 시각화.
             왜? "선택 2 / 4" 숫자만으로는 진행감이 약하다. -->
        <div class="sentence-progress">
          <!-- 동그라미 인디케이터 -->
          <div class="sentence-dots">
            <span
              v-for="i in requiredSentenceTokenCount"
              :key="`dot-${i}`"
              class="sentence-dot"
              :class="{ filled: i <= answerTokens.length }"
            ></span>
          </div>
          <!-- 프로그레스 바 -->
          <div class="sentence-progress-bar">
            <div
              class="sentence-progress-fill"
              :style="{ width: sentenceProgressPercent + '%' }"
            ></div>
          </div>
          <!-- 숫자 라벨 -->
          <span class="sentence-progress-label">
            {{ answerTokens.length }} / {{ requiredSentenceTokenCount }}
          </span>
        </div>

        <!-- ── 조합 영역 (내가 만드는 문장) ─────────────────── 
             클릭하면 해당 토큰이 보관함으로 돌아간다.
             정답/오답에 따라 glow/shake 적용. -->
        <div
          class="sentence-answer-zone"
          :class="answerZoneClass"
        >
          <div class="sentence-answer-zone-header">
            <span class="sentence-answer-zone-icon">🛤</span>
            <span>내가 만드는 문장</span>
          </div>

          <!-- 조합된 토큰들 -->
          <div class="sentence-answer-tokens">
            <!-- 채워진 토큰들 — 클릭하면 제거 -->
            <div
              v-for="(token, idx) in answerTokens"
              :key="`ans-${token.tokenId}-${idx}`"
              class="stk stk-answer"
              :class="{
                'stk-disabled': submissionDone,
                'stk-correct': submissionDone && gradeResult?.correct,
                'stk-wrong': submissionDone && !gradeResult?.correct
              }"
              @click="moveAnswerTokenToPool(idx)"
            >
              <!-- 한자(base) 텍스트 -->
              <span>{{ rubyBase(token.tokenText) }}</span>
              <!-- 후리가나(reading) — 있으면 아래에 작게 표시 -->
              <span v-if="hasRuby(token.tokenText)" class="stk-reading">
                {{ rubyReading(token.tokenText) }}
              </span>
            </div>

            <!-- 빈 슬롯 — 아직 채워지지 않은 자리 표시 -->
            <div
              v-for="i in remainingSlots"
              :key="`slot-${i}`"
              class="sentence-slot-empty"
            >
              ____
            </div>

            <!-- 완전히 빈 상태 안내 -->
            <div v-if="answerTokens.length === 0 && requiredSentenceTokenCount === 0" class="sentence-answer-empty">
              토큰을 클릭하여 문장을 조합하세요
            </div>
          </div>
        </div>

        <!-- ── 토큰 보관함 ────────────────────────────────── 
             사용된 토큰은 흐리게(stk-used) 남아있다.
             왜? 완전히 사라지면 "어디 갔지?" 혼란 발생. -->
        <div class="sentence-token-pool">
          <div class="sentence-pool-header">
            <span class="sentence-pool-icon">🧳</span>
            <span>토큰 보관함</span>
          </div>

          <div class="sentence-pool-tokens">
            <!-- allPoolTokens: 전체 토큰 목록 (사용됨 여부 포함) -->
            <div
              v-for="(token, idx) in allPoolTokens"
              :key="`pool-${token.tokenId}-${idx}`"
              class="stk"
              :class="{
                'stk-used': token._used,
                'stk-disabled': submissionDone
              }"
              @click="handlePoolTokenClick(token, idx)"
            >
              <!-- 한자(base) 텍스트 -->
              <span>{{ rubyBase(token.tokenText) }}</span>
              <!-- 후리가나(reading) — 있으면 아래에 작게 표시 -->
              <span v-if="hasRuby(token.tokenText)" class="stk-reading">
                {{ rubyReading(token.tokenText) }}
              </span>
            </div>
          </div>
        </div>

        <!-- ── 초기화 버튼 ───────────────────────────────── -->
        <button
          class="btn-reset"
          :disabled="submissionDone || answerTokens.length === 0"
          @click="resetSentenceBoard"
        >
          🔄 초기화
        </button>
      </div>
    </template>

    <!-- ── 카드형 선택지 그리드 (여행 티켓 스타일) ──────────
         왜 카드형? → 터치 영역이 넓어 모바일 UX가 좋고,
         시각적 피드백(hover/selected/correct/wrong)을
         자연스럽게 적용할 수 있다.
         
         UX 흐름:
         1️⃣ 카드 클릭 → selected 상태 (파란 강조 + 체크)
         2️⃣ 제출 버튼 활성화
         3️⃣ 제출 → correct/wrong 상태 표시
         4️⃣ 다음 버튼 등장 -->
    <div v-else class="choices-grid">
      <div
        v-for="choice in question.choices"
        :key="choice.choiceId"
        class="choice-card"
        :class="choiceCardClass(choice.choiceId)"
        @click="selectChoice(choice.choiceId)"
      >
        <!-- 체크/X 아이콘 — 선택/정답/오답 시 우상단에 표시 -->
        <span class="choice-check">
          {{ choiceCheckIcon(choice.choiceId) }}
        </span>

        <!-- 선택지 컨텐츠: 이모지 + 텍스트(후리가나 hover) -->
        <div class="choice-content">
          <!-- 선택지 이모지 (시각적 힌트) -->
          <span class="choice-emoji">{{ choiceEmoji(choice.choiceText) }}</span>

          <!-- 텍스트: 후리가나가 있으면 hover 시 표시 -->
          <span class="choice-text furigana-hover">
            <template v-if="hasRuby(choice.choiceText)">
              <ruby>
                <rb>{{ rubyBase(choice.choiceText) }}</rb>
                <rt>{{ rubyReading(choice.choiceText) }}</rt>
              </ruby>
            </template>
            <template v-else>{{ choice.choiceText }}</template>
          </span>
        </div>
      </div>
    </div>

    <!-- ── 제출 / 다음 버튼 영역 ──────────────────────────
         제출 전: 제출 버튼만 활성, 다음 비활성
         제출 후: 제출 비활성, 다음 활성 -->
    <div class="quiz-actions">
      <button
        class="btn-submit"
        @click="submit"
        :disabled="!canSubmit || submitting || submissionDone"
      >
        {{ submitting ? "⏳ 제출 중..." : "🎯 제출" }}
      </button>
      <button
        class="btn-next"
        @click="goNext"
        :disabled="!submissionDone || navigatingNext"
      >
        {{ question.seq < totalQuestions ? "➡️ 다음" : "📊 결과 보기" }}
      </button>
    </div>

    <!-- ── 정답/오답 피드백 영역 ────────────────────────────
         제출 후 결과를 큰 아이콘 + 텍스트로 표시.
         fadeCheckIn 애니메이션으로 부드럽게 등장 -->
    <div
      v-if="submissionDone && gradeResult"
      class="grade-feedback"
      :class="gradeResult.correct ? 'is-correct' : 'is-wrong'"
    >
      <span class="grade-feedback-icon">
        {{ gradeResult.correct ? '🎉' : '😣' }}
      </span>
      <div class="grade-feedback-text">
        <p>{{ gradeResult.correct ? '정답입니다! 잘하셨어요!' : '아쉽지만 오답입니다.' }}</p>
        <p style="font-size: 13px; font-weight: 400; margin-top: 4px; opacity: 0.8;">
          {{ gradeResult.feedbackMessage || (gradeResult.correct ? '계속 이 조자로!' : '다음에 다시 도전해보세요!') }} ➡️ 다음 버튼을 눌러주세요
        </p>
      </div>
    </div>

    <section v-if="submissionDone && gradeResult" class="feedback-stage-panel">
      <div class="feedback-stage-tabs">
        <button class="stage-tab" :class="{ active: feedbackStage === 1 }" @click="setFeedbackStage(1)">
          1단계 즉시 피드백
        </button>
        <button
          class="stage-tab"
          :class="{ active: feedbackStage === 2 }"
          :disabled="feedbackStage < 2"
          @click="setFeedbackStage(2)"
        >
          2단계 정답 공개
        </button>
        <button
          class="stage-tab"
          :class="{ active: feedbackStage === 3 }"
          :disabled="feedbackStage < 3"
          @click="setFeedbackStage(3)"
        >
          3단계 해설 공개
        </button>
      </div>

      <div class="stage-body">
        <div class="stage-block">
          <h4>1단계</h4>
          <p>{{ gradeResult.feedbackMessage || (gradeResult.correct ? "정답입니다!" : "오답입니다.") }}</p>
        </div>

        <div v-if="feedbackStage >= 2" class="stage-block">
          <h4>2단계</h4>
          <p><strong>정답:</strong> {{ stageCorrect?.jpText || "정답 정보가 없습니다." }}</p>
          <p><strong>해석:</strong> {{ stageCorrect?.koMeaning || "해석 정보가 없습니다." }}</p>

          <template v-if="isSentenceMode && stageSentence">
            <p><strong>정답 토큰:</strong> {{ stageSentence.correctTokens?.join(" → ") || "-" }}</p>
            <p v-if="stageSentence.diffHint"><strong>비교 힌트:</strong> {{ stageSentence.diffHint }}</p>
          </template>
        </div>

        <div v-if="feedbackStage >= 3" class="stage-block">
          <h4>3단계</h4>
          <p><strong>한 줄 해설:</strong> {{ stageExplanation?.oneLiner || "해설이 없습니다." }}</p>
          <p><strong>추가 설명:</strong> {{ stageExplanation?.detail || "추가 설명이 없습니다." }}</p>

          <template v-if="!isSentenceMode && stageChoices.length > 0">
            <p class="stage-subtitle">보기별 뜻/설명</p>
            <ul class="stage-choice-list">
              <li v-for="choice in stageChoices" :key="choice.choiceId">
                <strong>{{ choice.jpText }}</strong>
                <span> - {{ choice.koMeaning || "뜻 정보 없음" }}</span>
                <span v-if="choice.note"> ({{ choice.note }})</span>
              </li>
            </ul>
          </template>
        </div>
      </div>

      <button
        v-if="feedbackStage < 3"
        class="stage-next-btn"
        type="button"
        @click="feedbackStage = feedbackStage + 1"
      >
        다음 단계 보기
      </button>
    </section>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <p v-if="reportSuccessMessage" class="muted">{{ reportSuccessMessage }}</p>
  </section>

  <section class="card" v-else-if="loading">
    <p>문제를 불러오는 중입니다...</p>
  </section>

  <section class="card" v-else>
    <p class="error">{{ errorMessage || "문제를 불러오지 못했습니다." }}</p>
  </section>

  <div v-if="showReportModal" class="modal-backdrop" @click.self="closeReportModal">
    <section class="modal-card">
      <h3>문제 신고</h3>
      <label class="field">
        신고 유형
        <select v-model="reportType">
          <option value="TYPO">오탈자</option>
          <option value="WRONG_ANSWER">정답 오류</option>
          <option value="ETC">기타</option>
        </select>
      </label>
      <label class="field">
        신고 내용
        <textarea
          v-model="reportContent"
          rows="5"
          maxlength="1000"
          placeholder="신고 사유를 입력해 주세요."
        ></textarea>
      </label>
      <div class="actions">
        <button class="ghost" @click="closeReportModal">취소</button>
        <button @click="submitReport" :disabled="reportSubmitting">
          {{ reportSubmitting ? "제출 중..." : "신고 제출" }}
        </button>
      </div>
      <p v-if="reportErrorMessage" class="error">{{ reportErrorMessage }}</p>
    </section>
  </div>
</template>

<script setup>
import { computed, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { completeAttempt, getAttemptQuestion, submitAnswer } from "../api/quizApi";
import { createReport } from "../api/reportApi";
import { toggleFavorite } from "../api/favoriteApi";
import { useQuizStore } from "../stores/quizStore";

const route = useRoute();
const router = useRouter();
const quizStore = useQuizStore();

const question = ref(null);
const selectedChoiceId = ref(null);
const tokenPool = ref([]);
const answerTokens = ref([]);
const dragItem = ref(null);
const totalQuestions = ref(10);
const loading = ref(false);
const submitting = ref(false);
const navigatingNext = ref(false);
const errorMessage = ref("");

const submissionDone = ref(false);
const gradeResult = ref(null);
const feedbackStage = ref(1);
const stagePayload = ref(null);

const showReportModal = ref(false);
const reportType = ref("TYPO");
const reportContent = ref("");
const reportSubmitting = ref(false);
const reportErrorMessage = ref("");
const reportSuccessMessage = ref("");
const favoriteSubmitting = ref(false);
const isFavorited = ref(false);

const progressPercent = computed(() => {
  if (!question.value || !totalQuestions.value) {
    return 0;
  }
  return Math.floor((question.value.seq / totalQuestions.value) * 100);
});

const stageCorrect = computed(() => stagePayload.value?.correct || null);
const stageExplanation = computed(() => stagePayload.value?.explanation || null);
const stageSentence = computed(() => stagePayload.value?.sentence || null);
const stageChoices = computed(() => {
  if (!Array.isArray(stagePayload.value?.choices)) {
    return [];
  }
  return stagePayload.value.choices;
});

const sentenceTokenSource = computed(() => {
  if (!question.value) {
    return [];
  }
  if (Array.isArray(question.value.sentenceTokens) && question.value.sentenceTokens.length > 0) {
    return question.value.sentenceTokens.map((token) => ({
      tokenId: Number(token.tokenId),
      tokenText: token.tokenText
    }));
  }
  // sentenceTokens가 없을 때의 최소 호환 fallback
  if (Array.isArray(question.value.choices) && question.value.choices.length > 0) {
    return question.value.choices.map((choice) => ({
      tokenId: Number(choice.choiceId),
      tokenText: choice.choiceText
    }));
  }
  return [];
});

const sentenceDistractorSource = computed(() => {
  if (!question.value || !Array.isArray(question.value.choices)) {
    return [];
  }
  return question.value.choices.map((choice) => ({
    tokenId: Number(choice.choiceId),
    tokenText: choice.choiceText
  }));
});

const sentenceBoardSource = computed(() => {
  if (!isSentenceMode.value) {
    return [];
  }
  if (sentenceTokenSource.value.length === 0) {
    return [];
  }
  return [...sentenceTokenSource.value, ...sentenceDistractorSource.value];
});

const isSentenceMode = computed(() => {
  if (!question.value) {
    return false;
  }
  if (String(question.value.questionType || "").toUpperCase() === "SENTENCE") {
    return true;
  }
  if (Array.isArray(question.value.sentenceTokens) && question.value.sentenceTokens.length > 0) {
    return true;
  }
  return quizStore.selectedQuestionType === "SENTENCE";
});

const canSubmit = computed(() => {
  if (!question.value) {
    return false;
  }
  if (isSentenceMode.value) {
    return answerTokens.value.length > 0;
  }
  return !!selectedChoiceId.value;
});

const requiredSentenceTokenCount = computed(() => sentenceTokenSource.value.length);

/**
 * sentenceProgressPercent — 문장 조합 선택 진행률
 * 왜? 프로그레스 바의 width를 퍼센트로 제어하기 위해 computed로 분리.
 * @returns {number} 0~100 사이 퍼센트 값
 */
const sentenceProgressPercent = computed(() => {
  if (requiredSentenceTokenCount.value === 0) {
    return 0;
  }
  return Math.floor((answerTokens.value.length / requiredSentenceTokenCount.value) * 100);
});

/**
 * remainingSlots — 남은 빈 슬롯 개수
 * 왜? 조합 영역에 "____" 빈 슬롯을 렌더링할 때 사용.
 * 음수가 되지 않도록 Math.max(0, ...) 처리.
 * @returns {number} 빈 슬롯 개수
 */
const remainingSlots = computed(() => {
  return Math.max(0, requiredSentenceTokenCount.value - answerTokens.value.length);
});

/**
 * answerZoneClass — 조합 영역의 CSS 클래스 결정
 * 왜? 상태(빈/채움/정답/오답)별로 시각 피드백이 달라야 한다.
 * - 토큰 있으면: has-tokens (실선 테두리)
 * - 정답: is-correct (초록 glow)
 * - 오답: is-wrong (빨간 shake)
 */
const answerZoneClass = computed(() => {
  if (submissionDone.value && gradeResult.value) {
    return {
      'has-tokens': true,
      'is-correct': gradeResult.value.correct,
      'is-wrong': !gradeResult.value.correct
    };
  }
  return {
    'has-tokens': answerTokens.value.length > 0
  };
});

/**
 * allPoolTokens — 전체 토큰 목록 (사용됨 여부 포함)
 * 왜? 기존 tokenPool은 사용된 토큰을 splice로 제거했다.
 * 새 디자인에서는 "사용된 토큰을 흐리게 남겨둔다"이므로
 * 전체 목록에 _used 플래그를 추가해서 렌더링한다.
 * 
 * 구현: sentenceBoardSource(전체 토큰)에서
 * answerTokens에 포함된 것을 _used: true로 표시
 */
const allPoolTokens = computed(() => {
  // answerTokens에 있는 tokenId 사용 횟수를 카운팅
  const usedMap = {};
  for (const t of answerTokens.value) {
    usedMap[t.tokenId] = (usedMap[t.tokenId] || 0) + 1;
  }

  // 초기 셔플된 순서를 유지하기 위해 tokenPool 기반이 아닌
  // initialPoolOrder 기반으로 렌더링
  const result = [];
  const tempUsedMap = { ...usedMap };

  for (const t of initialPoolOrder.value) {
    const used = (tempUsedMap[t.tokenId] || 0) > 0;
    if (used) {
      tempUsedMap[t.tokenId] -= 1;
    }
    result.push({ ...t, _used: used });
  }
  return result;
});

// 초기 셔플 순서를 보존하기 위한 ref
const initialPoolOrder = ref([]);

/**
 * handlePoolTokenClick — 보관함 토큰 클릭 핸들러
 * 왜? 사용된 토큰(stk-used)은 클릭해도 무시해야 하고,
 * 필요 수량을 초과하면 추가하지 않아야 한다.
 * tokenPool에서 제거하는 대신 answerTokens에 추가만 한다.
 */
function handlePoolTokenClick(token) {
  // 제출 완료 후 또는 이미 사용된 토큰은 무시
  if (submissionDone.value || token._used) {
    return;
  }
  // 필요 수량 초과 방지
  if (answerTokens.value.length >= requiredSentenceTokenCount.value) {
    return;
  }
  // answerTokens에 추가 (원본 토큰 데이터만)
  answerTokens.value.push({
    tokenId: token.tokenId,
    tokenText: token.tokenText
  });
}

function splitRuby(text) {
  const value = String(text ?? "");
  const separatorIndex = value.indexOf("|");
  if (separatorIndex <= 0 || separatorIndex >= value.length - 1) {
    return null;
  }
  return {
    base: value.slice(0, separatorIndex),
    reading: value.slice(separatorIndex + 1)
  };
}

function hasRuby(text) {
  return splitRuby(text) !== null;
}

function rubyBase(text) {
  return splitRuby(text)?.base ?? String(text ?? "");
}

function rubyReading(text) {
  return splitRuby(text)?.reading ?? "";
}

/* ── 카드 선택지 헬퍼 함수들 ─────────────────────────────
   왜 분리? Template에서 직접 복잡한 조건식을 쓰면
   가독성이 떨어지므로, 상태 판별 로직을 함수로 추출한다. */

/**
 * selectChoice — 선택지 카드 클릭 핸들러
 * 왜? 기존 radio v-model 대신 카드 클릭으로 선택을 처리한다.
 * 제출 완료 후에는 클릭을 무시한다 (함수 레벨 가드).
 * @param {number} choiceId - 선택한 choiceId
 */
function selectChoice(choiceId) {
  // 이미 제출 완료된 상태면 클릭 무시
  if (submissionDone.value) {
    return;
  }
  // 같은 카드 다시 클릭 시 선택 해제 (토글)
  if (selectedChoiceId.value === choiceId) {
    selectedChoiceId.value = null;
    return;
  }
  selectedChoiceId.value = choiceId;
}

/**
 * choiceCardClass — 각 선택지 카드의 CSS 클래스 결정
 * 왜? 카드의 시각 상태(selected/correct/wrong/disabled)를
 * 한 곳에서 관리하면 template이 깔끔해진다.
 * 
 * 상태 판별 로직:
 * - 제출 전: selected만 적용
 * - 제출 후: correct(정답 카드), wrong(내가 고른 오답 카드), disabled(나머지)
 * @param {number} choiceId - 해당 카드의 choiceId
 * @returns {Object} Vue 클래스 바인딩 객체
 */
function choiceCardClass(choiceId) {
  const isSelected = selectedChoiceId.value === choiceId;

  // 제출 전: selected 상태만
  if (!submissionDone.value) {
    return {
      selected: isSelected           // 클릭한 카드만 파란 강조
    };
  }

  // 제출 후: 정답/오답/정답공개 시각 표시
  const isCorrect = gradeResult.value?.correct;
  const wasMyChoice = gradeResult.value?.selectedChoiceId === choiceId;
  // correctChoiceId: 서버에서 정답 ID를 보내주면 활용.
  // 없으면 내가 맞힌 카드만 correct 표시.
  const correctId = gradeResult.value?.correctChoiceId;
  const isTheCorrectCard = correctId != null && correctId === choiceId;

  return {
    // ✅ 내가 고른 카드가 정답일 때: 초록 glow + pulse
    correct: isCorrect && wasMyChoice,
    // ❌ 내가 고른 카드가 오답일 때: 빨간 glow + shake
    wrong: !isCorrect && wasMyChoice,
    // 🟢 틀렸을 때 실제 정답 카드 공개: 초록 테두리 (딜레이 후 등장)
    'correct-reveal': !isCorrect && !wasMyChoice && isTheCorrectCard,
    // 제출 후 모든 카드 비활성화
    disabled: true
  };
}

/**
 * choiceCheckIcon — 카드 우상단 체크/X 아이콘 결정
 * 왜? 선택 → ✓, 정답 → ✓, 오답 → ✗ 로 구분해야
 * 시각적 피드백이 명확해진다.
 * @param {number} choiceId
 * @returns {string} 아이콘 문자
 */
function choiceCheckIcon(choiceId) {
  const isSelected = selectedChoiceId.value === choiceId;

  // 제출 전: 선택된 카드에 체크
  if (!submissionDone.value) {
    return isSelected ? "✓" : "";
  }

  // 제출 후: 정답 ✓, 오답 ✗
  const wasMyChoice = gradeResult.value?.selectedChoiceId === choiceId;
  const correctId = gradeResult.value?.correctChoiceId;
  const isCorrect = gradeResult.value?.correct;
  const isTheCorrectCard = correctId != null && correctId === choiceId;

  // 내가 고른 카드
  if (wasMyChoice) {
    return isCorrect ? "✓" : "✗";
  }
  // 틀렸을 때 실제 정답 카드에도 ✓ 표시
  if (!isCorrect && isTheCorrectCard) {
    return "✓";
  }
  return "";
}

/**
 * choiceEmoji — 선택지 텍스트 기반 이모지 매핑
 * 왜? 이모지를 붙이면 시각적 힌트가 되어
 * 단어 학습 효과가 올라간다.
 * 
 * 매핑 전략:
 * - 자주 나오는 일본어 단어 키워드로 이모지 결정
 * - 매칭되지 않으면 기본 이모지(📝) 사용
 * @param {string} text - 선택지 원문 텍스트
 * @returns {string} 이모지
 */
function choiceEmoji(text) {
  // 후리가나(|) 구분자가 있으면 base(한자)만 사용
  const base = rubyBase(text);

  // 키워드 → 이모지 매핑 테이블
  const emojiMap = [
    // 장소 관련
    { keywords: ["家", "いえ", "うち", "집"], emoji: "🏠" },
    { keywords: ["部屋", "へや", "방"], emoji: "🚪" },
    { keywords: ["店", "みせ", "가게"], emoji: "🏬" },
    { keywords: ["駅", "えき", "역"], emoji: "🚉" },
    { keywords: ["空港", "くうこう", "공항"], emoji: "✈️" },
    { keywords: ["学校", "がっこう", "학교"], emoji: "🏫" },
    { keywords: ["病院", "びょういん", "병원"], emoji: "🏥" },
    { keywords: ["銀行", "ぎんこう", "은행"], emoji: "🏦" },
    { keywords: ["ホテル", "호텔"], emoji: "🏨" },
    { keywords: ["レストラン", "식당"], emoji: "🍽️" },
    // 물건 관련
    { keywords: ["席", "せき", "자리"], emoji: "💺" },
    { keywords: ["切符", "きっぷ", "표", "チケット", "티켓"], emoji: "🎫" },
    { keywords: ["電車", "でんしゃ", "전철"], emoji: "🚃" },
    { keywords: ["バス", "버스"], emoji: "🚌" },
    { keywords: ["タクシー", "택시"], emoji: "🚕" },
    { keywords: ["水", "みず", "물"], emoji: "💧" },
    { keywords: ["食べ物", "たべもの", "음식"], emoji: "🍱" },
    { keywords: ["お金", "おかね", "돈"], emoji: "💰" },
    { keywords: ["電話", "でんわ", "전화"], emoji: "📞" },
    { keywords: ["地図", "ちず", "지도"], emoji: "🗺️" },
    // 사람 관련
    { keywords: ["人", "ひと", "사람"], emoji: "👤" },
    { keywords: ["友達", "ともだち", "친구"], emoji: "🤝" },
    { keywords: ["先生", "せんせい", "선생님"], emoji: "👨‍🏫" },
    // 시간/날씨
    { keywords: ["天気", "てんき", "날씨"], emoji: "🌤️" },
    { keywords: ["時間", "じかん", "시간"], emoji: "⏰" },
    { keywords: ["今日", "きょう", "오늘"], emoji: "📅" },
    // 행동 관련
    { keywords: ["食べ", "たべ", "먹"], emoji: "🍽️" },
    { keywords: ["飲", "の", "마시"], emoji: "🥤" },
    { keywords: ["行", "い", "가"], emoji: "🚶" },
    { keywords: ["買", "か", "사"], emoji: "🛒" },
    { keywords: ["見", "み", "보"], emoji: "👀" }
  ];

  // 매핑 테이블에서 첫 번째 매칭 검색
  for (const entry of emojiMap) {
    for (const keyword of entry.keywords) {
      if (base.includes(keyword)) {
        return entry.emoji;
      }
    }
  }

  // 매칭 없으면 기본 이모지
  return "📝";
}

function shuffle(tokens) {
  const arr = [...tokens];
  for (let i = arr.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]];
  }
  return arr;
}

function resetSentenceBoard() {
  // 셔플된 전체 토큰 목록을 initialPoolOrder에 저장
  // allPoolTokens computed가 이걸 기반으로 _used 플래그를 계산한다
  initialPoolOrder.value = shuffle(sentenceBoardSource.value);
  // 기존 tokenPool도 호환을 위해 동기화
  tokenPool.value = [...initialPoolOrder.value];
  answerTokens.value = [];
  dragItem.value = null;
}

function movePoolTokenToAnswer(index) {
  if (submissionDone.value) {
    return;
  }
  if (answerTokens.value.length >= requiredSentenceTokenCount.value) {
    return;
  }
  const [token] = tokenPool.value.splice(index, 1);
  if (token) {
    answerTokens.value.push(token);
  }
}

function moveAnswerTokenToPool(index) {
  if (submissionDone.value) {
    return;
  }
  // answerTokens에서 제거 (allPoolTokens computed가 자동으로 _used 해제)
  answerTokens.value.splice(index, 1);
}

function startDrag(source, index) {
  if (submissionDone.value) {
    return;
  }
  dragItem.value = { source, index };
}

function dropToAnswer() {
  if (!dragItem.value || submissionDone.value) {
    return;
  }
  if (dragItem.value.source === "pool") {
    if (answerTokens.value.length >= requiredSentenceTokenCount.value) {
      dragItem.value = null;
      return;
    }
    movePoolTokenToAnswer(dragItem.value.index);
  }
  dragItem.value = null;
}

function dropToPool() {
  if (!dragItem.value || submissionDone.value) {
    return;
  }
  if (dragItem.value.source === "answer") {
    moveAnswerTokenToPool(dragItem.value.index);
  }
  dragItem.value = null;
}

function dropOnAnswerIndex(targetIndex) {
  if (!dragItem.value || submissionDone.value) {
    return;
  }

  if (dragItem.value.source === "pool") {
    if (answerTokens.value.length >= requiredSentenceTokenCount.value) {
      dragItem.value = null;
      return;
    }
    const [token] = tokenPool.value.splice(dragItem.value.index, 1);
    if (token) {
      answerTokens.value.splice(targetIndex, 0, token);
    }
  } else {
    const [token] = answerTokens.value.splice(dragItem.value.index, 1);
    if (token) {
      answerTokens.value.splice(targetIndex, 0, token);
    }
  }
  dragItem.value = null;
}

function dropOnPoolIndex(targetIndex) {
  if (!dragItem.value || submissionDone.value) {
    return;
  }

  if (dragItem.value.source === "answer") {
    const [token] = answerTokens.value.splice(dragItem.value.index, 1);
    if (token) {
      tokenPool.value.splice(targetIndex, 0, token);
    }
  } else {
    const [token] = tokenPool.value.splice(dragItem.value.index, 1);
    if (token) {
      tokenPool.value.splice(targetIndex, 0, token);
    }
  }
  dragItem.value = null;
}

function setFeedbackStage(targetStage) {
  if (targetStage < 1 || targetStage > 3) {
    return;
  }
  if (targetStage > feedbackStage.value) {
    return;
  }
  feedbackStage.value = targetStage;
}

async function loadQuestion() {
  try {
    loading.value = true;
    errorMessage.value = "";
    question.value = null;

    const attemptId = Number(route.params.attemptId);
    const seq = Number(route.params.seq);
    const response = await getAttemptQuestion(attemptId, seq);

    question.value = response;
    totalQuestions.value = response.totalQuestions;
    selectedChoiceId.value = null;
    submissionDone.value = false;
    gradeResult.value = null;
    feedbackStage.value = 1;
    stagePayload.value = null;
    isFavorited.value = false;

    if (isSentenceMode.value) {
      resetSentenceBoard();
    } else {
      tokenPool.value = [];
      answerTokens.value = [];
    }

    quizStore.setCurrentAttempt(attemptId);
    quizStore.setCurrentSeq(seq);
    quizStore.cacheQuestion(seq, response);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      errorMessage.value = "잘못된 요청입니다. 문제 번호를 확인해 주세요.";
      return;
    }
    if (status === 401) {
      errorMessage.value = "인증이 필요합니다. 로그인 후 다시 시도해 주세요.";
      return;
    }
    if (status === 403) {
      errorMessage.value = "다른 사용자의 퀴즈 시도에는 접근할 수 없습니다.";
      return;
    }
    if (status === 404) {
      errorMessage.value = "문제 또는 시도를 찾을 수 없습니다.";
      return;
    }
    errorMessage.value = "문제를 불러오지 못했습니다.";
  } finally {
    loading.value = false;
  }
}

watch(
  () => [route.params.attemptId, route.params.seq],
  () => {
    void loadQuestion();
  },
  { immediate: true }
);

async function submit() {
  try {
    submitting.value = true;
    errorMessage.value = "";

    const attemptId = Number(route.params.attemptId);
    const seq = Number(route.params.seq);
    const payload = {
      seq,
      choiceId: isSentenceMode.value ? null : selectedChoiceId.value,
      orderedTokenIds: isSentenceMode.value ? answerTokens.value.map((token) => token.tokenId) : null
    };

    const res = await submitAnswer(attemptId, payload);

    submissionDone.value = true;
    gradeResult.value = {
      correct: !!res.correct,
      selectedChoiceId: res.selectedChoiceId,
      // correctChoiceId: 서버가 정답 ID를 보내주면 오답 시 정답 카드 공개에 활용
      correctChoiceId: res.correctChoiceId ?? null,
      feedbackMessage: res.feedbackMessage || null
    };
    stagePayload.value = res.stagePayload || null;
    feedbackStage.value = 1;

    totalQuestions.value = res.totalQuestions;
    if (question.value?.questionId) {
      quizStore.setSubmitResult(question.value.questionId, res);
    }
    quizStore.setSubmitted(seq, true);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      errorMessage.value = "이미 제출했거나 유효하지 않은 답안입니다.";
      return;
    }
    if (status === 401) {
      errorMessage.value = "인증이 필요합니다. 로그인 후 다시 시도해 주세요.";
      return;
    }
    if (status === 403) {
      errorMessage.value = "다른 사용자의 퀴즈에는 제출할 수 없습니다.";
      return;
    }
    if (status === 404) {
      errorMessage.value = "제출 대상 문제를 찾을 수 없습니다.";
      return;
    }
    if (status === 409) {
      errorMessage.value = "이미 완료된 퀴즈입니다. 결과 화면으로 이동해 주세요.";
      return;
    }
    errorMessage.value = "답안 제출 중 오류가 발생했습니다.";
  } finally {
    submitting.value = false;
  }
}

async function goNext() {
  if (!submissionDone.value || !question.value) {
    return;
  }

  try {
    navigatingNext.value = true;
    errorMessage.value = "";

    const attemptId = Number(route.params.attemptId);
    const seq = Number(route.params.seq);
    const nextSeq = seq + 1;

    if (nextSeq <= totalQuestions.value) {
      router.push(`/quiz/attempts/${attemptId}/questions/${nextSeq}`);
      return;
    }

    await completeAttempt(attemptId);
    router.push(`/quiz/attempts/${attemptId}/result`);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      errorMessage.value = "아직 제출하지 않은 문제가 있습니다.";
      return;
    }
    if (status === 401) {
      errorMessage.value = "로그인이 필요합니다. 로그인 후 다시 시도해 주세요.";
      return;
    }
    if (status === 403) {
      errorMessage.value = "완료할 권한이 없습니다.";
      return;
    }
    errorMessage.value = "다음 진행 중 오류가 발생했습니다.";
  } finally {
    navigatingNext.value = false;
  }
}

function openReportModal() {
  reportType.value = "TYPO";
  reportContent.value = "";
  reportErrorMessage.value = "";
  reportSuccessMessage.value = "";
  showReportModal.value = true;
}

function closeReportModal() {
  showReportModal.value = false;
  reportErrorMessage.value = "";
}

async function toggleFavoriteClick() {
  if (!question.value?.questionId || favoriteSubmitting.value) {
    return;
  }

  try {
    favoriteSubmitting.value = true;
    errorMessage.value = "";
    const response = await toggleFavorite(question.value.questionId);
    isFavorited.value = !!response?.favorited;
  } catch (error) {
    const status = error?.response?.status;
    if (status === 401) {
      errorMessage.value = "즐겨찾기는 로그인 후 이용할 수 있습니다.";
      return;
    }
    if (status === 403) {
      errorMessage.value = "즐겨찾기 권한이 없습니다.";
      return;
    }
    if (status === 404) {
      errorMessage.value = "해당 문제를 찾을 수 없습니다.";
      return;
    }
    errorMessage.value = "즐겨찾기 처리 중 오류가 발생했습니다.";
  } finally {
    favoriteSubmitting.value = false;
  }
}

async function submitReport() {
  if (!question.value?.questionId) {
    reportErrorMessage.value = "신고할 문제 정보가 없습니다.";
    return;
  }
  if (!reportContent.value.trim()) {
    reportErrorMessage.value = "신고 내용을 입력해 주세요.";
    return;
  }

  try {
    reportSubmitting.value = true;
    reportErrorMessage.value = "";

    await createReport(question.value.questionId, reportType.value, reportContent.value.trim());
    showReportModal.value = false;
    reportSuccessMessage.value = "신고가 접수되었습니다.";
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      reportErrorMessage.value = "신고 요청 값이 올바르지 않습니다.";
      return;
    }
    if (status === 401) {
      reportErrorMessage.value = "신고하려면 로그인 상태를 확인해 주세요.";
      return;
    }
    if (status === 403) {
      reportErrorMessage.value = "신고 권한이 없습니다.";
      return;
    }
    if (status === 404) {
      reportErrorMessage.value = "신고 대상 문제를 찾을 수 없습니다.";
      return;
    }
    reportErrorMessage.value = "신고 등록 중 오류가 발생했습니다.";
  } finally {
    reportSubmitting.value = false;
  }
}
</script>
