import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { login as loginApi } from "../api/authApi";

export const useAuthStore = defineStore("auth", () => {
  const accessToken = ref(localStorage.getItem("accessToken") || "");
  const isLoggedIn = computed(() => accessToken.value.length > 0);

  function setToken(token) {
    accessToken.value = token || "";
    if (accessToken.value) {
      localStorage.setItem("accessToken", accessToken.value);
    } else {
      localStorage.removeItem("accessToken");
    }
  }

  async function login(email, password) {
    const token = await loginApi({ email, password });
    setToken(token.accessToken);
  }

  function logout() {
    setToken("");
  }

  return { accessToken, isLoggedIn, login, logout, setToken };
});
