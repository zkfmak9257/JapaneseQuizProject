<template>
  <!-- ============================================================
       메인 페이지 — 일본 소도시 감성 리디자인 (카테고리 탭 + 카드 버전)

       구조:
       1. Hero Section: 배경 이미지 + 오버레이 + 타이틀 + CTA
       2. 카테고리 선택: Pill Toggle (단어/문장)
       3. 상황 선택: 대분류 탭 칩 → 하위 항목 이미지 카드 그리드
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

    <!-- ── 3. 상황 선택 (대분류 탭 + 하위 카드 그리드) ─────────
         상단: 대분류를 가로 스크롤 칩으로 배치
         하단: 선택된 대분류의 하위 항목을 이미지 카드 그리드로 표시 -->
    <section class="section-block">
      <h2 class="section-title">
        <span class="section-icon">🗺️</span>
        상황 선택
      </h2>
      <p class="section-desc">카테고리를 선택한 뒤, 세부 상황을 골라주세요. 선택하지 않으면 랜덤으로 출제됩니다.</p>

      <!-- ── 대분류 드롭다운 셀렉트 ─────────────────────────
           왜 드롭다운?
           - 카테고리가 많아질수록 가로 공간 절약 가능
           - 모바일에서도 네이티브 select UX 활용
           - 한눈에 현재 선택 상태를 파악하기 쉬움 -->
      <div class="category-dropdown-wrapper">
        <select
          class="category-dropdown"
          :value="activeGroupIndex ?? ''"
          @change="onGroupChange($event)"
        >
          <!-- 기본 옵션: 미선택 상태 → 전체 랜덤 -->
          <option value="" disabled>카테고리를 선택하세요</option>
          <!-- 대분류 목록을 option으로 렌더링 -->
          <option
            v-for="(group, gIdx) in sceneGroups"
            :key="gIdx"
            :value="gIdx"
          >
            {{ group.icon }} {{ group.title }}
          </option>
        </select>
        <!-- 커스텀 화살표 아이콘: 기본 select 화살표를 숨기고 직접 그림 -->
        <span class="dropdown-arrow">
          <svg width="18" height="18" viewBox="0 0 20 20" fill="none">
            <path d="M6 8L10 12L14 8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </span>
      </div>

      <!-- ── 하위 항목 카드 그리드 ───────────────────────── 
           대분류가 선택되었을 때만 표시
           기존 scene-card 스타일을 그대로 재사용 -->
      <div v-if="activeGroupIndex !== null" class="scene-grid">
        <button
          class="scene-card"
          :class="{ selected: selectedSubItem?.groupIdx === activeGroupIndex && selectedSubItem?.itemIdx === iIdx }"
          v-for="(item, iIdx) in sceneGroups[activeGroupIndex].items"
          :key="iIdx"
          @click="toggleSubItem(activeGroupIndex, iIdx)"
        >
          <!-- 카드 이미지: 일본풍 일러스트 + 이모지 오버레이 -->
          <div class="scene-image">
            <img :src="sceneGroups[activeGroupIndex].image" :alt="item.name" loading="lazy" />
            <div class="scene-image-overlay"></div>
            <span class="scene-image-emoji">{{ item.emoji }}</span>
            <div
              v-if="selectedSubItem?.groupIdx === activeGroupIndex && selectedSubItem?.itemIdx === iIdx"
              class="selected-badge"
            >✓</div>
          </div>
          <!-- 카드 정보: 이름 + 상세 설명 -->
          <div class="scene-info">
            <strong class="scene-name">{{ item.name }}</strong>
            <span class="scene-desc" v-if="item.desc">{{ item.desc }}</span>
          </div>
        </button>
      </div>

      <!-- 대분류 미선택 시 안내 메시지 -->
      <div v-else class="empty-state">
        <span class="empty-icon">🗾</span>
        <p class="empty-text">위에서 상황 카테고리를 선택하면<br/>세부 상황이 표시됩니다</p>
      </div>
    </section>

    <!-- ── 4. 시작하기 버튼 (하단 고정형) ───────────────────
         현재 선택 상태를 요약하여 표시 -->
    <section class="start-section">
      <div class="start-summary">
        <!-- 현재 선택된 옵션 표시 -->
        <span class="summary-chip">{{ categoryType === 'WORD' ? '📝 단어' : '💬 문장' }}</span>
        <span class="summary-chip">{{ selectedSceneSummary }}</span>
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

