<template>
  <div class="login-page">
    <div class="ed-card login-card" :class="{ 'is-submitted': isApproved }">
      <!-- 상단 장식선 (레드) -->
      <div class="card-topline"></div>

      <!-- 1️⃣ 3단 타이틀 계층 (브랜드/문서명/보조를 한 덩어리로 묶음) -->
      <div class="ed-header">
        <div class="ed-title-brand">旅Quiz</div>
        <h1 class="ed-title-doc">入国カード</h1>
        <p class="ed-title-sub">타비퀴즈 입국 신청서</p>
      </div>

      <!-- 폼 본문 -->
      <form @submit.prevent="onSubmit" class="ed-form" :class="{ 'pointer-events-none': isApproved }">
        
        <div class="ed-table">
          
          <!-- 2️⃣ 개인 정보 섹션 바 (위아래 여백과 tracking 강조) -->
          <div class="section-divider">
            <span class="divider-text">個人情報 / 개인 정보</span>
          </div>

          <!-- 이름 -->
          <div class="ed-row">
            <div class="ed-col-head border-right">
               <div class="txt-jp-lg">パスポート名</div>
               <div class="txt-kr-sub">여권 이름</div>
            </div>
            <!-- 3️⃣ 입력 필드 폭 다르게 (리듬감) -->
            <div class="ed-col-field">
              <div class="input-wrap input-short">
                <input id="nickname" v-model="nickname" type="text" placeholder="예: タビ太郎 / 타비타로" required class="ed-input" autocomplete="username" />
              </div>
            </div>
          </div>

          <!-- 이메일 -->
          <div class="ed-row border-top">
            <div class="ed-col-head border-right">
               <div class="txt-jp-lg">Eメール</div>
               <div class="txt-kr-sub">이메일 (ID)</div>
            </div>
            <div class="ed-col-field">
              <div class="input-wrap input-long">
                <input id="email" v-model="email" type="email" placeholder="you@example.com" required class="ed-input" autocomplete="email" />
              </div>
            </div>
          </div>

          <!-- 2️⃣ 보안 정보 섹션 바 -->
          <div class="section-divider border-top">
            <span class="divider-text">セキュリティ / 보안 정보</span>
          </div>

          <!-- 암호 -->
          <div class="ed-row border-top">
            <div class="ed-col-head border-right">
               <div class="txt-jp-lg">旅行者の暗号</div>
               <div class="txt-kr-sub">여행자 암호</div>
            </div>
            <div class="ed-col-field">
                <div class="input-wrap input-medium">
                  <input id="password" v-model="password" type="password" placeholder="8자 이상, 영/숫자/특수기호" required class="ed-input" autocomplete="new-password" />
                </div>
            </div>
          </div>

          <!-- 암호 재확인 -->
          <div class="ed-row border-top">
            <div class="ed-col-head border-right">
               <div class="txt-jp-lg">暗号の再確認</div>
               <div class="txt-kr-sub">암호 재확인</div>
            </div>
             <div class="ed-col-field">
                 <div class="input-wrap input-medium">
                   <input id="passwordConfirm" v-model="passwordConfirm" type="password" placeholder="한 번 더 입력해 주세요" required class="ed-input" autocomplete="new-password" />
                 </div>
              </div>
          </div>

          <!-- 2️⃣ 확인/서명 섹션 -->
          <div class="section-divider border-top">
            <span class="divider-text">確認・署名 / 확인 및 서명</span>
          </div>

          <!-- 4️⃣ 체크박스 확인란 박스 (연한 웜그레이 배경/점선 테두리) -->
          <div class="ed-row border-top question-section-wrap">
             <div class="q-row">
              <label class="q-check-box">
                <input type="checkbox" v-model="agreement" required class="check-styled">
                <div class="q-text-group">
                  <span class="q-text-jp">記載内容に同意します。</span>
                  <span class="q-text-kr">기재 내용에 동의합니다.</span>
                </div>
              </label>
            </div>
          </div>
        </div>

        <div v-if="errorMessage" class="login-error ed-error">
          <span class="err-icon">⚠ 審査不可 - 심사 불가</span><br>
          {{ errorMessage }}
        </div>

        <!-- 하단 영역 -->
        <div class="ed-footer">
          <!-- 서명(Signature) 밑줄형 입력란 -->
          <div class="signature-area">
             <div class="sig-label">
                <span class="sig-jp-lg">署名</span>
                <span class="sig-kr-sub">서명</span>
             </div>
             <div class="sig-input-wrap input-extralong">
               <input id="signatureText" type="text" v-model="signatureText" class="sig-input" placeholder="例) タビ太郎 / 예) 타비타로" required />
             </div>
          </div>

          <!-- 제출 버튼 및 도장 컨테이너 (relative) -->
          <div class="action-row">
            <!-- 5️⃣ CTA 버튼 강화 -->
            <button type="submit" class="stamp-btn" :disabled="loading || isApproved || !agreement">
               <span v-if="loading" class="btn-loader"></span>
               <span v-else class="btn-text">
                  <span class="btn-jp">✈ 入国申請</span>
                  <span class="btn-kr">입국 신청</span>
               </span>
               <span v-if="loading" style="margin-left: 8px;">審査中...</span>
            </button>
            
            <!-- 🎯 입국허가 도장 (버튼 우측에 배치) -->
            <div class="stamp-circular" :class="{ 'is-visible': isApproved }">
              <div class="stamp-inner">
                <span class="st-top">入国許可</span>
                <span class="st-bot">旅Quiz</span>
              </div>
            </div>
          </div>
        </div>
      </form>

      <!-- 6️⃣ 로그인 링크 세계관 통일 (맨 아래 작은 텍스트) -->
      <div class="back-link-row">
        <span class="login-link-dimmed">
          이미 입국 기록이 있으신가요? 
          <RouterLink to="/login" class="login-link-text">入国記録照会 (로그인)</RouterLink>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useAuthStore } from "../stores/authStore";

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const email = ref("");
const nickname = ref("");
const password = ref("");
const passwordConfirm = ref("");
const signatureText = ref("");

