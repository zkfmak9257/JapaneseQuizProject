import { reactive } from "vue";

const state = reactive({
  visible: false,
  type: "alert",   // "alert" | "confirm"
  title: "",
  message: "",
  confirmText: "확인",
  cancelText: "취소",
  resolve: null,
});

function showAlert(message, title = "알림") {
  return new Promise((resolve) => {
    state.visible = true;
    state.type = "alert";
    state.title = title;
    state.message = message;
    state.confirmText = "확인";
    state.resolve = resolve;
  });
}

function showConfirm(message, title = "확인") {
  return new Promise((resolve) => {
    state.visible = true;
    state.type = "confirm";
    state.title = title;
    state.message = message;
    state.confirmText = "확인";
    state.cancelText = "취소";
    state.resolve = resolve;
  });
}

function close(result) {
  state.visible = false;
  if (state.resolve) {
    state.resolve(result);
    state.resolve = null;
  }
}

export function useModal() {
  return { state, showAlert, showConfirm, close };
}