/* ── 카테고리별 이미지 임포트 ─────────────────────────────
   Vite의 import로 빌드 시 해싱된 URL로 자동 변환
   기존 assets 이미지를 카테고리에 맞게 매핑 */
import imgAirport from "../assets/scenes/travel.png";
import imgTransport from "../assets/scenes/travel.png";
import imgHotel from "../assets/scenes/daily-life.png";
import imgFood from "../assets/scenes/restaurant.png";
import imgShopping from "../assets/scenes/shopping.png";
import imgNightlife from "../assets/scenes/business.png";
import imgEmergency from "../assets/scenes/emergency.png";

const router = useRouter();

/* ── 상태(State) 관리 ──────────────────────────────────── */
const categoryType = ref("WORD");

/* ── 대분류 + 하위 항목 데이터 구조 ─────────────────────────
   7개 대분류 × 평균 3~5개 하위 항목 = 총 26개 상황
   
   설계 원칙:
   - 밀러 법칙: 각 대분류 하위 항목 3~5개로 인지 부하 최소화
   - 실용성: 실제 일본 여행에서 빈번히 발생하는 상황 위주
   - sceneId: 백엔드 DB quiz_scenes 테이블에 매핑 */
const sceneGroups = [
  {
    icon: "✈️",
    title: "공항 / 입국·출국",
    sceneId: 1,               // DB: 공항/입국·출국(scene_id=1)
    image: imgAirport,
    items: [
      { emoji: "📍", name: "위치 · 시설 찾기",
        desc: "출국장, 게이트, 환전소, 화장실, 흡연실, 버스 승강장 등" },
      { emoji: "🛂", name: "출입국 심사 응답",
        desc: "체류 목적, 기간, 숙소, 동행 여부 등" },
      { emoji: "🆘", name: "문제 발생 · 도움 요청",
        desc: "티켓 오류, 게이트 변경, 분실, 문의 등" }
    ]
  },
  {
    icon: "🚉",
    title: "교통 / 이동",
    sceneId: 2,               // DB: 교통/이동(scene_id=2)
    image: imgTransport,
    items: [
      { emoji: "🚃", name: "기차 · 지하철 이용",
        desc: "매표, 자동발권기, 플랫폼, 개찰구, 노선 확인" },
      { emoji: "🔄", name: "환승 · 시간 · 길 묻기",
        desc: "환승 위치, 소요 시간, 막차, 방향 확인" },
      { emoji: "🚏", name: "버스 이용",
        desc: "정류장, 하차, 요금, 노선 확인" },
      { emoji: "🚕", name: "택시 이용",
        desc: "목적지 설명, 요금, 경로, 영수증" },
      { emoji: "🚗", name: "렌터카",
        desc: "대여, 반납, 보험, 네비, 좌측통행, ETC 카드" }
    ]
  },
  {
    icon: "🏨",
    title: "숙박",
    sceneId: 3,
    image: imgHotel,
    items: [
      { emoji: "🛎️", name: "예약 확인 · 체크인",
        desc: "예약명 확인, 숙박 일정, 기본 안내" },
      { emoji: "🛏️", name: "객실 요청",
        desc: "룸 변경, 추가 요청, 침대 타입 등" },
      { emoji: "🔧", name: "문제 해결",
        desc: "시설 고장, 분실물, 소음 등" },
      { emoji: "💳", name: "체크아웃 · 결제",
        desc: "결제, 영수증, 짐 보관" }
    ]
  },
  {
    icon: "🍣",
    title: "음식 / 술",
    sceneId: 4,               // DB: 식당(scene_id=4)
    image: imgFood,
    items: [
      { emoji: "🪑", name: "입장 · 자리 · 대기",
        desc: "인원, 예약, 대기, 자리 요청" },
      { emoji: "📋", name: "주문 · 추천 · 요청",
        desc: "추천, 옵션 변경, 알레르기, 추가 요청" },
      { emoji: "💰", name: "계산",
        desc: "결제, 영수증, 분할 계산 등" },
      { emoji: "🍶", name: "이자카야 · 바",
        desc: "술 주문, 자리 이동, 이용 문의" }
    ]
  },
  {
    icon: "🏪",
    title: "쇼핑 / 상점",
    sceneId: 5,               // DB: 쇼핑(scene_id=5)
    image: imgShopping,
    items: [
      { emoji: "🏪", name: "편의점 · 계산",
        desc: "결제, 봉투, 포장, 기본 문의" },
      { emoji: "💊", name: "드럭스토어 · 약국",
        desc: "인기 상품 추천, 화장품 문의, 복용 안내" },
      { emoji: "👔", name: "의류 · 피팅",
        desc: "사이즈, 색상, 착용, 교환" },
      { emoji: "🛍️", name: "면세점",
        desc: "여권 확인, 면세 처리, 수령 안내" },
      { emoji: "📱", name: "전자제품 매장",
        desc: "호환성, 전압, 환불/교환" }
    ]
  },
  {
    icon: "🌙",
    title: "야간 / 즐길거리",
    sceneId: 6,               // DB: 야간/즐길거리(scene_id=6)
    image: imgNightlife,
    items: [
      { emoji: "🎶", name: "클럽 · 입장 문의",
        desc: "입장 가능 여부, 요금, 드레스코드" },
      { emoji: "🍺", name: "펍 · 바",
        desc: "이용 안내, 자리, 주문" },
      { emoji: "🎤", name: "카라오케",
        desc: "예약, 요금, 시간 연장, 음료 주문" },
      { emoji: "🕹️", name: "오락실(ゲーセン)",
        desc: "이용 방법, 환전, 경품 교환" },
      { emoji: "♨️", name: "온천 · 목욕탕(銭湯)",
        desc: "이용 절차, 에티켓, 요금, 타월 대여" }
    ]
  },
  {
    icon: "🚨",
    title: "긴급 상황",
    sceneId: 7,               // DB: 긴급상황(scene_id=7)
    image: imgEmergency,
    items: [
      { emoji: "🏥", name: "병원",
        desc: "접수, 증상 설명, 결제" },
      { emoji: "🚔", name: "경찰 · 신고",
        desc: "분실, 도난, 신고 절차" },
      { emoji: "📞", name: "긴급 공통 도움 요청",
        desc: "위치 설명, 통역 요청, 구조 요청" }
    ]
  },
  {
    icon: "🏛️",
    title: "관광지 / 명소",
    sceneId: 8,               // DB: 관광지/명소(scene_id=8)
    image: imgAirport,        // 관광 이미지 재활용
    items: [
      { emoji: "🎫", name: "입장권 · 예약",
        desc: "티켓 구매, 할인, 사전 예약 확인" },
      { emoji: "📸", name: "사진 촬영 · 매너",
        desc: "촬영 가능 여부, 삼각대, 플래시 사용" },
      { emoji: "🗣️", name: "가이드 · 안내 문의",
        desc: "오디오 가이드, 투어 시간, 추천 코스" }
    ]
  }
];

