<template>
  <section class="card">
    <h2>마이페이지</h2>

    <div class="mypage-grid">
      <section class="panel">
        <h3>회원 정보 수정</h3>
        <form class="form" @submit.prevent>
          <input v-model="profile.nickname" type="text" placeholder="닉네임" />
          <input v-model="profile.email" type="email" placeholder="이메일" disabled />
          <button type="button" disabled>저장(연동 예정)</button>
        </form>
      </section>

      <section class="panel">
        <h3>학습 바로가기</h3>
        <div class="actions actions-left">
          <RouterLink class="btn-link" to="/quiz/wrong-answers">오답노트</RouterLink>
          <RouterLink class="btn-link secondary" to="/quiz/favorites">즐겨찾기</RouterLink>
        </div>
        <div class="actions actions-left">
          <a href="#stats" class="btn-link secondary">통계 보기</a>
        </div>
      </section>
    </div>

    <section id="stats" class="panel">
      <h3>학습 통계</h3>
      <p class="muted">통계 API 연동 전 기본 카드 UI입니다.</p>
      <div class="stats-grid">
        <article class="stat-card">
          <p class="muted">총 시도 수</p>
          <strong>{{ stats.totalAttempts }}</strong>
        </article>
        <article class="stat-card">
          <p class="muted">평균 정답률</p>
          <strong>{{ stats.averageAccuracy }}%</strong>
        </article>
        <article class="stat-card">
          <p class="muted">누적 정답 수</p>
          <strong>{{ stats.totalCorrect }}</strong>
        </article>
        <article class="stat-card">
          <p class="muted">최근 학습일</p>
          <strong>{{ stats.lastSolvedAt || "-" }}</strong>
        </article>
      </div>
    </section>
  </section>
</template>

<script setup>
import { reactive } from "vue";
import { RouterLink } from "vue-router";

const profile = reactive({
  nickname: "",
  email: ""
});

const stats = reactive({
  totalAttempts: 0,
  averageAccuracy: 0,
  totalCorrect: 0,
  lastSolvedAt: ""
});
</script>
