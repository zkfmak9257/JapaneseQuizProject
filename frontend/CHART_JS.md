# Chart.js 도입 및 구현 내용

## 도입 배경

여행기록(`/me/history`) 페이지를 구현하면서 학습 통계를 단순 숫자 카드로만 표시하면
사용자가 자신의 성취를 한눈에 파악하기 어렵다고 판단했습니다.
정답률·완료율을 시각적으로 보여주기 위해 차트 라이브러리 도입을 결정했습니다.

### Chart.js를 선택한 이유

| 비교 항목 | Chart.js | ECharts | D3.js |
|-----------|----------|---------|-------|
| 번들 크기 | 가벼움 (트리쉐이킹 지원) | 무거움 | 매우 무거움 |
| Vue 3 호환 | canvas 직접 사용 가능 | 가능 | 가능 |
| 설치 난이도 | 낮음 (`npm install chart.js`) | 낮음 | 높음 |
| 커스터마이징 | 중간 | 높음 | 매우 높음 |
| 도넛 차트 | 기본 내장 | 기본 내장 | 직접 구현 |

- **vue-chartjs 미사용**: 래퍼 없이 `canvas ref` + `onMounted`로 직접 Chart 인스턴스를 생성해
  불필요한 의존성을 줄이고 번들 크기를 최소화했습니다.
- **트리쉐이킹 적용**: 사용하는 컴포넌트(`DoughnutController`, `ArcElement`, `Tooltip`, `Legend`)만
  선택적으로 `Chart.register()`하여 전체 Chart.js를 임포트하지 않았습니다.

---

## 설치

```bash
cd frontend
npm install chart.js
```

**설치된 버전**: `chart.js` (package.json 참고)

---

## 구현 내용

### 적용 페이지: `MeHistoryListView.vue` (`/me/history`)

`GET /api/stats/me` 응답 데이터를 기반으로 도넛 차트 2개를 렌더링합니다.

#### 차트 1 — 정답률 도넛 차트

| 항목 | 내용 |
|------|------|
| 타입 | Doughnut |
| 데이터 | `correctAnswers` (정답) vs `totalAnswers - correctAnswers` (오답) |
| 색상 | 정답 `#22c55e` (초록) / 오답 `#f87171` (빨강) |
| 중앙 표시 | `accuracyRate`% |

#### 차트 2 — 여행 완료율 도넛 차트

| 항목 | 내용 |
|------|------|
| 타입 | Doughnut |
| 데이터 | `completedAttempts` (완료) vs `totalAttempts - completedAttempts` (미완료) |
| 색상 | 완료 `#3b82f6` (파랑) / 미완료 `#e2e8f0` (회색) |
| 중앙 표시 | `completionRate`% |

---

## 코드 구조

```js
// 필요한 컴포넌트만 등록 (트리쉐이킹)
import { Chart, DoughnutController, ArcElement, Tooltip, Legend } from "chart.js";
Chart.register(DoughnutController, ArcElement, Tooltip, Legend);

// Vue canvas ref
const accuracyChartRef = ref(null);
const completionChartRef = ref(null);
let accuracyChart = null;
let completionChart = null;

// 차트 생성 (API 데이터 로드 완료 후 호출)
function buildCharts() { ... }

// 메모리 누수 방지: 컴포넌트 언마운트 시 차트 인스턴스 파괴
onUnmounted(() => {
  accuracyChart?.destroy();
  completionChart?.destroy();
});
```

---

## 주의사항

- Chart.js는 `<canvas>` 요소가 DOM에 존재한 뒤 인스턴스를 생성해야 합니다.
  API 로드 완료 → `v-else` 블록 렌더링 → `setTimeout(buildCharts, 50)`으로 DOM 렌더링을 기다립니다.
- 차트를 재생성할 때 이전 인스턴스를 `destroy()`하지 않으면 중복 렌더링 경고가 발생합니다.
  `buildCharts()` 내부에서 항상 기존 인스턴스를 먼저 파괴합니다.