// activeGroupIndex: 현재 선택된 대분류 탭 인덱스 (null이면 미선택)
const activeGroupIndex = ref(null);

// selectedSubItem: 현재 선택된 하위 항목 { groupIdx, itemIdx }
// null이면 미선택 → 전체(랜덤) 모드
const selectedSubItem = ref(null);

/* ── Computed 속성 ─────────────────────────────────────── */

// selectedSceneId: 선택된 하위 항목의 대분류에 매핑된 DB sceneId
const selectedSceneId = computed(() => {
  if (selectedSubItem.value === null) return null;
  return sceneGroups[selectedSubItem.value.groupIdx].sceneId;
});

// selectedSceneSummary: 하단 요약 영역에 표시할 텍스트
const selectedSceneSummary = computed(() => {
  if (selectedSubItem.value === null) return "🗺️ 전체(랜덤)";
  const group = sceneGroups[selectedSubItem.value.groupIdx];
  const item = group.items[selectedSubItem.value.itemIdx];
  return `${group.icon} ${item.name}`;
});

/* ── 이벤트 핸들러 ─────────────────────────────────────── */

// onGroupChange: 드롭다운에서 대분류 선택 시 호출
// event.target.value가 문자열이므로 Number()로 변환
function onGroupChange(event) {
  const val = event.target.value;
  if (val === "") {
    // 빈 값 선택 → 전체 모드
    activeGroupIndex.value = null;
    selectedSubItem.value = null;
  } else {
    activeGroupIndex.value = Number(val);
    // 대분류 변경 시 이전 하위 선택 해제
    selectedSubItem.value = null;
  }
}

// toggleSubItem: 하위 항목 카드 클릭 시 선택/해제 토글
function toggleSubItem(gIdx, iIdx) {
  if (
    selectedSubItem.value &&
    selectedSubItem.value.groupIdx === gIdx &&
    selectedSubItem.value.itemIdx === iIdx
  ) {
    // 같은 카드 재클릭 → 선택 해제
    selectedSubItem.value = null;
  } else {
    // 새로운 카드 선택
    selectedSubItem.value = { groupIdx: gIdx, itemIdx: iIdx };
  }
}

