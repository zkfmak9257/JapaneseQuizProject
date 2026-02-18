import { createRouter, createWebHistory } from "vue-router";
import { storeToRefs } from "pinia";
import { useAuthStore } from "../stores/authStore";
import LoginView from "../views/LoginView.vue";
import SignupView from "../views/SignupView.vue";
import MainView from "../views/MainView.vue";
import QuizStartView from "../views/QuizStartView.vue";
import QuizSolveView from "../views/QuizSolveView.vue";
import QuizResultView from "../views/QuizResultView.vue";
import FavoriteListView from "../views/FavoriteListView.vue";
import WrongAnswerListView from "../views/WrongAnswerListView.vue";

const routes = [
  { path: "/", component: MainView },
  { path: "/login", component: LoginView },
  { path: "/signup", component: SignupView },
  { path: "/quiz/start", component: QuizStartView },
  { path: "/quiz/attempts/:attemptId/questions/:seq", component: QuizSolveView, props: true },
  { path: "/quiz/attempts/:attemptId/result", component: QuizResultView, props: true },
  { path: "/quiz/favorites", component: FavoriteListView, meta: { requiresAuth: true } },
  { path: "/quiz/wrong-answers", component: WrongAnswerListView, meta: { requiresAuth: true } }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to) => {
  const authStore = useAuthStore();
  const { isLoggedIn } = storeToRefs(authStore);

  if (to.meta.requiresAuth && !isLoggedIn.value) {
    return { path: "/login", query: { redirect: to.fullPath } };
  }
  return true;
});

export default router;
