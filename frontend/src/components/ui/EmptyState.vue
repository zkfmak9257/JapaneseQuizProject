<template>
  <div class="state-box">
    <p class="muted">{{ message }}</p>
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    default: "표시할 데이터가 없습니다."
  }
});
</script>
