<template>
  <section class="card" v-if="question">
    <div class="quiz-top">
      <div class="quiz-progress">
        <h2>문제 {{ question.seq }} / {{ totalQuestions }}</h2>
        <div class="progress-track">
          <div class="progress-fill" :style="{ width: `${progressPercent}%` }"></div>
        </div>
      </div>
      <button class="report-button" @click="openReportModal">신고하기</button>
    </div>

    <p class="muted">유형: {{ isSentenceMode ? "문장 조합" : "단어 선택" }}</p>
    <p>{{ question.questionText }}</p>

    <template v-if="isSentenceMode">
      <div class="sentence-builder">
        <p class="muted">한국어 문장에 맞게 일본어 토큰을 드래그해서 올바른 순서로 조합하세요.</p>
        <p class="muted">선택 {{ answerTokens.length }} / {{ requiredSentenceTokenCount }}</p>

        <div class="dnd-columns">
          <section class="dnd-column">
            <h4>토큰 보관함</h4>
            <div class="token-dropzone" @dragover.prevent @drop="dropToPool">
              <p v-if="tokenPool.length === 0" class="muted">남은 토큰이 없습니다.</p>
              <button
                v-for="(token, idx) in tokenPool"
                :key="`pool-${token.tokenId}-${idx}`"
                class="ghost token-btn"
                draggable="true"
                :disabled="submissionDone"
                @dragstart="startDrag('pool', idx)"
                @dragover.prevent
                @drop.prevent="dropOnPoolIndex(idx)"
                @click="movePoolTokenToAnswer(idx)"
              >
                {{ token.tokenText }}
              </button>
            </div>
          </section>

          <section class="dnd-column">
            <h4>내가 조합한 문장</h4>
            <div class="token-dropzone answer-zone" @dragover.prevent @drop="dropToAnswer">
              <p v-if="answerTokens.length === 0" class="muted">여기에 토큰을 올려주세요.</p>
              <button
                v-for="(token, idx) in answerTokens"
                :key="`ans-${token.tokenId}-${idx}`"
                class="ghost token-btn"
                draggable="true"
                :disabled="submissionDone"
                @dragstart="startDrag('answer', idx)"
                @dragover.prevent
                @drop.prevent="dropOnAnswerIndex(idx)"
                @click="moveAnswerTokenToPool(idx)"
              >
                {{ token.tokenText }}
              </button>
            </div>
          </section>
        </div>

        <div class="actions actions-left">
          <button class="ghost" :disabled="submissionDone || answerTokens.length === 0" @click="resetSentenceBoard">
            초기화
          </button>
        </div>
      </div>
    </template>

    <ul v-else class="choices">
      <li v-for="choice in question.choices" :key="choice.choiceId">
        <label>
          <input
            type="radio"
            name="choice"
            :value="choice.choiceId"
            v-model="selectedChoiceId"
            :disabled="submissionDone"
          />
          {{ choice.choiceText }}
        </label>
      </li>
    </ul>

    <div class="actions actions-left">
      <button @click="submit" :disabled="!canSubmit || submitting || submissionDone">
        {{ submitting ? "제출 중..." : "제출" }}
      </button>
      <button class="ghost" @click="goNext" :disabled="!submissionDone || navigatingNext">
        {{ question.seq < totalQuestions ? "다음" : "결과 보기" }}
      </button>
    </div>

    <section v-if="submissionDone && gradeResult" class="grade-box">
      <p :class="gradeResult.correct ? 'success' : 'error'">
        {{ gradeResult.correct ? "정답입니다." : "오답입니다." }}
      </p>
      <p class="muted">다음 버튼을 눌러 계속 진행해 주세요.</p>
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

const showReportModal = ref(false);
const reportType = ref("TYPO");
const reportContent = ref("");
const reportSubmitting = ref(false);
const reportErrorMessage = ref("");
const reportSuccessMessage = ref("");

const progressPercent = computed(() => {
  if (!question.value || !totalQuestions.value) {
    return 0;
  }
  return Math.floor((question.value.seq / totalQuestions.value) * 100);
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
    return requiredSentenceTokenCount.value > 0 && answerTokens.value.length === requiredSentenceTokenCount.value;
  }
  return !!selectedChoiceId.value;
});

const requiredSentenceTokenCount = computed(() => sentenceTokenSource.value.length);

function shuffle(tokens) {
  const arr = [...tokens];
  for (let i = arr.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]];
  }
  return arr;
}

function resetSentenceBoard() {
  tokenPool.value = shuffle(sentenceBoardSource.value);
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
  const [token] = answerTokens.value.splice(index, 1);
  if (token) {
    tokenPool.value.push(token);
  }
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
      selectedChoiceId: res.selectedChoiceId
    };

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
