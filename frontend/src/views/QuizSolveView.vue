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
    <p>{{ question.questionText }}</p>
    <ul class="choices">
      <li v-for="choice in question.choices" :key="choice.choiceId">
        <label>
          <input type="radio" name="choice" :value="choice.choiceId" v-model="selectedChoiceId" />
          {{ choice.choiceText }}
        </label>
      </li>
    </ul>
    <button @click="submit" :disabled="!selectedChoiceId || submitting">{{ submitting ? "제출 중..." : "제출" }}</button>
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
import { computed, ref, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import { completeAttempt, getAttemptQuestion, submitAnswer } from "../api/quizApi";
import { createReport } from "../api/reportApi";

const route = useRoute();
const router = useRouter();

const question = ref(null);
const selectedChoiceId = ref(null);
const totalQuestions = ref(10);
const loading = ref(false);
const submitting = ref(false);
const errorMessage = ref("");

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

watchEffect(async () => {
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
});

async function submit() {
  try {
    submitting.value = true;
    errorMessage.value = "";

    const attemptId = Number(route.params.attemptId);
    const seq = Number(route.params.seq);
    const res = await submitAnswer(attemptId, seq, selectedChoiceId.value);
    totalQuestions.value = res.totalQuestions;

    const nextSeq = seq + 1;
    if (nextSeq <= res.totalQuestions) {
      router.push(`/quiz/attempts/${attemptId}/questions/${nextSeq}`);
      return;
    }

    await completeAttempt(attemptId);
    router.push(`/quiz/attempts/${attemptId}/result`);
  } catch (error) {
    const status = error?.response?.status;
    if (status === 400) {
      errorMessage.value = "이미 제출했거나 유효하지 않은 선택지입니다.";
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
    errorMessage.value = "답안 제출 중 오류가 발생했습니다.";
  } finally {
    submitting.value = false;
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
