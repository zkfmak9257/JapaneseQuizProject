<template>
  <!-- ============================================================
       메인 페이지 — 일본 소도시 감성 리디자인
       
       구조:
       1. Hero Section: 배경 이미지 + 오버레이 + 타이틀 + CTA
       2. 카테고리 선택: Pill Toggle (단어/문장)
       3. Scene 선택: 이미지 카드 그리드 (8개)
       4. 시작하기 버튼
       ============================================================ -->
  <section class="quiz-home">

    <!-- ── 1. Hero Section ─────────────────────────────────
         fullwidth 배경 이미지 + 어두운 오버레이
         중앙에 타이틀과 CTA 버튼 배치 -->
    <div class="hero">
      <!-- 구름 애니메이션: CSS @keyframes로 천천히 움직이는 구름들 -->
      <div class="clouds">
        <div class="cloud cloud-1">☁</div>
        <div class="cloud cloud-2">☁</div>
        <div class="cloud cloud-3">☁</div>
      </div>

      <!-- 오버레이: 배경 이미지 위에 어두운 레이어 → 텍스트 가독성 확보 -->
      <div class="hero-overlay"></div>

      <!-- Hero 콘텐츠: 중앙 정렬 -->
      <div class="hero-content">
        <p class="hero-eyebrow">JAPANESE QUIZ</p>
        <h1 class="hero-title">일본어, 여행처럼 배우다.</h1>
        <p class="hero-subtitle">일상 · 여행 · 비즈니스 상황별 일본어 퀴즈</p>

        <!-- CTA 버튼 그룹 -->
        <div class="hero-actions">
          <!-- 메인 CTA: 퀴즈 시작 (현재 선택된 옵션으로 시작) -->
          <button class="cta-primary" @click="onStart">
            🌸 퀴즈 시작하기
          </button>
          <!-- 서브 CTA: 여행 모드 바로 시작 (sceneId=3 고정) -->
          <button class="cta-secondary" @click="onStartTravel">
            🚃 여행 모드 바로 시작
          </button>
        </div>
      </div>
    </div>

    <!-- ── 2. 카테고리 선택 (Pill Toggle) ───────────────────
         단어/문장 모드를 알약형 토글로 선택
         선택된 건 하늘색, 미선택은 투명 -->
    <section class="section-block">
      <h2 class="section-title">
        <span class="section-icon">📚</span>
        카테고리 선택
      </h2>

      <div class="pill-toggle">
        <!-- :class 바인딩: categoryType 값에 따라 active 클래스 토글 -->
        <button
          :class="['pill', { active: categoryType === 'WORD' }]"
          @click="categoryType = 'WORD'"
        >
          <!-- 이모지 + 텍스트로 직관적인 UI -->
          📝 단어 모드
        </button>
        <button
          :class="['pill', { active: categoryType === 'SENTENCE' }]"
          @click="categoryType = 'SENTENCE'"
        >
          💬 문장 모드
        </button>
      </div>
    </section>

    <!-- ── 3. Scene 선택 (이미지 카드 그리드) ────────────────
         8개 상황별 이미지 카드를 그리드로 배치
         hover 시 확대 + 그림자, 선택 시 하이라이트 -->
    <section class="section-block">
      <h2 class="section-title">
        <span class="section-icon">🗺️</span>
        상황 선택
      </h2>
      <p class="section-desc">학습하고 싶은 상황을 선택하세요. 선택하지 않으면 랜덤으로 출제됩니다.</p>

      <div class="scene-grid">
        <!-- v-for: scenes 배열을 순회하며 카드 렌더링 -->
        <button
          class="scene-card"
          :class="{ selected: selectedSceneId === scene.id }"
          v-for="scene in scenes"
          :key="scene.id"
          @click="toggleScene(scene.id)"
        >
          <!-- 카드 이미지 영역: scene별 다른 에셋 이미지 표시 -->
          <div class="scene-image">
            <img :src="scene.image" :alt="scene.name" loading="lazy" />
            <!-- 선택 시 표시되는 체크 배지 -->
            <div v-if="selectedSceneId === scene.id" class="selected-badge">✓</div>
          </div>
          <!-- 카드 정보 영역 -->
          <div class="scene-info">
            <strong class="scene-name">{{ scene.name }}</strong>
            <span class="scene-label">Scene {{ scene.id }}</span>
          </div>
        </button>
      </div>
    </section>

    <!-- ── 4. 시작하기 버튼 (하단 고정형) ───────────────────
         현재 선택 상태를 요약하여 표시 -->
    <section class="start-section">
      <div class="start-summary">
        <!-- 현재 선택된 옵션 표시 -->
        <span class="summary-chip">{{ categoryType === 'WORD' ? '📝 단어' : '💬 문장' }}</span>
        <span class="summary-chip">{{ selectedSceneName }}</span>
        <span class="summary-chip">10문제</span>
      </div>
      <button class="start-button" @click="onStart">
        🌸 퀴즈 시작하기
      </button>
    </section>
  </section>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";

