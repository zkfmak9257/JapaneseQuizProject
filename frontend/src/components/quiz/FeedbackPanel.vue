<!--
  FeedbackPanel.vue — 퀴즈 결과 피드백 카드
  UX 흐름: ① 상태 배지 → ② 비교 → ③ 해설 → ④ 보조 → ⑤ CTA
-->
<template>
  <section
    v-if="visible"
    class="fb-card"
    :class="isCorrect ? 'fb-correct' : 'fb-wrong'"
    ref="cardRef"
  >
    <!-- ① 결과 상태 배지 -->
    <div class="fb-status">
      <span class="fb-badge" :class="isCorrect ? 'fb-badge--clear' : 'fb-badge--fail'">
        {{ isCorrect ? '🎉 정답!' : '❌ 오답' }}
      </span>
      <p class="fb-status-msg">
        {{ isCorrect
          ? '잘 했어요! 실전에서도 이 표현을 바로 쓸 수 있습니다.'
          : '아래에서 정답과 내 답을 비교해보세요.' }}
      </p>
    </div>

    <!-- ② 비교 영역 ─────────────────────────────────────── -->

    <!-- 문장 조합 모드: 토큰별 후리가나 + 위치 비교 -->
    <div v-if="isSentenceMode" class="fb-sentence-compare">
      <!-- 🟢 정답 문장 -->
      <div class="fb-sent-box fb-sent-box--correct">
        <div class="fb-sent-box__label">✔️ 정답 문장</div>
        <div class="fb-sent-tokens">
          <span
            v-for="(tok, idx) in correctTokens"
            :key="'c-'+idx"
            class="fb-token"
            :class="tokenMatchClass(idx, tok)"
          >
            <template v-if="tok.reading && hasKanji(tok.base)">
              <ruby>{{ tok.base }}<rt>{{ tok.reading }}</rt></ruby>
            </template>
            <template v-else>{{ tok.base }}</template>
          </span>
        </div>
        <p class="fb-sent-meaning" v-if="correct.meaning">{{ correct.meaning }}</p>
      </div>

      <!-- 📖 단어/표현 해설 (접힘형 — 정답 + 오답 토큰 모두) -->
      <div class="fb-glossary" v-if="correctTokens.length > 0">
        <button class="fb-glossary__toggle" @click="showMeanings = !showMeanings">
          📖 단어/표현 해설 보기 {{ showMeanings ? '▴' : '▾' }}
        </button>
        <div v-show="showMeanings" class="fb-glossary__body">
          <!-- 정답 토큰 해설 -->
          <div class="fb-glossary__section">
            <div class="fb-glossary__section-label">✔️ 정답 문장 구성</div>
            <div v-for="(g, i) in correctGlossary" :key="'cg-'+i" class="fb-glossary__item">
              <span class="fb-glossary__jp">
                <template v-if="g.reading && hasKanji(g.base)">
                  <ruby>{{ g.base }}<rt>{{ g.reading }}</rt></ruby>
                </template>
                <template v-else>{{ g.base }}</template>
              </span>
              <span class="fb-glossary__ko">{{ g.meaning }}</span>
              <span v-if="g.role" class="fb-glossary__role">{{ g.role }}</span>
            </div>
          </div>
          <!-- 오답 전용 토큰 (정답에 없는 것만) -->
          <div v-if="wrongOnlyGlossary.length > 0" class="fb-glossary__section">
            <div class="fb-glossary__section-label">✗ 내가 사용한 토큰 (오답)</div>
            <div v-for="(g, i) in wrongOnlyGlossary" :key="'wg-'+i" class="fb-glossary__item fb-glossary__item--wrong">
              <span class="fb-glossary__jp">
                <template v-if="g.reading && hasKanji(g.base)">
                  <ruby>{{ g.base }}<rt>{{ g.reading }}</rt></ruby>
                </template>
                <template v-else>{{ g.base }}</template>
              </span>
              <span class="fb-glossary__ko">{{ g.meaning }}</span>
              <span v-if="g.role" class="fb-glossary__role">{{ g.role }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 🔴 내가 제출한 문장 (오답일 때만) -->
      <div v-if="!isCorrect" class="fb-sent-box fb-sent-box--wrong">
        <div class="fb-sent-box__label">✗ 내가 제출한 문장</div>
        <div class="fb-sent-tokens" v-if="selectedTokens.length > 0">
          <span
            v-for="(tok, idx) in selectedTokens"
            :key="'s-'+idx"
            class="fb-token"
            :class="tokenWrongClass(idx, tok)"
          >
            <template v-if="tok.reading && hasKanji(tok.base)">
              <ruby>{{ tok.base }}<rt>{{ tok.reading }}</rt></ruby>
            </template>
            <template v-else>{{ tok.base }}</template>
          </span>
        </div>
        <p v-else class="fb-sent-empty">제출한 문장 정보를 불러올 수 없습니다.</p>
      </div>

      <!-- 📍 어순 비교 (오답 — 후리가나 + 뜻 힌트 포함) -->
      <div v-if="!isCorrect && diffItems.length > 0" class="fb-diff">
        <div class="fb-diff__title">📍 어순 비교</div>
        <div v-for="(d, i) in diffItems" :key="i" class="fb-diff__row">
          <span class="fb-diff__pos">{{ d.position }}번째</span>
          <span class="fb-diff__wrong">
            {{ d.got }}
            <span v-if="d.gotReading" class="fb-diff__reading">{{ d.gotReading }}</span>
          </span>
          <span class="fb-diff__arrow">→</span>
          <span class="fb-diff__right">
            {{ d.expected }}
            <span v-if="d.expectedReading" class="fb-diff__reading">{{ d.expectedReading }}</span>
          </span>
          <span v-if="d.hint" class="fb-diff__hint">{{ d.hint }}</span>
        </div>
      </div>

      <!-- 💬 패턴 힌트 (일반적 가이드 대체) -->
      <p class="fb-guide" v-if="patternHint">💬 {{ patternHint }}</p>
    </div>

    <!-- 단어 선택 모드: 카드형 비교 -->
    <div v-else class="fb-compare">
      <div class="fb-box fb-box--correct">
        <div class="fb-box__label"><span class="fb-box__icon">✔️</span> 정답</div>
        <p class="fb-box__jp">{{ correct.kanji }}</p>
        <p class="fb-box__kana" v-if="correct.kana && correct.kana !== '-'">{{ correct.kana }}</p>
        <p class="fb-box__meaning" v-if="correct.meaning">{{ correct.meaning }}</p>
      </div>
      <div v-if="!isCorrect" class="fb-box fb-box--wrong">
        <div class="fb-box__label"><span class="fb-box__icon">✗</span> 내가 고른 답</div>
        <p class="fb-box__jp">{{ selected.kanji }}</p>
        <p class="fb-box__kana" v-if="selected.kana && selected.kana !== '-'">{{ selected.kana }}</p>
        <p class="fb-box__meaning" v-if="selected.meaning">{{ selected.meaning }}</p>
      </div>
    </div>

    <!-- ③ 해설 토글 -->
    <div class="fb-explain" v-if="keyPoint || choices.length > 0">
      <button class="fb-explain__btn" @click="showExplain = !showExplain">
        📘 해설 {{ showExplain ? '접기' : '펼치기' }}
        <span class="fb-explain__arrow">{{ showExplain ? '▴' : '▾' }}</span>
      </button>
      <div v-show="showExplain" class="fb-explain__body">
        <div class="fb-explain__point" v-if="keyPoint"><p>💡 {{ keyPoint }}</p></div>
        <div v-if="choices.length > 0" class="fb-vocab">
          <div class="fb-vocab__header"><span>보기</span><span>읽기</span><span>뜻</span></div>
          <div v-for="(c, idx) in choices" :key="idx" class="fb-vocab__row">
            <span class="fb-vocab__jp">{{ c.kanji }}</span>
            <span class="fb-vocab__kana">{{ c.kana }}</span>
            <span class="fb-vocab__ko">{{ c.meaning }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 가이드 힌트 (단어 모드 전용 — 문장 모드는 patternHint가 대체) -->
    <p class="fb-guide" v-if="!isSentenceMode && guideText">💬 {{ guideText }}</p>


    <!-- ⑤ Primary CTA -->
    <button class="fb-btn-primary" @click="$emit('next')">다음 여행지로 이동 ✈️</button>
  </section>
</template>

<script setup>
/**
 * FeedbackPanel — 퀴즈 결과 피드백 전용 컴포넌트
 *
 * 두 가지 모드:
 * 1) 단어 선택 — kanji/kana/meaning 카드형
 * 2) 문장 조합 — 토큰별 후리가나 + 위치 비교 하이라이트
 */
import { ref, computed, watch, nextTick } from 'vue';

const props = defineProps({
  visible: { type: Boolean, default: false },
  isCorrect: { type: Boolean, default: false },
  isSentenceMode: { type: Boolean, default: false },
  isFavorited: { type: Boolean, default: false },
  correct: { type: Object, default: () => ({ kanji: '-', kana: '-', meaning: '' }) },
  selected: { type: Object, default: () => ({ kanji: '-', kana: '-', meaning: '' }) },
  /** 문장 모드: 정답 토큰 배열 [{ base, reading, meaning?, role? }] */
  correctTokens: { type: Array, default: () => [] },
  /** 문장 모드: 제출 토큰 배열 [{ base, reading }] */
  selectedTokens: { type: Array, default: () => [] },
  choices: { type: Array, default: () => [] },
  serverError: { type: String, default: '' },
  guideText: { type: String, default: '' },
  keyPoint: { type: String, default: '' }
});




const showExplain = ref(false);
const showMeanings = ref(false);
const favoriteAnimating = ref(false);
const cardRef = ref(null);

/**
 * handleFavorite — 저장 버튼 클릭 시 짧은 애니메이션 후 이벤트 emit
 * 왜 애니메이션? 사용자가 "클릭이 되었는지" 시각적으로 알 수 있도록.
 */
function handleFavorite() {
  if (favoriteAnimating.value) return;
  favoriteAnimating.value = true;
  emit('toggle-favorite');
  setTimeout(() => { favoriteAnimating.value = false; }, 600);
}

const emit = defineEmits(['next', 'toggle-favorite', 'go-wrong-note', 'retry-submit', 'dismiss-error']);

watch(() => props.visible, (val) => {
  if (val) {
    showExplain.value = false;
    showMeanings.value = false;
    nextTick(() => { cardRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' }); });
  }
});

/**
 * 일본어 토큰 → { 뜻, 역할 } 로컬 사전
 * meaning: 한국어 뜻
 * role: 품사/문법 역할 (조사, 동사, 명사 등)
 */
const DICT = {
  // ── 조사 ──
  'は': { m: '~은/는', r: '주제 조사' },
  'が': { m: '~이/가', r: '주어 조사' },
  'を': { m: '~을/를', r: '목적격 조사' },
  'に': { m: '~에/에게', r: '방향·대상 조사' },
  'で': { m: '~에서/로', r: '장소·수단 조사' },
  'と': { m: '~와/과', r: '병렬·인용 조사' },
  'も': { m: '~도', r: '포함 조사' },
  'の': { m: '~의', r: '소유 조사' },
  'へ': { m: '~으로', r: '방향 조사' },
  'から': { m: '~부터/에서', r: '출발점 조사' },
  'まで': { m: '~까지', r: '종점 조사' },
  'より': { m: '~보다', r: '비교 조사' },
  'か': { m: '~인가?', r: '의문 조사' },
  'よ': { m: '~요', r: '강조 종조사' },
  'ね': { m: '~네', r: '동의 요청 종조사' },
  // ── 경어·정중 표현 ──
  'です': { m: '~입니다', r: '정중 서술' },
  'ます': { m: '~합니다', r: '정중 동사 어미' },
  'ました': { m: '~했습니다', r: '정중 과거' },
  'ません': { m: '~하지 않습니다', r: '정중 부정' },
  'ませんか': { m: '~하시겠습니까?', r: '정중 부정 의문' },
  'ください': { m: '~해 주세요', r: '요청 표현' },
  'します': { m: '합니다', r: '동사·정중' },
  'しました': { m: '했습니다', r: '동사·과거' },
  'ありません': { m: '없습니다', r: '정중 부정' },
  'あります': { m: '있습니다', r: '존재·정중' },
  'ありますか': { m: '있습니까?', r: '존재·의문' },
  'ございます': { m: '있습니다', r: '존경어' },
  'でした': { m: '~이었습니다', r: '과거 서술' },
  'ましょう': { m: '~합시다', r: '제안 표현' },
  'たいです': { m: '~하고 싶습니다', r: '희망 표현' },
  'できます': { m: '할 수 있습니다', r: '가능 표현' },
  'できますか': { m: '할 수 있습니까?', r: '가능·의문' },
  'できません': { m: '할 수 없습니다', r: '불가능 표현' },
  // ── 동사·표현 ──
  'お願い': { m: '부탁', r: '명사' },
  'お願いします': { m: '부탁합니다', r: '요청 정중 표현' },
  'ありがとう': { m: '감사합니다', r: '감사 표현' },
  'ありがとうございます': { m: '대단히 감사합니다', r: '감사·존경' },
  'すみません': { m: '실례합니다', r: '사과·호출' },
  'なくしました': { m: '잃어버렸습니다', r: '동사·과거' },
  '忘れました': { m: '잊었습니다', r: '동사·과거' },
  '落としました': { m: '떨어뜨렸습니다', r: '동사·과거' },
  '見つかりません': { m: '찾을 수 없습니다', r: '동사·부정' },
  '教えてください': { m: '알려주세요', r: '요청 표현' },
  '見せてください': { m: '보여주세요', r: '요청 표현' },
  '持ってきてください': { m: '가져와 주세요', r: '요청 표현' },
  '行きたいです': { m: '가고 싶습니다', r: '희망 표현' },
  '知りたいです': { m: '알고 싶습니다', r: '희망 표현' },
  '泊まります': { m: '묵습니다', r: '동사·정중' },
  '泊まりたいです': { m: '묵고 싶습니다', r: '희망 표현' },
  '買いたいです': { m: '사고 싶습니다', r: '희망 표현' },
  '食べたいです': { m: '먹고 싶습니다', r: '희망 표현' },
  '飲みたいです': { m: '마시고 싶습니다', r: '희망 표현' },
  // ── 의문사 ──
  'いくら': { m: '얼마', r: '의문사' },
  'どこ': { m: '어디', r: '의문사' },
  'なに': { m: '무엇', r: '의문사' },
  '何': { m: '무엇', r: '의문사' },
  'いつ': { m: '언제', r: '의문사' },
  'どう': { m: '어떻게', r: '의문사' },
  'どれ': { m: '어느 것', r: '의문사' },
  'どの': { m: '어떤', r: '연체사' },
  'どちら': { m: '어디/어느 쪽', r: '의문사' },
  'どのくらい': { m: '어느 정도', r: '의문 표현' },
  // ── 여행 빈출 명사 ──
  'ホテル': { m: '호텔', r: '명사' },
  'チェックイン': { m: '체크인', r: '명사' },
  'チェックアウト': { m: '체크아웃', r: '명사' },
  '予約': { m: '예약', r: '명사' },
  '部屋': { m: '방', r: '명사' },
  '荷物': { m: '짐', r: '명사' },
  '切符': { m: '표/티켓', r: '명사' },
  '駅': { m: '역', r: '명사' },
  '空港': { m: '공항', r: '명사' },
  '電車': { m: '전철', r: '명사' },
  'タクシー': { m: '택시', r: '명사' },
  'バス': { m: '버스', r: '명사' },
  '病院': { m: '병원', r: '명사' },
  '薬': { m: '약', r: '명사' },
  'トイレ': { m: '화장실', r: '명사' },
  '水': { m: '물', r: '명사' },
  'お水': { m: '물', r: '명사·정중' },
  'お茶': { m: '차', r: '명사·정중' },
  'メニュー': { m: '메뉴', r: '명사' },
  '会計': { m: '계산', r: '명사' },
  'レシート': { m: '영수증', r: '명사' },
  '領収書': { m: '영수증', r: '명사' },
  '両替': { m: '환전', r: '명사' },
  '両替所': { m: '환전소', r: '명사' },
  '搭乗口': { m: '탑승구', r: '명사' },
  '出口': { m: '출구', r: '명사' },
  '入口': { m: '입구', r: '명사' },
  '写真': { m: '사진', r: '명사' },
  '名前': { m: '이름', r: '명사' },
  '時間': { m: '시간', r: '명사' },
  '今日': { m: '오늘', r: '명사' },
  '明日': { m: '내일', r: '명사' },
  '大丈夫': { m: '괜찮다', r: '형용동사' },
  '財布': { m: '지갑', r: '명사' },
  'パスポート': { m: '여권', r: '명사' },
  'カード': { m: '카드', r: '명사' },
  '鍵': { m: '열쇠', r: '명사' },
  '電話': { m: '전화', r: '명사' },
  '携帯': { m: '휴대폰', r: '명사' },
  'お金': { m: '돈', r: '명사' },
  '交番': { m: '파출소', r: '명사' },
  '警察': { m: '경찰', r: '명사' },
  '大使館': { m: '대사관', r: '명사' },
  'アレルギー': { m: '알레르기', r: '명사' },
  'おすすめ': { m: '추천', r: '명사' },
  'これ': { m: '이것', r: '지시대명사' },
  'それ': { m: '그것', r: '지시대명사' },
  'あれ': { m: '저것', r: '지시대명사' },
  'ここ': { m: '여기', r: '지시대명사' },
  'そこ': { m: '거기', r: '지시대명사' },
  'あそこ': { m: '저기', r: '지시대명사' },
  'この': { m: '이', r: '연체사' },
  'その': { m: '그', r: '연체사' },
  'あの': { m: '저', r: '연체사' },
  '一つ': { m: '하나', r: '수사' },
  '二つ': { m: '둘', r: '수사' },
  '一人': { m: '한 명', r: '수사' },
  '二人': { m: '두 명', r: '수사' }
};

/** 뜻만 반환 (구 버전 호환) */
function lookupMeaning(text) {
  return DICT[text]?.m || null;
}

/** 역할만 반환 */
function lookupRole(text) {
  return DICT[text]?.r || null;
}

function hasKanji(text) {
  return /[\u4E00-\u9FFF]/.test(text);
}

function hasHangul(text) {
  return /[ㄱ-ㅎㅏ-ㅣ가-힣]/.test(String(text || ''));
}

function resolveMeaning(token) {
  const base = token?.base || '';
  const dbMeaning = token?.meaning || '';
  const dictMeaning = lookupMeaning(base);

  // DB 뜻이 한국어면 우선 사용, 아니면 로컬 사전 뜻 우선.
  if (dbMeaning && hasHangul(dbMeaning)) return dbMeaning;
  if (dictMeaning) return dictMeaning;
  if (dbMeaning) return dbMeaning;
  return base;
}

function normalizePatternPart(token) {
  const base = token?.base || '';
  const rawRole = String(token?.role || lookupRole(base) || '').trim();

  if (!rawRole) return base;
  if (rawRole.includes('조사')) return base;
  if (rawRole === '미분류') return base;

  // 역할명이 너무 길거나 상세한 경우 학습 패턴용으로 단순화
  if (rawRole.includes('명사')) return '[명사]';
  if (rawRole.includes('동사')) return '[동사]';
  if (rawRole.includes('형용사')) return '[형용사]';
  if (rawRole.includes('형용동사')) return '[형용동사]';
  if (rawRole.includes('의문사')) return '[의문사]';
  if (rawRole.includes('연체사')) return '[연체사]';
  if (rawRole.includes('수사')) return '[수량]';
  if (rawRole.includes('지시대명사')) return '[지시어]';
  if (rawRole.includes('요청')) return '[요청 표현]';
  if (rawRole.includes('불가능')) return '[불가능 표현]';
  if (rawRole.includes('가능')) return '[가능 표현]';
  if (rawRole.includes('희망')) return '[희망 표현]';
  if (rawRole.includes('감사')) return '[감사 표현]';
  if (rawRole.includes('사과')) return '[사과 표현]';

  return `[${rawRole}]`;
}

function tokenMatchClass(idx, tok) {
  if (props.isCorrect) return 'fb-token--match';
  const submitted = props.selectedTokens[idx];
  if (submitted && submitted.base === tok.base) return 'fb-token--match';
  return 'fb-token--correct-highlight';
}

function tokenWrongClass(idx, tok) {
  const correct = props.correctTokens[idx];
  if (correct && correct.base === tok.base) return 'fb-token--match';
  return 'fb-token--mismatch';
}

/**
 * diffItems — 후리가나 + 뜻 힌트가 포함된 위치 비교
 */
const diffItems = computed(() => {
  if (props.isCorrect) return [];
  const result = [];
  const maxLen = Math.max(props.correctTokens.length, props.selectedTokens.length);
  for (let i = 0; i < maxLen; i++) {
    const cTok = props.correctTokens[i];
    const sTok = props.selectedTokens[i];
    const expected = cTok?.base || '(없음)';
    const got = sTok?.base || '(없음)';
    if (expected !== got) {
      // 후리가나 (한자 토큰만)
      const expectedReading = (cTok?.reading && hasKanji(expected)) ? cTok.reading : '';
      const gotReading = (sTok?.reading && hasKanji(got)) ? sTok.reading : '';
      // 짧은 문맥 힌트 생성
      const gotMeaning = resolveMeaning(sTok);
      const expectedMeaning = resolveMeaning(cTok);
      let hint = '';
      if (gotMeaning && expectedMeaning) {
        hint = `"${gotMeaning}" → "${expectedMeaning}"`;
      } else if (expectedMeaning) {
        hint = `정답: "${expectedMeaning}"`;
      }
      result.push({ position: i + 1, expected, got, expectedReading, gotReading, hint });
    }
  }
  return result;
});

/**
 * correctGlossary — 정답 토큰 전부 (뜻이 없어도 표시)
 * 왜 전부? 사용자가 "해설 펼쳤는데 일부만 있다"고 느끼지 않도록.
 * 사전에 없으면 의미 영역이 비지 않도록 토큰 원문을 fallback으로 표시.
 */
const correctGlossary = computed(() => {
  return props.correctTokens.map(tok => ({
    base: tok.base,
    reading: tok.reading,
    meaning: resolveMeaning(tok),
    role: tok.role || lookupRole(tok.base) || ''
  }));
});

/**
 * wrongOnlyGlossary — 내가 사용한 토큰 중 정답에 없는 것만
 * 왜 "정답에 없는 것만"? 중복되면 정보가 겹쳐 지저분하므로.
 */
const wrongOnlyGlossary = computed(() => {
  if (props.isCorrect) return [];
  const correctBases = new Set(props.correctTokens.map(t => t.base));
  const seen = new Set();
  return props.selectedTokens
    .filter(tok => {
      if (correctBases.has(tok.base)) return false; // 정답에 있는 건 제외
      if (seen.has(tok.base)) return false; // 중복 제거
      seen.add(tok.base);
      return true;
    })
    .map(tok => ({
      base: tok.base,
      reading: tok.reading,
      meaning: resolveMeaning(tok),
      role: tok.role || lookupRole(tok.base) || ''
    }));
});

/**
 * patternHint — 정답 문장의 패턴을 자동 생성하여 학습 힌트 제공
 * 예: "[명사] + を + [동사]" 패턴
 * 왜? "여행 상황에서 자주 쓰는 표현입니다" 같은 일반 문구 대신
 * 실제 문장 구조를 분석해서 보여주면 학습 효과가 높다.
 */
const patternHint = computed(() => {
  if (props.correctTokens.length === 0) return '';
  const parts = props.correctTokens.map((tok) => normalizePatternPart(tok));
  return `이 문장의 패턴: ${parts.join(' + ')}`;
});
</script>

<style scoped>
/* ═══════════════════════════════════════════════
   FeedbackPanel 스타일
   ═══════════════════════════════════════════════ */

.fb-card {
  margin-top: 2rem;
  padding: 2rem 1.5rem;
  border-radius: 16px;
  background: #fffdf7;
  box-shadow: 0 8px 30px rgba(0,0,0,0.07);
  display: flex;
  flex-direction: column;
  gap: 1.8rem;
  position: relative;
  overflow: hidden;
  animation: cardSlideUp 0.35s ease-out;
}
.fb-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0;
  width: 5px; height: 100%;
}
.fb-correct::before { background: #16a34a; }
.fb-wrong::before   { background: #dc2626; }
.fb-correct { border: 1px solid rgba(22,163,74,0.15); }
.fb-wrong   { border: 1px solid rgba(220,38,38,0.15); }
@keyframes cardSlideUp {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ── ① 상태 ───────────────────── */
.fb-status { text-align: center; padding-bottom: 1.5rem; border-bottom: 2px dashed #e2e8f0; }
.fb-badge {
  display: inline-block; padding: 0.5rem 1.5rem; border-radius: 999px;
  font-size: 1.1rem; font-weight: 800; color: white;
}
.fb-badge--clear { background: linear-gradient(135deg,#16a34a,#22c55e); box-shadow: 0 4px 12px rgba(22,163,74,0.3); }
.fb-badge--fail  { background: linear-gradient(135deg,#dc2626,#ef4444); box-shadow: 0 4px 12px rgba(220,38,38,0.3); }
.fb-status-msg { margin: 0.8rem 0 0; font-size: 0.95rem; color: #64748b; }

/* ── ② 단어 비교 ──────────────── */
.fb-compare { display: grid; grid-template-columns: 1fr; gap: 1rem; }
@media (min-width: 520px) { .fb-compare { grid-template-columns: 1fr 1fr; } }
.fb-box { padding: 1.2rem; border-radius: 12px; border: 1px solid; }
.fb-box--correct { background: #dcfce7; border-color: #86efac; }
.fb-box--wrong   { background: #fee2e2; border-color: #fca5a5; }
.fb-box__label { display: flex; align-items: center; gap: 4px; font-size: 0.8rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.04em; margin-bottom: 0.6rem; }
.fb-box--correct .fb-box__label { color: #15803d; }
.fb-box--wrong   .fb-box__label { color: #b91c1c; }
.fb-box__jp { margin: 0; font-size: 1.6rem; font-weight: 800; color: #0f172a; line-height: 1.3; }
.fb-box__kana { margin: 0.2rem 0 0; font-size: 0.9rem; color: #64748b; }
.fb-box__meaning { margin: 0.5rem 0 0; font-size: 1rem; color: #334155; font-weight: 500; }

/* ── ② 문장 비교 (토큰별 후리가나) ── */
.fb-sentence-compare { display: flex; flex-direction: column; gap: 1rem; }
.fb-sent-box { padding: 1.2rem; border-radius: 12px; border: 1px solid; }
.fb-sent-box--correct { background: #dcfce7; border-color: #86efac; }
.fb-sent-box--wrong   { background: #fee2e2; border-color: #fca5a5; }
.fb-sent-box__label {
  font-size: 0.8rem; font-weight: 700; letter-spacing: 0.04em; margin-bottom: 0.8rem;
  display: flex; align-items: center; justify-content: space-between;
}
.fb-sent-box--correct .fb-sent-box__label { color: #15803d; }
.fb-sent-box--wrong   .fb-sent-box__label { color: #b91c1c; }
.fb-sent-tokens { display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 0.6rem; }
.fb-sent-empty { font-size: 0.9rem; color: #94a3b8; font-style: italic; margin: 0; }




/* 개별 토큰 칩 */
.fb-token {
  display: inline-block; padding: 0.4rem 0.7rem; border-radius: 8px;
  font-size: 1.1rem; font-weight: 700; color: #0f172a;
  background: white; border: 2px solid #e2e8f0;
  transition: all 0.2s ease; line-height: 1.4;
}
.fb-token ruby { font-size: 1.1rem; }
.fb-token rt { font-size: 0.6rem; color: #64748b; font-weight: 500; }
.fb-token--match { border-color: #86efac; background: rgba(22,163,74,0.08); }
.fb-token--correct-highlight { border-color: #16a34a; background: rgba(22,163,74,0.15); box-shadow: 0 0 0 1px #16a34a; }
.fb-token--mismatch { border-color: #fca5a5; background: rgba(220,38,38,0.1); color: #b91c1c; }

/* 접힘형 단어 해설 (Glossary — 정답 + 오답 두 섹션) */
.fb-glossary {
  border: 1px solid #e0f2fe; border-radius: 10px;
  overflow: hidden; background: #f0f9ff;
}
.fb-glossary__toggle {
  width: 100%; display: flex; justify-content: space-between; align-items: center;
  padding: 0.75rem 1rem; background: transparent; border: none;
  font-size: 0.9rem; font-weight: 700; color: #0369a1; cursor: pointer;
  transition: background 0.15s;
}
.fb-glossary__toggle:hover { background: rgba(3,105,161,0.06); }
.fb-glossary__body {
  padding: 0 1rem 0.8rem; display: flex; flex-direction: column; gap: 0.8rem;
  border-top: 1px dashed #bae6fd;
}
.fb-glossary__section { display: flex; flex-direction: column; gap: 0; }
.fb-glossary__section-label {
  font-size: 0.75rem; font-weight: 700; color: #64748b;
  padding: 0.5rem 0 0.3rem; letter-spacing: 0.03em;
}
.fb-glossary__item {
  display: flex; align-items: baseline; gap: 0.6rem;
  padding: 0.45rem 0.5rem; border-bottom: 1px solid #e0f2fe;
  border-radius: 4px;
}
.fb-glossary__item:last-child { border-bottom: none; }
.fb-glossary__item--wrong { background: rgba(220,38,38,0.04); }
.fb-glossary__jp {
  font-size: 1rem; font-weight: 700; color: #0f172a;
  min-width: 80px; flex-shrink: 0;
}
.fb-glossary__jp ruby { font-size: 1rem; }
.fb-glossary__jp rt { font-size: 0.55rem; color: #64748b; }
.fb-glossary__ko { font-size: 0.88rem; color: #334155; flex: 1; }
.fb-glossary__role {
  font-size: 0.68rem; font-weight: 600; color: #6366f1;
  padding: 0.15rem 0.45rem; border-radius: 999px;
  background: rgba(99,102,241,0.08); white-space: nowrap; flex-shrink: 0;
}

.fb-sent-meaning { margin: 0.3rem 0 0; font-size: 0.95rem; color: #334155; font-weight: 500; }

/* ── 어순 비교 (diff — 후리가나 + 뜻 힌트) ── */
.fb-diff {
  padding: 1rem; border-radius: 10px;
  background: #fefce8; border: 1px solid #fde68a;
}
.fb-diff__title { font-size: 0.85rem; font-weight: 700; color: #92400e; margin-bottom: 0.6rem; }
.fb-diff__row {
  display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap;
  padding: 0.5rem 0; font-size: 0.9rem;
  border-bottom: 1px dashed #fde68a;
}
.fb-diff__row:last-child { border-bottom: none; }
.fb-diff__pos { font-weight: 700; color: #78716c; min-width: 45px; font-size: 0.8rem; }
.fb-diff__wrong {
  padding: 0.2rem 0.5rem; border-radius: 6px;
  background: #fee2e2; color: #b91c1c; font-weight: 700;
  text-decoration: line-through;
}
.fb-diff__arrow { color: #94a3b8; font-size: 0.8rem; }
.fb-diff__right {
  padding: 0.2rem 0.5rem; border-radius: 6px;
  background: #dcfce7; color: #15803d; font-weight: 700;
}
.fb-diff__reading {
  font-size: 0.7rem; font-weight: 500; color: #64748b;
  margin-left: 2px;
}
.fb-diff__hint {
  width: 100%; font-size: 0.78rem; color: #78716c; font-style: italic;
  padding-left: 45px; margin-top: -2px;
}

/* ── ③ 해설 ────────────────────── */
.fb-explain { border: 1px solid #e2e8f0; border-radius: 10px; overflow: hidden; background: #f8fafc; }
.fb-explain__btn {
  width: 100%; display: flex; justify-content: space-between; align-items: center;
  padding: 0.9rem 1rem; background: transparent; border: none;
  font-size: 0.95rem; font-weight: 700; color: #0369a1; cursor: pointer; transition: background 0.15s;
}
.fb-explain__btn:hover { background: rgba(3,105,161,0.04); }
.fb-explain__body { padding: 0.5rem 1rem 1rem; border-top: 1px dashed #e2e8f0; display: flex; flex-direction: column; gap: 1rem; }
.fb-explain__point { padding: 0.8rem 1rem; background: rgba(3,105,161,0.06); border-left: 3px solid #0369a1; border-radius: 6px; font-size: 0.95rem; color: #0f172a; line-height: 1.5; }
.fb-explain__point p { margin: 0; }

.fb-vocab { display: grid; gap: 0.4rem; }
.fb-vocab__header { display: grid; grid-template-columns: 2fr 2fr 3fr; gap: 0.5rem; padding: 0 0.6rem 0.4rem; font-size: 0.78rem; font-weight: 700; color: #94a3b8; border-bottom: 2px solid #e2e8f0; }
.fb-vocab__row { display: grid; grid-template-columns: 2fr 2fr 3fr; gap: 0.5rem; padding: 0.6rem; border-radius: 6px; background: white; border: 1px solid #f1f5f9; font-size: 0.9rem; }
.fb-vocab__jp { font-weight: 700; color: #0f172a; }
.fb-vocab__kana { color: #64748b; font-size: 0.85rem; }
.fb-vocab__ko { color: #334155; }
@media (max-width: 480px) { .fb-vocab__header, .fb-vocab__row { grid-template-columns: 1fr 1fr; } .fb-vocab__kana { display: none; } }

/* ── 가이드 ────────────────────── */
.fb-guide { margin: 0; padding: 0.6rem 0.8rem; font-size: 0.85rem; color: #64748b; background: #f0f9ff; border-radius: 8px; border: 1px solid #e0f2fe; }

/* ── ④ 보조 ────────────────────── */
.fb-secondary { display: flex; gap: 0.6rem; }
.fb-btn-outline { flex: 1; padding: 0.7rem; background: white; border: 1px solid #cbd5e1; border-radius: 10px; font-size: 0.9rem; font-weight: 600; color: #334155; cursor: pointer; text-align: center; transition: all 0.15s; }
.fb-btn-outline:hover { border-color: #94a3b8; background: #f8fafc; }
.fb-btn-outline--saved {
  border-color: #f59e0b; background: #fffbeb; color: #92400e; font-weight: 700;
  animation: favPop 0.4s cubic-bezier(0.34,1.56,0.64,1);
}
@keyframes favPop {
  0% { transform: scale(1); }
  40% { transform: scale(1.08); }
  100% { transform: scale(1); }
}
.fb-btn-ghost { flex: 1; padding: 0.7rem; background: rgba(3,105,161,0.06); border: none; border-radius: 10px; font-size: 0.9rem; font-weight: 600; color: #0369a1; cursor: pointer; text-align: center; transition: all 0.15s; }
.fb-btn-ghost:hover { background: rgba(3,105,161,0.1); }

/* ── ⑤ CTA ─────────────────────── */
.fb-btn-primary {
  width: 100%; padding: 1.2rem; font-size: 1.15rem; font-weight: 800;
  border: none; border-radius: 14px; background: #0f172a; color: #fbbf24;
  box-shadow: 0 6px 20px rgba(15,23,42,0.25); cursor: pointer;
  transition: all 0.2s cubic-bezier(0.34,1.56,0.64,1); margin-top: 0.5rem;
}
.fb-btn-primary:hover { background: #1e293b; transform: translateY(-3px) scale(1.01); box-shadow: 0 8px 25px rgba(15,23,42,0.35); }
.fb-btn-primary:active { transform: translateY(0) scale(0.99); }
</style>
