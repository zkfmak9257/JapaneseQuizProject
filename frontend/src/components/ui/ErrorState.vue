<template>
  <div class="state-box">
    <p class="error">{{ message }}</p>
    <button v-if="showRetry" class="ghost" @click="$emit('retry')">다시 시도</button>
  </div>
</template>

<script setup>
defineEmits(["retry"]);

defineProps({
  message: {
    type: String,
    default: "오류가 발생했습니다."
  },
  showRetry: {
    type: Boolean,
    default: false
  }
});
</script>
