<template>
  <!-- ── 상단 고정 토스트 에러 메시지 ── -->
  <div v-if="errorMessage && !submissionDone" class="toast-error">
    ⚠️ {{ errorMessage }}
  </div>

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
              <span class="stk-content">
                <template v-if="hasRuby(token.tokenText)">
                  <ruby>
                    <span class="ruby-base">{{ rubyBase(token.tokenText) }}</span>
                    <rt>{{ rubyReading(token.tokenText) }}</rt>
                  </ruby>
                </template>
                <template v-else>{{ token.tokenText }}</template>
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
              <span class="stk-content">
                <template v-if="hasRuby(token.tokenText)">
                  <ruby>
                    <span class="ruby-base">{{ rubyBase(token.tokenText) }}</span>
                    <rt>{{ rubyReading(token.tokenText) }}</rt>
                  </ruby>
                </template>
                <template v-else>{{ token.tokenText }}</template>
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

    <!-- ── 카드형 선택지 그리드 (여행 티켓 스타일) ────────── -->
    <div v-if="!isSentenceMode" class="choices-grid">
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
                <span class="ruby-base">{{ rubyBase(choice.choiceText) }}</span>
                <rt>{{ rubyReading(choice.choiceText) }}</rt>
              </ruby>
            </template>
            <template v-else>{{ choice.choiceText }}</template>
          </span>
        </div>
      </div>
    </div>
    <!-- ── 제출 버튼 영역 ────────────────────────── -->
    <div v-if="!submissionDone" class="quiz-actions">
      <button
        class="btn-submit"
        @click="submit"
        :disabled="!canSubmit || submitting"
      >
        {{ submitting ? "⏳ 제출 중..." : "🎯 제출" }}
      </button>
    </div>
  </section>

  <FeedbackPanel
    :visible="submissionDone && !!gradeResult"
    :is-correct="!!gradeResult?.correct"
    :correct="feedbackCorrectItem"
    :selected="feedbackSelectedItem"
    :choices="feedbackChoices"
    :server-error="submissionDone ? '' : errorMessage"
    :guide-text="feedbackGuideText"
    :key-point="feedbackKeyPoint"
    @next="goNext"
    @toggle-favorite="toggleFavoriteClick"
    @go-wrong-note="goWrongNote"
    @retry-submit="submit"
    @dismiss-error="dismissFeedbackError"
  />

  <section class="card" v-if="!question && loading">
  </section>

  <section class="card" v-if="!question && !loading">
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
import FeedbackPanel from "../components/quiz/FeedbackPanel.vue";

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

const feedbackCorrectItem = computed(() => {
  const jp = stageCorrect.value?.jpText || "정답 정보 없음";
  return {
    kanji: rubyBase(jp),
    kana: rubyReading(jp) || "-",
    meaning: stageCorrect.value?.koMeaning || "해석 정보가 없습니다."
  };
});

const feedbackSelectedItem = computed(() => {
  if (isSentenceMode.value) {
    const joined = answerTokens.value.map((t) => t.tokenText).join("");
    return {
      kanji: rubyBase(joined || "내 제출 답안"),
      kana: rubyReading(joined) || "-",
      meaning: stageSentence.value?.diffHint || "문장 조합 답안"
    };
  }

  const selected = stageChoices.value.find((c) => c.choiceId === selectedChoiceId.value);
  if (!selected) {
    return { kanji: "선택 정보 없음", kana: "-", meaning: "-" };
  }
  return {
    kanji: rubyBase(selected.jpText || "선택 정보 없음"),
    kana: rubyReading(selected.jpText || "") || "-",
    meaning: selected.koMeaning || "-"
  };
});

const feedbackChoices = computed(() => {
  return stageChoices.value.map((c) => ({
    kanji: rubyBase(c.jpText || "-"),
    kana: rubyReading(c.jpText || "") || "-",
    meaning: c.koMeaning || "-"
  }));
});

const feedbackGuideText = computed(() => {
  const scene = question.value?.sceneName || "여행";
  return `${scene} 상황에서 자주 쓰는 표현입니다. 정답과 해설을 확인하고 다음 미션으로 이동하세요.`;
});