// onStart: 현재 설정으로 퀴즈 시작
function onStart() {
  router.push({
    path: "/quiz/start",
    query: {
      questionType: categoryType.value,
      sceneId: selectedSceneId.value == null ? undefined : String(selectedSceneId.value)
    }
  });
}

// onStartTravel: 여행 모드(sceneId=2: 교통/이동) 바로 시작
function onStartTravel() {
  categoryType.value = "WORD";
  router.push({
    path: "/quiz/start",
    query: {
      questionType: "WORD",
      sceneId: "2"
    }
  });
}
</script>

<style scoped>
/* ============================================================
   MainView — 일본 소도시 감성 (카테고리 탭 + 카드 버전)
   ============================================================ */

/* ── 전체 컨테이너 ───────────────────────────────────── */
.quiz-home {
  --hero-height: var(--hero-height-main-compact);
  margin: -24px calc(-1 * var(--gutter)) 0;
}

/* ── Hero Section ────────────────────────────────────── */
.hero {
  position: relative;
  background-image: url("../assets/hero-bg.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: var(--hero-height, var(--hero-height-default));
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

/* ── 구름 애니메이션 ─────────────────────────────────── */
.clouds {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.cloud {
  position: absolute;
  font-size: 60px;
  opacity: 0.4;
  color: white;
  text-shadow: 0 0 40px rgba(255, 255, 255, 0.5);
  animation: float-cloud linear infinite;
}

.cloud-1 {
  top: 10%;
  font-size: 80px;
  opacity: 0.3;
  animation-duration: 30s;
}

.cloud-2 {
  top: 25%;
  font-size: 50px;
  opacity: 0.5;
  animation-duration: 22s;
  animation-delay: -8s;
}

.cloud-3 {
  top: 5%;
  font-size: 40px;
  opacity: 0.35;
  animation-duration: 26s;
  animation-delay: -15s;
}

@keyframes float-cloud {
  0% { transform: translateX(-120px); }
  100% { transform: translateX(calc(100vw + 120px)); }
}

/* ── Hero 오버레이 ───────────────────────────────────── */
.hero-overlay {
  position: absolute;
  inset: 0;
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
  z-index: 3;
  text-align: center;
  color: #fff;
  padding: 48px var(--gutter);
}

.hero-eyebrow {
  font-family: var(--font-display);
  font-size: 13px;
  letter-spacing: 4px;
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

.cta-primary {
  min-height: 52px;
  padding: 0 28px;
  border-radius: var(--radius-pill);
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
  box-shadow: 0 4px 20px rgba(244, 114, 182, 0.4);
}

.cta-primary:hover {
  transform: translateY(-2px) scale(1.03);
  box-shadow: 0 8px 30px rgba(244, 114, 182, 0.5);
}

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

/* ── 섹션 블록 ───────────────────────────────────────── */
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

/* ── Pill Toggle (단어/문장) ─────────────────────────── */
.pill-toggle {
  display: flex;
  gap: 0;
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

.pill.active {
  background: var(--ocean);
  color: #fff;
  box-shadow: 0 2px 12px rgba(58, 134, 184, 0.3);
}

.pill:not(.active):hover {
  background: rgba(126, 200, 227, 0.1);
  color: var(--ocean);
}

/* ============================================================
   대분류 탭 + 하위 카드 그리드
   
   왜 탭 방식?
   - 대분류를 먼저 선택 → 하위 항목 카드가 나타남
   - 기존의 이미지 카드 UI 형식을 유지하면서
     많은 하위 항목을 논리적으로 분류할 수 있음
   ============================================================ */

/* ── 대분류 드롭다운 셀렉트 ────────────────────────────── 
   왜 커스텀 드롭다운?
   - 기본 select 스타일은 브라우저마다 다름
   - 디자인 시스템에 맞는 일관된 스타일 보장
   - appearance: none으로 기본 화살표를 숨기고 커스텀 화살표 사용 */
.category-dropdown-wrapper {
  position: relative;
  width: 100%;
  max-width: 400px;
  margin-bottom: 20px;
}

.category-dropdown {
  /* 기본 select 스타일 초기화 */
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  /* 커스텀 스타일 적용 */
  width: 100%;
  padding: 14px 48px 14px 20px;  /* 오른쪽: 화살표 공간 확보 */
  border: 2px solid rgba(126, 200, 227, 0.25);
  border-radius: var(--radius-md);
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(8px);
  color: var(--dark);
  font-size: 16px;
  font-weight: 600;
  font-family: var(--font-display);
  cursor: pointer;
  outline: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 포커스/호버 시 하늘색 테두리 + 그림자 */
.category-dropdown:hover {
  border-color: var(--sky);
}

.category-dropdown:focus {
  border-color: var(--ocean);
  box-shadow: 0 0 0 3px rgba(58, 134, 184, 0.15);
}

/* 커스텀 화살표 아이콘 위치 */
.dropdown-arrow {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-muted);
  pointer-events: none;  /* 클릭 이벤트를 select로 전달 */
  display: flex;
  align-items: center;
  transition: color 0.2s ease;
}

/* select 포커스 시 화살표 색 변경 */
.category-dropdown:focus + .dropdown-arrow {
  color: var(--ocean);
}

/* ── Scene 카드 그리드 (기존 레이아웃 유지) ────────────── */
.scene-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 16px;
  margin-top: 4px;
  /* 카드 등장 시 부드러운 페이드인 애니메이션 */
  animation: cards-fade-in 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes cards-fade-in {
  0% { opacity: 0; transform: translateY(12px); }
  100% { opacity: 1; transform: translateY(0); }
}

/* ── Scene 카드 (기존 디자인 유지) ────────────────────── */
.scene-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(8px);
  border: 2px solid rgba(126, 200, 227, 0.2);
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 0;
  text-align: left;
  display: flex;
  flex-direction: column;
}

.scene-card:hover {
  transform: translateY(-6px) scale(1.02);
  box-shadow: 0 12px 40px rgba(58, 134, 184, 0.2);
  border-color: var(--sky);
}

.scene-card.selected {
  border-color: var(--ocean);
  background: rgba(126, 200, 227, 0.08);
  box-shadow: 0 4px 20px rgba(58, 134, 184, 0.2);
}

/* ── Scene 카드 이미지 영역 (일본풍 이미지 + 오버레이) ────── */
.scene-image {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
  overflow: hidden;
}

/* 이미지: object-fit으로 비율 유지하며 영역 채움 */
.scene-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 호버 시 이미지 살짝 확대 → 생동감 있는 인터랙션 */
.scene-card:hover .scene-image img {
  transform: scale(1.08);
}

/* 이미지 위 어두운 그라데이션: 이모지 가독성 보장 */
.scene-image-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg,
    rgba(0,0,0,0.05) 0%,
    rgba(0,0,0,0.25) 100%
  );
  pointer-events: none;
}

/* 이모지 오버레이: 이미지 좌하단에 배치 */
.scene-image-emoji {
  position: absolute;
  bottom: 10px;
  left: 12px;
  font-size: 28px;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.3));
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
  gap: 4px;
}

