<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="state.visible" class="modal-backdrop" @click.self="onBackdropClick">
        <div class="modal-card" role="dialog" aria-modal="true">

          <!-- 아이콘 -->
          <div class="modal-icon" :class="state.type">
            <span v-if="state.type === 'confirm'">🗑️</span>
            <span v-else>💬</span>
          </div>

          <!-- 제목 -->
          <h3 class="modal-title">{{ state.title }}</h3>

          <!-- 메시지 -->
          <p class="modal-message">{{ state.message }}</p>

          <!-- 버튼 -->
          <div class="modal-actions" :class="{ single: state.type === 'alert' }">
            <button
              v-if="state.type === 'confirm'"
              class="btn btn-cancel"
              @click="close(false)"
            >
              {{ state.cancelText }}
            </button>
            <button class="btn btn-confirm" @click="close(true)">
              {{ state.confirmText }}
            </button>
          </div>

        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { useModal } from "../../composables/useModal";

const { state, close } = useModal();

function onBackdropClick() {
  // confirm은 backdrop 클릭 시 취소, alert는 닫기
  close(state.type === "confirm" ? false : true);
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  z-index: 9999;
  background: rgba(15, 23, 42, 0.55);
  backdrop-filter: blur(3px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.modal-card {
  background: #fff;
  border-radius: 20px;
  padding: 32px 28px 24px;
  width: 100%;
  max-width: 360px;
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.25);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  text-align: center;
}

.modal-icon {
  font-size: 36px;
  line-height: 1;
}

.modal-title {
  font-size: 17px;
  font-weight: 800;
  color: #0f172a;
  margin: 0;
}

.modal-message {
  font-size: 14px;
  font-weight: 500;
  color: #475569;
  margin: 0;
  line-height: 1.6;
  word-break: keep-all;
}

.modal-actions {
  display: flex;
  gap: 10px;
  width: 100%;
  margin-top: 8px;
}
.modal-actions.single {
  justify-content: center;
}

.btn {
  flex: 1;
  padding: 12px;
  border-radius: 10px;
  border: none;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.18s;
}
.btn-cancel {
  background: #f1f5f9;
  color: #475569;
}
.btn-cancel:hover { background: #e2e8f0; }

.btn-confirm {
  background: #0f172a;
  color: #fff;
}
.btn-confirm:hover { background: #1e293b; }

.modal-actions.single .btn-confirm {
  max-width: 160px;
}

/* 트랜지션 */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.2s ease;
}
.modal-enter-active .modal-card,
.modal-leave-active .modal-card {
  transition: transform 0.2s ease, opacity 0.2s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
.modal-enter-from .modal-card,
.modal-leave-to .modal-card {
  transform: scale(0.93) translateY(10px);
  opacity: 0;
}
</style>