const feedbackKeyPoint = computed(() => {
  return stageExplanation.value?.oneLiner || stageExplanation.value?.detail || "핵심 해설이 준비되지 않았습니다.";
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
  const correctId = gradeResult.value?.correctChoiceId;
  const isTheCorrectCard = correctId != null && correctId === choiceId;

  return {
    // ✅ 내가 고른 카드가 정답이거나 틀렸을 때 실제 정답 카드일 때: is-correct-choice 적용 (초록)
    'is-correct-choice': (isCorrect && wasMyChoice) || (!isCorrect && isTheCorrectCard),
    // ❌ 내가 고른 카드가 오답일 때: is-wrong-choice 적용 (빨강)
    'is-wrong-choice': !isCorrect && wasMyChoice,
    // 정답/오답과 무관하게 모든 카드는 비활성화되어 hover 등 방지
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
  initialPoolOrder.value = shuffle(sentenceBoardSource.value);
  tokenPool.value = [...initialPoolOrder.value];
  answerTokens.value = [];
  dragItem.value = null;
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
    errorMessage.value = "";
    gradeResult.value = {
      correct: !!res.correct,
      selectedChoiceId: res.selectedChoiceId,
      // correctChoiceId: 서버가 정답 ID를 보내주면 오답 시 정답 카드 공개에 활용
      correctChoiceId: res.correctChoiceId ?? null,
      feedbackMessage: res.feedbackMessage || null
    };
    stagePayload.value = res.stagePayload || null;

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

function goWrongNote() {
  router.push("/quiz/wrong-answers");
}

function dismissFeedbackError() {
  errorMessage.value = "";
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

/* ========================================================= */
/*  Phase 12: Unified Learning-Centric Feedback Card Styles  */
/* ========================================================= */

.unified-feedback-card {
  margin-top: 2rem;
  padding: 1.5rem;
  border-radius: 16px;
  background: var(--bg-card);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;
}

.unified-feedback-card::before {
  content: "";
  position: absolute;
  top: 0; left: 0; width: 6px; height: 100%;
}

.unified-feedback-card.is-correct::before { background-color: var(--success); }
.unified-feedback-card.is-wrong::before   { background-color: var(--danger); }

.unified-feedback-card.is-correct { background-color: rgba(46, 204, 113, 0.03); }
.unified-feedback-card.is-wrong   { background-color: rgba(231, 76, 60, 0.03); }

/* 상단 미션 결과 헤더 (티켓/여권 배지 스타일) */
.ticket-header-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.8rem;
  padding-bottom: 1.5rem;
  border-bottom: 2px dashed var(--border-color);
  margin-bottom: 1rem;
}
.feedback-status-badge {
  padding: 0.4rem 1.2rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 800;
  letter-spacing: 0.05em;
  color: white;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}
.badge-clear { background: linear-gradient(135deg, #27ae60, #2ecc71); }
.badge-fail { background: linear-gradient(135deg, #e74c3c, #c0392b); }

.feedback-title {
  text-align: center;
}
.feedback-title-text {
  margin: 0;
  font-size: 1.6rem;
  font-weight: 800;
  letter-spacing: -0.02em;
}
.text-correct { color: var(--success); }
.text-wrong { color: var(--danger); }

.feedback-subtitle {
  margin: 0.4rem 0 0 0;
  font-size: 0.95rem;
  color: var(--text-muted);
}

/* 정답/오답 대조 영역 */
.feedback-answer-reveal {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}
.answer-label, .wrong-label {
  font-size: 0.85rem;
  font-weight: 700;
  letter-spacing: -0.02em;
  display: flex;
  align-items: center;
  gap: 0.3rem;
}
.answer-label { color: var(--success-dark, #27ae60); }
.wrong-label  { color: var(--danger-dark, #c0392b); }

.answer-jp-text {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 800;
  color: var(--text-color);
  letter-spacing: 0.02em;
}
.answer-sentence-tokens {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.3rem;
  font-weight: 700;
}
.answer-token-item ruby rt {
  font-size: 0.55em;
  color: var(--text-muted);
}
.answer-ko-meaning {
  margin: 0;
  font-size: 1rem;
  color: var(--text-muted);
}

.wrong-choice-compare {
  margin-top: 0.5rem;
  padding: 1rem;
  background-color: rgba(231, 76, 60, 0.05);
  border-radius: 8px;
  border-left: 3px solid var(--danger);
}
.wrong-jp-text {
  margin: 0.5rem 0 0 0;
  font-size: 1.1rem;
  font-weight: 700;
  color: var(--text-color);
}
.wrong-ko-meaning {
  font-weight: 400;
  color: var(--text-muted);
  font-size: 0.95rem;
}

/* 해설 토글 영역 */
.feedback-explanation {
  background: var(--bg-body);
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid var(--border-color);
}
.explanation-toggle-btn {
  width: 100%;
  text-align: left;
  padding: 1rem;
  background: transparent;
  border: none;
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--primary-color);
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: background 0.2s ease;
}
.explanation-toggle-btn:hover {
  background: rgba(0,0,0,0.02);
}
.explanation-content {
  padding: 0 1rem 1rem 1rem;
  border-top: 1px dashed var(--border-color);
  margin-top: -0.5rem;
  padding-top: 1rem;
}
.explanation-highlight {
  margin: 0 0 0.5rem 0;
  font-weight: 600;
  color: var(--text-color);
}
.explanation-detail {
  margin: 0 0 1rem 0;
  font-size: 0.95rem;
  color: var(--text-muted);
  line-height: 1.5;
}

/* 어휘 비교 그리드 (미니 단어장) */
.vocab-comparison-grid {
  display: grid;
  gap: 0.5rem;
}
.vocab-comparison-header {
  display: grid;
  grid-template-columns: 2fr 3fr auto;
  gap: 1rem;
  padding: 0 0.75rem 0.5rem 0.75rem;
  font-size: 0.85rem;
  font-weight: 700;
  color: var(--text-muted);
  border-bottom: 2px solid var(--border-color);
  margin-bottom: 0.5rem;
}
.vocab-item {
  display: grid;
  grid-template-columns: 2fr 3fr auto;
  gap: 1rem;
  padding: 0.75rem;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 6px;
  align-items: center;
  font-size: 0.9rem;
}
.vocab-jp {
  font-weight: 700;
  font-size: 1.05rem;
}
.vocab-ko {
  color: var(--text-color);
}
.vocab-note {
  font-size: 0.8rem;
  color: var(--text-light);
  font-style: italic;
  justify-self: end;
}
@media (max-width: 600px) {
  .vocab-item {
    grid-template-columns: 1fr;
    gap: 0.2rem;
  }
  .vocab-note {
    justify-self: start;
  }
}

/* 단일 CTA (다음 버튼) */
.feedback-cta {
  margin-top: 0.5rem;
  display: grid;
  gap: 0.75rem;
}

.feedback-cta-secondary {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.btn-action-secondary {
  flex: 1;
  padding: 0.8rem 1rem;
  border: 1px solid var(--border-color);
  background: #fff;
  color: var(--text-color);
  border-radius: 10px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
}

.btn-action-secondary:hover:not(:disabled) {
  background: rgba(0,0,0,0.02);
  border-color: #94a3b8;
}

.btn-action-secondary.ghost {
  background: rgba(52, 152, 219, 0.05);
  border-color: transparent;
  color: var(--primary-color);
}

.btn-action-secondary.ghost:hover {
  background: rgba(52, 152, 219, 0.1);
}

.btn-next-mission {
  width: 100%;
  background: #0f172a !important;
  color: #fbbf24 !important;
  padding: 1.2rem;
  font-size: 1.15rem;
  font-weight: 800;
  border-radius: 14px;
  box-shadow: 0 6px 20px rgba(15, 23, 42, 0.25) !important;
  margin-top: 0.5rem;
  border: none;
  cursor: pointer;
  transition: all 0.2s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.btn-next-mission:hover:not(:disabled) {
  background: #1e293b !important;
  transform: translateY(-3px) scale(1.02) !important;
  box-shadow: 0 8px 25px rgba(15, 23, 42, 0.35) !important;
}

.btn-next-mission:disabled {
  background: var(--border-color) !important;
  color: var(--text-muted) !important;
  cursor: not-allowed;
  box-shadow: none !important;
  transform: none !important;
}

.feedback-stepper {
  display: flex;
  gap: 0.4rem;
  flex-wrap: wrap;
  margin-bottom: 0.6rem;
}

.step-pill {
  font-size: 0.78rem;
  border-radius: 999px;
  border: 1px solid var(--border-color);
  padding: 0.25rem 0.55rem;
  color: var(--text-muted);
  background: #fff;
}

.step-pill.active {
  border-color: var(--primary-color);
  color: var(--primary-color);
  background: rgba(52, 152, 219, 0.08);
  font-weight: 700;
}

/* ── 미션 가이드 노트 (말풍선) ── */
.guide-note {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
  margin-bottom: 1.5rem;
  padding: 1.2rem;
  background: #e0f2fe;
  border: 1px solid #bae6fd;
  border-radius: 12px;
}
.guide-character {
  font-size: 2.2rem;
  background: white;
  width: 50px;
  height: 50px;
  min-width: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.guide-speech { flex-grow: 1; }
.guide-speech .context-title {
  margin: 0 0 0.4rem 0; font-size: 0.9rem; font-weight: 800; color: #0369a1;
}
.guide-speech .context-desc {
  margin: 0; font-size: 0.95rem; color: #0f172a; line-height: 1.5;
}

/* ========================================================= */
/*  Phase 13: Advanced Feedback Polish & In-place highlight  */
/* ========================================================= */

/* 정답/오답 컬러박스 분리 */
.answer-box {
  background-color: rgba(46, 204, 113, 0.08);
  border: 1px solid rgba(46, 204, 113, 0.3);
  border-radius: 8px;
  padding: 1.25rem;
  margin-bottom: 0.5rem;
}

.wrong-box {
  background-color: rgba(231, 76, 60, 0.05);
  border: 1px solid rgba(231, 76, 60, 0.3);
  border-radius: 8px;
  padding: 1.25rem;
  margin-bottom: 0.5rem;
}

/* 선택지 카드 애니메이션 강화 (여행 티켓 호버 적용) */
.choice-card:hover:not(.disabled) {
  transform: translateY(-3px) scale(1.01) !important;
  box-shadow: 0 8px 20px rgba(0,0,0,0.08) !important;
}

.choice-card.is-correct-choice {
  border-color: var(--success) !important;
  background-color: rgba(46, 204, 113, 0.05) !important;
  box-shadow: 0 0 0 2px var(--success) !important;
  animation: correctCardScale 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}
@keyframes correctCardScale {
  0% { transform: scale(1); }
  50% { transform: scale(1.02); }
  100% { transform: scale(1); }
}

.choice-card.is-correct-choice .choice-check {
  display: flex !important;
  color: var(--success);
  font-weight: bold;
}

.choice-card.is-wrong-choice {
  border-color: var(--danger) !important;
  background-color: rgba(231, 76, 60, 0.05) !important;
  animation: wrongCardShake 0.4s ease-in-out;
}
@keyframes wrongCardShake {
  0%, 100% { transform: translateX(0); }
  20%, 60% { transform: translateX(-4px); }
  40%, 80% { transform: translateX(4px); }
}

.choice-card.is-wrong-choice .choice-check {
  display: flex !important;
  color: var(--danger);
  font-weight: bold;
}

/* 상단 고정 토스트 에러 메시지 */
.toast-error {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: var(--danger);
  color: white;
  padding: 1rem 2rem;
  border-radius: 30px;
  box-shadow: 0 4px 15px rgba(231, 76, 60, 0.4);
  z-index: 9999;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  animation: slideDownFade 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

@keyframes slideDownFade {
  0% { top: -50px; opacity: 0; }
  100% { top: 20px; opacity: 1; }
}

.explanation-heading {
  margin: 0 0 0.8rem 0;
  font-size: 1.05rem;
  font-weight: 700;
  color: var(--text-color);
  border-bottom: 2px solid var(--border-color);
  padding-bottom: 0.4rem;
}