const agreement = ref(false);
const loading = ref(false);
const errorMessage = ref("");
const isApproved = ref(false); 

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
const NICKNAME_REGEX = /^[가-힣a-zA-Z0-9_]+$/;

watch(nickname, (newVal) => {
  if (!isApproved.value && document.activeElement.id !== 'signatureText') {
    signatureText.value = newVal;
  }
});

function validateForm() {
  if (!EMAIL_REGEX.test(email.value)) return "이메일 형식이 올바르지 않습니다.";
  if (nickname.value.length < 2 || nickname.value.length > 50) return "여권 이름(닉네임)은 2자 이상 50자 이하입니다.";
  if (!NICKNAME_REGEX.test(nickname.value)) return "여권 이름은 한글/영문/숫자/언더스코어만 가능합니다.";
  if (password.value.length < 8) return "암호는 8자 이상입니다.";
  if (!PASSWORD_REGEX.test(password.value)) return "암호는 영문/숫자/특수문자를 모두 포함해야 합니다.";
  if (password.value !== passwordConfirm.value) return "암호 확인이 일치하지 않습니다.";
  if (!signatureText.value.trim()) return "서명을 기재해주세요.";
  if (!agreement.value) return "기재 내용에 동의해야 합니다.";
  return "";
}

async function onSubmit() {
  const validationMessage = validateForm();
  if (validationMessage) {
    errorMessage.value = validationMessage;
    return;
  }

  try {
    loading.value = true;
    errorMessage.value = "";
    
    await authStore.register(email.value, nickname.value, password.value);
    
    // 심사 통과 애니메이션 트리거
    isApproved.value = true;
    
    setTimeout(() => {
      const redirect = route.query.redirect || "/quiz/start";
      router.push(String(redirect));
    }, 1800);

  } catch (error) {
    const status = error?.response?.status;
    const code = error?.response?.data?.code;
    const serverMessage = error?.response?.data?.message;

    if (status === 400) {
      errorMessage.value = "입력 정보가 거부되었습니다. 규정을 확인하세요.";
    } else if (status === 409) {
       errorMessage.value = "이미 가입된 이메일 또는 닉네임입니다.";
    } else {
      errorMessage.value = serverMessage || "네트워크 오류입니다. 다시 시도해주세요.";
    }
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
/* ── 입국신청서 원본 테마 (화이트/그레이/레드) ── */
.login-page {
  min-height: 100vh;
  min-height: 100dvh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 16px; /* 7️⃣ 상하단 여유 공간 확보 (Footer와 거리 등) */
  background: transparent;
  position: relative;
  font-family: Pretendard, "Noto Sans KR", Arial, sans-serif;
}

.ed-card {
  position: relative;
  width: min(800px, 100%);
  background: rgba(255, 255, 255, 0.98); 
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow:
    0 20px 60px rgba(15, 23, 42, 0.1),  
    0 8px 24px rgba(0, 0, 0, 0.04),  
    0 2px 6px rgba(0, 0, 0, 0.04);
  padding: 56px 48px; 
  color: #1e293b; 
  animation: cardEntrance 0.5s cubic-bezier(0.2, 0.8, 0.2, 1) both;
}

.card-topline {
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 6px;
  background: #b91c1c; 
  border-radius: 12px 12px 0 0;
}

@keyframes cardEntrance {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.pointer-events-none { pointer-events: none; }


/* 1️⃣ 3단 타이틀 계층 (한 덩어리) */
.ed-header {
  margin-bottom: 40px; 
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px; /* 요소들을 바짝 붙여 한 덩어리로 */
}
.ed-title-brand {
  font-size: 14px; font-weight: 800; color: #b91c1c; letter-spacing: 2px;
}
.ed-title-doc {
  font-size: 32px; font-weight: 900; letter-spacing: 4px; color: #1e293b; line-height: 1.2; margin: 0;
}
.ed-title-sub {
  font-size: 14px; font-weight: 600; color: #64748b; letter-spacing: 0.5px; margin: 4px 0 0 0;
}


/* 폼 테이블 공통 */
.ed-table {
  border: 1.5px solid #cbd5e1; 
  border-radius: 4px; /* 조금 더 서류다운 각진 모서리 */
  overflow: hidden; 
  display: flex;
  flex-direction: column;
  background: transparent;
  margin-bottom: 48px; 
}

/* 2️⃣ 섹션 구분선 (무게감, tracking, 상하 여백 조정) */
.section-divider {
  background: rgba(241, 245, 249, 0.9);
  padding: 12px 16px;
  border-bottom: 1.5px solid #cbd5e1;
}
.divider-text {
  font-size: 12px;
  font-weight: 700;
  color: #475569;
  letter-spacing: 2px; /* tracking-widest */
  text-transform: uppercase;
  display: block;
}

.ed-row { display: flex; width: 100%; border-bottom: 1.5px solid #cbd5e1; }
.ed-row:last-child { border-bottom: none; }
.border-top { border-top: 1.5px solid #cbd5e1; }
.border-right { border-right: 1.5px solid #cbd5e1; }

/* 행 헤더 */
.ed-col-head {
  width: 160px; /* 약간 넓혀서 안정감 */
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px 12px; 
  background: rgba(241, 245, 249, 0.5);
}
.txt-jp-lg {
  font-size: 16px;
  font-weight: 900;
  letter-spacing: 1px;
  color: #1e293b;
  margin-bottom: 4px;
  text-align: center;
}
.txt-kr-sub {
  font-size: 11px;
  font-weight: 600;
  color: #64748b;
  letter-spacing: 0.5px;
  text-align: center;
}

/* 폼 입력 블록 */
.ed-col-field {
  padding: 24px 32px; 
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex: 1;
}

.input-wrap { 
  display: flex; 
  align-items: center;
}

/* 3️⃣ 입력 필드 폭(리듬감) 설정 */
.input-short { max-width: 240px; width: 100%; }
.input-medium { max-width: 380px; width: 100%; }
.input-long { max-width: 480px; width: 100%; }
.input-extralong { width: 100%; max-width: 560px; }

/* 서류 양식에 맞춘 하얀 인풋 */
.ed-input {
  width: 100%; 
  padding: 12px 14px;
  border: 1px solid #cbd5e1; 
  border-radius: 4px; 
  background: #ffffff; 
  color: #1e293b; 
  font-size: 15px; 
  font-weight: 600;
  transition: all 0.2s ease;
  box-shadow: inset 0 1px 2px rgba(0,0,0,0.02); 
}
.ed-input::placeholder { color: #94a3b8; font-weight: 500;}
.ed-input:focus {
  outline: none; 
  border-color: #b91c1c; 
  background: #fff;
  box-shadow: 0 0 0 3px rgba(185, 28, 28, 0.1); 
}


/* 4️⃣ 체크박스 확인란 박스화 */
.question-section-wrap {
  background: #fdfbf7; /* 전체 행 연한 배경 */
  padding: 24px 32px;
}

.q-row {
  width: 100%;
}
.q-check-box {
  display: flex;
  align-items: center;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.6); 
  padding: 16px 24px;
  border-radius: 6px;
  border: 1px dashed #cbd5e1; /* 점선 테두리 확실히 */
  width: 100%;
  transition: all 0.2s ease;
}
.q-check-box:hover {
  background: #ffffff;
  border-color: #b91c1c;
}
.check-styled {
  margin-right: 16px;
  accent-color: #b91c1c; 
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.q-text-group {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.q-text-jp {
  font-size: 15px;
  font-weight: 900;
  color: #1e293b;
}
.q-text-kr {
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
}

/* 에러 메시지 */
.login-error {
  text-align: center; color: #b91c1c; font-size: 13px; font-weight: 600;
  margin-bottom: 24px; padding: 12px 14px; border-radius: 8px;
  background: rgba(185, 28, 28, 0.05); border: 1px solid rgba(185, 28, 28, 0.1);
}

/* 하단 영역 (서명 + 버튼) */
.ed-footer {
  margin-top: 16px;
}

/* 서명(Signature) 입력 박스 */
.signature-area {
  display: flex;
  align-items: flex-end; 
  width: 100%;
  margin-bottom: 48px; 
}
.sig-label {
  display: flex;
  flex-direction: column;
  margin-right: 24px;
  justify-content: flex-end;
  min-width: 60px;
}
.sig-jp-lg { font-size: 24px; font-weight: 900; color: #1e293b; line-height: 1;}
.sig-kr-sub { font-size: 14px; font-weight: 600; color: #64748b; margin-top: 6px;}

.sig-input-wrap {
  flex: 1;
}
.sig-input {
  width: 100%;
  border: none;
  border-bottom: 2px solid #1e293b; 
  background: transparent; 
  padding: 8px 12px 4px;
  font-size: 20px;
  font-weight: 700;
  color: #0f172a;
  outline: none;
  transition: border-color 0.2s ease;
  font-family: inherit;
  border-radius: 0;
}
.sig-input::placeholder { color: #cbd5e1; font-weight: 500; font-size: 18px; }
.sig-input:focus { border-bottom-color: #b91c1c; } 


/* 5️⃣ 제출 버튼 및 🎯 입국허가 도장 컨테이너 */
.action-row {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-bottom: 0px;
  position: relative; /* 도장 위치 잡기 위한 기준 */
}

.stamp-btn {
  width: 100%;
  max-width: 360px; 
  padding: 20px 24px; 
  border: none; 
  border-radius: 8px; 
  background-color: #b91c1c; /* 단색 레드 강력하게 */
  color: #ffffff; 
  cursor: pointer;
  transition: all 0.2s ease;
  min-height: 72px;
  display: flex; align-items: center; justify-content: center;
  box-shadow: 0 4px 12px rgba(185, 28, 28, 0.3); /* 약간의 shadow */
}

.btn-text {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1.2;
}
.btn-jp { font-size: 20px; font-weight: 900; letter-spacing: 4px; margin-left: 4px;}
.btn-kr { font-size: 13px; font-weight: 600; opacity: 0.9; margin-top: 4px; letter-spacing: 1px;}

.stamp-btn:hover:not(:disabled) {
  background-color: #a41818; 
  transform: translateY(2px); /* hover시 살짝 눌림 */
  box-shadow: 0 2px 4px rgba(185, 28, 28, 0.3);
}
.stamp-btn:active:not(:disabled) { 
  transform: translateY(4px); 
  box-shadow: none; 
}
.stamp-btn:disabled { opacity: 0.5; cursor: not-allowed; transform: none; box-shadow: none; }

.btn-loader {
  width: 24px; height: 24px; border: 3px solid rgba(255,255,255,0.3);
  border-top-color: #fff; border-radius: 50%; animation: spin 0.6s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* 🎯 입국허가 도장 (버튼 기준 우측 absolute, 기본 opacity 0) */
@keyframes stamp-bang {
  0% { opacity: 0; transform: scale(1.6) rotate(-18deg); filter: blur(2px); }
  60% { opacity: 1; transform: scale(0.92) rotate(-12deg); filter: blur(0px); }
  100% { opacity: 0.9; transform: scale(1.0) rotate(-12deg); }
}

.stamp-circular {
  position: absolute;
  right: calc(50% - 280px); /* 버튼 우측으로 계산 */
  top: -10px; /* 버튼 상단에 걸치게 */
  width: 96px;
  height: 96px;
  border-radius: 50%;
  color: #b91c1c;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
  pointer-events: none;
  
  opacity: 0; /* 평소엔 아예 안보임 */
  transform: rotate(-12deg);
}
.stamp-inner {
  border: 2.5px solid #b91c1c;
  border-radius: 50%;
  width: 82px;
  height: 82px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.stamp-circular.is-visible {
  border: 3.5px solid rgba(185, 28, 28, 0.9); 
  animation: stamp-bang 0.4s cubic-bezier(0.25, 1, 0.5, 1) forwards;
}
.stamp-circular.is-visible .stamp-inner {
  border: 4px dotted rgba(185, 28, 28, 0.9); 
}
.st-top { font-size: 16px; font-weight: 900; letter-spacing: 2px; line-height: 1.2; font-family: "Noto Serif", serif; color: inherit; }
.st-bot { font-size: 13px; font-weight: 800; letter-spacing: 1px; line-height: 1; margin-top: 4px; border-top: 2px solid currentColor; padding-top: 4px; font-family: "Noto Serif", serif; color: inherit;}


/* 6️⃣ 로그인 링크 세계관 통일 */
.back-link-row {
  margin-top: 48px;
  text-align: center;
}
.login-link-dimmed {
  font-size: 13px;
  color: #64748b;
  font-weight: 500;
}
.login-link-text {
  color: #0f172a;
  text-decoration: underline;
  margin-left: 6px;
  font-weight: 700;
  transition: color 0.2s ease;
}
.login-link-text:hover { color: #b91c1c; }

/* ── 반응형 (모바일) ── */
@media (max-width: 768px) {
  .ed-card { padding: 40px 24px; border-radius: 12px; }
  .ed-row { flex-direction: column; }
  .ed-col-head { 
    width: 100%; border-right: none; border-bottom: 1.5px solid #cbd5e1; 
    flex-direction: row; gap: 12px; justify-content: flex-start;
    padding: 16px 20px;
  }
  
  .input-short, .input-medium, .input-long, .input-extralong { max-width: 100%; }
  .ed-col-field { padding: 24px 20px; }
  .question-section-wrap { padding: 20px; }
  
  .stamp-circular { right: 10px; top: -30px; width: 80px; height: 80px; }
  .stamp-inner { width: 68px; height: 68px; }
  .st-top { font-size: 14px; }
  .st-bot { font-size: 11px; }
  
  .signature-area { flex-direction: column; align-items: flex-start; gap: 12px; }
  .sig-label { text-align: left; }
}
</style>