/* ── Scene 카드용 이미지 임포트 ────────────────────────────
   Vite에서는 import로 가져온 에셋이 빌드 시 해싱된 URL로 변환된다.
   이렇게 하면 캐시 버스팅(cache busting) 등이 자동 처리된다. */
import imgDailyLife from "../assets/scenes/daily-life.png";
import imgBusiness from "../assets/scenes/business.png";
import imgTravel from "../assets/scenes/travel.png";
import imgRestaurant from "../assets/scenes/restaurant.png";
import imgShopping from "../assets/scenes/shopping.png";
import imgHospital from "../assets/scenes/hospital.png";
import imgSchool from "../assets/scenes/school.png";
import imgEmergency from "../assets/scenes/emergency.png";

const router = useRouter();

/* ── 상태(State) 관리 ──────────────────────────────────── */
// categoryType: 단어(WORD) 또는 문장(SENTENCE) 모드 선택
const categoryType = ref("WORD");

// scenes: 8개 상황 데이터 배열 — id, name, image 포함
// 기존 로직에서 image 필드만 추가됨
const scenes = [
  { id: 1, name: "일상회화", image: imgDailyLife },
  { id: 2, name: "비즈니스", image: imgBusiness },
  { id: 3, name: "여행",     image: imgTravel },
  { id: 4, name: "식당",     image: imgRestaurant },
  { id: 5, name: "쇼핑",     image: imgShopping },
  { id: 6, name: "병원",     image: imgHospital },
  { id: 7, name: "학교",     image: imgSchool },
  { id: 8, name: "긴급상황", image: imgEmergency }
];

// selectedSceneId: 현재 선택된 scene (null이면 전체 랜덤)
const selectedSceneId = ref(null);

// computed: 선택된 scene의 이름을 반환 (UI 표시용)
const selectedSceneName = computed(() => {
  if (selectedSceneId.value === null) return "🗺️ 전체(랜덤)";
  const scene = scenes.find(s => s.id === selectedSceneId.value);
  return scene ? `🗺️ ${scene.name}` : "🗺️ 전체(랜덤)";
});

/* ── 이벤트 핸들러 ─────────────────────────────────────── */

// toggleScene: 카드 클릭 시 선택/해제 토글
// 같은 카드를 다시 클릭하면 선택 해제 → 전체(랜덤) 모드로 전환
function toggleScene(id) {
  selectedSceneId.value = selectedSceneId.value === id ? null : id;
}

// onStart: 현재 설정으로 퀴즈 시작
// query parameter로 questionType과 sceneId를 전달
function onStart() {
  router.push({
    path: "/quiz/start",
    query: {
      questionType: categoryType.value,
      sceneId: selectedSceneId.value == null ? undefined : String(selectedSceneId.value)
    }
  });
}

