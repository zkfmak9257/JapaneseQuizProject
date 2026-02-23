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

    <!-- ── 1. Hero Section (보딩패스 / 여권 테마 + Slow Zoom Anim) ── -->
    <div class="hero ticket-hero">
      <!-- 🎬 영상 배경 1: 빈 공간을 채우는 꽉 찬 블러 배경 -->
      <video 
        ref="heroVideoBlurRef"
        class="hero-video-bg-blur" 
        :src="currentSeason.src" 
        autoplay 
        loop 
        muted 
        playsinline
      ></video>

      <!-- 🎬 영상 배경 2: 잘림 없이 원본 비율을 유지하는 메인 비디오 -->
      <video 
        ref="heroVideoRef"
        class="hero-video-bg" 
        :src="currentSeason.src" 
        autoplay 
        loop 
        muted 
        playsinline
      ></video>
      <div class="stamp-pattern"></div>
      <div class="hero-overlay"></div>

      <!-- 🌸 계절 테마 선택기 (우측 하단) -->
      <div class="season-selector">
        <button 
          v-for="season in seasons" 
          :key="season.id"
          class="season-btn"
          :class="{ active: currentSeason.id === season.id }"
          @click="selectSeason(season)"
        >
          {{ season.icon }} {{ season.name }}
        </button>
      </div>
      
      <!-- 텍스트 콘텐츠 (Fade-in 효과 적용) -->
      <div class="hero-content hero-content-anim">
      </div>
    </div>

    <!-- ── 2. 여정 선택 (모드 & 상황) ───────── -->
    <section class="section-block situation-selection">
      
      <div class="selection-header-layout">
        <div>
          <h2 class="section-title">
            <span class="section-icon">🗺️</span>
            여정 선택
          </h2>
          <p class="section-desc">모드를 선택하고, 즉시 떠나고 싶은 상황 카드를 클릭하세요!</p>
        </div>
        
        <!-- 모드 선택 (여기로 이동 = 흐름 통일) -->
        <div class="mode-toggle-wrapper">
          <div class="pill-toggle">
            <button :class="['pill', { active: categoryType === 'WORD' }]" @click="categoryType = 'WORD'">
              📝 단어
            </button>
            <button :class="['pill', { active: categoryType === 'SENTENCE' }]" @click="categoryType = 'SENTENCE'">
              💬 문장
            </button>
          </div>
        </div>
      </div>

      <!-- 검색창 -->
      <div class="search-box">
        <span class="search-icon">🔍</span>
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="어떤 상황을 연습할까요? (예: 게이트, 체크인, 경찰)"
        />
        <button v-if="searchQuery" class="clear-btn" @click="searchQuery = ''">✕</button>
      </div>

      <div class="selection-container">
        <!-- 대분류 탭 (스크롤) -->
        <div class="category-tabs-wrapper" v-show="!searchQuery">
          <!-- 스크롤 컨테이너 밖의 고정 네비게이션 버튼 -->
          <button class="tab-nav-btn left" @click="scrollTabs(-250)" aria-label="이전 탭">◀︎</button>
          
          <div class="category-tabs" @wheel="onTabWheel" ref="tabsContainerRef">
            <!-- ✨ 전체 보기 (선택/비선택 시 다른 버튼들과 위계 분리) -->
            <button 
              class="tab-item tab-all" 
              :class="{ active: activeGroupIndex === null }" 
              @click="onCategoryTabClick(null, $event)"
            >
              🗺️ 전체 보기
            </button>
            <button 
              v-for="(group, gIdx) in sceneGroups" 
              :key="gIdx"
              class="tab-item"
              :class="{ active: activeGroupIndex === gIdx }"
              :style="activeGroupIndex === gIdx ? { backgroundColor: group.theme, borderColor: group.theme, color: 'white', boxShadow: '0 4px 12px ' + group.theme + '66, 0 0 0 1px ' + group.theme } : {}"
              @click="onCategoryTabClick(gIdx, $event)"
            >
              {{ group.icon }} {{ group.title }}
            </button>
            <div class="tab-spacer"></div>
          </div>

          <button class="tab-nav-btn right" @click="scrollTabs(250)" aria-label="다음 탭">▶︎</button>
        </div>

        <!-- 하위 항목 패널 -->
        <div class="subcategory-panel">
          
          <!-- 검색 시 피드백 -->
          <div class="search-feedback" v-if="searchQuery">
            <p>검색 결과: <strong>{{ displayItems.length }}</strong>건</p>
          </div>

          <div v-if="searchQuery && displayItems.length === 0" class="empty-state">
            <span class="empty-icon">😥</span>
            <p class="empty-text">검색 결과가 없습니다.</p>
          </div>
          <div v-else class="scene-grid-container">
            
            <!-- ── 4. Passport Status Card (Inline Sticky: Boarding Pass Theme) ─────────────────── -->
            <transition name="slide-down">
              <div class="passport-status-wrapper" v-if="selectedSubItem !== null">
                <div class="boarding-pass-card">
                  <!-- 좌측: 티켓 정보 -->
                  <div class="bpc-left">
                    <div class="bpc-header">
                      <span class="bpc-title">🛂 BOARDING PASS</span>
                      <button class="bpc-close-btn" @click="selectedSubItem = null" aria-label="선택 취소">✕</button>
                    </div>
                    <div class="bpc-body">
                      <div class="bpc-row">
                        <div class="bpc-field">
                          <span class="bpc-label">MODE</span>
                          <span class="bpc-value">{{ categoryType === 'WORD' ? '📝 단어' : '💬 문장' }}</span>
                        </div>
                        <div class="bpc-field">
                          <span class="bpc-label">DESTINATION</span>
                          <span class="bpc-value">{{ selectedGroupTitle }}</span>
                        </div>
                      </div>
                      <div class="bpc-field">
                        <span class="bpc-label">MISSION</span>
                        <!-- 강화된 선택 상태 시각화 -->
                        <div class="bpc-mission-highlight">
                          <span class="bpc-value">{{ selectedItemName }}</span>
                          <span class="bpc-badge-ready">READY</span>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 절취선 분리선 -->
                  <div class="bpc-divider"></div>
                  
                  <!-- 우측: 액션 및 바코드 -->
                  <div class="bpc-right">
                    <div class="bpc-barcode"></div>
                    <button class="bpc-cta-start" @click="onStartSubItem">
                      <span class="cta-icon">✈️</span>
                      <span class="cta-text">여정 시작하기</span>
                    </button>
                  </div>
                </div>
              </div>
            </transition>

            <div class="scene-grid">
              
              <!-- 상황 카드 렌더링 (클릭 시 선택 상태 토글) -->
              <button
                class="scene-card"
                :class="{ selected: selectedSubItem?.groupIdx === item.gIdx && selectedSubItem?.itemIdx === item.iIdx }"
                v-for="item in displayItems"
                :key="`${item.gIdx}-${item.iIdx}`"
                @click="selectDisplayItem(item)"
              >
                <div class="scene-image">
                  <img :src="item.image" :alt="item.name" loading="lazy" />
                  <div class="scene-image-overlay"></div>
                  <!-- 선택 배지 -->
                  <div v-if="selectedSubItem?.groupIdx === item.gIdx && selectedSubItem?.itemIdx === item.iIdx" class="selected-badge">✓</div>
                </div>
                <div class="scene-info">
                  <div class="scene-badge" v-if="searchQuery || activeGroupIndex === null" :style="{ color: item.theme || 'var(--ocean)', backgroundColor: (item.theme || 'var(--ocean)') + '26' }">
                    {{ item.groupIcon }} {{ item.groupTitle }}
                  </div>
                  <strong class="scene-name">{{ item.name }}</strong>
                  <span class="scene-desc" v-if="item.desc">{{ item.desc }}</span>
                </div>
              </button>
              
              <!-- '전체' 탭일 때만 보이는 무작위 챌린지 특별 카드 (맨 하단 배치) -->
              <button 
                v-if="activeGroupIndex === null && !searchQuery" 
                class="scene-card random-card" 
                @click="onStart"
              >
                <div class="random-content">
                  <span class="random-emoji">🎲</span>
                  <div class="random-text">
                    <h3>🎲 무작위 챌린지 (전체 랜덤)</h3>
                    <p>어떤 상황이 나올지 모르는 서바이벌 모드!</p>
                  </div>
                  <span class="random-arrow">→</span>
                </div>
              </button>

            </div> <!-- closes .scene-grid -->
          </div> <!-- closes .scene-grid-container -->
        </div> <!-- closes .subcategory-panel -->
      </div> <!-- closes .selection-container -->
    </section>

  </section>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";

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
const route = useRoute();

