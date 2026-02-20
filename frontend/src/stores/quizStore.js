import { reactive, ref } from "vue";
import { defineStore } from "pinia";

export const useQuizStore = defineStore("quiz", () => {
  const currentAttemptId = ref(null);
  const currentSeq = ref(1);
  const selectedQuestionType = ref(null);
  const selectedSceneId = ref(null);
  const questionCache = reactive({});
  const submitResults = reactive({});
  const isSubmitted = reactive({});

  function setCurrentAttempt(attemptId) {
    currentAttemptId.value = attemptId;
    currentSeq.value = 1;
  }

  function setCurrentSeq(seq) {
    currentSeq.value = seq;
  }

  function setStartOptions({ questionType = null, sceneId = null } = {}) {
    selectedQuestionType.value = questionType;
    selectedSceneId.value = sceneId;
  }

  function cacheQuestion(seq, question) {
    questionCache[seq] = question;
  }

  function setSubmitResult(questionId, result) {
    submitResults[questionId] = result;
  }

  function setSubmitted(seq, submitted) {
    isSubmitted[seq] = !!submitted;
  }

  function resetAttemptState() {
    currentAttemptId.value = null;
    currentSeq.value = 1;
    selectedQuestionType.value = null;
    selectedSceneId.value = null;
    Object.keys(questionCache).forEach((key) => delete questionCache[key]);
    Object.keys(submitResults).forEach((key) => delete submitResults[key]);
    Object.keys(isSubmitted).forEach((key) => delete isSubmitted[key]);
  }

  return {
    currentAttemptId,
    currentSeq,
    selectedQuestionType,
    selectedSceneId,
    questionCache,
    submitResults,
    isSubmitted,
    setCurrentAttempt,
    setCurrentSeq,
    setStartOptions,
    cacheQuestion,
    setSubmitResult,
    setSubmitted,
    resetAttemptState
  };
});