// onStartTravel: 여행 모드(sceneId=3) 바로 시작 — Hero CTA용
function onStartTravel() {
  selectedSceneId.value = 3;
  categoryType.value = "WORD";
  router.push({
    path: "/quiz/start",
    query: {
      questionType: "WORD",
      sceneId: "3"
    }
  });
}
</script>

<style scoped>
/* ============================================================
   MainView — 일본 소도시 감성 스타일
   ============================================================ */

/* ── 전체 컨테이너 ───────────────────────────────────── */
.quiz-home {
  /* 메인 화면은 시각 밀도를 위해 컴팩트 Hero 예외를 사용 */
  --hero-height: var(--hero-height-main-compact);
  /* content 클래스의 max-width 제한을 벗어나기 위해 음수 마진 사용 */
  margin: -24px calc(-1 * var(--gutter)) 0;
}

/* ── Hero Section ────────────────────────────────────── */
.hero {
  position: relative;
  /* 배경 이미지: AI 생성된 바닷가 기차역 일러스트 */
  background-image: url("../assets/hero-bg.png");
  background-size: cover;     /* 이미지가 영역을 꽉 채움 */
  background-position: center; /* 중앙 기준으로 크롭 */
  background-repeat: no-repeat;
  min-height: var(--hero-height, var(--hero-height-default));
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden; /* 구름 애니메이션이 영역 밖으로 나가지 않게 */
}

/* ── 구름 애니메이션 ─────────────────────────────────── 
   왜? 정적 이미지에 살짝 움직이는 요소를 추가하면
   페이지가 "살아있는" 느낌을 줘서 체류 시간↑ */
.clouds {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none; /* 클릭 이벤트 무시 — 구름이 버튼 클릭 방해 방지 */
  z-index: 1;
}

.cloud {
  position: absolute;
  font-size: 60px;
  opacity: 0.4;
  /* 흰색 + 반투명 → 자연스러운 구름 느낌 */
  color: white;
  text-shadow: 0 0 40px rgba(255, 255, 255, 0.5);
  /* ♻️ animation: 이름 / 지속시간 / 타이밍 / 반복 */
  animation: float-cloud linear infinite;
}

/* 각 구름마다 다른 위치/속도/크기로 자연스러운 움직임 */
.cloud-1 {
  top: 10%;
  font-size: 80px;
  opacity: 0.3;
  animation-duration: 30s; /* 느리게 → 뒤쪽 구름 느낌 */
}

.cloud-2 {
  top: 25%;
  font-size: 50px;
  opacity: 0.5;
  animation-duration: 22s;
  animation-delay: -8s; /* 시작 시간 다르게 → 동기화 방지 */
}

.cloud-3 {
  top: 5%;
  font-size: 40px;
  opacity: 0.35;
  animation-duration: 26s;
  animation-delay: -15s;
}

/* @keyframes: 왼쪽 밖에서 시작 → 오른쪽 밖으로 이동 */
@keyframes float-cloud {
  0% {
    transform: translateX(-120px);
  }
  100% {
    transform: translateX(calc(100vw + 120px));
  }
}

/* ── Hero 오버레이 ───────────────────────────────────── 
   왜? 배경 이미지가 밝을 때 텍스트가 안 보일 수 있음
   어두운 그라디언트 오버레이로 텍스트 가독성 보장 */
.hero-overlay {
  position: absolute;
  inset: 0; /* top: 0; right: 0; bottom: 0; left: 0; 의 축약 */
  /* 다중 그라디언트: 하단은 진하게, 상단은 살짝만 */
  background: linear-gradient(
    180deg,
    rgba(26, 45, 61, 0.2) 0%,
    rgba(26, 45, 61, 0.5) 50%,
    rgba(26, 45, 61, 0.75) 100%
  );
  z-index: 2;
}

/* ── Hero 콘텐츠 ─────────────────────────────────────── */
.hero-content {
  position: relative;
  z-index: 3; /* 오버레이보다 위에 위치 */
  text-align: center;
  color: #fff;
  padding: 48px var(--gutter);
}