const tabsContainerRef = ref(null);

function scrollTabs(amount) {
  if (tabsContainerRef.value) {
    tabsContainerRef.value.scrollBy({ left: amount, behavior: 'smooth' });
  }
}

/* ── 히어로 영상 테마 (계절) 관리 ──────────────────── */
const heroVideoRef = ref(null);
const heroVideoBlurRef = ref(null);

const seasons = [
  { id: 'main', icon: '🏠', name: '기본', src: '/bg/main.mp4' },
  { id: 'spring', icon: '🌸', name: '봄', src: '/bg/spring.mp4' },
  { id: 'summer', icon: '🌻', name: '여름', src: '/bg/summer.mp4' },
  { id: 'autumn', icon: '🍁', name: '가을', src: '/bg/autumn.mov' },
  { id: 'winter', icon: '❄️', name: '겨울', src: '/bg/winter.mp4' }
];

const currentSeason = ref(seasons[0]);

function selectSeason(season) {
  currentSeason.value = season;
}

// 테마 변경 시 비디오 소스를 다시 로드하고 재생 시작 보장
watch(currentSeason, () => {
  if (heroVideoRef.value) {
    heroVideoRef.value.load();
    heroVideoRef.value.play().catch(err => console.log('Video autoplay prevented:', err));
  }
  if (heroVideoBlurRef.value) {
    heroVideoBlurRef.value.load();
    heroVideoBlurRef.value.play().catch(err => console.log('Blur video autoplay prevented:', err));
  }
});

