import { createRouter, createWebHistory } from "vue-router";
import { storeToRefs } from "pinia";
import { useAuthStore } from "../stores/authStore";
import LoginView from "../views/LoginView.vue";
import SignupView from "../views/SignupView.vue";
import MainView from "../views/MainView.vue";
import MyPageView from "../views/MyPageView.vue";
import MeHistoryListView from "../views/MeHistoryListView.vue";
import MeHistoryDetailView from "../views/MeHistoryDetailView.vue";
import MeStatsView from "../views/MeStatsView.vue";
import MeProfileView from "../views/MeProfileView.vue";
import QuizStartView from "../views/QuizStartView.vue";
import QuizSolveView from "../views/QuizSolveView.vue";
import QuizResultView from "../views/QuizResultView.vue";
import FavoriteListView from "../views/FavoriteListView.vue";
import WrongAnswerListView from "../views/WrongAnswerListView.vue";
import AdminDashboardView from "../views/AdminDashboardView.vue";
import AdminReportsView from "../views/AdminReportsView.vue";
import AdminStatsView from "../views/AdminStatsView.vue";
import ForbiddenView from "../views/ForbiddenView.vue";

const routes = [
  { path: "/", component: MainView },
  { path: "/403", component: ForbiddenView },
  { path: "/login", component: LoginView },
  { path: "/signup", component: SignupView },
  { path: "/mypage", component: MyPageView, meta: { requiresAuth: true } },
  { path: "/me", redirect: "/mypage" },
  { path: "/me/history", component: MeHistoryListView, meta: { requiresAuth: true } },
  { path: "/me/history/:attemptId", component: MeHistoryDetailView, meta: { requiresAuth: true } },
  { path: "/me/wrong-notes", redirect: "/quiz/wrong-answers" },
  { path: "/me/favorites", redirect: "/quiz/favorites" },
  { path: "/me/stats", component: MeStatsView, meta: { requiresAuth: true } },
  { path: "/me/profile", component: MeProfileView, meta: { requiresAuth: true } },
  { path: "/quiz/start", component: QuizStartView },
  { path: "/quiz/attempts/:attemptId/questions/:seq", component: QuizSolveView },
  { path: "/quiz/attempts/:attemptId/result", component: QuizResultView },
  { path: "/quiz/favorites", component: FavoriteListView, meta: { requiresAuth: true } },
  { path: "/quiz/wrong-answers", component: WrongAnswerListView, meta: { requiresAuth: true } },
  { path: "/admin", component: AdminDashboardView, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: "/admin/reports", component: AdminReportsView, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: "/admin/stats", component: AdminStatsView, meta: { requiresAuth: true, requiresAdmin: true } }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach(async (to) => {
  const authStore = useAuthStore();
  const { isLoggedIn, isAdmin } = storeToRefs(authStore);

  if ((to.path === "/login" || to.path === "/signup") && isLoggedIn.value) {
    return { path: "/mypage" };
  }

  if (to.meta.requiresAuth && !isLoggedIn.value) {
    return { path: "/login", query: { redirect: to.fullPath } };
  }

  if (to.meta.requiresAdmin) {
    if (!authStore.profile) {
      await authStore.fetchMe();
    }
    if (!isAdmin.value) {
      return { path: "/403" };
    }
  }

  return true;
});

export default router;
