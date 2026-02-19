import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { login as loginApi, register as registerApi } from "../api/authApi";
import { getMyProfile } from "../api/memberApi";

export const useAuthStore = defineStore("auth", () => {
  const accessToken = ref(localStorage.getItem("accessToken") || "");
  const profile = ref(
    localStorage.getItem("authProfile") ? JSON.parse(localStorage.getItem("authProfile")) : null
  );
  const isLoggedIn = computed(() => accessToken.value.length > 0);
  const role = computed(() => profile.value?.role || "");
  const isAdmin = computed(() => role.value === "ADMIN");

  function setToken(token) {
    accessToken.value = token || "";
    if (accessToken.value) {
      localStorage.setItem("accessToken", accessToken.value);
    } else {
      localStorage.removeItem("accessToken");
    }
  }

  function setProfile(nextProfile) {
    profile.value = nextProfile || null;
    if (profile.value) {
      localStorage.setItem("authProfile", JSON.stringify(profile.value));
    } else {
      localStorage.removeItem("authProfile");
    }
  }

  async function login(email, password) {
    const token = await loginApi({ email, password });
    setToken(token.accessToken);
    await fetchMe();
  }

  async function register(email, nickname, password) {
    const token = await registerApi({ email, nickname, password });
    setToken(token.accessToken);
    await fetchMe();
  }

  async function fetchMe() {
    if (!accessToken.value) {
      setProfile(null);
      return null;
    }
    try {
      const me = await getMyProfile();
      setProfile(me);
      return me;
    } catch (_error) {
      setProfile(null);
      return null;
    }
  }

  function logout() {
    setToken("");
    setProfile(null);
  }

  return {
    accessToken,
    profile,
    role,
    isAdmin,
    isLoggedIn,
    login,
    register,
    fetchMe,
    logout,
    setToken,
    setProfile
  };
});