/* ── 상태(State) 관리 ──────────────────────────────────── */
const categoryType = ref("WORD");
const searchQuery = ref("");

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
    theme: "#38bdf8", /* sky-400 */
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
    theme: "#14b8a6", /* teal-500 */
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
    theme: "#6366f1", /* indigo-500 */
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
    theme: "#f97316", /* orange-500 */
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
    theme: "#ec4899", /* pink-500 */
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
    theme: "#8b5cf6", /* violet-500 */
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
    theme: "#ef4444", /* red-500 */
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
    theme: "#06b6d4", /* cyan-500 */
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

/* ── Computed (검색 및 표시용 데이터) ────────────────────── */
const allSubItems = computed(() => {
  return sceneGroups.flatMap((group, gIdx) => 
    group.items.map((item, iIdx) => ({
      ...item, 
      gIdx, 
      iIdx, 
      groupTitle: group.title,
      groupIcon: group.icon,
      image: group.image,
      theme: group.theme
    }))
  );
});

const displayItems = computed(() => {
  if (searchQuery.value.trim()) {
    const q = searchQuery.value.trim().toLowerCase();
    return allSubItems.value.filter(item => 
      item.name.toLowerCase().includes(q) || 
      item.desc.toLowerCase().includes(q) ||
      item.groupTitle.toLowerCase().includes(q)
    );
  } else {
    // 탭 선택: null이면 모든 아이템 표시, 아니면 해당 탭의 아이템만 표시
    if (activeGroupIndex.value === null) {
      return allSubItems.value;
    }
    const group = sceneGroups[activeGroupIndex.value];
    return group.items.map((item, iIdx) => ({
      ...item, gIdx: activeGroupIndex.value, iIdx, 
      groupTitle: group.title, groupIcon: group.icon, image: group.image, theme: group.theme
    }));
  }
});

/* ── 딥링크(URL 동기화) 로직 ────────────────────────────── */
onMounted(() => {
  const qType = route.query.questionType;
  const qCategory = route.query.category;
  const qSituation = route.query.situation;
  
  if (qType === 'WORD' || qType === 'SENTENCE') {
    categoryType.value = qType;
  }
  
  if (qCategory !== undefined) {
    const gIdx = Number(qCategory);
    if (gIdx >= 0 && gIdx < sceneGroups.length) {
      activeGroupIndex.value = gIdx;
      
      if (qSituation !== undefined) {
        const iIdx = Number(qSituation);
        if (iIdx >= 0 && iIdx < sceneGroups[gIdx].items.length) {
          selectedSubItem.value = { groupIdx: gIdx, itemIdx: iIdx };
        }
      }
    }
  }
});

watch([categoryType, activeGroupIndex, selectedSubItem], () => {
  router.replace({
    query: {
      ...route.query,
      questionType: categoryType.value,
      category: activeGroupIndex.value ?? undefined,
      situation: selectedSubItem.value?.itemIdx ?? undefined
    }
  }).catch(() => {});
});

/* ── Computed 속성 ─────────────────────────────────────── */

const selectedSceneId = computed(() => {
  if (selectedSubItem.value === null) return null;
  return sceneGroups[selectedSubItem.value.groupIdx].sceneId;
});

