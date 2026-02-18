<template>
  <div class="state-box">
    <p class="muted">{{ message }}</p>
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    default: "로딩 중입니다..."
  }
});
</script>