.hero-eyebrow {
  font-family: var(--font-display);
  font-size: 13px;
  letter-spacing: 4px; /* 넓은 자간 → 고급스러운 느낌 */
  opacity: 0.85;
  margin-bottom: 12px;
  font-weight: 500;
}

.hero-title {
  font-family: var(--font-display);
  font-size: clamp(32px, 5vw, 64px);
  font-weight: 800;
  line-height: 1.25;
  margin-bottom: 10px;
  /* 텍스트에 살짝 빛나는 그림자 → 가독성 + 감성 */
  text-shadow: 0 2px 20px rgba(0, 0, 0, 0.3);
}

.hero-subtitle {
  font-size: clamp(16px, 2vw, 20px);
  opacity: 0.9;
  margin-bottom: 28px;
  font-weight: 300;
  letter-spacing: 1px;
}

/* ── CTA 버튼 그룹 ───────────────────────────────────── */
.hero-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
}

/* 메인 CTA: 눈에 띄는 그라데이션 버튼 */
.cta-primary {
  min-height: 52px;
  padding: 0 28px;
  border-radius: var(--radius-pill);
  /* 분홍→보라 그라데이션: 🌸 벚꽃 느낌 */
  background: linear-gradient(135deg, #f472b6, #a855f7);
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  font-family: var(--font-display);
  border: none;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  /* 살짝 떠있는 느낌의 그림자 */
  box-shadow: 0 4px 20px rgba(244, 114, 182, 0.4);
}

.cta-primary:hover {
  transform: translateY(-2px) scale(1.03);
  box-shadow: 0 8px 30px rgba(244, 114, 182, 0.5);
}

/* 서브 CTA: 투명 배경 + 테두리 */
.cta-secondary {
  min-height: 52px;
  padding: 0 28px;
  border-radius: var(--radius-pill);
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(8px);
  color: #fff;
  font-size: 16px;
  font-weight: 500;
  font-family: var(--font-display);
  border: 1px solid rgba(255, 255, 255, 0.3);
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.cta-secondary:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-2px);
}

/* ── 섹션 블록 (카테고리/씬 선택 공통) ─────────────────── */
.section-block {
  max-width: var(--container-main);
  margin: 0 auto;
  padding: 32px var(--gutter) 0;
}