const activeGroupTitle = computed(() => {
  if (activeGroupIndex.value === null) return null;
  const group = sceneGroups[activeGroupIndex.value];
  return `${group.icon} ${group.title}`;
});

const selectedGroupTitle = computed(() => {
  if (!selectedSubItem.value) return "";
  const group = sceneGroups[selectedSubItem.value.groupIdx];
  return `${group.icon} ${group.title}`;
});

const selectedItemName = computed(() => {
  if (!selectedSubItem.value) return "";
  const group = sceneGroups[selectedSubItem.value.groupIdx];
  return group.items[selectedSubItem.value.itemIdx].name;
});

/* ── 이벤트 핸들러 ─────────────────────────────────────── */

function onCategoryTabClick(idx, event) {
  activeGroupIndex.value = idx;
  
  // 선택된 탭을 자연스럽게 화면 중앙으로 스크롤 이동
  if (event && event.currentTarget) {
    event.currentTarget.scrollIntoView({ behavior: 'smooth', inline: 'center', block: 'nearest' });
  }
}

// PC 환경에서 마우스 휠로 가로 스크롤을 지원
function onTabWheel(event) {
  // 세로(Y) 휠 움직임이 가로(X)보다 클 때만 가로 스크롤로 변환
  if (Math.abs(event.deltaY) > Math.abs(event.deltaX)) {
    event.preventDefault(); // 페이지 세로 스크롤 방지
    event.currentTarget.scrollLeft += event.deltaY;
  }
}

// 카드를 누르면 즉시 시작이 아닌, 여행 정보 세팅(선택)
function selectDisplayItem(item) {
  if (selectedSubItem.value && selectedSubItem.value.groupIdx === item.gIdx && selectedSubItem.value.itemIdx === item.iIdx) {
    selectedSubItem.value = null; // 이미 선택된 항목 누르면 해제
  } else {
    selectedSubItem.value = { groupIdx: item.gIdx, itemIdx: item.iIdx };
  }
}

// 실제 '여정 시작하기' 버튼 (패스포트 카드 내부)
function onStartSubItem() {
  if (!selectedSubItem.value) return;
  router.push({
    path: "/quiz/start",
    query: {
      questionType: categoryType.value,
      sceneId: String(sceneGroups[selectedSubItem.value.groupIdx].sceneId)
    }
  });
}