.scene-name {
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 700;
  color: var(--dark);
  line-height: 1.3;
}

/* desc: 카드 하단에 세부 상황 설명을 회색 소텍스트로 표시
   인지과학 — 정보 청킹: 이름(핵심) + 설명(보조)으로 2단계 제공 */
.scene-desc {
  font-size: 12px;
  color: var(--text-muted);
  line-height: 1.4;
  /* 2줄까지만 표시, 넘으면 ... 처리 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* ── 빈 상태 (대분류 미선택 시) ──────────────────────── */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 20px;
  background: rgba(126, 200, 227, 0.04);
  border: 2px dashed rgba(126, 200, 227, 0.2);
  border-radius: var(--radius-lg);
  margin-top: 4px;
}

.empty-icon {
  font-size: 36px;
  margin-bottom: 12px;
  /* 살짝 위아래로 바운스하는 애니메이션 */
  animation: bounce-hint 1.5s ease-in-out infinite;
}

@keyframes bounce-hint {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.empty-text {
  color: var(--text-muted);
  font-size: 15px;
  font-family: var(--font-display);
  font-weight: 500;
}

/* ── 시작 섹션 (하단) ────────────────────────────────── */
.start-section {
  max-width: var(--container-main);
  margin: 0 auto;
  padding: 28px var(--gutter) 40px;
  text-align: center;
}

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

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .cta-primary,
  .cta-secondary {
    width: 100%;
    max-width: 280px;
  }

  /* 모바일: 드롭다운 풀 너비 */
  .category-dropdown-wrapper {
    max-width: 100%;
  }

  .category-dropdown {
    font-size: 15px;
    padding: 12px 44px 12px 16px;
  }

  /* 모바일에서 카드 2열 */
  .scene-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }

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