.section-title {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: var(--dark);
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-icon {
  font-size: 24px;
}

.section-desc {
  color: var(--text-muted);
  font-size: 14px;
  margin-bottom: 16px;
}

/* ── Pill Toggle (단어/문장 모드 선택) ─────────────────── 
   왜 Pill?
   - select 박스보다 직관적
   - 2개 옵션 전환에 가장 적합한 UI 패턴 */
.pill-toggle {
  display: flex;
  gap: 0;
  /* 배경 트랙: 미선택 영역의 배경색 */
  background: rgba(126, 200, 227, 0.12);
  border-radius: var(--radius-pill);
  padding: 4px;
  width: fit-content;
}

.pill {
  padding: 10px 24px;
  border: none;
  border-radius: var(--radius-pill);
  background: transparent;
  color: var(--text-muted);
  font-size: 15px;
  font-weight: 500;
  font-family: var(--font-display);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 선택된 Pill: 하늘색 배경 + 흰 텍스트 */
.pill.active {
  background: var(--ocean);
  color: #fff;
  /* 선택 시 살짝 올라가는 느낌의 그림자 */
  box-shadow: 0 2px 12px rgba(58, 134, 184, 0.3);
}

.pill:not(.active):hover {
  background: rgba(126, 200, 227, 0.1);
  color: var(--ocean);
}

/* ── Scene 그리드 ────────────────────────────────────── */
.scene-grid {
  display: grid;
  /* auto-fit + minmax: 반응형 그리드의 핵심
     화면 너비에 따라 자동으로 열 수 조정 */
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 16px;
  margin-top: 16px;
}

/* ── Scene 카드 ──────────────────────────────────────── */
.scene-card {
  /* 카드 기본 스타일 */
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(8px);
  border: 2px solid rgba(126, 200, 227, 0.2);
  border-radius: var(--radius-lg);
  overflow: hidden; /* 이미지 둥근 모서리 적용 */
  cursor: pointer;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 0;
  text-align: left;
  display: flex;
  flex-direction: column;
}

/* 호버 효과: 살짝 확대 + 떠오르는 그림자 */
.scene-card:hover {
  transform: translateY(-6px) scale(1.02);
  box-shadow: 0 12px 40px rgba(58, 134, 184, 0.2);
  border-color: var(--sky);
}

/* 선택된 카드: 하늘색 테두리 + 배경 틴트 */
.scene-card.selected {
  border-color: var(--ocean);
  background: rgba(126, 200, 227, 0.08);
  box-shadow: 0 4px 20px rgba(58, 134, 184, 0.2);
}

/* ── Scene 카드 이미지 ───────────────────────────────── */
.scene-image {
  position: relative;
  width: 100%;
  /* aspect-ratio: 이미지 비율 고정 (가로:세로 = 16:10) */
  aspect-ratio: 16 / 10;
  overflow: hidden;
}

.scene-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지 비율 유지하면서 영역 채움 */
  transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 카드 호버 시 이미지 살짝 확대 → 생동감 */
.scene-card:hover .scene-image img {
  transform: scale(1.08);
}

/* 선택 배지: 체크마크 원형 배지 */
.selected-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 28px;
  height: 28px;
  background: var(--ocean);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  /* 나타날 때 스케일 애니메이션 */
  animation: badge-pop 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 8px rgba(58, 134, 184, 0.4);
}

@keyframes badge-pop {
  0% { transform: scale(0); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}

/* ── Scene 카드 정보 ─────────────────────────────────── */
.scene-info {
  padding: 12px 14px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.scene-name {
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 700;
  color: var(--dark);
}

.scene-label {
  font-size: 12px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* ── 시작 섹션 (하단) ────────────────────────────────── */
.start-section {
  max-width: var(--container-main);
  margin: 0 auto;
  padding: 28px var(--gutter) 40px;
  text-align: center;
}

/* 현재 선택 상태 요약 칩 */
.start-summary {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.summary-chip {
  display: inline-block;
  padding: 6px 14px;
  background: rgba(126, 200, 227, 0.12);
  color: var(--ocean);
  border-radius: var(--radius-pill);
  font-size: 13px;
  font-weight: 500;
  font-family: var(--font-display);
}

/* 시작하기 버튼: 크고 눈에 띄는 그라데이션 */
.start-button {
  min-height: 52px;
  padding: 0 48px;
  border-radius: var(--radius-pill);
  background: linear-gradient(135deg, var(--ocean), var(--train));
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  font-family: var(--font-display);
  border: none;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(58, 134, 184, 0.3);
}

.start-button:hover {
  transform: translateY(-3px) scale(1.03);
  box-shadow: 0 8px 30px rgba(58, 134, 184, 0.4);
}

.start-button:active {
  transform: translateY(-1px);
}

/* ── 반응형 (모바일) ──────────────────────────────────── */
@media (max-width: 767px) {
  .hero-title {
    font-size: 32px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  /* 모바일: CTA 버튼 세로 배치 */
  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .cta-primary,
  .cta-secondary {
    width: 100%;
    max-width: 280px;
  }

  /* 모바일에서 카드 2열 */
  .scene-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }

  /* 모바일: 섹션 상단 여백 줄임 */
  .section-block {
    padding-top: 24px;
  }

  .start-button {
    width: 100%;
    max-width: 320px;
    min-height: 52px;
    font-size: 16px;
    padding: 0 32px;
  }
}

/* 아주 작은 화면: 카드 1열 */
@media (max-width: 479px) {
  .scene-grid {
    grid-template-columns: 1fr;
  }
}


</style>