// onStart: '전체' 탭 무작위 챌린지 전용 버튼
function onStart() {
  router.push({
    path: "/quiz/start",
    query: {
      questionType: categoryType.value,
      // sceneId 전달안함 -> 서버에서 전체 랜덤 출제
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
  min-height: var(--hero-height, var(--hero-height-default));
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

/* ── Hero Section 영상 배경 ────────────────────────────────────── */

/* 1. 뒷단 몽환적 블러 배경 (빈 틈을 채움) */
.hero-video-bg-blur {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: blur(24px) saturate(1.2) brightness(0.7);
  transform: scale(1.1); /* 블러 테두리가 하얗게 뜨는 현상 방지 */
  z-index: 0;
}

/* 2. 앞단 원본 비율 유지 영상 (너무 크게 크롭되지 않게) */
.hero-video-bg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: contain; /* cover 대신 contain 적용으로 100% 원본 노출 */
  filter: saturate(1.1) brightness(0.9);
  z-index: 1; /* 블러 배경보다 위 */
}

/* 히어로 오버레이 밑으로 깔림 보장 */
.stamp-pattern, .hero-overlay {
  z-index: 2; /* 비디오 2개보다 위에 오도록 승격 */
}

/* ── 계절 테마 선택기 (Hero 우측 하단 둥둥) ── */
.season-selector {
  position: absolute;
  right: 24px;
  bottom: 24px;
  z-index: 10;
  display: flex;
  gap: 8px;
  background: rgba(26, 45, 61, 0.4);
  backdrop-filter: blur(8px);
  padding: 6px;
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.season-btn {
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  padding: 6px 12px;
  font-size: 13px;
  font-weight: 600;
  border-radius: 999px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.season-btn:hover {
  color: white;
  background: rgba(255, 255, 255, 0.15);
}

.season-btn.active {
  background: white;
  color: var(--dark);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transform: scale(1.05);
  font-weight: 800;
}

/* 히어로 텍스트 컨텐츠 등장 애니메이션 */
.hero-content-anim {
  position: relative;
  z-index: 2;
  animation: fadeUp 1s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}
@keyframes fadeUp {
  0% { opacity: 0; transform: translateY(15px); }
  100% { opacity: 1; transform: translateY(0); }
}

/* 탑승 준비 완료 깜빡임 효과 */
.animate-pulse-subtle {
  animation: pulseSubtle 3s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}
@keyframes pulseSubtle {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.6; }
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

/* ── Hero 오버레이 (다층 그라데이션 + 브랜드 틴트 + 인터랙션 방지) ── */
.hero-overlay {
  position: absolute;
  inset: 0;
  /* 
    1) 상단 강(타이틀 가독성), 중앙 약(영상 본연의 색 유지), 하단 무(답답함 해소)
    2) 양 가장자리 미세 비네팅(Vignette)으로 시선을 중앙 집중
    3) 브랜드 틴트: 딥 인디고(26, 45, 61) 적용으로 통일감 부여
  */
  background: 
    linear-gradient(
      180deg,
      rgba(26, 45, 61, 0.45) 0%,      /* 상단 가장 어둡게 */
      rgba(26, 45, 61, 0.15) 35%,     /* 상단~중앙 자연스럽게 풀림 */
      rgba(26, 45, 61, 0.05) 70%,     /* 중앙 메인 디테일 보존 */
      rgba(26, 45, 61, 0.0) 100%      /* 하단은 투명하게 (답답함 해소) */
    ),
    radial-gradient(
      circle at center,
      transparent 40%,
      rgba(26, 45, 61, 0.2) 150%      /* 외곽 약한 비네팅 터치 */
    );
  
  /* 마우스/터치 인터랙션을 하위 계절 버튼, 스크롤 등에 온전히 통과시킴 */
  pointer-events: none;
  
  /* 부모 컨테이너(hero)와 경계가 일치하도록 곡률 상속 */
  border-radius: inherit;
  z-index: 2;
}

/* ── 스탬프 패턴 (여권 감성) ──────────────────────────── */
.stamp-pattern {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255, 255, 255, 0.1) 2px, transparent 2px);
  background-size: 30px 30px;
  opacity: 0.15;
  z-index: 1;
}

/* ── Hero 콘텐츠 ─────────────────────────────────────── */
.hero-content {
  position: relative;
  z-index: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px var(--gutter);
  width: 100%;
}

/* ── 보딩 패스 카드 ───────────────────────────────────── */
.boarding-pass {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(12px);
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.5);
  animation: slideDownTicket 0.8s cubic-bezier(0.16, 1, 0.3, 1);
  color: var(--dark);
}

@keyframes slideDownTicket {
  from { opacity: 0; transform: translateY(-40px) scale(0.95); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

.bp-header {
  background: var(--ocean-deep);
  color: white;
  padding: 12px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 2px;
}
.bp-class { color: var(--crossing); }

.bp-body {
  padding: 32px 24px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.bp-label {
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 700;
  letter-spacing: 2px;
  margin-bottom: 12px;
  text-transform: uppercase;
}
.bp-dest {
  font-family: var(--font-display);
  font-size: clamp(24px, 5vw, 36px);
  font-weight: 900;
  color: var(--dark);
  margin-bottom: 20px;
  letter-spacing: -0.5px;
}
.bp-mission {
  font-size: 15px;
  color: var(--ocean);
  font-weight: 600;
  background: rgba(126, 200, 227, 0.15);
  padding: 8px 16px;
  border-radius: var(--radius-pill);
}

.bp-footer { display: flex; justify-content: flex-end; align-items: center; background: rgba(0,0,0,0.02); padding: 12px 24px; border-top: 1px dashed rgba(0,0,0,0.1); }
.boarding-time { font-family: var(--font-display); font-weight: 800; font-size: 14px; color: var(--ocean); letter-spacing: 1px; }

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

.selection-header-layout {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}
.section-title {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 800;
  color: var(--dark);
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.section-icon { font-size: 28px; }
.section-desc { color: var(--text-muted); font-size: 15px; margin-bottom: 0; }

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
   상황 선택 UI (검색 + 카테고리 탭 형태)
   ============================================================ */
.situation-selection {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-box {
  position: relative;
  width: 100%;
}
.search-box input {
  width: 100%;
  padding: 16px 48px 16px 44px;
  border-radius: var(--radius-md);
  border: 2px solid rgba(126, 200, 227, 0.25);
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(8px);
  font-size: 15px;
  font-weight: 600;
  color: var(--dark);
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}
.search-box input:focus {
  border-color: var(--ocean);
  box-shadow: 0 0 0 3px rgba(58, 134, 184, 0.15);
}
.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px;
  color: #94a3b8;
}
.clear-btn {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  background: #e2e8f0;
  color: #64748b;
  border: none;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  font-size: 12px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}
.clear-btn:hover { background: #cbd5e1; color: var(--dark); }

.category-tabs-wrapper {
  margin-bottom: 20px;
  position: relative;
  /* 버튼이 살짝 밖으로 나가도록 여백 허용 */
  margin-left: -8px; 
  margin-right: -8px;
  padding-left: 8px;
  padding-right: 8px;
}

.category-tabs {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  /* 부모 box-shadow나 border가 잘리지 않게 위아래 패딩 확보 및 
     좌/우 화살표 버튼과 버튼이 겹치지 않게 충분한 좌우 여백(안전 영역) 더 넓게 확보 */
  padding: 4px 48px 12px 48px;
  -ms-overflow-style: none; /* IE, Edge 스크롤바 완전 숨김 */
  scrollbar-width: none; /* Firefox 스크롤바 완전 숨김 */
  scroll-behavior: smooth;
  /* 오른쪽 끝으로 갈수록 페이드스루(스며들듯 사라짐) 마스크 효과 */
  -webkit-mask-image: linear-gradient(to right, black 85%, transparent 100%);
  mask-image: linear-gradient(to right, black 85%, transparent 100%);
}
.category-tabs::-webkit-scrollbar { display: none; /* Chrome, Safari 스크롤바 완전 숨김 */ }

/* 여유 공간 스페이서 */
.tab-spacer {
  min-width: 40px;
  visibility: hidden;
}

/* 좌우 스크롤 내비게이션 화살표 */
.tab-nav-btn {
  position: absolute;
  top: calc(50% - 4px); /* 하단 스크롤 여백(12px)과 상단(4px) 비대칭 보정하여 시각적 완벽한 중앙 정렬 */
  transform: translateY(-50%);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.7); /* bg-white/70 */
  backdrop-filter: blur(4px);
  border: 1px solid #e2e8f0; /* border-slate-200 */
  color: #64748b; /* slate-500 */
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  opacity: 0.7;
  transition: all 0.2s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05); /* shadow-sm */
}
.tab-nav-btn:hover {
  opacity: 1;
  background: rgba(255, 255, 255, 0.95);
  color: var(--dark);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06); /* shadow */
  transform: translateY(-50%) scale(1.05);
}
.tab-nav-btn:active {
  transform: translateY(-50%) scale(0.95);
}
.tab-nav-btn.left { left: 4px; }
.tab-nav-btn.right { right: 4px; }

.tab-item {
  white-space: nowrap;
  padding: 10px 18px;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(126, 200, 227, 0.3);
  border-radius: var(--radius-pill);
  font-size: 15px;
  font-weight: 700;
  color: var(--text-muted);
  font-family: var(--font-display);
  cursor: pointer;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}
.tab-item:hover { background: rgba(126, 200, 227, 0.1); color: var(--ocean); border-color: rgba(126, 200, 227, 0.6); transform: translateY(-1px); }
.tab-item.active { 
  background: var(--ocean); 
  color: white; 
  border-color: var(--ocean); 
  box-shadow: 0 4px 12px rgba(58, 134, 184, 0.4), 0 0 0 2px var(--ocean); /* 테두리를 진하게 보이도록 2px 링 추가 */
  transform: scale(1.05); 
  font-weight: 900; 
  letter-spacing: 0.2px;
}

/* ✨ "전체 보기" 탭 전용 위계 변경 (Outline Style 분리) */
.tab-item.tab-all {
  background: transparent;
  border: 1.5px dashed rgba(126, 200, 227, 0.6);
  color: var(--ocean);
}
.tab-item.tab-all:hover {
  background: rgba(126, 200, 227, 0.05);
}
.tab-item.tab-all.active {
  background: rgba(126, 200, 227, 0.15);
  border: 1.5px solid var(--ocean);
  color: var(--ocean);
  box-shadow: none;
}

.search-feedback {
  margin-bottom: 16px;
  font-size: 14px;
  color: var(--text-muted);
  font-weight: 600;
}
.search-feedback strong {
  color: var(--ocean);
}

.scene-grid-container {
  position: relative;
}

.scene-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
  animation: fade-in 0.3s ease;
}

@keyframes fade-in {
  0% { opacity: 0; transform: translateY(8px); }
  100% { opacity: 1; transform: translateY(0); }
}

/* ── 무작위 챌린지 특별 카드 ── */
.random-card {
  grid-column: 1 / -1; /* 가로 전체 너비 확보 */
  background: linear-gradient(135deg, var(--ocean), var(--train));
  border: none;
  padding: 0;
}
.random-card:hover {
  transform: translateY(-4px) scale(1.01);
  box-shadow: 0 12px 30px rgba(58, 134, 184, 0.3);
}
.random-content {
  display: flex;
  align-items: center;
  padding: 24px;
  gap: 16px;
  color: white;
  width: 100%;
}
.random-emoji { font-size: 40px; animation: bounce-subtle 2s infinite; }
.random-text { display: flex; flex-direction: column; flex: 1; text-align: left; }
.random-text h3 { margin: 0; font-size: 20px; font-weight: 900; font-family: var(--font-display); letter-spacing: -0.5px; }
.random-text p { margin: 4px 0 0; font-size: 13px; opacity: 0.9; }
.random-arrow { font-size: 24px; font-weight: bold; background: rgba(255,255,255,0.2); width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; border-radius: 50%; }

.scene-card {
  background: rgba(255, 255, 255, 0.9);
  border: 2px solid rgba(126, 200, 227, 0.2);
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  text-align: left;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s, box-shadow 0.2s, border-color 0.2s;
}
.scene-card:hover { transform: translateY(-4px); box-shadow: 0 10px 30px rgba(58, 134, 184, 0.15); border-color: var(--sky); }

/* 선택 시 피드백 강조 (링 추가) */
.scene-card.selected { 
  border-width: 3px;
  border-color: var(--ocean); 
  background: rgba(126, 200, 227, 0.08); 
  box-shadow: 0 8px 24px rgba(58, 134, 184, 0.25); 
  transform: translateY(-4px) scale(1.02); 
}

.scene-image { position: relative; width: 100%; aspect-ratio: 16 / 9; overflow: hidden; }
.scene-image img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; }
.scene-card:hover .scene-image img { transform: scale(1.08); }
.scene-card.selected .scene-image img { transform: scale(1.1); filter: brightness(0.8); }
.scene-image-overlay { position: absolute; inset: 0; background: linear-gradient(180deg, rgba(0,0,0,0.05) 0%, rgba(0,0,0,0.3) 100%); pointer-events: none; }
.scene-card.selected .scene-image-overlay { background: rgba(58, 134, 184, 0.4); } /* 선택 시 파란색 틴트 추가 */

.selected-badge { 
  position: absolute; 
  top: 12px; 
  right: 12px; 
  width: 32px; 
  height: 32px; 
  background: var(--ocean); 
  border: 2px solid white;
  border-radius: 50%; 
  display: flex; 
  align-items: center; 
  justify-content: center; 
  color: white; 
  font-weight: 900; 
  font-size: 18px; 
  box-shadow: 0 4px 12px rgba(58, 134, 184, 0.5); 
  animation: scale-in 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275); 
}

@keyframes scale-in {
  0% { transform: scale(0); }
  100% { transform: scale(1); }
}

.scene-info { padding: 12px; display: flex; flex-direction: column; gap: 4px; }
.scene-badge { font-size: 11px; font-weight: 800; padding: 2px 6px; border-radius: 4px; width: fit-content; margin-bottom: 2px; }
.scene-name { font-size: 14px; font-weight: 800; color: var(--dark); line-height: 1.3; }

/* 설명을 1줄로 줄이고 Hover 시 풀 텍스트 보이도록 밀도 개선 */
.scene-desc { 
  font-size: 12px; 
  color: var(--text-muted); 
  line-height: 1.4; 
  white-space: nowrap; 
  overflow: hidden; 
  text-overflow: ellipsis; 
  transition: all 0.3s;
}
.scene-card:hover .scene-desc {
  white-space: normal;
}

.empty-state { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 40px 20px; background: rgba(126, 200, 227, 0.04); border: 2px dashed rgba(126, 200, 227, 0.2); border-radius: var(--radius-lg); text-align: center; }
.empty-icon { font-size: 36px; margin-bottom: 12px; }
.empty-text { font-size: 15px; font-weight: 600; color: var(--text-muted); line-height: 1.5; }

/* ── 4. Passport Status Card (Boarding Pass Theme) ─────────────────── */
.passport-status-wrapper {
  position: sticky;
  top: 16px;
  z-index: 50;
  margin-bottom: 24px;
}

.boarding-pass-card {
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.12), 0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  border: 1px solid rgba(126, 200, 227, 0.4);
  overflow: hidden;
}

/* 수직 절취선 구조 (Tablet+) */
@media (min-width: 768px) {
  .boarding-pass-card {
    flex-direction: row;
  }
}

.bpc-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px 24px;
}

.bpc-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--dark);
}

.bpc-title {
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 900;
  letter-spacing: 2px;
  color: var(--dark);
}

.bpc-close-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #94a3b8;
  cursor: pointer;
  padding: 4px;
  line-height: 1;
}
.bpc-close-btn:hover { color: var(--dark); }

.bpc-body {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.bpc-row {
  display: flex;
  gap: 32px;
}

.bpc-field {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.bpc-label {
  font-family: monospace;
  font-size: 11px;
  color: #64748b;
  letter-spacing: 1px;
}

.bpc-value {
  font-size: 16px;
  font-weight: 800;
  color: var(--dark);
}

/* MISSION 라인 강조 */
.bpc-mission-highlight {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(126, 200, 227, 0.15); /* bg-primary/10 느낌의 연한 톤 */
  padding: 4px 12px;
  border-radius: 8px;
  width: fit-content;
  margin-left: -4px; /* 라벨과 정렬 맞추기 위한 미세 조정 */
}
.bpc-mission-highlight .bpc-value {
  font-size: 16px;
  font-weight: 800;
  color: var(--dark);
}

.bpc-badge-ready {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  font-weight: 800;
  color: var(--ocean-deep);
  background: rgba(58, 134, 184, 0.15);
  padding: 2px 8px;
  border-radius: 9999px; /* rounded-full */
  letter-spacing: 0.5px;
}
.bpc-badge-ready::before {
  content: '●';
  font-size: 8px;
}

.bpc-divider {
  width: 100%;
  height: 2px;
  background: transparent;
  border-top: 2px dashed rgba(126, 200, 227, 0.4);
  position: relative;
  margin: 0;
}
@media (min-width: 768px) {
  .bpc-divider {
    width: 2px;
    height: auto;
    border-top: none;
    border-left: 2px dashed rgba(126, 200, 227, 0.4);
  }
}
/* 절취선 양 끝 반원 구멍 */
.bpc-divider::before, .bpc-divider::after {
  content: '';
  position: absolute;
  width: 24px;
  height: 24px;
  background: #f8fafc; /* 상위 배경색 매칭 */
  border-radius: 50%;
  border: 1px solid rgba(126, 200, 227, 0.4);
  z-index: 1;
}
@media (max-width: 767px) {
  .bpc-divider::before { left: -14px; top: -12px; border-right-color: transparent; border-bottom-color: transparent; transform: rotate(45deg); }
  .bpc-divider::after { right: -14px; top: -12px; border-left-color: transparent; border-bottom-color: transparent; transform: rotate(-45deg); }
}
@media (min-width: 768px) {
  .bpc-divider::before { top: -14px; left: -12px; border-bottom-color: transparent; border-right-color: transparent; transform: rotate(45deg); }
  .bpc-divider::after { bottom: -14px; left: -12px; border-top-color: transparent; border-right-color: transparent; transform: rotate(-135deg); }
}

.bpc-right {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: stretch;
  padding: 20px 24px;
  background: rgba(126, 200, 227, 0.05);
}
@media (min-width: 768px) {
  .bpc-right {
    min-width: 260px;
    background: transparent;
  }
}

.bpc-barcode {
  height: 48px;
  margin-bottom: 20px;
  background-image: repeating-linear-gradient(
    90deg,
    var(--dark),
    var(--dark) 2px,
    transparent 2px,
    transparent 4px,
    var(--dark) 4px,
    var(--dark) 7px,
    transparent 7px,
    transparent 11px
  );
  opacity: 0.2;
}

.bpc-cta-start {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 16px 20px;
  background: linear-gradient(135deg, var(--ocean), var(--ocean-deep));
  color: white;
  border: none;
  border-radius: 12px;
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 900;
  letter-spacing: 1px;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(58, 134, 184, 0.4);
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.bpc-cta-start:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 12px 32px rgba(58, 134, 184, 0.6);
}
.bpc-cta-start:active {
  transform: translateY(0) scale(0.98);
}
.cta-icon { font-size: 20px; }

/* 반응형 모바일 (기존 CSS와 호환) */
@media (max-width: 767px) {
  .hero-title { font-size: 42px; }
  .hero-subtitle { font-size: 18px; }
  .scene-grid { grid-template-columns: repeat(2, 1fr); gap: 10px; }
  .selection-header-layout { flex-direction: column; align-items: stretch; gap: 12px; }
  .pill-toggle { width: 100%; display: flex; }
  .pill-toggle .pill { flex: 1; text-align: center; }
}

</style>
