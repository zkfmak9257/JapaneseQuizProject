  pipeline {
      agent any

      stages {
          stage('Checkout') {
              steps {
                  checkout scm
              }
          }

          stagangseonghun@gangseonghun-ui-MacBookAir JapaneseQuizProject % codex

             ╭─────────────────────────────────────────────╮
             │ >_ OpenAI Codex (v0.104.0)                  │
             │                                             │
             │ model:     gpt-5.3-codex   /model to change │
             │ directory: ~/JapaneseQuizProject            │
             ╰─────────────────────────────────────────────╯

               Tip: Start a fresh idea with /new; the previous session stays in history.


             › 일단 우리프로젝트 분석좀해줘



             • Explored
               └ List ls -la
                 List rg --files
                 List .

             ────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 백엔드/프론트엔드가 모두 있는 구조로 확인되었습니다. 이제 README, build.gradle, application.yml, 핵심 보안/도메인 코드를 읽어서 실제 아키
               텍처와 현재 상태를 정확히 정리하겠습니다.



             • 핵심 설정은 확인했고, 지금부터는 실제 도메인 흐름(인증, 퀴즈, 신고, 통계)과 CQRS 분리 수준을 코드 기준으로 점검하겠습니다. 컨트롤러/서비
               스/엔티티를 묶어서 읽겠습니다.


             • Explored
               └ Read AuthController.java, MemberCommandService.java, QuizCommandService.java, QuizQueryService.java, ApiResponse.java, ErrorCode.java, GlobalExceptionHandler.java
                 Search @RestController|@RequestMapping|@GetMapping|@PostMapping|@PutMapping|@PatchMapping|@DeleteMapping in jpquiz
                 Read JpaConfig.java, MybatisConfig.java, Member.java, ProblemReportCommandService.java

             ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 도메인 서비스는 확인했고, 프론트엔드가 백엔드 구조와 얼마나 잘 맞물리는지도 함께 봐야 정확한 분석이 됩니다. 라우팅/인증 저장소/API 클라이언트 계층을 확인하겠습니다.

             • Explored
               └ Read authStore.js, package.json, index.js, http.js

             ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 현재 구조는 파악되었고, 코드 품질 분석 정확도를 위해 테스트/빌드 상태를 한 번 확인하겠습니다. 동시에 통계/신고 모듈과 데이터 접근 계층(CQRS: Command Query Responsibility
               Segregation, 명령과 조회 책임 분리) 구현 일관성도 점검하겠습니다.

             • Explored
               └ Read StatsController.java, StatsQueryService.java, AdminProblemReportController.java, ProblemReportQueryService.java

             ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 코드 구조 분석은 충분히 수집되었고, 실제 동작 신뢰도를 위해 백엔드 테스트를 실행해 현재 안정성을 확인하겠습니다. 실패 항목이 있으면 분석에 포함하겠습니다.

             • Ran ./gradlew test
               └ Exception in thread "main" java.io.FileNotFoundException: /Users/gangseonghun/.gradle/wrapper/dists/gradle-8.14-bin/38aieal9i53h9rfe7vjup95b9/gradle-8.14-bin.zip.lck
                 (Operation not permitted)
                 … +3 lines
                     at java.base/java.io.RandomAccessFile.<init>(RandomAccessFile.java:223)
                     at org.gradle.wrapper.GradleWrapperMain.main(SourceFile:67)

             ✔ You approved codex to run ./gradlew test this time

             • Ran ./gradlew test
               └ Starting a Gradle Daemon, 1 incompatible Daemon could not be reused, use --status for details
                 > Task :compileJava UP-TO-DATE
                 … +7 lines
                 BUILD SUCCESSFUL in 14s
                 4 actionable tasks: 1 executed, 3 up-to-date

             ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 백엔드 테스트는 성공했습니다. 프론트엔드도 빌드 기준으로 점검해서 전체 프로젝트 완성도를 같이 평가하겠습니다.

             • Ran npm run build
               └
                 > jpquiz-frontend@0.0.1 build
                 … +23 lines
                 dist/assets/index-D5DEqkZg.js        435.04 kB │ gzip: 151.59 kB
                 ✓ built in 2.27s

             • Explored
               └ Search \. mission-btn|mission-btn in frontend

             ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 프론트 빌드 경고 원인까지 확인했습니다. 마지막으로 DB 스키마와 매퍼를 읽어 백엔드 모델과 정합성(컬럼/상태값/조인 구조)을 점검한 뒤 종합 분석을 드리겠습니다.

             • Explored
               └ Read schema.sql, ProblemReportMapper.xml, StatsMapper.xml, QuizCommandMapper.xml, SecurityUtil.java, JwtAuthenticationFilter.java, JwtTokenProvider.java,
             MemberController.java,
                      ProblemReportCommandServiceTest.java, QuizController.java, SecurityConfig.java, GlobalExceptionHandler.java
                 Search GUEST_QUIZ_LIMIT_EXCEEDED|/api/health|printStackTrace\(|@EnableJpaAuditing|ORDER BY RAND\(|JpaConfig|MybatisConfig in java
                 Read WrongAnswerListView.vue, QuizCommandMapper.xml, JpQuizApplication.java, ProblemReportCommandServiceTest.java, HealthController.java
                 List test

             ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 1) 전체 개념 요약
               현재 프로젝트는 백엔드(Spring Boot, 스프링 부트) + 프론트엔드(Vue, 뷰) 모노레포(Monorepo, 단일 저장소) 구조이며, 퀴즈 풀이/회원/신고/통계를 중심으로 설계되어 있습니다.
               핵심 아키텍처는 CQRS(Command Query Responsibility Segregation, 명령·조회 책임 분리) 의도를 반영했고, 인증은 JWT(JSON Web Token, 토큰 기반 인증) 기반입니다.

               2) 왜 필요한지 (문제 -> 해결)
               문제는 퀴즈 도메인이 “상태 변경(제출, 완료, 신고 처리)”과 “조회(문항 조회, 통계 조회)”가 동시에 많다는 점입니다.
               해결로 command/query 패키지 분리, JPA(Java Persistence API, 자바 ORM 표준)+MyBatis(마이바티스, SQL 매퍼) 병행, ApiResponse/ErrorResponse 표준화를 사용해서 복잡도를 통제하고 있
               습니다.

               3) 구조 설명

               - 계층 분리는 presentation -> application -> infrastructure 흐름이 비교적 잘 지켜집니다.
               - SRP(Single Responsibility Principle, 단일 책임 원칙) 관점에서 컨트롤러는 DTO 반환 중심으로 책임이 좁게 유지됩니다.
               - 보안은 SecurityFilterChain(시큐리티 필터 체인) + JwtAuthenticationFilter 조합으로 무상태(Stateless, 세션 미사용) 처리합니다.
               - 프론트는 router(라우터) + pinia(상태 저장소) + axios interceptor(요청/응답 가로채기)로 인증 만료 재발급 흐름이 구현돼 있습니다.
               - 확장 시 주요 변경 포인트:
                   - 새 도메인: presentation/application/query|command/infrastructure 패키지 추가
                   - 인증 정책: 보안 매처와 필터 정책 조정
                   - 통계 방식: MyBatis SQL만 교체해도 API 계약 유지 가능
               - 레거시 방식(Legacy, 혼합 서비스 직접 SQL) 대비 모던 방식(Modern, 계층/책임 분리)의 선택 근거는 유지보수성과 변경 영향도 축소입니다.

               4) 코드 기반 확인 결과

               - 보안 정책 핵심: SecurityConfig.java (/Users/gangseonghun/JapaneseQuizProject/src/main/java/com/team/jpquiz/global/security/SecurityConfig.java#L90)
               - 공통 예외: GlobalExceptionHandler.java (/Users/gangseonghun/JapaneseQuizProject/src/main/java/com/team/jpquiz/global/error/GlobalExceptionHandler.java#L13)
               - 퀴즈 쓰기 흐름: QuizCommandService.java (/Users/gangseonghun/JapaneseQuizProject/src/main/java/com/team/jpquiz/quiz/command/application/QuizCommandService.java)
               - 퀴즈 읽기 흐름: QuizQueryService.java (/Users/gangseonghun/JapaneseQuizProject/src/main/java/com/team/jpquiz/quiz/query/application/QuizQueryService.java)
               - 통계 집계 SQL: StatsMapper.xml (/Users/gangseonghun/JapaneseQuizProject/src/main/resources/mappers/stats/StatsMapper.xml)
               - DB 스키마: schema.sql (/Users/gangseonghun/JapaneseQuizProject/db/schema.sql)
               - 백엔드 테스트 실행: ./gradlew test 성공
               - 프론트 빌드 실행: npm run build 성공(경고 1건)

               5) 실무 장단점
               장점

               - 책임 분리와 DTO 정책이 명확해 협업/확장에 유리합니다.
               - JWT 재발급 회전(Refresh Token Rotation, 리프레시 토큰 교체)까지 반영되어 보안 기본기가 좋습니다.
               - 통계 쿼리에 윈도 함수(Window Function, 행 순위 함수)를 사용해 FIRST/LATEST 기준 분석이 가능합니다.

               리스크/개선 포인트

               - /api/health 엔드포인트는 있는데 보안 permitAll에 누락되어 운영 헬스체크와 충돌 가능성이 있습니다.
                 HealthController.java (/Users/gangseonghun/JapaneseQuizProject/src/main/java/com/team/jpquiz/common/presentation/HealthController.java#L10), SecurityConfig.java (/Users/
                 gangseonghun/JapaneseQuizProject/src/main/java/com/team/jpquiz/global/security/SecurityConfig.java#L93)
               - 예외 처리에서 printStackTrace() 사용은 운영 로그 품질/보안에 불리합니다.
                 GlobalExceptionHandler.java (/Users/gangseonghun/JapaneseQuizProject/src/main/java/com/team/jpquiz/global/error/GlobalExceptionHandler.java#L37)
               - ORDER BY RAND()는 데이터 증가 시 성능 저하 가능성이 큽니다.
                 QuizCommandMapper.xml (/Users/gangseonghun/JapaneseQuizProject/src/main/resources/mappers/quiz/QuizCommandMapper.xml#L34)
               - 프론트 CSS 오타로 빌드 경고가 발생합니다(. mission-btn).
                 WrongAnswerListView.vue (/Users/gangseonghun/JapaneseQuizProject/frontend/src/views/WrongAnswerListView.vue#L650)
               - 테스트 파일이 1개라 회귀 방어력이 약합니다.
                 ProblemReportCommandServiceTest.java (/Users/gangseonghun/JapaneseQuizProject/src/test/java/com/team/jpquiz/report/command/application/
                 ProblemReportCommandServiceTest.java#L1)

               성능/확장성/유지보수 코멘트

               - 현재 구조는 유지보수성은 양호하지만, 문제 수가 커질 때 랜덤 추출 SQL과 통계 대용량 집계 SQL이 병목이 될 가능성이 높아 인덱스·샘플링 전략 고도화가 필요합니다.

               원하시면 다음 단계로 1) 즉시 수정이 필요한 3개 이슈(health 공개, printStackTrace 제거, CSS 오타)부터 제가 바로 패치해드리겠습니다.



             › # 1. DevOps

               ## 1-1. 과거와 현재

               - 과거에는 새로운 서비스 출시를 위해 오랜 시간 개발하고 배포하는데 많은 시간을 소요해야 했다.(코드개발, 테스트 등)
               - 전통적인 개발 및 서비스 인도까지의 프로세스는 어느 팀도 제품 전체의 완료를 책임지지 않는다.
                   - **개발팀**에게 ‘완료’의 의미는 **요구사항을 구현**하는 것이다.
                   - **QA팀**에게 ‘완료’의 의미는 **코드를 테스트**하는 것이다.
                   - **운영팀**에게 ‘완료’의 의미는 **코드를 릴리스**하는 것이다.
               - 현재는 서비스 출시 속도가 빠르고 업데이트 주기가 빈번하다.
               - 현재의 트렌드에 맞게 개발된 소프트웨어가 **안정성을 유지**하면서도 사용자에게 **신속하게 제공**될 수 있도록 개발, 테스트, 배포, 운영의 업무 사이클을 자동화된 하나의 프로세
               스로 통합할 필요성이 생겼다.

               ## 1-2. DevOps란

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/157ec4f8-a195-4068-bb04-33bd1fe672c5/Untitled.png)

               <aside>
               💡 단절된 개발과 운영 간의 프로세스를 원활하게 연결하고 자동화 방법을 통해 효율성을 극대화하는 방법(서로 다른 업무의 통합)이다.
               DevOps는 소프트웨어 개발(Dev)과 IT 운영(Ops)의 경계를 허물고, 지속적인 통합(CI), 지속적인 배포(CD), 자동화 등을 통해 더 빠른 소프트웨어 개발과 더 높은 운영 효율을 달성하려는
               문화, 움직임, 관행의 집합이다.

               </aside>

               ## 1-3. DevOps의 핵심 요소(CAMS)

               <aside>
               💡  DevOps의 핵심 원칙을 나타내는 약어로, Culture (문화), Automation (자동화), Measurement (측정), Sharing (공유)의 첫 글자를 따서 만들어졌다. 이 네 가지 요소는 DevOps가 추구
               하는 가치와 실천 방식이다.

               </aside>

               1. **Culture(문화)**
               : DevOps 문화는 **`협업과 의사소통을 중시`**한다.
               개발(Dev) 팀과 운영(Ops) 팀 간의 장벽을 허물고, 모든 이해관계자가 목표 달성을 위해 긴밀하게 협력하도록 장려하는 문화이다. 이는 조직 내 신뢰 구축, 공통 목표에 대한 집중, 지속
               적인 개선을 추구하는 환경을 조성한다.
               (각 부서 또는 조직이 본인들의 이익만을 우선 시하는 사일로화 되는 것을 방지해야 한다.)
               2. **Automation(자동화)**
               : DevOps에서는 **`반복 가능하고 예측 가능한 작업을 자동화`**함으로써, 수동 작업의 오류를 줄이고 효율성을 높인다.
               인프라 관리, 코드 배포, 테스트 실행, 모니터링 등 다양한 프로세스에 자동화를 적용한다. 자동화를 통해 개발부터 배포까지의 프로세스가 더 빠르고, 안정적이며, 효율적으로 이루어진
               다.
               (작업자의 실수를 예방하고 서비스의 일관성을 유지한다.)
               3. **Measurement(측정)**
               : 성능, 프로세스, 효과성을 **`정량적으로 측정하고 분석하는 것을 강조`**한다.
               이는 개선점을 식별하고, 의사 결정 과정에 필요한 데이터를 제공한다. DevOps에서는 지속적인 측정과 피드백이 중요하며, 시스템의 성능, 응답 시간, 버그 발생 빈도 등을 모니터링하고,
               이를 기반으로 지속적인 개선을 도모한다.
               (측정할 수 없으면 관리할 수 없고 관리할 수 없으면 개선 시킬 수 없다.)
               4. **Sharing(공유)**
               : **`지식, 아이디어, 성공 사례뿐만 아니라 실패 경험까지 공유하는 것을 중요`**시한다.
               이는 학습과 성장을 촉진하고, 조직 전체의 개선을 위한 아이디어와 해결책을 발굴하는 데 도움이 된다. 공유 문화는 DevOps의 핵심 요소 중 하나로, 팀 간의 벽을 허물고 조직 전체가 하
               나의 목표를 향해 나아가도록 한다.
               (협업 툴을 충분히 활용해야 한다.)

               ## 1-4. 소프트웨어 개발/아키텍처/배포/인프라 패러다임의 진화

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/5e6d9f71-d357-448a-a36f-8bf6337a84cc/Untitled.png)

               - 1980년대까지 개발 프로세스는 주로 워터폴 모델을 따랐고, 애플리케이션은 일체형(모놀리식) 아키텍처로 구축되었다. 이러한 애플리케이션들은 물리적 서버에 배포되고, 데이터센터 환
               경에서 운영되었다.
               - 1990년대에는 애자일 개발 방법론이 소개되면서 개발 프로세스가 점차 변화하기 시작했다. 아키텍처는 N-계층으로 발전하여 더 모듈화된 구조를 갖게 되었고, 가상 서버 기술이 등장하
               여 배포와 패키징에 변화를 가져왔다. 이 시기에는 호스팅 서비스가 인기를 얻기 시작했다.
               - 2000년대에 들어서면서 DevOps 운동이 시작되었다. 이는 개발과 운영의 경계를 허물고, CI/CD의 개념을 통해 지속적인 통합과 배포를 중요시했다. 마이크로서비스 아키텍처가 등장하여
               서비스들이 더 작고 독립적인 단위로 분리되었고, 컨테이너 기술이 배포 방법을 혁신했다.

               ## 1-5. DevOps Toolchain

               - **소스 코드 관리**: Git, GitHub 등
               - **CI 서버**: Jenkins, GitHub Actions 등
                   - 빌드, 테스트, 배포 과정의 자동화 서버
               - **컨테이너·이미지 관리**: Docker, AWS ECR 등
                   - 애플리케이션과 그 종속성을 격리된 환경인 컨테이너에 담아 실행
                   - 애플리케이션의 실행 환경을 코드화하고, 다른 환경에서도 동일한 방식으로 실행될 수 있도록 보장
               - **인프라 코드화(IaC)**: Terraform, Ansible 등
                   - IaC(Infrastructure as Code)는 인프라를 코드로 관리하고 배포하는 방법으로 서버, 네트워크, 스토리지 등 인프라를 코드로 정의하고 자동으로 배포
                   - 수동 설정의 위험을 줄이고, 환경을 코드화하여 일관성 있는 환경을 제공하여 재현 가능, 버전 관리 가능
               - **오케스트레이션·배포**: Kubernetes, ArgoCD 등
                   - 여러 개의 컨테이너를 배포하고, 모니터링하고, 자동으로 확장하는 등의 작업을 처리
               - **테스트 자동화**: JUnit, Selenium 등
               - **모니터링·로그**: Prometheus, Grafana, ELK 스택(Elasticsearch, Logstash, Kibana) 등
                   - 로그 수집 및 분석, 시각화 등으로 시스템의 상태를 지속적으로 점검하고, 발생할 수 있는 문제를 빠르게 해결
               - **협업·알림**: Slack, Teams 등

               ## 1-6. CI/CD 파이프라인

               <aside>
               💡 CI (Continuous Integration, 지속적 통합)와 CD (Continuous Delivery 또는 Continuous Deployment, 지속적 제공 또는 지속적 배포)는 소프트웨어 개발의 현대적 접근 방식에서 중요
               한 역할을 한다. 이들은 개발의 효율성을 높이고, 소프트웨어의 품질을 개선하며, 배포 과정을 자동화하여 더 빠른 속도로 시장에 출시할 수 있게 돕는다.

               </aside>

               - **CI (Continuous Integration)**: 개발자들이 작업한 코드를 주기적으로 (보통은 하루에 여러 번) 메인 저장소에 통합(머지)하는 것을 의미한다. 이 과정에서 자동화된 빌드와 테스트
               가 수행되어, 코드 변경 사항이 시스템에 통합될 때 발생할 수 있는 문제를 조기에 발견하고 수정한다.
               - **CD (Continuous Delivery)**: CI 과정을 통해 테스트된 코드를 자동으로 빌드하고 테스트 환경이나 스테이징 환경에 배포하는 것을 의미한다. 목표는 소프트웨어가 항상 출시 준비 상
               태를 유지하도록 하는 것이다.
               - **CD (Continuous Deployment)**: Continuous Delivery의 한 단계 더 나아가, 자동화된 테스트를 통과한 코드를 자동으로 프로덕션 환경에 배포하는 것이다. 이 과정은 수동 개입 없이
               이루어진다.
               - **장점**
                   - **개발 속도와 효율성 향상**: 자동화를 통해 개발 과정이 더 빠르고 효율적이 되며, 개발자는 코드 작성에 더 집중할 수 있다.
                   - **품질 개선**: 자동화된 테스트와 지속적인 통합을 통해 소프트웨어의 품질이 개선된다.
                   - **배포 속도 증가**: 자동화된 배포 과정을 통해 새로운 기능과 수정 사항을 빠르게 시장에 출시할 수 있다.
               - **단점**
                   - **시간과 비용**: CI/CD 파이프라인을 설계하고 구현하는 데 초기에 상당한 시간과 비용이 소요된다. 적절한 도구 선택, 시스템 구성, 그리고 팀 교육에 대한 투자가 필요하다.
                   - **학습 곡선**: 개발팀이 CI/CD 도구와 방법론에 익숙해지기까지는 시간이 걸리며, 초기에는 생산성이 저하될 수 있다.
                   - **파이프라인 유지 관리**: 다수의 도구와 프로세스를 관리해야 하며, 파이프라인의 복잡성은 시간이 지남에 따라 증가할 수 있다. 이로 인해 유지 관리가 어려워질 수 있다.

               ### CI/CD 파이프라인 단계

               1. **코드 커밋 & 브랜치 관리**
               2. **자동 빌드**
               3. **정적 분석 & 단위 테스트**
               4. **통합 테스트 & 배포 패키징**
               5. **자동화된 프로모션(스테이징→프로덕션)**
               6. **롤백 & 배포 전략**(블루-그린, 카나리아, 롤링 업데이트)

               <aside>
               💡

               DevOps는 단순한 도구나 기술에 그치지 않고, **문화와 관행**의 변화이다.

               </aside>

               - Docker, Kubernetes, Jenkins와 같은 도구는 DevOps의 원활한 구현을 돕지만, 그 이면에는 **협업과 자동화, 지속적인 피드백**이 뒷받침되어야 한다.
               - **개발자**와 **운영팀**은 더 이상 각자의 영역에만 갇히지 않고, 하나의 팀으로 협업하며, 보다 빠르고 안정적인 배포가 가능해진다.
               - **자동화**는 반복적인 작업을 제거하고, **빠른 피드백**을 통해 품질을 개선하며, **지속적인 개선**을 이루는 핵심 요소다.
               - DevOps 환경에서 개발자는 더 빠르고, 효율적으로 소프트웨어를 개발하고, 그 결과물은 사용자에게 신속하게 전달될 수 있다.# 1. ELK for Logging

               ![img.png](attachment:1d01fdfb-63d2-413c-8fed-5a91a5e61044:img.png)

               ## 1-1. ELK란?

               <aside>
               💡

               ELK는 **Elasticsearch**, **Logstash**, **Kibana**로 구성된 로그 수집 및 분석 스택이다. 애플리케이션 로그를 수집하고 저장한 뒤, 실시간으로 검색하고 시각화하는 데 사용된다. 실
               시간 로그 분석, 성능 추적, 장애 원인 파악 등에 활용된다.

               </aside>

               - **Elasticsearch**: JSON 기반 로그 저장 및 검색 (Log 저장용 DB)
               - **Logstash**: 다양한 소스의 로그 수집 및 구조화
               - **Kibana**: 시각화 도구로 대시보드 생성

               ## 1-2. ELK 구성 흐름

               ```
               Spring Boot → Logstash → Elasticsearch → Kibana
               ```

               Spring Boot 로그를 JSON 형식으로 Logstash로 전송하고, Logstash는 이를 Elasticsearch에 저장하며, Kibana는 Elasticsearch를 시각화한다.

               ## 1-3. Logstash 구조

               **기본 구조**

               ```
               input {
                 tcp {
                   port => 5000
                   codec => json
                 }
               }

               output {
                 elasticsearch {
                   hosts => ["elasticsearch:9200"]
                   index => "order-service-%{+YYYY.MM.dd}"
                 }
               }
               ```

               - input 블록 : Spring Boot가 `logback-spring.xml`을 통해 JSON 로그를 `logstash:5000`으로 전송함
                   - tcp: 로그 데이터를 TCP 프로토콜을 통해 수신
                   - port => 5000: Logstash가 수신 대기할 포트 (Spring Boot 로그 전송 대상 포트)
                   - codec => json: 수신되는 데이터가 JSON 형식임을 명시하여 자동 파싱
               - output 블록 : Logstash가 수신한 로그를 Elasticsearch에 하루 단위로 저장하도록 설정
                   - elasticsearch: 로그를 Elasticsearch로 전송
                   - hosts: Elasticsearch의 주소 (`docker-compose` 상의 서비스 이름 사용)
                   - index: 로그를 저장할 인덱스 이름 지정
                       - `"order-service-%{+YYYY.MM.dd}"`는 로그 수집 날짜 기준으로 인덱스를 생성
                       - 예: `order-service-202x.xx.xx`

               ## 1-4. Elasticsearch 쿼리 예시

               ```bash
               curl -X GET "http://localhost:9200/<인덱스>/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   ...
                 }
               }'

               ```

               - `X GET`: GET 요청
               - `H`: HTTP 헤더 (`Content-Type`은 JSON 명시)
               - `d`: 요청 바디(JSON 쿼리)
               - `<인덱스>`: `order-service-*` 와 같이 패턴 가능

               ### 1-4-1. **특정 로그 레벨 조회**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "match": {
                     "level": "WARN"
                   }
                 }
               }'

               ```

               ### 1-4-2. **특정 단어 포함 로그 조회**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "match": {
                     "message": "Order created"
                   }
                 }
               }'

               ```

               ### 1-4-3. **기간 필터링 포함**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "range": {
                     "@timestamp": {
                       "gte": "now-1h",
                       "lte": "now"
                     }
                   }
                 }
               }'

               ```

               ### 1-4-4. **복합 조건**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "bool": {
                     "must": [
                       { "match": { "level": "INFO" } },
                       { "match": { "message": "Order list" } }
                     ]
                   }
                 }
               }'

               ```

               ### 1-4-5. **로그 개수 제한**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "size": 3,
                 "query": {
                   "match_all": {}
                 }
               }'

               ```

               ## 1-5. Kibana 인덱스 패턴 등록 방법

               1. Kibana 접속: [http://localhost:5601](http://localhost:5601/)
               2. 좌측 메뉴 → **Discover** 클릭
               3. **Create Index Pattern** 버튼 클릭
               4. 인덱스 이름 입력: `order-service-*`
               5. 타임 필드 선택: `@timestamp` (또는 자동 선택된 필드)
               6. 저장 후 Discover에서 로그 확인 가능

               인덱스가 보이지 않는다면 Elasticsearch 인덱스 생성 여부 확인

               ```bash
               curl http://localhost:9200/_cat/indices?v
               ```

               # 2. Monitoring

               ## 2-1. Monitoring이란?

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/e680de87-7f45-4879-92d7-ab5efc25b0f7/Untitled.png)

               <aside>
               💡 실제 서비스를 운영하다 보면 장애가 발생 했을 때 알림 서비스를 받아야 되거나 신속하게 성능을 분석하거나 하는 등 복잡한 네트워크를 추적(감시)하고 관리하는 기능을 필요로 하게
               된다.

               이러한 실시간 네트워크 문제 해결을 위해 표, 차트, 그래프, 대시보드 및 보고서 형태의 데이터를 제공하면 손쉽게 확인할 수 있다.

               따라서 전체적인 모니터링 시스템은 데이터 **수집, 통합, 시각화**의 단계로 구성된다.

               </aside>

               # 3. Prometheus

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/6b4f9145-c7e5-4363-9c88-a1cd7884668c/Untitled.png)

               <aside>
               💡 오픈 소스 **모니터링 및 경고 도구**로, 주로 시스템 및 서비스 모니터링에 사용된다.
               2012년 SoundCloud에서 개발되기 시작해 이후 많은 조직에서 널리 채택되어 사용된다.

               </aside>

               ## 3-1. Prometheus의 특징

               - **데이터 수집**
                   - Prometheus는 시간에 따른 데이터 수집을 지원하며, 주로 ‘메트릭’ 형태로 정보를 수집한다. 메트릭은 서버의 CPU 사용량, 메모리 사용, 네트워크 트래픽 등 다양한 시스템 지표를
               포함할 수 있다.
               - **경고**
                   - 문제가 감지되었을 때 사용자에게 알림을 보내는 경고 시스템을 제공한다.
               - **쿼리 및 분석**
                   - Prometheus의 강력한 쿼리 언어를 사용하여 저장된 데이터를 쿼리하고 분석할 수 있다.

               ## 3-2. Prometheus의 원리

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/e169f815-b636-41aa-b52a-0790636571df/Untitled.png)

               - **Micrometer**
                   - springboot의 actuator가 제공하는 기능이다.
                   - JVM 기반의 애플리케이션 Metrics를 제공하고 처리한다.

                       (애플리케이션의 메트릭(측정 지표) 퍼사드(상위 수준 인터페이스)로 마이크로 미터가 정한 표준이 모니터링 툴을 사용할 때 제공된다.)

                       > **메트릭**: 시간에 따라 추이를 추적할만한 가치가 있는 여러가지 데이터들
                       >
                   - 다양한 모니터링 시스템을 지원한다.
               - **Actuator**
                   - Spring의 라이브러리도 CPU 사용량,  커넥션 풀과 같은 다양한 정보를 제공한다.

                   ```json
                   {"names": [
                     "application.ready.time",
                     "application.started.time",
                     "disk.free","disk.total",
                     "hikaricp.connections",
                     "hikaricp.connections.acquire",
                     "hikaricp.connections.active",
                     "hikaricp.connections.idle",
                     "hikaricp.connections.max",
                     "hikaricp.connections.usage",
                     "http.server.requests",
                     "http.server.requests.active",
                     "jdbc.connections.active",
                     "jdbc.connections.idle",
                     "jdbc.connections.max",
                     "jdbc.connections.min",
                     "jvm.buffer.count",
                     "jvm.buffer.memory.used",
                     "jvm.memory.used",
                     "jvm.memory.max",
                     "logback.events",
                     "process.cpu.usage",
                     "process.uptime",
                     "system.cpu.count",
                     "system.cpu.usage",
                     "tomcat.sessions.active.current",
                     "tomcat.sessions.rejected"
                     ]
                   }
                   ```

               - 프로메테우스는 애플리케이션에서 발생한 메트릭들의 과거 이력까지 보관하는 DB 역할을 한다.

               ## 3-3. Prometheus 설정 파일

               ```yaml
               global:
                 scrape_interval: 15s

               scrape_configs:
                 - job_name: 'spring-boot'
                   metrics_path: '/actuator/prometheus'
                   static_configs:
                     - targets: ['order-service:8080']

               ```

               - `global.scrape_interval: 15s`
                   - 전체 기본 스크래핑 주기 (기본값은 1분 → 여기서는 15초마다 수집)
                   - Prometheus가 대상 서비스로부터 메트릭을 얼마나 자주 가져올지 설정
               - `scrape_configs`
                   - 어떤 대상을 어떻게 수집할지 정의하는 리스트
                   - 하나의 대상은 `job_name`을 갖고, 여러 `target`을 가질 수 있다
               - `job_name: 'spring-boot'`
                   - 수집 작업에 대한 이름 (Grafana 등에서 메트릭 분류에 사용됨)
               - `metrics_path: '/actuator/prometheus'`
                   - Spring Boot의 메트릭 노출 엔드포인트 (Spring Actuator에서 자동 제공)
                   - 내부적으로 micrometer가 `/actuator/prometheus` 형식의 데이터를 반환
               - `targets: ['order-service:8080']`
                   - 메트릭을 수집할 대상
                   - 도커 네트워크 내 컨테이너 이름(`order-service`)과 포트(`8080`)
                   - Prometheus는 이 주소로 HTTP 요청을 보내 메트릭 데이터를 가져옴
               - 동작 흐름
                   1. Prometheus는 15초마다 `http://order-service:8080/actuator/prometheus`로 요청
                   2. Spring Boot 애플리케이션은 Actuator + Micrometer를 통해 메트릭 JSON 응답 반환
                   3. Prometheus는 이를 수집해 시계열 데이터로 저장

               ## 3-4. Prometheus UI 사용법

               - 접속 주소: [http://localhost:9090](http://localhost:9090/)
               - 메트릭 이름 자동 완성 기능 지원

                   → `/actuator/prometheus`를 통해 수집된 모든 메트릭 이름 확인 가능

               - PromQL 쿼리 실행
                   - 쿼리를 입력한 후 `Execute` 버튼 클릭
                   - 결과는 표 형식 (Table) 또는 그래프 (Graph) 탭에서 확인 가능

                   | 예시 | 설명 |
                   | --- | --- |
                   |  `http_server_requests_seconds_count` | HTTP 요청 수 (엔드포인트 단위) |
                   | `jvm_memory_used_bytes` | JVM 힙 메모리 사용량 |
                   | `process_cpu_usage` | CPU 사용률 (0~1 사이 소수) |
                   | `system_cpu_usage` | 전체 시스템 CPU 사용률 |
                   | `rate(http_server_requests_seconds_count[1m])` | 1분간 HTTP 요청 처리 속도 (초당 요청 수) |

                   → 특정 메트릭을 기준으로 현재 값을 보거나 시계열 변화 확인 가능


               # 4. Grafana

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/254b7aa1-c96e-4538-a2a6-4d496912de0f/Untitled.png)

               <aside>
               💡

               오픈 소스 **분석 및 시각화 플랫폼**이다.
               대시보드 생성과 데이터 시각화에 주로 사용되며, 다양한 데이터 소스와 호환된다.

               </aside>

               ## 4-1. Grafana의 특징

               - **다양한 데이터 소스 지원**
                   - Grafana는 Prometheus, InfluxDB, MySQL, PostgreSQL 등 다양한 데이터 소스와 연동할 수 있다.
               - **대시보드**
                   - 사용자는 여러 형태의 차트, 그래프 및 알림을 포함한 사용자 정의 대시보드를 생성할 수 있다.
               - **시각적 디스플레이**
                   - 복잡한 데이터를 쉽게 이해할 수 있도록 돕는 강력한 시각적 도구를 제공한다.

               ## 4-2. Grafana의 원리

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/9cb91440-57d6-403c-b980-914331cbe68d/Untitled.png)

               - 프로메테우스에 쌓인 데이터를 조회해서 사용자가 보기 쉽도록 대시보드로 시각화해 준다.
               - 데이터를 다양한 통계치나 수많은 그래프 형태로 보여준다.

               ## 4-3. Grafana 데이터 소스 설정 (datasource.yml)

               ```yaml
               apiVersion: 1

               datasources:
                 - name: Prometheus
                   type: prometheus
                   access: proxy
                   url: http://prometheus:9090
                   isDefault: true

               ```

               | 항목 | 의미 |
               | --- | --- |
               | `apiVersion: 1` | Grafana 프로비저닝 설정의 버전 |
               | `name` | UI에서 표시될 데이터 소스 이름 |
               | `type` | 데이터 소스 종류 (`prometheus`, `mysql`, `elasticsearch` 등) |
               | `access: proxy` | Grafana 서버가 직접 Prometheus에 요청을 보냄 |
               | `url` | 데이터 소스의 실제 주소 (`prometheus:9090` → Docker 내 서비스명 기준) |
               | `isDefault: true` | 기본 데이터 소스로 설정 (그래프 만들 때 자동 선택됨) |

               ## 4-4. Grafana UI 사용법

               - 접속 주소 : [http://localhost:3000](http://localhost:3000/)
               - 기본 로그인 : `admin / admin`

               ### 기본 설정 확인 (처음 1회)

               1. 좌측 사이드바 → Gear 아이콘(⚙) > Data Sources 클릭
               2. `Prometheus` 데이터 소스가 등록되어 있는지 확인 (Test 버튼 클릭)
               3. 연결 상태가 "Data source is working"이면 성공

               ### 대시보드 만들기

               1. Dashboard → New Dashboard → Add new panel 클릭
               2. Query: PromQL 입력
               3. Run query 클릭 → 실시간 데이터 시각화 확인
               4. Visualization 변경, panel options > Title, Description 변경 등 테스트
               5. 상단 "Apply" → 대시보드 저장# WebSocket과 STOMP (실시간 통신과 브로커 연동)

               <aside>
               ☝

               기존의 HTTP 통신의 한계를 극복하고 실시간 양방향 통신을 구현하기 위해 WebSocket을 사용하며, 이를 메시지 브로커와 효율적으로 연동하여 관리하기 위해 STOMP 프로토콜을 활용한다.

               </aside>

               ## **1-1. WebSocket**

               ### **1-1-1. WebSocket이란?**

               - 클라이언트와 서버 간에 단일 TCP 연결을 통해 양방향(Bi-directional), 풀 이중(Full-duplex) 통신을 제공하는 프로토콜이다.
               - 브라우저와 웹 서버 간의 실시간 데이터 전송을 위해 만들어졌다.

               ### **1-1-2. HTTP 통신과의 차이점**

               - **HTTP**: 클라이언트가 요청(Request)을 보낼 때만 서버가 응답(Response)하는 단방향 구조. 채팅을 구현하려면 주기적으로 서버에 요청을 보내는 폴링(Polling) 방식 등이 필요하여
               리소스 낭비가 발생함.
               - **WebSocket**: 한 번 연결이 수립(Handshake)되면 연결이 지속적으로 유지됨. 서버가 원할 때 클라이언트에게 데이터를 직접 푸시(Server Push)할 수 있어 지연 없는 실시간 통신이 가
               능함.

               ## **1-2.  STOMP (Simple Text Oriented Messaging Protocol)**

               ### **1-2-1. STOMP의 필요성**

               - WebSocket은 데이터가 오가는 '통로' 역할만 할 뿐, 주고받는 데이터의 형식이나 라우팅(누가 누구에게 보내는지)에 대한 규격이 없다.
               - STOMP는 WebSocket 위에서 동작하는 메시징 프로토콜로, 메시지의 발행(Publish)과 구독(Subscribe) 구조를 제공하여 메시지를 쉽게 라우팅할 수 있게 해준다.

               ### **1-2-2. STOMP와 메시지 브로커의 결합**

               - STOMP를 사용하면 프론트엔드 클라이언트가 메시지 브로커의 Pub/Sub 모델을 직접 활용할 수 있다.
               - **구독(Subscribe)**: 클라이언트가 특정 토픽(예: 특정 채팅방, 특정 알림 채널)을 구독한다.
               - **발행(Publish)**: 다른 클라이언트나 서버가 해당 토픽으로 메시지를 발행한다.
               - **브로커(Broker)**: STOMP 메시지 브로커가 발행된 메시지를 받아, 해당 토픽을 구독 중인 모든 클라이언트에게 전달(Broadcasting)한다.

               ---

               # 1. 메시지 브로커(Message Broker)

               <aside>
               💡

               메시지 브로커는 서비스 간 비동기 통신을 중개하는 시스템이다. 프로듀서(Producer)가 보낸 메시지를 저장하고, 이를 컨슈머(Consumer)가 꺼내 처리하도록 중개한다.

               </aside>

               ## 1-1. 핵심 개념

               ### 1-1-1. Message Broker 수행 기능

               - 메시지 수신, 저장, 전달
               - 큐(queue) 또는 토픽(topic) 방식의 메시지 전송
               - 신뢰성 보장 : 메시지 유실 방지, 중복 방지
               ## 1-2. 메시지 전달 방식

               ### 1-2-1. Point-to-Point (Queue 기반)

               - 메시지는 하나의 큐에 쌓이고, 한 명의 컨슈머만 소비
               - 일반적으로 하나의 작업을 하나의 주체가 처리해야 할 경우 사용됨
               - 작업 분산 (Work Distribution)
                   - 여러 컨슈머가 동일한 큐를 바라보고 있다가 메시지를 하나씩 가져가 분산 처리함
                   - 예 : 이미지 리사이징, 이메일 발송 등 대량 작업 처리 시
               - 백그라운드 처리 (Background Processing)
                   - 사용자 요청으로 발생한 작업을 즉시 처리하지 않고 큐에 넣어 별도의 워커가 나중에 처리
                   - 예 : 결제 승인 후 영수증 이메일 발송

               ### 1-2-2. Publish/Subscribe (Topic 기반)

               - 프로듀서가 메시지를 토픽(Topic) 또는 익스체인지(Exchange)에 발행(Publish)
               - 이를 다수의 컨슈머(Subscriber)가 동시에 구독(Subscribe)하여 메시지를 수신
               - 이벤트 브로드캐스트 (Event Broadcast)
                   - 하나의 이벤트를 여러 시스템에 동시에 전파
                   - 예 : 주문 발생 → 재고 시스템, 알림 시스템, 배송 시스템 모두가 반응
               - 다중 시스템 알림 (Multi-System Notification)
                   - 사용자 활동 내역을 로그 서버, 알림 서버, 통계 서버에 동시 전송
                   - 예 : 로그인 이벤트 발생 시 보안 시스템 + 로그 수집기 + 관리자 알림

               ## 1-3. 메시지 브로커의 장점

               ### 1-3-1. 내결함성(Reliability)

               - 메시지가 유실되지 않고, 한 번만 처리되도록 보장하는 특성
               - ACK/NACK란?
                   - 메시지를 처리한 후 컨슈머가 브로커에게 '처리 성공'을 알리는 응답을 ACK(acknowledgement) 라고 한다.
                   - 반대로, 처리 중 문제가 생겨서 ‘실패했음’을 알리는 응답을 NACK(negative acknowledgement) 라고 한다.
                   - 브로커는 ACK를 받으면 해당 메시지를 삭제하고, NACK를 받거나 시간이 지나도 응답이 없으면 메시지를 다시 보내거나 보관한다.
               - 이메일 전송 요청 메시지를 큐에서 꺼낸 컨슈머가 이메일을 성공적으로 보내고 ACK를 보냄 → 브로커는 해당 메시지를 삭제
               - 이메일 서버 오류 등으로 실패 → NACK → 브로커는 나중에 다시 시도하거나 다른 컨슈머에게 전달

               ### 1-3-2. 로드 레벨링(Load Leveling)

               - 트래픽이 몰릴 때 즉시 처리하지 않고, 큐에 임시 저장하여 순차적으로 처리함으로써 서버 과부하를 방지하는 기법
               - 갑자기 수천 개의 요청이 몰려와도, 메시지를 바로 처리하지 않고 큐에 순서대로 저장해두고, 하나씩 처리함으로써 시스템이 안정적이고 일정한 속도로 일할 수 있게 해준다.
               - 쇼핑몰에서 명절 이벤트로 주문 요청이 폭주
                   - 주문 메시지를 큐에 쌓아두고, 주문 처리 컨슈머들이 하나씩 순차적으로 처리
                   - 웹서버는 빠르게 응답하고, 백엔드는 부하 없이 안정적으로 처리 가능

               ### 1-3-3. 확장성(Scalability)

               - 컨슈머를 여러 개로 늘릴 수 있어, 처리해야 할 메시지가 많아져도 시스템을 쉽게 확장할 수 있음
               - 컨슈머 수를 늘리는 것만으로도 병렬 처리 능력 향상
               - 큐가 하나라도, 컨슈머 인스턴스가 많으면 동시에 여러 메시지를 처리할 수 있음
               - 이미지 리사이징 작업이 많을 때, 컨슈머를 1개에서 10개로 늘리면 처리 속도도 최대 10배 향상
               - 시스템을 멈추지 않고 확장할 수 있으므로 매우 유연함

               ### 1-3-4. 서비스 간 결합도 감소(장애 전파 방지)

               - 브로커를 사용하면 서비스 간 직접 통신하지 않아도 되므로, 한쪽 서비스가 장애가 나도 전체 시스템에 영향을 주지 않음
               - 동기 호출이란?
                   - A 서비스가 B 서비스에게 요청하고 B가 응답할 때까지 기다리는 방식
                   - B가 죽으면 A도 멈추거나 오류 발생 → 장애 전파
               - 브로커를 통한 비동기 호출은?
                   - A가 B에게 요청을 보내는 대신, 브로커에 메시지를 보내고 바로 종료
                   - B는 나중에 메시지를 꺼내서 처리
                   - B가 잠깐 죽어 있어도 메시지는 브로커에 남아 있음 → 장애 전파 없음
               - 주문 요청을 받은 A 서비스가 "주문 생성" 메시지를 브로커에 보냄
               - 배송 시스템(B 서비스)이 잠시 내려가 있어도, 메시지는 큐에 남아 있다가 복구 후 다시 처리됨

                   → A는 영향을 받지 않고 정상 동작


               ## 1-4. 대표 메시지 브로커

               <aside>
               💡

               메시지 브로커는 다양한 구조와 목적을 가진 시스템이 존재하며, 그 중에서도 RabbitMQ와 Kafka는 가장 널리 사용되는 대표적인 브로커이다. 두 시스템은 메시지 처리 방식, 저장 구조,
               설계 철학에서 차이를 보이며, 상황에 따라 선택 기준이 달라진다.

               </aside>

               ### 1-4-1. RabbitMQ vs Kafka

               | 항목 | RabbitMQ | Kafka |
               | --- | --- | --- |
               | **기반 프로토콜** | AMQP (Advanced Message Queuing Protocol) | 자체 설계 프로토콜 (Kafka Protocol) |
               | **전송 방식** | Push: 브로커가 컨슈머에게 메시지를 밀어줌 | Pull: 컨슈머가 브로커로부터 직접 메시지를 가져감 |
               | **저장 구조** | 메시지를 큐에 저장하며, 일반적으로 메시지 소비 후 삭제 (필요 시 디스크 기반 durable 설정 가능) | 모든 메시지를 디스크 로그에 저장, 기본적으로 보존(append-
               only) 컨슈머는 오프셋을 기준으로 원하는 시점부터 읽기 가능 |
               | **주요 장점** | 다양한 라우팅, 낮은 지연(latency), 경량 구조, 설정 유연함 | 대용량 처리, 확장성, 장애 복구 용이, 스트리밍 처리에 특화 |
               | **적합한 용도** | 백오피스 이벤트 처리, 결제 흐름, 실시간 알림, MSA 간 단일 이벤트 흐름 | 로그 수집, 대시보드, 실시간 분석, IoT 데이터 처리, 빅데이터 파이프라인 |
               | **내결함성** | 메시지 확인(ACK/NACK), 재시도 등 신뢰성 높음 | 메시지 자체가 디스크에 남기 때문에 복구와 재처리에 강함 |
               - RabbitMQ는 빠른 응답과 복잡한 라우팅이 필요한 시스템에 적합
               - Kafka는 많은 양의 데이터를 안정적으로 저장하고 처리하는 데 최적화

               ### 1-4-2. 기타 브로커

               ### ActiveMQ

               - **기반**: JMS(Java Message Service)
               - **특징**: 다양한 프로토콜 지원, Java 환경과의 높은 호환성
               - **적합한 상황**: Java 레거시 시스템과 연동하거나 JMS 기반 프로젝트를 클라우드로 이전할 때

               ### AWS SQS (Simple Queue Service)

               - **기반**: AWS에서 제공하는 완전 관리형 서비스
               - **특징**: 서버 설치 없이 사용 가능, 자동 확장, Lambda/FIFO/지연 큐 지원
               - **적합한 상황**: 클라우드 환경에서 빠르게 큐 기반 아키텍처를 구성하고 싶을 때

               ## 1-5. 메시지 브로커 활용 사례

               <aside>
               💡

               메시지 브로커는 시스템 아키텍처, 운영 안정성, 사용자 경험, 실시간 기능 구현 등 다양한 목적에서 활용된다. 아래는 핵심 사례와 선택적으로 확장 가능한 활용 항목이다.

               </aside>

               ### 1-5-1. 마이크로서비스 아키텍처(MSA)

               - 마이크로서비스 간 직접 호출 대신 메시지 브로커를 통해 통신하면 서비스 간 결합도를 낮출 수 있어 장애 전파를 막고 독립성을 유지할 수 있다.
               - 주문 서비스가 주문 메시지를 브로커에 발행하면, 재고 서비스, 배송 서비스, 알림 서비스가 각자 그 메시지를 구독해 독립적으로 처리함. 재고 서비스가 일시적으로 중단돼도 주문 서
               비스는 정상 동작함.

               ### 1-5-2. 트래픽 폭주 대응

               - 갑작스러운 요청 폭주 시 메시지를 큐에 버퍼링해 순차적으로 처리함으로써 시스템이 안정적으로 유지될 수 있다.
               - 블랙프라이데이처럼 사용자 주문이 폭주할 때, 주문 요청을 큐에 넣고 백엔드는 하나씩 메시지를 처리. 서버는 과부하 없이 빠르게 응답하고, 처리 실패 없이 순차 대응 가능.

               ### 1-5-3. 사용자 응답 속도 개선

               - 시간이 오래 걸리는 작업을 메시지 큐로 분리해 비동기 처리함으로써 사용자에게 빠른 응답을 제공할 수 있다.
               - 회원가입 시 인증 메일을 전송할 때, 메일 발송을 큐에 위임하고 서버는 즉시 “가입 완료” 응답을 반환. 백엔드는 이후에 메일을 발송함.

               ### 1-5-4. 실시간 기능 (SSE, 채팅 등)

               - 백엔드 서버를 수평 확장(replica 2개 이상)하면 사용자는 서로 다른 서버에 연결된다. 이때 서버 간 실시간 메시지 공유를 위해 메시지 브로커의 Pub/Sub 모델이 필요하다.
               - A 사용자가 채팅 메시지를 보낼 때 Kafka나 Redis에 메시지를 발행하면, 다른 서버에 연결된 B 사용자가 이를 실시간으로 수신할 수 있다. 서버 간 연결을 메시지 브로커가 중개해주는
               구조.

               ### 1-5-5. 로그 및 모니터링 이벤트 수집

               - 여러 시스템에서 발생하는 이벤트 로그를 중앙 브로커로 수집해 로그 분석, 모니터링, 경보 시스템과 연동할 수 있다.
               - Kafka를 사용해 마이크로서비스 로그를 전송하고, 이를 Logstash → Elasticsearch → Kibana로 연동해 시각화 및 검색 가능.

               ### 1-5-6. 지연 처리 (Delay Queue)

               - 메시지 처리 시점을 지정하거나 일정 시간 후에 처리되도록 할 수 있다.
               - 사용자에게 1시간 후에 알림을 보내야 할 경우, 메시지 큐(RabbitMQ 등)에 TTL(Time-To-Live) 설정을 적용해 지연된 처리를 구현.# 1. 도커(Docker) 개요

               ## 1-1. 도커(Docker)란?

               - 도커(Docker)는 리눅스 응용 프로그램들을 프로세스 격리 기술들을 사용해 컨테이너로 실행하고 관리하는 오픈 소스 프로젝트이다.
               - 컨테이너 기반 오픈소스 가상화 플랫폼 이라고 정의할 수 있다.

               ![docker-logo.jpg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e1c1f39b-3fe0-40a4-bed9-da1772172926/docker-logo.jpg)

               ![container.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c8c997ca-0599-40c7-b10a-fbe8928098b0/container.png)

               > 도커 컨테이너는 일종의 소프트웨어를 소프트웨어의 실행에 필요한 모든 것을 포함하는 완전한 파일 시스템 안에 감싼다. 여기에는 코드, 런타임, 시스템도구, 시스템 라이브러리 등 서
               버에 설치되는 무엇이든 아우른다. 이는 실행 중인 환경에 관계 없이 언제나 동일하게 실행될 것을 보증한다.
               >
               >
               > 출처 : [도커 공식 홈](https://www.docker.com/why-docker)
               >
               - 도커는 리눅스에서 운영 체제 수준의 가상화의 추상화 및 자동화 계층을 추가적으로 제공한다. 도커는 cgroups와 커널 이름 공간과 같은 리눅스 커널의 기능들과 OverayFS, aufs와 같은
               유니언 가능 파일 시스템의 리소스 격리 기능을 사용하며, 이를 통해 독립적인 컨테이너가 하나의 리눅스 인스턴스 안에서 실행할 수 있게 함으로써 가상 머신을 시작하여 유지보수를 해
               야 하는 부담을 없애준다.
               - 프랑스의 PaaS 기업인 닷클라우드 라는 회사에서 솔로몬 하익스가 내부 프로젝트로서 도커를 시작했고, 2013년 3월 오픈 소스로 출시되었다.

               ## 1-2. 도커(Docker) 이미지와 컨테이너

               - 도커 이미지와 컨테이너는 도커를 이해하는데 있어 가장 중요한 개념이다.


                   | 구분 | 설명 |
                   | --- | --- |
                   | 이미지(Image) | 서비스 운영에 필요한 서버 프로그램, 소스코드 및 라이브러리, 컴파일 된 실행 파일을 묶는 형태를 도커 이미지라고 한다. 즉, 특정 프로세스를 실행하기 위한 모
               든 파일과 설정값을 지닌 파일들의 묶음으로 볼 수 있다. 도커 이미지들은 github와 유사한 서비스인 DockerHub를 통해 버젼관리 및 배포가 가능하다. |
                   | 컨테이너(Container) | 이미지를 실행한 상태로, 응용 프로그램의 종속성과 함께 응용프로그램 자체를 패키징 or 캡슐화 하여 격리된 공간에서 프로세스를 동작시키는 기술이다. |

               ## 1-3. 도커(Docker)의 사용 이유

               ### 1-3-1. 도커의 장점

               - VM을 사용하지 않고 도커 엔진을 이용하여 동작하기 때문에 성능 개선과 동시에 메모리 용량을 적게 요구한다.
               - 컨테이너를 실행하기 위한 모든 정보를 가지고 있기 때문에, 새로운 환경에서 이것 저것 설치할 필요 없이 새로운 서버에 이미지만 다운받아서 컨테이너를 생성할 수 있다.
               - 개발환경 설정할 때 초기 세팅이 빠르고 실행환경을 강제화할 수 있다.
               - 도커는 개발자가 원하는 환경 세팅을 모듈식 유닛을 조합함으로써 만들수 있게 해준다. 이는 개발 주기, 기능 배포, 버그 수정의 속도를 높여준다.

               ## 1-3. 도커의 도구

               [제목 없음](https://www.notion.so/2ff649136c1181fa8bc8d7b4a655f917?pvs=21)

               ![Gemini_Generated_Image_5frs9g5frs9g5frs.png](attachment:e88981a7-1242-448b-9957-3ff4f43a70a3:Gemini_Generated_Image_5frs9g5frs9g5frs.png)# 6. Docker 컴포즈

               ## 6-1. Docker 컴포즈 개요

               ### 6-1-1. Docker 컴포즈란?

               > 일반적인 애플리케이션은 단일로 기동되어 작동하는 경우보다 여러 개의 애플리케이션이 서로 유기적으로 기동된다.
               각 컨테이너가 하나의 애플리케이션을 담당하는 경우, 여러개의 컨테이너를 동시에 관리해야하는 역할이 필요하게 되었고,
               이때 사용할 수 있는 툴이 도커 컴포즈이다. 도커 컴포즈는 yaml 포맷으로 설정파일을 작성해야한다.
               >

               ### 6-1-2. yaml 이란?

               - YAML Ain't Markup Language 의 약자
               - JSON, XML 과 같이 시스템 같 데이터 교환을 위해 만들어졌으며, Key-Value 구조를 기본으로 한다.
               - 대소문자를 구분하며 구조를 구분할 때는 들여쓰기를 기준으로 한다.
               - 값으로 문자열, 숫자, boolean을 모두 가질 수 있다.
               - 주석 표기는 # 으로 표시한다.
               - YAML 파일에서 들여쓰기 할 때 탭은 도커 컴포즈가 인식하지 못하기 때문에 2개의 공백(Space)을 이용해 구분한다.
               - yaml 간단 예제

               ```powershell
               person:
                 name: goatohgiraffers
                 age: 37
                 address: Korea
                 phone: '01090311339'
                 email: goatohgiraffer@mail.com

               ```

               ## 6-2. docker-compose.yml 작성

               - 예시

               ```powershell
               services:
                 app:
                   build:
                     context: ./DOCKER_EXAMPLE
                     dockerfile: Dockerfile
                   ports:
                     - "8080:8080"
                   container_name: web
                   restart: always
                   depends_on:
                     - db
                 db:
                   image: mysql:5.7
                   volumes:
                     - ./mysqldata:/var/lib/mysql
                   environment:
                     - MYSQL_ROOT_PASSWORD=catohgiraffers
                     - MYSQL_DATABASE=cat0124!!
                   ports:
                     - "3306:3306"
                   container_name: db

               ```

               ### 6-2-1. version

               ```powershell
               version: "3"
               ```

               - Docker Compose 파일 제일 위에는 버전을 명시해야한다. 각 버전별로 명령어와 표기법이 달라질수 있다.
               - 버전관련 정보는 공식 홈페이지에서 확인한다.

               ### 6-2-2. services

               ```powershell
               services:
                   app:
                       ...
                   db:
                       ...

               ```

               - 서비스는 Compose에서 실행할 컨테이너에 대한 명세이다.
               - 각 서비스별로 옵션을 설정할 수 있다.
               - 서비스명은 컨테이너 명과 다른 개념이다. 동일하게 착각하면 안된다.

               ### 6-2-3. image

               ```powershell
               services:
                   app:
                       ...
                       image: catohgiraffers:1.0
                       ...
                   db:
                       ...
                       image: mysql:5.7
                       ...

               ```

               - 컨테이너화할 이미지를 명시한다.
               - dockerfile내에 FROM을 활용해 베이스 이미지 설정하는 것과 유사하다.

               ### 6-2-4. build

               ```powershell
               services:
                   app:
                       ...
                       build:
                           context: .
                           dockerfile: Dockerfile
                       ...
                   db:
                       ...

               ```

               - 로컬에 이미지가 존재하거나 Docker Hub에 Push한 이미지가 있다면 이미지명과 태그명을 활용하여 컨테이너를 구성할수 있다.
               - 일반적으로는 dockerfile에 명시된 이미지를 기반으로 컨테이너를 구성한다.

               ### 6-2-5. command

               ```powershell
               services:
                   app:
                       ...
                       build:
                           context: .
                           dockerfile: Dockerfile
                       command: java -jar app.jar
                       ...
                   db:
                       ...

               ```

               - 컨테이너화할 곳에 명령을 미리 세팅할 수 있다.
               - 보통 컴파일러나 특정 언어로 작성된 애플리케이션을 실행할 때 사용한다.

               ### 6-2-6. port

               ```powershell
               services:
                   app:
                       ...
                       ports: "8080:8080"
                       ...
                   db:
                       ...

               ```

               - 포트 포워딩을 세팅하는 부분이며 docker run -p 60080:8080과 동일한 기능이다. 포트 앞뒤로 따옴표를 꼭 붙여야한다.

               ### 6-2-7. depends_on

               ```powershell
               services:
                   app:
                       ...
                       depends_on:
                          - db

                   ```bash
                   docker volume prune -a
                   ```

                   이 명령어는 확인 메시지가 나오며, `y`를 입력하면 삭제가 진행된다. `docker volume prune`은 컨테이너와 연결되지 않은 모든 볼륨을 삭제한다.


               # 2. Container Modification(컨테이너 개조)

               <aside>
               💡

               기존 컨테이너에 새로운 설정이나 패키지를 추가한 후, 이를 새로운 이미지로 저장하는 작업이다. 예를 들어 패키지를 설치하거나 설정을 변경하여 커스텀 이미지를 만들 수 있다. 이를
               통해 자주 사용하는 설정이 포함된 베이스 이미지를 만들어 배포 환경을 일관성 있게 유지할 수 있다.

               </aside>

               ## 2-1. 예제 - Ubuntu 컨테이너에 `curl` 설치 및 이미지 저장

               `curl`은 데이터를 전송하기 위해 사용하는 CLI 도구로, 주로 HTTP 또는 FTP 프로토콜을 통해 서버와 데이터를 주고받을 때 사용된다. `curl`을 통해 API 요청을 실행하거나 데이터를 다
               운로드할 수 있다.

               1. **Ubuntu 컨테이너에 curl 설치 후 이미지 생성**

                   `curl`을 Ubuntu 컨테이너에 설치하고, 이를 새로운 이미지로 저장하는 과정을 설명한다.

                   - **Ubuntu 컨테이너 실행 및 접속**

                       Ubuntu 컨테이너를 실행하고 bash 쉘로 접속한다.

                       ```bash
                       docker run -it --name myubuntu ubuntu bash
                       ```

                   - **컨테이너에서 curl 설치**

                       컨테이너 내부에서 `apt` 패키지 관리자를 사용하여 `curl`을 설치한다.

                       ```bash
                       apt update && apt install -y curl
                       ```

                       `apt update`는 패키지 목록을 업데이트하고, `apt install -y curl`은 `curl`을 설치한다.

                   - **curl 설치 확인**

                       `curl`이 제대로 설치되었는지 버전을 확인한다.

                       ```bash
                       curl --version
                       ```

                       `curl` 버전 정보가 출력되면 설치가 완료된 것이다.

                   - **새로운 이미지로 저장**

                       `curl`이 설치된 Ubuntu 컨테이너를 기반으로 새로운 이미지를 생성한다. 이 작업을 통해 동일한 설정이 포함된 커스텀 이미지를 만들 수 있다.

                       ```bash
                       docker commit myubuntu ubuntu-with-curl
                       ```

                       `docker commit` 명령어는 `myubuntu` 컨테이너 상태를 기반으로 `ubuntu-with-curl`이라는 이름의 새 이미지를 생성한다.

               2. **커스텀 이미지 실행 및 curl 사용 예시**

                   생성된 `ubuntu-with-curl` 이미지를 이용해 새로운 컨테이너를 실행하고, `curl` 명령을 테스트한다.

                   ```bash
                   docker run -it ubuntu-with-curl bash
                   ```

                   컨테이너 내에서 `curl`을 통해 URL에 GET 요청을 보낸다. 예를 들어, `example.com`에 대한 요청을 보내면 다음과 같이 실행할 수 있다.

                   ```bash
                   curl <http://example.com>
                   ```

                   해당 URL의 응답이 출력되면 `curl`이 정상 작동하는 것을 확인할 수 있다.# 1. springboot project jar로 빌드하기(gradle 기준)

               ```bash
               # 방법 1 : Gradle 메뉴 > Tasks > build > build 실행

               # 방법 2 : 기존 빌드된 것을 유지하면서 새로 build
               ./gradlew build

               # 방법 3 : 기존에 빌드된 것들을 지우고 새로 build
               ./gradlew clean build
               ```

               <aside>
               👉

               Mac / Linux 사용 시 권한 문제로 gradlew 가 실행되지 않을 경우 아래 명령어를 수행하여 권한을 부여한다. (비밀번호 입력 필요)

               ```bash
               # 관리자 권한으로 gradlew 파일에 실행 권한 부여
               sudo chmod +x gradlew
               ```

               </aside>

               # 2. DockerFile 생성

               ## 2-1. 빌드 스테이지에서 build를 하지 않을 경우

               - 이미 빌드된 결과물을 가져와 COPY 하는 경우

               ```docker
               # AWS에서 관리하는 JDK 21이 미리 설치된 Alpine Linux(가볍고 용량 작은 Linux) 이미지를 기반으로 한다.
               FROM amazoncorretto:21-alpine

               # 빌드된 JAR 파일을 컨테이너의 루트 디렉토리에 app.jar로 복사한다.
               COPY build/libs/*.jar app.jar

               # 컨테이너 시작 시 app.jar 파일을 실행하는 명령을 설정한다.
               ENTRYPOINT ["java", "-jar", "app.jar"]

               ```

               ## 2-2. 빌드 스테이지에서 build를 하는 멀티 스테이지 빌드의 경우

               - 빌드된 결과물이 없어 Docker 안에서 소스 코드를 가져와 빌드하는 경우

               ```docker
               ## 1. 빌드 스테이지 시작
               # Gradle 이미지를 가져오고, 빌드 스테이지를 'build'라는 이름으로 설정한다.
               FROM gradle:jdk21-alpine AS build

               # 컨테이너 내부의 작업 디렉토리를 /app으로 설정한다.
               WORKDIR /app

               # 현재 디렉토리 내의 모든 파일과 폴더를 컨테이너의 /app 디렉토리로 복사한다.
               COPY . .

               # Gradle을 사용하여 프로젝트를 빌드한다. (daemon 프로세스는 사용하지 않는다.)
               RUN ./gradlew clean build --no-daemon

               ## 2. 실행 스테이지 시작
               # AWS JDK 21 버전의 이미지를 가져와 JVM 환경을 구축한다.
               FROM amazoncorretto:21-alpine

               # 빌드 스테이지에서 생성된 JAR 파일을 복사한다.
               COPY --from=build /app/build/libs/*.jar ./

               # JAR 파일을 나열하고 grep을 사용해 'plain'이라는 단어가 포함되지 않은 줄을 선택하여 app.jar로 변경한다.
               RUN mv $(ls *.jar | grep -v plain) app.jar

               # app.jar를 리눅스 환경에서 실행하여 스프링 부트 서버를 시작한다.
               ENTRYPOINT ["java", "-jar", "app.jar"]

               ```

               1. 빌드 스테이지
                   1. Docker 데몬은 `gradle:jdk21-alpine` 이미지를 기반으로 임시 컨테이너를 생성하고 그 위에 `/app` 디렉토리를 만들며 호스트의 모든 소스 코드를 복사한다.
                   2. `./gradlew clean build --no-daemon` 명령이 실행되며, 이때 `--no-daemon` 옵션을 지정한 이유는 Docker 빌드 시 매번 독립적인 JVM 프로세스에서 빌드를 수행함으로써 Gradle
               데몬이 남기는 백그라운드 프로세스나 캐시 데이터를 제거하고 이미지가 불필요하게 무거워지는 것을 방지하기 위함이다.
                   3. 빌드가 끝나면 `/app/build/libs` 아래에 생성된 JAR 파일이 포함된 중간 이미지(intermediate image)가 레이어 형태로 저장된다.
               2. 런타임 스테이지
                   1. `amazoncorretto:21-alpine` 이미지를 새롭게 풀링하여 또 다른 임시 컨테이너를 만들고, 빌드 스테이지의 중간 이미지에서 생성된 JAR 파일만을 복사해 온다.
                   2. 복사된 JAR은 `mv` 명령으로 `app.jar`로 이름을 통일한 다음, ENTRYPOINT로 설정된 `java -jar app.jar`를 통해 컨테이너가 시작될 때 애플리케이션이 즉시 실행되도록 한다.
               3. 이미지 생성 완료
                   1. 최종적으로 만들어진 이미지는 빌드 툴과 소스 코드를 포함하지 않기 때문에 용량이 작고 보안 상 노출 면이 줄어든다.
                   2. 멀티 스테이지 빌드를 쓰면 빌드 환경과 런타임 환경을 명확히 분리하면서, 최종 이미지를 훨씬 작고 안전하게 유지할 수 있다.

               - DockerFile을 활용한 Docker Image 생성

                   ```bash
                   # 도커 이미지의 이름은 소문자로만 작성할 것
                   $ docker build -t {dockerhub계정이름}/sw-boot-project .

                   # 만들어진 이미지들 확인
                   $ docker images

                   # 내부로는 8080 포트로 배포되어 있지만 외부에서 container에 접근할 때 8055 포트를 사용한다.
                   $ docker run -d -p 8055:8080 --name first {dockerhub계정이름}/sw-boot-project

                   # 올라간 컨테이너 확인
                   $ docker ps
                   ```


               - 배포 확인
                   - 헬스 체크 : http://localhost:8055/health
                   - 더하기 : http://localhost:8055/plus?num1=10&num2=20

               ![image.png](attachment:b1a71ef6-49e9-49a5-bfda-de46cd72acdb:image.png)- DockerFile 생성

                   ```docker
                   # Node.js LTS 버전이 설치된 Alpine Linux 이미지를 가져온다.
                   FROM node:lts-alpine

                   # 컨테이너 내부의 작업 디렉토리를 /app으로 설정한다.
                   WORKDIR /app

                   # 현재 디렉토리 내의 모든 파일과 폴더를 컨테이너의 /app 디렉토리로 복사한다.
                   COPY . .

                   # npm을 사용하여 프로젝트의 종속성을 설치한다.
                   RUN npm install

                   # 컨테이너가 시작될 때 npm run dev 명령을 실행한다.
                   # --는 이후의 인자를 npm 스크립트에 전달하기 위한 것이다.
                   # --host 0.0.0.0는 서버가 모든 네트워크 인터페이스에서 접근 가능하도록 설정한다.
                   CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
                   ```


               - DockerFile을 활용한 Docker Image 생성

                   ```bash
                   # 도커 이미지의 이름은 소문자로만 작성
                   $ docker build -t {dockerhub계정명}/sw-vue-project .

                   # 만들어진 이미지들을 확인한다.
                   $ docker images

                   # 내부로는 5173포트로 배포되어 있지만 외부에서 container에 접근할 때 8011 포트를 사용한다.
                   $ docker run -d -p 8011:5173 --name second {dockerhub계정명}/sw-vue-project

                   # 올라간 컨테이너를 확인
                   $ docker ps
                   ```## Spring Boot & Vue.js 멀티 컨테이너 구성

               ### **1. 폴더 구조**

               ```
               project-root/
               ├── backend/
               │   ├── Dockerfile
               │   └── <Spring Boot project files>
               ├── frontend/
               │   ├── Dockerfile
               │   └── <Vue.js project files>
               └── docker-compose.yml

               ```

               ### **2. Spring Boot Dockerfile** (`backend/Dockerfile`)

               ```
               # backend/Dockerfile
               FROM gradle:jdk17-alpine AS build
               WORKDIR /app
               COPY . .
               RUN gradle clean build --no-daemon
               FROM openjdk:17-alpine
               COPY --from=build /app/build/libs/*.jar ./
               RUN mv $(ls *.jar | grep -v plain) app.jar
               ENTRYPOINT ["java", "-jar", "app.jar"]

               ```

               ### **3. Vue.js Dockerfile** (`frontend/Dockerfile`)

               ```
               # frontend/Dockerfile
               FROM node:lts-alpine
               WORKDIR /app
               COPY . .
               RUN npm install
               CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]

               ```

               ### **4. Docker Compose 파일** (`docker-compose.yml`)

               ```yaml
               # Docker Compose 파일의 버전을 정의한다. 최신 기능과 호환성을 결정한다.
               services:
                 backend:
                       # 해당 디렉토리의 Dockerfile을 사용해 이미지를 빌드
                   build: ./chap01-boot-project
                   # 컨테이너 이름을 'backend'로 설정
                   container_name: backend
                   # 호스트의 8080 포트를 컨테이너의 8080 포트로 매핑
                   ports:
                     - "8080:8080"
                   # 사용자 정의 네트워크에 연결
                   networks:
                     - app-public
                     - app-private

                 frontend:
                       # 해당 디렉토리의 Dockerfile을 사용해 이미지를 빌드
                   build: ./chap01-vue-project
                   # 컨테이너 이름을 'frontend'로 설정
                   container_name: frontend
                   # 호스트의 5173 포트를 컨테이너의 5173 포트로 매핑
                   ports:
                     - "5173:5173"
                   # 동일 네트워크에 연결하여 DNS로 통신 가능
                   networks:
                     - app-public

                 tester:
                   image: alpine
                   container_name: net-tester
                   command: sleep 3600       # 실습용으로 쉘 접근 전용
                   networks:
                     - app-public

               # 네트워크 정의
               networks:
                 app-public:
                   driver: bridge
                 app-private:
                   driver: bridge
                   internal: true            # 외부(호스트) 라우팅 차단
               ```

               ### **5. docker-compose 실행**

               ```bash
               # dockerfile들과 network를 활용한 docker-compose 실행으로
               # docker image들을 각 위치의 Dockerfile들로 생성 및 docker container 띄우기
               docker-compose up --build -d
               ```

               > • --build 옵션의 역할
               Docker Compose가 서비스를 시작하기 전에 관련된 모든 이미지를 강제로 다시 빌드

               • --build 없이 docker-compose up만 사용할 경우
               이미 빌드된 이미지가 있다면 그것을 사용하고 없는 경우에만 새로 빌드
               >

               ### **6. docker-compose 종료 및 제거**

               ```bash
               # docker container들 종료
               docker-compose down
               ```

               ### 7. 네트워크 실습

               ### 7-1. 네트워크 목록·정보 확인

               ```bash
               # 생성된 네트워크 확인
               docker network ls

               # app-public 상세 정보
               docker network inspect {{projectroot}}_app-public

               # app-private 상세 정보
               docker network inspect {{projectroot_app}}-private
               ```

               ### 7-2. 서비스 이름(DNS) 호출 테스트

               ```bash
               # 줄 바꿈을 나타내는 명령어
               # bashshell : \ (백슬래시)
               # powershell : ` (백틱)

               # frontend 컨테이너에서 backend HTTP 엔드포인트 호출
               docker-compose exec frontend \
                 sh -c "wget -qO- http://backend:8080/health"

               # tester 컨테이너에서 backend로 ping
               docker-compose exec tester \
                 sh -c "ping -c 3 backend"
               ```

               ### 7-3. private 네트워크 격리 확인

               ```bash
               # backend는 app-private에도 연결되어 있지만,
               # frontend와 tester는 private 네트워크에 연결되지 않으므로
               # 아래와 같이 private IP로 직접 ping 시도하면 실패해야 함.

               # private 서브넷 IP 확인 (inspect 결과에서 확인)
               PRIVATE_IP=$(docker network inspect {{projectroot_app}}-app_private \
                 -f '{{range .Containers}}{{.Name}} {{.IPv4Address}}{{"\n"}}{{end}}' \
                 | awk '/backend/ {print $2}' | cut -d/ -f1)

               echo $PRIVATE_IP
               # → 예: 172.19.0.2

               # tester에서 private IP로 ping 시도
               docker-compose exec tester \
                 sh -c "ping -c 2 $PRIVATE_IP"
               # → Destination Host Unreachable (격리 상태 확인)

               # backend만 private에 연결된 상태 유지하며,
               # public 네트워크로는 정상 통신 가능한 점을 다시 확인
               ```

               ### 7-4. 런타임에 네트워크 연결·해제 실습

               ```bash
               # tester 컨테이너를 app-private에도 연결
               docker network connect docker-compose_app-private net-tester

               # 이제 tester → PRIVATE_IP ping 성공
               docker-compose exec tester \
                 sh -c "ping -c 2 $PRIVATE_IP"

               # 다시 격리하려면
               docker network disconnect docker-compose_app-private net-tester
               ``` 일단 아무것도 하지말고 학습만해둬


             • 네, 요청하신 내용은 지금 작업 없이 학습용으로만 반영해두겠습니다.
               정리하면 DevOps(데브옵스), CI/CD(지속적 통합/지속적 배포), ELK(로그 수집·분석 스택), Prometheus(모니터링 시스템), Grafana(시각화 플랫폼), WebSocket(실시간 양방향 통신),
               STOMP(메시지 프로토콜), Message Broker(메시지 브로커), Docker(컨테이너 플랫폼), Docker Compose(멀티 컨테이너 오케스트레이션) 맥락까지 기억해두었습니다.

               원하실 때 이 내용을 현재 프로젝트에 맞는 실무 적용안으로 바로 연결해드리겠습니다.


             › # 1. 쿠버네티스

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/0c37d3af-9ac3-409b-abaf-1ae2f908187f/Untitled.png)

               ## 1-1. 쿠버네티스란

               <aside>
               💡 **쿠버네티스(Kubernetes, K8s)**는 자동화된 컨테이너 배포, 스케일링, 관리를 제공하는 오픈소스 플랫폼으로 **컨테이너 오케스트레이션 도구**의 일종이다.
               구글에서 처음 개발했고, Cloud Native Computing Foundation(CNCF)에서 관리한다.
               컨테이너화된 애플리케이션의 배치, 확장 및 관리를 간소화하며, 클라우드 환경에서의 무중단 서비스, 확장성, 높은 가용성을 보장한다.

               </aside>

               > **컨테이너 오케스트레이션**이란
               : **시스템 전체를 총괄하고 여러 개의 컨테이너를 관리하는 것**
                 쉽게 말해 컨테이너 생성이나 관리에 있어 번거로운 수고를 덜어주는 도구이다.
               >

               ## 1-2. 쿠버네티스 등장 배경

               <aside>
               💡 2013년 도커가 등장하면서 많은 서비스들이 리눅스 컨테이너 기술을 도입하며 도커라이징하게 된다. 하지만 도커 이미지와 관리할 컨테이너가 늘어남에 따라 인스턴스 상태 관리나 자
               동화 툴의 필요성이 대두되게 되었다.
               이 때 쿠버네티스가 이러한 요구에 부응하기 위해 탄생해 현재는 컨테이너 오케스트레이션 도구의 표준 역할을 하고 있다. 구글 내부에서 사용하던 Borg를 기반으로 2015년에 첫 버전이
               출시 되어 현재는 **CNCF(Cloud Native Computing Foundation)에 기부되어 클라우드 네이티브 기술 발전을 위한 프로젝트로 관리**되고 있다.

               </aside>

               ## 1-3. 도커 컴포즈와 쿠버네티스의 차이

               <aside>
               💡 **도커 컴포즈**는 옵션을 지정해 수동으로 컨테이너의 수를 바꿔야 하지만
               **쿠버네티스**는 yml 파일에 정의한 설정대로 컨테이너를 생성하거나 삭제하면서 **바람직한 상태를 유지하는 기능**이 있다.
               (etcd DB로 관리하며 모니터링 기능으로 확인이 가능하기 때문이다.)

               **쿠버네티스**는 문제가 되는 컨테이너를 삭제하고 새로운 컨테이너를 만들거나 컨테이너가 삭제 되면 다시 설정대로 생성하는 등 정해진 상태를 유지해 낸다. 기본적으로 사람이 개입해
               서 컨테이너를 삭제하기 보다는 삭제 명령이 아니라 **yml 파일(매니페스트)**에서 **바람직한 상태를 수정**해야 한다.

               </aside>

               ## 1-4. 도커 데스크탑을 활용한 쿠버네티스 설치(feat.kubectl)

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/7ac9496e-f8db-447c-a223-a1e210a7ecfd/Untitled.png)

               # 2. 쿠버네티스의 클러스터

               <aside>
               💡 **클러스터(Cluster)**란 컨테이너 형태의 애플리케이션을 호스팅하는 물리/가상 환경의 노드들로 이루어진 집합을 말한다.
               클러스터는 클러스터 내부 요소들을 관리(제어)하는 **`마스터 노드`**와 컨테이너화 된 애플리케이션을 실행하는 **`워커 노드`**로 구성되어 있다.
               클러스터는 사람이 개입하지 않아도 마스터 노드에 설정된 내용에 따라 워커 노드가 관리 되며 자율적으로 동작한다. 관리자는 마스터 노드의 초기 설정 후 가끔 조정만 하면 되며, 직접
               워커 노드를 관리하지 않는다.

               </aside>

               ## 2-1. 마스터 노드와 워커 노드

               <aside>
               ☝

               Control Plane(마스터 노드) :  계획을 세우고 명령하는 관리자

               Node(워커 노드) :  실제로 땀 흘려 일하는 일꾼

               </aside>

               - **마스터 노드(컨트롤 플레인 역할)**
                   - **컨테이너의 라이프사이클을 정의하고, 배포, 관리하기 위한 쿠버네티스의 컴포넌트**이다.
                   - 마스터 노드에는 컨테이너 등의 상태를 관리하기 위해 etcd라는 **`키-값 타입`**의 데이터베이스가 설치된다.
                   - 마스터 노드를 설정하는 관리자 컴퓨터에는 **`kubectl`**이 설치되어야 마스터 노드에 로그인하고 설정을 관리할 수 있다.
                   - 마스터 노드는 컨테이너를 실행하지 않는다.
                   - 마스터 노드 구성


                       | **kube-apiserver** | 사용자와 컨트롤 플레인과 통신하는 쿠버네티스 API |
                       | --- | --- |
                       | **etcd** | 모든 클러스터 데이터를 담아 정보 전반을 관리하는 키-값 타입의 데이터 베이스이다. |
                       | **kube-scheduler** | 파드를 워커 노드에 할당한다. |
                       | **kube-controller-manager** | 컨트롤러를 통합 관리하고 실행한다. |
                       | **cloud-controller-manager** | 클라우드 서비스와 연동해 서비스를 생성한다. |
               - **워커 노드**
                   - 모든 클러스터는 최소 한 개의 워커 노드를 가진다.
                   - 워커 노드는 컨테이너가 실제 동작하는 서버이므로 **도커 엔진 같은 컨테이너 엔진이  필요**하다.
                   - 워커 노드는 구성 요소인 파드를 호스트한다.
                   - 워커 노드 구성


                       | **kube-let** | 클러스터의 각 노드에서 실행되는 에이전트로 마스터 노드의
                       kube-scheduler와 연동해 **파드를 배치**하고 확실하게 **실행 되도록 관리**한다.
                       (쿠버네티스를 통해 생성되지 않은 컨테이너는 관리하지 않는다.) |
                       | --- | --- |
                       | **kube-proxy** | 각 노드에서 실행되는 네트워크 프록시로 쿠버네티스의 서비스 개념의 구현부이다. **네트워크 통신의 라우팅을 담당**한다.
                       (트래픽이 실제로 전달되어야 하는 파드 내의 포트를 지정, 서비스가 받은 트래픽을 파드의 어떤 포트로 전달할지 결정) |

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/85b3703e-6041-4906-b8a8-18ca8c56ef5b/Untitled.png)

               ![                                                              <클러스터 아키텍처>](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-
               4490d601f1ba/91e80db7-27f7-442a-9111-4e3acb511885/Untitled.png)

                                                                             <클러스터 아키텍처>

               - 이미지 흐름
                   1. 사용자 요청 : 사용자가 api 서버에 “애플리케이션 배포” 요청
                   2. 데이터 저장 : api는 이 정보(“애플리케이션 배포” 요청)를 etcd에 저장
                   3. 배치 결정 : sched가 비어있는 Node를 찾아 api에 알려줌
                   4. 실행 명령 : api가 해당 노드의 kubelet에게 “컨테이너 실행”을 명령함
                   5. 네트워크 설정 : kube-proxy가 해당 컨테이너로 접근할 수 있는 길(네트워크)를 뚫어줌


               # 3. 쿠버네티스 구성 요소

               ## 3-1. 파드(Pod)

               <aside>
               💡 쿠버네티스에서 **`컨테이너를 관리하는 단위`**이다.
               **`컨테이너와 볼륨을 함께 묶은 것`**으로 기본적으로는 파드 하나가 컨테이너 하나이지만 컨테이너가 여러 개인 파드도 있을 수 있다.

               </aside>

               ## 3-2. 디플로이먼트(Deployment)와 레플리카셋(ReplicaSet)

               <aside>
               💡 **디플로이먼트(Deployment)**는 **`파드와 레플리카셋(ReplicaSet)에 대한 선언적 업데이트를 제공`**한다.
               이 말은 파드와 레플리카셋에 대한 의도하는 상태를 설정하고 파드의 디플로이(배포)를 관리하는 요소임을 뜻한다.

               **레플리카셋(ReplicaSet)은 `레플리카(또는 파드)의 수를 관리`**한다.

               **레플리카(Replica)는 `레플리카셋이 관리하는 동일한 구성의 파드`를** 라고 한다.

               쿠버네티스는 상위 객체를 통해 관리한다.

               **Deployment  >  ReplicaSet  >  Pod**

               레플리카셋만 단독으로 다루지 않고 주로 디플로이먼트로 레플리카셋에 대한 정의를 함께 한다.

               </aside>

               ![                             <Deployment 관련 구성 요소들>](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/ad8cd634-fa1a-4c3a-
               96d6-1109a867ba76/Untitled.png)

                                            <Deployment 관련 구성 요소들>

               ## 3-3. 서비스(Service)

               <aside>
               💡 서비스는 여러 개의 파드가 모여 구성 되는데 한 서비스가 관리하는 파드는 모두 기본적으로 동일한 구성을 갖는다. 또한 여러 개의 워커 노드에 걸쳐 동작하더라도 이들을 모두 하나
               의 서비스가 관리한다.

               **각 서비스는 고정된 IP(Cluster IP)를 부여 받아 외부에서는 이 IP 주소로 접근하며 이 때 서비스가 적절한 파드들로 분배한다. 즉, `로드 밸런서의 역할`을 한다.**
               하지만 서비스가 분배하는 통신은 한 워커 노드 안으로 국한되며 여러 워커 노드 간의 분배는 실제 로드 밸런서 또는 인그레스가 담당한다. (마스터 노드도 워커 노드도 아닌 별도의 노드
               에서 동작)

               </aside>

               - 구성 요소 정리

               | 리소스 이름 | 내용 |
               | --- | --- |
               | 파드 | 컨테이너와 볼륨을 합한 것 |
               | 디플로이먼트 | 파드의 배포를 관리 |
               | 레플리카세트 | 파드의 수를 관리 |
               | 서비스 | 파드에 요청을 배분 |

               # 4. 매니페스트(manifest, 명시하다)

               <aside>
               💡 매니페스트(manifest)는 **`파드나 서비스에 대한 설정을 작성한 파일(정의 파일)이다`**.
               yaml 또는 json 형식으로 작성할 수 있지만 기본적으로 사람이 설정 파일을 읽고 쓰기 용이하도록 yaml 파일을 사용한다.

               매니페스트 파일은 **파드, 서비스, 디플로이먼트, 레플리카셋과 같은 리소스 단위로 작성**한다.
               **디플로이먼트를 작성하면 레플리카셋과 파드도 함께 설정되게 된다.**

               파드나 레플리카셋만 따로 작성하는 것은 쿠버네티스의 기능(바람직한 상태(지정한 개수의 파드를 자동으로 유지))을 온전히 사용하기 힘들어 사용되거나 권장되지 않는다.

               </aside>

               - 기본적인 매니페스트 파일 항목

               ```bash
               # API 그룹 및 버전
               apiVersion:

               # 리소스 유형
               kind:

               # 메타 데이터
               metadata:

               # 리소스 내용
               spec:
               ```

               ## 4-1. 리소스 설정

               - 자주 사용되는 리소스의 API그룹 및 리소스 유형
                   - 최신 매니페스트 작성 유형
                       - kubectl api-resources를 작성한다.

                       ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/87aabb4c-486e-4f59-a049-390d0677da00/Untitled.png)


               | 리소스 | API 그룹/버전 | 리소스 유형 |
               | --- | --- | --- |
               | 파드 | core/v1(v1으로 축약 가능) | Pod |
               | 서비스 | core/v1(v1으로 축약 가능) | Service |
               | 디플로이먼트 | apps/v1 | Deployment |
               | 레플리카세트 | apps/v1 | ReplicaSet |## 1. Ingress

               ![{6CA2B3A7-35FA-4B73-A9B4-3C2B3A844332}.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/57f2f670-f055-4286-8b72-
               5bd0ef782ad6/6CA2B3A7-35FA-4B73-A9B4-3C2B3A844332.png)

               <aside>
               💡

               Ingress는 Kubernetes 클러스터 외부에서 내부 서비스로의 **`HTTP 및 HTTPS 트래픽을 제어하는 API 객체`**이다. 이는 다양한 서비스에 대한 단일 진입점을 제공하여, 클러스터의 복잡한
               네트워킹 요구 사항을 효율적으로 관리할 수 있게 한다.

               </aside>

               ![ingress.svg](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/a50f1987-8627-4f46-bcf2-0e18388baa34/ingress.svg)

               ### 1-1. Ingress의 주요 기능

               1. **로드 밸런싱**: Ingress는 여러 서비스에 대한 요청을 균등하게 분산시켜 클러스터의 부하를 관리하는 데 도움을 준다.
               2. **SSL/TLS 종료**: 클라이언트와 Ingress 간의 암호화된 연결을 관리하며, 백엔드 서비스와의 연결은 일반적으로 비암호화로 유지할 수 있다. 이를 통해 백엔드 서비스의 복잡성을 줄
               일 수 있다.
               3. **URL 경로 및 호스트 기반 라우팅**: 요청된 URL 경로 또는 호스트 이름에 따라 트래픽을 다양한 서비스로 라우팅할 수 있다. 이를 통해 서비스의 유연성과 관리 효율성을 높일 수 있
               다.
               4. **복잡한 라우팅 규칙**: 정규 표현식을 사용하여 복잡한 경로 매칭 규칙을 설정할 수 있어, 다양한 요청에 대해 세밀한 제어가 가능하다.

               ### 1-2. Ingress의 동작 방식

               <aside>
               💡

               Ingress는 일반적으로 Ingress Controller와 함께 사용되며
               Ingress Controller는 Ingress 리소스를 실제로 구현하는 컴포넌트이다.
               Ingress Controller는 클러스터에서 정의된 규칙을 적용하고, 해당 규칙에 따라 트래픽을 조정하는 역할을 한다. Ingress Controller는 클러스터의 엔트리 포인트 역할을 하며, 효율적인
               트래픽 관리와 보안을 제공하는 중요한 컴포넌트이다.

               </aside>

               - **다양한 구현체:** Nginx, HAProxy, Traefik 등 여러 종류의 Ingress Controller가 존재하며, 각각 고유한 기능과 성능 특성을 가지고 있다.

               ## 2. 배포 전략 (무중단 배포)

               <aside>
               💡

               Kubernetes는 애플리케이션의 배포 및 업데이트를 위한 여러 가지 전략을 제공한다. 이들 전략은 애플리케이션의 가용성과 안정성을 높이고, 배포 과정에서의 리스크를 최소화하는 데 도
               움을 준다.
               주요 무중단 배포 방식에는 **롤링 업데이트**, **카나리 배포**, **블루-그린 배포**가 있다.

               </aside>

               ### 2-1. 롤링 업데이트 (Rolling Update)

               <aside>
               💡

               **롤링 업데이트**는 기존 애플리케이션의 버전을 점진적으로 업데이트하는 방식이다. 새로운 버전의 파드를 점진적으로 추가하면서 동시에 기존 파드를 종료하여, 서비스 중단 없이 업데
               이트를 수행할 수 있다. 쿠버네티스의 기본 배포 방식이다.

               </aside>

               - 서비스가 완전히 중단되지 않으며, 점진적인 업데이트로 인해 애플리케이션의 가용성을 유지한다.
               - 쿠버네티스의 기본 `Deployment`가 지원하며, 레플리카 수를 조정해 가용성을 관리한다.
               - 새 버전이 실패할 경우 이전 버전으로 쉽게 롤백할 수 있다.
               - Ingress나 로드밸런서가 외부 트래픽을 받아서 파드로 분산시키기 때문에, 롤링 업데이트 중에도 서비스가 끊기지 않는다.

               ### 2-2. 카나리 배포 (Canary Deployment)

               <aside>
               💡

               **카나리 배포**는 새로운 버전의 애플리케이션을 전체 트래픽의 일부에만 배포하여 성능과 안정성을 검증하는 방법이다. 초기에는 소수의 사용자에게만 새로운 버전을 제공하고, 안정성
               을 검증한 후 점진적으로 트래픽을 확대하여 전체 사용자에게 배포한다.

               </aside>

               - 전체 트래픽을 새 버전으로 한 번에 전환하지 않으므로 무중단 배포가 가능하다.
               - 소수 사용자로부터 피드백을 받아, 오류나 문제를 초기에 발견하여 수정할 수 있다.
               - 전체 서비스에 영향 없이, 특정 트래픽만 새 버전으로 라우팅하므로 문제 발생 시 신속하게 기존 버전으로 되돌릴 수 있다.
               - Ingress나 로드밸런서의 라우팅 규칙을 통해 트래픽의 일부를 카나리 버전으로 전송하며, 특정 경로나 헤더 조건을 기준으로 트래픽을 구분할 수도 있다.

               ### 2-3. 블루-그린 배포 (Blue-Green Deployment)

               <aside>
               💡

               **블루-그린 배포**는 두 개의 환경(블루와 그린)을 사용하는 방법으로, 현재 버전은 블루 환경에서 실행되고, 새로운 버전은 그린 환경에서 실행된다. 새로운 버전이 준비되면 트래픽을
               그린 환경으로 전환한다.

               </aside>

               - 새 버전이 준비되면 블루에서 그린 환경으로 전체 트래픽을 즉시 전환할 수 있다.
               - 문제 발생 시 간단히 트래픽을 다시 블루 환경으로 전환하여 롤백할 수 있다.
               - 블루와 그린 두 개의 환경이 별도 배포되므로, 새 버전을 철저히 테스트한 후 라이브 전환을 할 수 있어 안정성이 높다.
               - 로드밸런서나 Ingress를 사용해 전체 트래픽을 원하는 환경으로 전환할 수 있다.## 1. Volume

               <aside>
               💡

               쿠버네티스에서 볼륨(Volume)은 컨테이너의 라이프사이클에 종속적인 데이터를 저장할 수 있는 위치를 제공한다.
               기본 볼륨은 `Pod` 내에서 여러 컨테이너가 데이터를 공유하거나, Pod 재시작 시 데이터를 유지할 수 있도록 도와준다.
               `emptyDir`은 가장 단순한 타입의 볼륨으로 주로 임시 데이터나 Pod 내 컨테이너 간 데이터 공유를 위해 사용되며,  Pod가 시작될 때 생성, Pod가 삭제될 때 데이터도 함께 삭제된다.
               `emptyDir`외에도 `configMap`, `secret` 등 다양한 볼륨 타입이 있다.

               </aside>

               ### 1-1. 설정 예제

               - 웹 애플리케이션 컨테이너에서 로그 파일을 기록하고,
               **사이드카 컨테이너가 이를 실시간으로 모니터링**하는 예제이다.
               - busybox : 경량 리눅스 배포판으로, Docker와 Kubernetes 환경에서 간단한 테스트 및 스크립트 실행을 위해 자주 사용되며, 일반적인 리눅스 명령어를 지원하여, Pod와 컨테이너를 빠르
               게 테스트하거나 네트워크, 스토리지 관련 작업을 확인하는 데 유용하다.
               - **web-app 컨테이너**: 로그를 `/var/log/webapp.log` 파일에 주기적으로 기록한다.
               - **log-monitor 컨테이너**: `emptyDir` 볼륨을 통해 공유된 `/var/log/webapp.log` 파일을 실시간으로 모니터링한다.

               ```yaml
               # log-collector.yml
               apiVersion: v1
               kind: Pod
               metadata:
                 name: log-collector # 파드의 이름 정의
               spec:
                 containers:
                 # [컨테이너 1] 로그 생성기 (Writer)
                 - name: web-app
                   image: busybox
                   # 5초마다 현재 날짜와 메시지를 /var/log/webapp.log 파일에 추가 기록
                   command: ["sh", "-c", "while true; do echo $(date) Hello from web app >> /var/log/webapp.log; sleep 5; done"]
                   volumeMounts:
                   - name: logs          # 아래 정의된 'logs' 볼륨을 참조
                     mountPath: /var/log  # 컨테이너 내 저장 경로 (이 경로에 쓴 데이터는 볼륨에 저장됨)

                 # [컨테이너 2] 로그 감시자 (Reader)
                 - name: log-monitor
                   image: busybox
                   # 공유된 경로의 로그 파일을 실시간으로 읽어서 표준 출력(stdout)으로 전달
                   command: ["sh", "-c", "tail -f /var/log/webapp.log"]
                   volumeMounts:
                   - name: logs          # 컨테이너 1과 '동일한' 이름의 볼륨을 참조
                     mountPath: /var/log  # 동일한 경로를 마운트하여 컨테이너 1이 쓴 파일을 읽음

                 # 파드 내부에서 컨테이너들이 공유할 저장소 정의
                 volumes:
                 - name: logs            # 볼륨 식별자 (컨테이너들이 이 이름을 보고 연결함)
                   emptyDir: {}          # 임시 저장소 타입: 파드가 실행되는 동안만 유지되며 삭제 시 데이터 소멸
               ```

               ![스크린샷 2026-02-12 오전 6.13.50.png](attachment:8822f3f0-dfb4-4f3e-9a27-5f31f80e5bc2:스크린샷_2026-02-12_오전_6.13.50.png)

               ### 1-2. 테스트 예제

               - `log-collector` Pod를 생성하고, `log-monitor` 컨테이너가 `web-app` 컨테이너의 로그를 제대로 읽는지 확인한다.
               - 로그 파일이 `log-monitor` 컨테이너에서 실시간으로 출력되는지 확인한다.

               ```yaml
               # Pod 생성
               kubectl apply -f log-collector.yml

               # log-monitor 컨테이너의 로그 확인 (실시간으로 웹 앱 로그를 읽어야 함)
               kubectl logs log-collector -c log-monitor

               # Pod 삭제
               kubectl delete pod log-collector

               # Pod 재생성
               kubectl apply -f log-collector.yml

               # log-monitor 컨테이너의 로그  확인 (기존 로그가 없고 새 로그가 기록되기 시작해야 함)
               kubectl logs log-collector -c log-monitor
               ```

               ## 2. PersistenceVolume(PV) & PersistentVolumeClaim(PVC)

               <aside>
               💡

               **PersistentVolume(PV)**는 클러스터 내에서 독립적으로 존재하는 스토리지 자원이다.
               클러스터 관리자가 생성하며, 스토리지가 클러스터 내에서 유지된다.
               (Pod가 지워져도 내용이 유지된다)

               **PersistentVolumeClaim(PVC)**는 사용자가 스토리지 사용 권한을 요청을 할 때 사용하는 리소스이다.
               사용자 관점에서 스토리지를 요청하면 PVC를 통해 자동으로 PV와 연결된다.
               PV와 PVC는 볼륨을 Pod의 라이프사이클과 분리하여 보다 영속적인 스토리지를 제공한다.

               </aside>

               ### 2-1. 설정 예제

               - 애플리케이션에서 사용하는 **MySQL 데이터베이스의 데이터를 지속적으로 보관**하기 위한 예제이다. MySQL 컨테이너는 PVC를 통해 데이터베이스 파일을 영구 저장한다.
               - **PV**: `/mnt/mysql-data` 디렉토리를 2Gi (크기로 제공한다.

               ```yaml
               # pv-mysql.yml
               apiVersion: v1
               kind: PersistentVolume # 리소스 종류: 클러스터 레벨의 스토리지 리소스인 PV 정의
               metadata:
                 name: pv-mysql       # 이 퍼시스턴트 볼륨의 고유 이름
               spec:
                 capacity:
                   storage: 2Gi       # 저장 용량 설정: 2 Gigabytes 할당
                 accessModes:
                   - ReadWriteOnce    # 접근 모드: 하나의 노드에 의해서만 읽기/쓰기 가능 (MySQL과 같은 단일 DB에 주로 사용)
                 hostPath:            # 실제 데이터가 저장될 물리적 위치 (로컬 스토리지 사용)
                   path: "/mnt/mysql-data" # 워커 노드의 실제 디렉토리 경로 (이 폴더에 데이터가 저장됨)
               ```

               - **PVC**: MySQL 컨테이너에서 사용할 2Gi의 스토리지 사용 권한을 요청한다.

               ```yaml
               # pvc-mysql.yml
               apiVersion: v1
               kind: PersistentVolumeClaim # 리소스 종류: 스토리지 자원을 요청하는 PVC 정의
               metadata:
                 name: pvc-mysql           # 이 요청서(클레임)의 이름. 나중에 파드에서 이 이름을 참조함
               spec:
                 accessModes:
                   - ReadWriteOnce        # 접근 모드: PV에 설정된 것과 동일하게 '하나의 노드에서만 읽기/쓰기' 요청
                 resources:
                   requests:
                     storage: 2Gi         # 요청 용량: "나에게 2Gi만큼의 저장 공간을 할당해줘"라고 요청
               ```

               - **MySQL 컨테이너**: `/var/lib/mysql` 경로에 PVC를 마운트하여 데이터베이스 데이터를 영구적으로 저장한다.

               ```yaml
               # mysql.yml
               apiVersion: v1
               kind: Pod
               metadata:
                 name: mysql                # 생성될 파드의 이름
               spec:
                 containers:
                 - name: mysql              # 컨테이너 이름
                   image: mysql:8.0         # 사용할 도커 이미지 버전
                   env:                     # MySQL 실행을 위한 필수 환경 변수 설정
                     - name: MYSQL_ROOT_PASSWORD
                       value: "root1234" # 루트 계정 비밀번호 설정
                   volumeMounts:            # [연결 2] 컨테이너 내부와 볼륨 연결
                   - name: mysql-storage     # 아래 'volumes'에서 정의한 이름과 일치해야 함
                     mountPath: /var/lib/mysql # MySQL의 데이터가 실제로 저장되는 컨테이너 내부 경로

                 volumes:                   # [연결 1] 외부 저장소 정의
                 - name: mysql-storage      # 이 볼륨 설정의 고유 이름
                   persistentVolumeClaim:   # PVC를 통해 스토리지를 가져오겠다고 선언
                     claimName: pvc-mysql   # [핵심] 이미 생성된 'pvc-mysql' 요청서를 사용하여 스토리지 연결
               ```

               ![image.png](attachment:543b5900-bceb-4143-a647-15e60a7871bb:image.png)

               ### 2-2. 테스트 예제

               - `mysql` Pod를 생성하고, 데이터베이스에 간단한 데이터를 입력해 본다.
               - Pod를 삭제한 후 재생성하여 데이터가 보존되는지 확인한다.

               ```yaml
               # PV, PVC 및 MySQL Pod 생성
               kubectl apply -f pv-mysql.yml
               kubectl apply -f pvc-mysql.yml
               kubectl apply -f mysql.yml

               # MySQL에 접속하여 데이터베이스와 테이블 생성
               kubectl exec -it mysql -- mysql -u root -p
               Enter password: root1234
               mysql> CREATE DATABASE testdb; USE testdb; CREATE TABLE test (id INT, name VARCHAR(20)); INSERT INTO test VALUES (1, 'Kubernetes');

               # 데이터 확인
               mysql> USE testdb; SELECT * FROM test;

               # MySQL Pod 삭제 후 재생성
               mysql> exit

               kubectl delete pod mysql
               kubectl apply -f mysql.yml

               # 데이터가 보존되는지 확인
               kubectl exec -it mysql -- mysql -u root -p
               Enter password: root1234
               mysql> USE testdb; SELECT * FROM test;

               ```## 1. ConfigMap과 Secret

               <aside>
               💡

               ConfigMap과 Secret은 Kubernetes에서 애플리케이션 설정 데이터를 관리하는 데 사용된다.
               이 둘은 유사하지만, ConfigMap은 일반 설정 데이터에, Secret은 민감한 데이터(예: 비밀번호, API 키 등)에 사용된다는 차이점이 있다.

               </aside>

               ### 1-1. ConfigMap

               <aside>
               💡

               **ConfigMap**은 애플리케이션에서 사용되는 설정 데이터를 저장하는 Kubernetes 리소스이다.
               YAML이나 JSON 형식의 설정 데이터를 키-값 쌍으로 저장하여 컨테이너 내부 환경 변수나 설정 파일에 주입할 수 있다. 비밀이 아닌 일반적인 설정 데이터를 저장할 때 사용된다.

               </aside>

               ### 1-2. Secret

               <aside>
               💡

               Secret은 민감한 데이터를 저장하는 Kubernetes 리소스이다.
               ConfigMap과 비슷한 방식으로 데이터를 저장하지만, 비밀번호, 인증 토큰, API 키 등의 민감한 정보를 보호하기 위해 사용된다. Secret에 저장된 데이터는 **`Base64로 인코딩`**되어 저
               장되며, 적절한 권한을 가진 사용자만 접근할 수 있도록 보안이 강화되어 있다.

               </aside>

               ## 2. ConfigMap & Secret 설정하여 확인

               ### 2-1. ConfigMap 리소스 `configmap.yml` 파일 생성

               ```yaml
               apiVersion: v1
               kind: ConfigMap
               metadata:
                 name: db-config
               data:
                 database_url: "jdbc:mariadb://localhost:3306/menudb"
                 timeout: "30"

               ```

               ### 2-2. Secret 리소스 `secret.yml` 파일 생성

               - Secret 데이터는 Base64로 인코딩되어야 하므로, 명령을 통해 인코딩된 값을 사용해야 한다.

               ```docker
               # Linux/Mac OS Terminal
               echo -n 'swcamp' | base64
               # Windows PowerShell
               [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("swcamp"))
               ```

               ```yaml
               apiVersion: v1
               kind: Secret
               metadata:
                 name: db-secret
               type: Opaque
               data:
                 username: c3djYW1w          # 'swcamp'의 Base64 인코딩 값
                 password: c3djYW1w

               ```

               ### 2-3. Deployment 리소스 `boot005dep.yml` 파일 생성

               ```yaml
               apiVersion: apps/v1
               kind: Deployment
               metadata:
                 name: boot005dep # 디플로이먼트 리소스의 이름
               spec:
                 replicas: 1 # 실행할 파드(Pod)의 개수
                 selector:
                   matchLabels:
                     app: boot005kube # 어떤 파드를 관리할지 결정하는 라벨 조건
                 template:
                   metadata:
                     labels:
                       app: boot005kube # 생성되는 파드에 부착되는 라벨
                   spec:
                     containers:
                       - name: boot-container
                         image: {{Dockerhub username}}/k8s-boot-ing:latest # 사용할 애플리케이션 이미지
                         imagePullPolicy: Always
                         env:
                           # [1] 일반 설정 정보: ConfigMap에서 참조
                           - name: DATABASE_URL # 애플리케이션 내부에서 인식할 환경 변수 이름
                             valueFrom:
                               configMapKeyRef:
                                 name: db-config # 참조할 ConfigMap 리소스 이름
                                 key: database_url # 해당 ConfigMap 내부에 정의된 키(Key) 이름
                           - name: TIMEOUT
                             valueFrom:
                               configMapKeyRef:
                                 name: db-config
                                 key: timeout

                           # [2] 보안이 필요한 정보: Secret에서 참조
                           - name: USERNAME # 데이터베이스 사용자 계정명
                             valueFrom:
                               secretKeyRef:
                                 name: db-secret # 참조할 Secret 리소스 이름 (보통 base64 인코딩되어 저장됨)
                                 key: username # Secret 내부에 정의된 키 이름
                           - name: PASSWORD # 데이터베이스 비밀번호
                             valueFrom:
                               secretKeyRef:
                                 name: db-secret
                                 key: password
               ```

               ### 2-4. 테스트 실행

               1. **ConfigMap 생성**

                   ```bash
                   kubectl apply -f configmap.yml
                   ```

               2. **Secret 생성**

                   ```bash
                   kubectl apply -f secret.yml
                   ```

               3. **Deployment 생성**

                   ```bash
                   kubectl apply -f boot005dep.yml
                   ```

               4. **ConfigMap과 Secret 적용 확인**

                   Deployment가 잘 생성되었는지 확인하기 위해 `kubectl get pods` 명령을 사용하여 Pod가 정상적으로 실행 중인지 확인한다.

               5. **Pod 내 환경 변수 확인**

                   생성된 Pod 내부에 접근하여 환경 변수가 올바르게 설정되었는지 확인할 수 있다.

                   ```bash
                   kubectl exec -it <pod-name> -- /bin/sh
                   ```

                   그런 다음, 환경 변수를 출력하여 ConfigMap과 Secret 값이 제대로 설정되었는지 확인한다.

                   ```bash
                   echo $DATABASE_URL   # ConfigMap에서 설정한 값 확인
                   echo $TIMEOUT        # ConfigMap에서 설정한 값 확인
                   echo $USERNAME       # Secret에서 설정한 값 확인
                   echo $PASSWORD       # Secret에서 설정한 값 확인

                   printenv DATABASE_URL  # ConfigMap에서 설정한 값 확인
                   printenv TIMEOUT       # ConfigMap에서 설정한 값 확인
                   printenv USERNAME      # Secret에서 설정한 값 확인
                   printenv PASSWORD      # Secret에서 설정한 값 확인

                   ```## 1. Health Check

               <aside>
               💡

               Kubernetes에서는 애플리케이션의 상태를 점검하기 위해 **Liveness Probe**와 **Readiness Probe**라는 두 가지 헬스 체크 기능을 제공한다. 이 두 가지는 애플리케이션의 라이프사이클
               과 서비스 연결 상태를 관리하는 데 중요한 역할을 한다.

               </aside>

               ### 1-1. Liveness Probe(리브니스 프로브)

               <aside>
               💡

               **Liveness Probe**는 애플리케이션이 **정상적으로 실행되고 있는지**를 확인하는 데 사용된다.
               Liveness Probe가 실패하면 Kubernetes는 해당 컨테이너를 **자동으로 재시작**하여 장애 상태를 회복할 수 있도록 한다. 이로써, 애플리케이션이 멈추거나 응답하지 않을 경우 Liveness
               Probe가 이를 감지하고 문제 해결을 시도하게 된다.

               </aside>

               ### 1-2. Readiness Probe(레디니스 프로브)

               <aside>
               💡

               **Readiness Probe**는 애플리케이션이 **외부 요청을 받을 준비가 되었는지** 확인하는 데 사용된다. Readiness Probe가 실패하면 Kubernetes는 해당 Pod로 **트래픽을 전달하지 않도록
               ** 설정하여, 준비가 되지 않은 애플리케이션이 요청을 받지 않도록 한다. 예를 들어, 애플리케이션이 초기화 중이거나 DB 연결이 준비되지 않은 경우 Readiness Probe가 실패로 설정되어
               외부 트래픽을 차단할 수 있다.

               </aside>

               ## 2. Liveness 및 Readiness Probe 설정하여 확인

               ### 2-1. Deployment 리소스 `vue006dep.yml` 생성

               - **livenessProbe**: 잘못된 포트로 인해 실패하고, 설정된 `failureThreshold: 1`에 따라 한 번만 실패해도 컨테이너가 재시작된다.
               - **readinessProbe**: 잘못된 포트로 인해 실패하고, 설정된 `failureThreshold: 1`에 따라 한 번만 실패해도 준비 상태에서 제외된다. 준비 상태에서 제외된 Pod는 트래픽을 받지 않게
               된다.

               ```yaml
               apiVersion: apps/v1
               kind: Deployment
               metadata:
                 name: vue006dep
               spec:
                 replicas: 1
                 selector:
                   matchLabels:
                     app: vue006kube
                 template:
                   metadata:
                     labels:
                       app: vue006kube
                   spec:
                     containers:
                       - name: vue-container
                         image: {{Dockerhub Username}}/k8s-vue-ing:latest
                         imagePullPolicy: Always
                         ports:
                           - containerPort: 80
                         livenessProbe:
                           httpGet:
                             path: /
                             port: 80
               #              port: 8080                # 잘못된 포트로 설정하여 livenessProbe 실패 유도
                           initialDelaySeconds: 5       # 컨테이너 시작 후 5초 대기 후 확인 시작
                           periodSeconds: 5             # 5초마다 헬스 체크
                           failureThreshold: 1          # 1회 실패 시 재시작 트리거
                         readinessProbe:
                           httpGet:
                             path: /
                             port: 80
               #              port: 8080                # 잘못된 포트로 설정하여 readinessProbe 실패 유도
                           initialDelaySeconds: 3       # 컨테이너 시작 후 3초 대기 후 확인 시작
                           periodSeconds: 5             # 5초마다 헬스 체크
                           failureThreshold: 1          # 1회 실패 시 준비 상태에서 제외

               ```

               ### 2-2. Service 리소스 `vue006ser.yml` 생성

               ```yaml
               apiVersion: v1
               kind: Service
               metadata:
                 name: vue006ser
               spec:
                 type: ClusterIP
                 ports:
                   - port: 8000
                     targetPort: 80
                 selector:
                   app: vue006kube
               ```

               ### 2-3. 80 포트로 정상 수행 테스트

               1. **테스트 전 리소스는 제거하고 실행한다.**

                   ```yaml
                   kubectl delete --all deployments,service,pods
                   ```

               2. **Deployment & Service 생성**

                   ```bash
                   kubectl apply -f vue006dep.yml
                   kubectl apply -f vue006ser.yml
                   ```

               3. **Pod 상태 확인**

                   `kubectl get pods` 명령어로 Pod 상태를 확인한다. 정상적으로 생성 된 모습을 확인할 수 있다.

                   ```bash
                   kubectl get pods
                   ```

               4. **서비스 연결 상태 확인**

                   Deployment가 Service와 연결된 경우, 준비된 Pod의 IP 목록을 확인할 수 있다.

                   ```bash
                   kubectl get endpoints vue006ser
                   ```


               ### 2-4. 8080 포트 `vue006dep.yml` 파일 수정 후 실패 테스트

               1. 테스트 전 리소스는 제거하고 실행한다.

                   ```yaml
                   kubectl delete --all deployments,service
                   ```

               2. **Deployment & Service 생성**

                   ```bash
                   kubectl apply -f vue006dep.yml
                   kubectl apply -f vue006ser.yml
                   ```

               3. **Pod 상태 확인**

                   `kubectl get pods` 명령어로 Pod 상태를 확인한다. `w` 옵션을 사용하면 실시간으로 Pod 상태를 확인할 수 있으며, Liveness Probe 실패로 인해 컨테이너가 재시작되는 과정을 볼 수
               있다.  `RESTARTS` 컬럼에 **재시작 횟수**가 증가하는 것을 관찰할 수 있다.

                   ```bash
                   # 새 터미널에서 수행
                   kubectl get pods -w
                   ```

               4. **Pod의 이벤트 로그 확인**

                   `kubectl describe pod <pod-name>` 명령어로 Pod의 상세 이벤트 로그를 확인하여 Liveness Probe와 Readiness Probe의 실패 내역을 확인할 수 있다. 이 명령어의 출력에서
               **Liveness Probe 실패로 인한 재시작** 이벤트와 **Readiness Probe 실패로 인해 준비 상태에서 제외된** 이벤트를 확인할 수 있다.

                   ```bash
                   kubectl describe pod <pod-name>
                   ```

               5. **서비스 연결 상태 확인**

                   Readiness Probe가 실패한 경우, 서비스에서 해당 Pod로 트래픽이 전달되지 않는다. Readiness Probe가 실패한 Pod의 IP는 목록에서 제외되어 있음을 확인할 수 있다.

                   ```bash
                   kubectl get endpoints vue006ser
                   ```### 실습 전 도커 이미지 생성 후 도커 허브에 push하기

               - Vue 프로젝트를 도커 이미지로 생성 후 push

               ```bash
               docker build -t {{dockerhub username}}/k8s-vue .

               docker push {{dockerhub username}}/k8s-vue
               ```

               - Spring Boot 프로젝트를 도커 이미지 생성 후 push

               ```bash
               # Mac 또는 Linux는 gradlew 파일에 실행 권한 부여
               sudo chmod +x ./gradlew

               docker build -t {{dockerhub username}}/k8s-boot .

               docker push {{dockerhub username}}/k8s-boot
               ```

               # 매니페스트 작성

               ## 1. 디플로이먼트 작성

               - vue001dep.yml 파일 생성

                   ```yaml
                   # 사용할 Kubernetes API 버전을 명시한다. 'apps/v1'은 Deployment를 포함한 여러 리소스를 지원한다.
                   apiVersion: apps/v1

                   # 생성하려는 리소스의 종류를 나타낸다. 여기서는 Deployment를 생성하려고 한다.
                   kind: Deployment

                   # 리소스에 대한 메타데이터를 정의한다. 이름, 네임스페이스, 레이블 등을 포함할 수 있다.
                   metadata:
                     # Deployment의 이름을 설정한다. 이 이름으로 Deployment를 식별할 수 있다.
                     name: vue001dep

                   # Deployment의 사양을 정의한다. 여기에는 레플리카 수, 셀렉터, 템플릿 등이 포함된다.
                   spec:

                     # 디플로이먼트가 특정한 레이블이 부여된 파드를 관리할 수 있도록 하는 설정
                     selector:
                       # 레이블 기반 선택을 설정한다.
                       matchLabels:
                         # 이 셀렉터는 'app=vue001kube' 레이블이 있는 파드만을 대상으로 한다.
                         app: vue001kube

                     # 생성할 파드의 정보를 기재
                     template:
                       # 파드 템플릿을 정의한다. 이 템플릿은 파드 생성에 사용된다.

                       # 파드 하나의 메타데이터
                       metadata:
                         # 생성될 파드에 'app=vue001kube' 레이블을 부여한다.
                         labels:
                           app: vue001kube

                       # 파드 하나의 스펙
                       spec:
                         # 파드 내에 포함될 컨테이너 목록을 정의한다.
                         containers:
                         - name: vue-container # 컨테이너의 이름을 설정한다.
                           # 사용할 컨테이너 이미지를 지정한다.
                           image: {{dockerhub username}}/k8s-vue:latest
                           # 이미지 풀 정책을 'Always'로 설정하여 항상 최신 이미지를 사용하도록 한다.
                           imagePullPolicy: Always
                           # 컨테이너가 사용할 포트 정보를 정의한다.
                           ports:
                           - containerPort: 5173 # 컨테이너가 리스닝할 포트 번호를 지정한다.

                   ```

                   - 이 YAML 파일로 **vue001kube**라는 레이블을 가진 파드를 관리하는 **vue001dep**라는 이름의 Deployment를 생성한다. 이 Deployment는 **{{dockerhub username}}/k8s-
               vue:latest** 이미지를 사용하는 컨테이너를 포함한 파드를 배포한다.
                   **imagePullPolicy: Always**는 쿠버네티스가 파드를 시작할 때마다 해당 이미지의 최신 버전을 가져오도록 보장한다.

               - boot001dep.yml 파일 생성

                   ```yaml
                   # 사용할 Kubernetes API 버전을 명시한다. 'apps/v1'은 Deployment를 포함한 여러 리소스를 지원한다.
                   apiVersion: apps/v1

                   # 생성하려는 리소스의 종류를 나타낸다. 여기서는 Deployment를 생성하려고 한다.
                   kind: Deployment

                   # 리소스에 대한 메타데이터를 정의한다. 이름, 네임스페이스, 레이블 등을 포함할 수 있다.
                   metadata:
                     # Deployment의 이름을 설정한다. 이 이름으로 Deployment를 식별할 수 있다.
                     name: boot001dep

                   # Deployment의 사양을 정의한다. 여기에는 레플리카 수, 셀렉터, 템플릿 등이 포함된다.
                   spec:

                     # 디플로이먼트가 관리할 파드를 선택하는 조건을 정의한다.
                     selector:
                       # 레이블 기반 선택을 설정한다.
                       matchLabels:
                         # 이 셀렉터는 'app=boot001kube' 레이블이 있는 파드만을 대상으로 한다.
                         app: boot001kube

                     # 생성할 파드의 레플리카 수를 설정한다. 여기서는 3개의 파드를 생성한다.
                     replicas: 3

                     # 파드 템플릿을 정의한다. 이 템플릿은 파드 생성에 사용된다.
                     template:

                       # 파드 하나의 메타데이터를 정의한다.
                       metadata:
                         # 파드에 부여할 레이블을 설정한다.
                         labels:
                           # 생성될 파드에 'app=boot001kube' 레이블을 부여한다.
                           app: boot001kube

                       # 파드의 스펙을 정의한다.
                       spec:
                         # 파드 내에 포함될 컨테이너 목록을 정의한다.
                         containers:
                         - name: boot-container # 컨테이너의 이름을 설정한다.
                           image: {{dockerhub username}}/k8s-boot:latest # 사용할 컨테이너 이미지를 지정한다.
                           imagePullPolicy: Always # 이미지 풀 정책을 'Always'로 설정하여 항상 최신 이미지를 사용하도록 한다.

                           # 컨테이너가 사용할 포트 정보를 정의한다.
                           ports:
                           - containerPort: 8080 # 컨테이너가 리스닝할 포트 번호를 지정한다.

                   ```


               ![image.png](attachment:55787b26-c970-45b9-b9b7-75a7ad05c927:image.png)

               ## 2. 서비스 작성

               - vue001ser.yml 파일 생성

                   ```yaml
                   # 네트워크 서비스로 파드 집합에서 실행중인 애플리케이션을 노출하는 방법
                   apiVersion: v1 # 사용할 Kubernetes API 버전을 명시한다. 'v1'은 Service와 같은 기본 리소스를 지원한다.

                   kind: Service # 생성하려는 리소스의 종류를 나타낸다. 여기서는 Service를 생성하려고 한다.

                   metadata:
                     # Service의 이름을 설정한다. 이 이름으로 Service를 식별할 수 있다.
                     name: vue001ser

                   spec: # Service의 사양을 정의한다. 여기에는 서비스 유형, 포트 설정, 셀렉터 등이 포함된다.
                     # 서비스 유형을 NodePort로 설정한다. 이는 클러스터 외부에서 내부 파드에 접근할 수 있도록 한다.
                     type: NodePort

                     ports: # 서비스의 포트 설정을 정의한다.

                     - port: 8000 # 클러스터 내부에서 다른 서비스를 접근할 때 사용할 포트 번호를 설정한다.

                       # 컨테이너 포트(파드의 포트)
                       targetPort: 5173 # 파드 내에서 리스닝할 포트 번호를 설정한다.

                       protocol: TCP # 사용할 프로토콜을 TCP로 설정한다.

                       # 워커 노드의 포트
                       nodePort: 30000 # 클러스터의 모든 노드에서 이 포트를 통해 서비스에 접근할 수 있다.(30000 ~ 32767)

                     selector:
                       # 이 셀렉터는 'app=vue001kube' 레이블이 있는 파드를 서비스의 대상으로 지정한다.
                       app: vue001kube

                   ```

                   - 이 YAML 파일은 **vue001ser**라는 이름의 Service를 생성한다. 이 서비스는 **NodePort** 유형으로 설정되어 있으며, 이는 클러스터 외부의 사용자가 **NodePort**로 지정된 클러
               스터 노드의 특정 포트(여기서는 30000번)를 통해 서비스에 접근할 수 있음을 의미한다. 서비스는 **port**로 지정된 8000번 포트에서 외부 요청을 받아, **selector**에 의해 선택된
               **app=vue001kube** 레이블을 가진 파드의 **targetPort**인 5173번 포트로 요청을 전달한다.
                   - 이 구성을 통해, Kubernetes 클러스터 내부에서 실행 중인 애플리케이션을 외부에서 접근 가능하게 하며, 특정 파드에 직접적으로 접근하는 대신 서비스를 통해 접근함으로써 파드
               의 확장성과 관리성을 향상시킨다.

               - boot001ser.yml 파일 생성

                   ```yaml
                   apiVersion: v1 # 사용할 Kubernetes API 버전을 명시한다. 'v1'은 Service와 같은 기본 리소스를 지원한다.

                   kind: Service # 생성하려는 리소스의 종류를 나타낸다. 여기서는 Service를 생성하려고 한다.

                   metadata:
                     # Service의 이름을 설정한다. 이 이름으로 Service를 식별할 수 있다.
                     name: boot001ser

                   spec: # Service의 사양을 정의한다. 여기에는 서비스 유형, 포트 설정, 셀렉터 등이 포함된다.
                     # 서비스 유형을 NodePort로 설정한다. 이는 클러스터 외부에서 내부 파드에 접근할 수 있도록 한다.
                     type: NodePort

                     ports: # 서비스의 포트 설정을 정의한다.
                     - port: 8001 # 클러스터 내부에서 다른 서비스를 접근할 때 사용할 포트 번호를 설정한다.
                       targetPort: 8080 # 파드 내에서 리스닝할 포트 번호를 설정한다.
                       protocol: TCP # 사용할 프로토콜을 TCP로 설정한다.
                       nodePort: 30001 # 클러스터의 모든 노드에서 이 포트를 통해 서비스에 접근할 수 있다.(30000 ~ 32767)

                     selector:
                       # 이 셀렉터는 'app=boot001kube' 레이블이 있는 파드를 서비스의 대상으로 지정한다.
                       app: boot001kube

                   ```

                   ![image.png](attachment:c2839a9c-81d8-4d9e-95b4-bf9f236eab1e:image.png)


               ## 3. 매니페스트 조작

               - 매니페스트를 적용하기 위한 kubectl 명령어들


                   | **분류** | **명령어** | **설명** |
                   | --- | --- | --- |
                   | **적용 및 생성** | `kubectl apply -f <파일명>` | YAML 파일에 정의된 대로 리소스를 생성하거나 변경 사항을 적용합니다. |
                   | **상태 조회** | `kubectl get deployments` | 현재 클러스터에서 실행 중인 디플로이먼트 목록을 확인합니다. |
                   |  | `kubectl get pods` | 현재 실행 중인 파드(Pod)들의 목록과 상태를 확인합니다. |
                   |  | `kubectl get services` | 생성된 서비스(Service)와 클러스터 IP, 포트 정보를 확인합니다. |
                   | **삭제** | `kubectl delete pods <pod명>` | 특정 파드를 강제로 삭제합니다. (디플로이먼트가 있는 경우 다시 살아날 수 있음) |
                   |  | `kubectl delete deployments <deployment명>` | 특정 디플로이먼트를 삭제합니다. (관리하던 파드들도 함께 삭제됨) |
                   |  | `kubectl delete services <service명>` | 특정 서비스를 삭제하여 외부/내부 노출을 중단합니다. |
                   | **관리 및 업데이트** | `kubectl rollout restart deployments <deployment명>` | 디플로이먼트를 재시작하여 변경된 이미지를 다시 불러오거나 파드를 순차적으로 교체합니다. |
                   | **문제 해결** | `kubectl logs <pod명>` | 특정 파드(컨테이너) 내부에서 발생하는 로그를 확인하여 오류를 진단합니다. |

                   ```bash
                   # Deployment 적용
                   kubectl apply -f <deployment yml 파일명(확장자 포함)>
                   kubectl get deployments
                   kubectl get pods

                   # Service 적용
                   kubectl apply -f <service yml 파일명(확장자 포함)>
                   kubectl get services

                   # 기존 것을 지울려면
                   kubectl delete pods <pod명>
                   kubectl delete deployments <deployment명>
                   kubectl delete services <service명>

                   # 로그 확인하기
                   kubectl logs <pod명>

                   # 이미지 변경후 새로운 이미지 가져오기
                   kubectl rollout restart deployments <deployment명>
                   ```## 1. Ingress 관련 설정 코드

               ### Vue 관련

               - nginx.conf 파일 추가

                   ```bash
                   # Nginx 서버 설정 블록
                   server {
                       # 80번 포트에서 HTTP 요청을 수신
                       listen 80;
                       # 서버 이름을 localhost로 설정
                       server_name localhost;

                       # 모든 요청 처리
                       location / {
                           # '/usr/share/nginx/html/' 디렉토리를 루트로 설정
                           root /usr/share/nginx/html/;
                           # 요청된 URI에 해당하는 파일을 찾고, 없으면 '/index.html'로 폴백
                           try_files $uri $uri/ /index.html;
                       }
                   }
                   ```

                   > Kubernetes 클러스터에서 Nginx 컨테이너는 Pod의 일부로 실행되며 build 된 vue의 정적 파일을 배포하는 웹서버 역할을 할 것이다.
                   >

               - Dockerfile 수정

                   ```bash
                   FROM node:lts-alpine AS build-stage
                   WORKDIR /app
                   COPY package*.json ./
                   RUN npm install
                   COPY . .
                   RUN npm run build

                   # nginx 이미지를 사용하여 프로덕션 스테이지를 구성한다.
                   FROM nginx:stable-alpine AS production-stage
                   # 빌드 스테이지에서 생성된 정적 파일들을 Nginx의 기본 웹 서버 디렉토리로 복사한다.
                   # 이렇게 하면 Nginx가 Vue.js 애플리케이션의 빌드된 파일들을 서빙할 수 있게 된다.
                   COPY --from=build-stage /app/dist /usr/share/nginx/html

                   # 로컬 디렉토리의 nginx.conf 파일을 Nginx의 설정 디렉토리로 복사한다.
                   # 이 설정 파일은 Nginx의 동작을 커스터마이즈하는데 사용된다.
                   # 예를 들어, 라우팅 규칙, SSL 설정, 로깅 등을 정의할 수 있다.
                   COPY ./nginx.conf /etc/nginx/conf.d/default.conf
                   EXPOSE 80
                   CMD ["nginx", "-g", "daemon off;"]
                   ```


               - vue002dep.yml 파일 생성

                   ```bash
                   apiVersion: apps/v1
                   kind: Deployment
                   metadata:
                     name: vue002dep
                   spec:
                     selector:
                       matchLabels:
                         app: vue002kube
                     template:
                       metadata:
                         labels:
                           app: vue002kube
                       spec:
                         containers:
                         - name: vue-container
                           image: {{Dockerhub username}}/k8s-vue-ing:latest
                           imagePullPolicy: Always
                           ports:
                           - containerPort: 80      # 5173이 아니라 80으로 바꿔야 한다.
                   ```


               - vue002ser.yml 파일 생성

                   ```bash
                   apiVersion: v1
                   kind: Service
                   metadata:
                     name: vue002ser
                   spec:
                     type: **ClusterIP**
                     ports:
                     - port: 8000
                       targetPort: 80      # 5173에서 80으로 변경
                     selector:
                       app: vue002kube
                   ```


               > ClusterIP : 쿠버네티스 클러스터 내에서만 접근 가능한 가상의 IP 주소를 할당. 내부 서비스 간의 통신을 위한 목적으로 사용. 외부에서 접근할 필요가 없는 백엔드 서비스에 적합.
               (우리는 ingress로 접근)
               NodePort : 클러스터의 각 노드에 특정 포트를 개방하여, 외부에서 접근할 수 있는 서비스를 제공. 개발 및 테스트 환경에서 외부 접근을 쉽게 할 수 있도록 지원.
               >

               ### Spring Boot 관련

               - boot002dep.yml 파일 생성

                   ```bash
                   apiVersion: apps/v1
                   kind: Deployment
                   metadata:
                     name: boot002dep
                   spec:
                     selector:
                       matchLabels:
                         app: boot002kube
                     # replicas: 3
                     replicas: 1
                     template:
                       metadata:
                         labels:
                           app: boot002kube
                       spec:
                         containers:
                         - name: boot-container
                           image: {{Dockerhub username}}/k8s-boot-ing:latest
                           imagePullPolicy: Always
                           ports:
                           - containerPort: 8080
                   ```


               - boot002ser.yml 파일 생성

                   ```bash
                   apiVersion: v1
                   kind: Service
                   metadata:
                     name: boot002ser
                   spec:
                     type: ClusterIP
                     ports:
                     - port: 8001
                       targetPort: 8080
                     selector:
                       app: boot002kube
                   ```


               ### Ingress

               - ingress002.yml 파일 생성

                   ```bash
                   apiVersion: networking.k8s.io/v1
                   kind: Ingress
                   metadata:
                     name: ingress002
                     annotations:
                       # 기본적으로 NGINX Ingress 컨트롤러는 모든 HTTP 트래픽을 HTTPS로 리다이렉트한다.
                       # 이 설정을 "false"로 지정하면 HTTP 요청을 HTTPS로 자동 리다이렉트 하지 않는다.
                       # 개발 환경이나 SSL/TLS가 필요 없는 상황에서 유용하다.
                       nginx.ingress.kubernetes.io/ssl-redirect: "false"
                       # 매칭된 경로에서 /boot 부분을 제거하고 나머지 부분만 백엔드 서비스로 전달한다.
                       nginx.ingress.kubernetes.io/rewrite-target: /$2
                   spec:
                     # 사용할 Ingress 컨트롤러의 클래스를 지정. 여기서는 NGINX Ingress 컨트롤러를 사용하도록 설정
                     ingressClassName: nginx
                     rules:
                     - http:
                         paths:
                         - path: /()(.*)
                           # rewriter와 관련된 사이트 주소: https://kubernetes.github.io/ingress-nginx/examples/rewrite/
                           # ImplementationSpecific은 Ingress 컨트롤러의 구현에 따라 경로 매칭 방식이 결정된다.
                           # NGINX Ingress Controller의 경우, 정규 표현식을 포함한 더 유연한 라우팅 규칙을 적용할 수 있다.
                           # 이는 Prefix나 Exact보다 더 복잡한 경로 매칭을 가능하게 한다.
                           # 예: /, /about, /users 등
                           pathType: ImplementationSpecific
                           backend:
                             service:
                               name: vue002ser
                               port:
                                 number: 8000
                         - path: /boot(/|$)(.*)
                           # 여기서도 ImplementationSpecific을 사용하여 /boot로 시작하는 모든 경로를 매칭한다.
                           # (/|$)는 /boot 다음에 /가 오거나 문자열이 끝나는 경우를 모두 포함한다.
                           # (.*)는 그 뒤의 모든 문자열을 캡처한다.
                           # 예: /boot, /boot/, /boot/plus, /boot/users 등
                           pathType: ImplementationSpecific
                           backend:
                             service:
                               name: boot002ser
                               port:
                                 number: 8001
                   ```


               ### Ingress 설정 후 기존 코드와 달라져야 할 부분

               - **프론트엔드**에서 백엔드의 워커노드로 요청하는 부분 수정

                   ```jsx
                   /* Ingress 적용 이전 30001번의 백엔드 워커노드와 통신 */
                   // const response = await fetch(`http://localhost:30001/plus?num1=${num1.value}&num2=${num2.value}`);

                   /* Ingress를 활용한 절대 경로로 통신 */
                   const response = await fetch(`/boot/plus?num1=${num1.value}&num2=${num2.value}`);
                   ```

               - **백엔드**에서 프론트엔드에 대한 CORS 처리 불필요
               - 수정 후 docker image를 build하고 docker hub에 push 한다.

                   ```bash
                   # vue 프로젝트 폴더로 이동 후 수행
                   docker build -t {{Dockerhub username}}/k8s-vue-ing .
                   docker push {{Dockerhub username}}/k8s-vue-ing

                   # boot 프로젝트 폴더로 이동 후 수행
                   docker build -t {{Dockerhub username}}/k8s-boot-ing .
                   docker push {{Dockerhub username}}/k8s-boot-ing
                   ```


               ## 2. Nginx를 위한 Ingress Controller 설치 및 ingress 적용 코드

               - nginx-Ingress 컨트롤러 설치

                   ```docker
                   kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml
                   ```

               - 현재 클러스터에 설정된 Ingress 클래스 확인

                   ```docker
                   kubectl get ingressclass
                   ```

               - 쿠버네티스 클러스터에 리소스 배포

                   ```docker
                   kubectl apply -f ingress002.yml
                   ```

               - Deployment, Service 설정

                   ```docker
                   kubectl apply -f vue002ser.yml
                   kubectl apply -f boot002ser.yml
                   kubectl apply -f vue002dep.yml
                   kubectl apply -f boot002dep.yml

                   kubectl get deployments,services
                   ```

               - Ingress 컨트롤러 IP 확인

                   ```bash
                   kubectl get ingress
                   ```

               - 프론트 엔드 접근

                   ```docker
                   http://<INGRESS_IP>/
                   ```

               - 백엔드 접근

                   ```docker
                   http://<INGRESS_IP>/boot
                   ```


               ![image.png](attachment:24985d41-dbad-4983-8ac9-4362632857bb:image.png)

               <aside>
               ☝

               ### ① 외부 클라이언트 접속 (External Client)

               - 사용자가 브라우저를 통해 도메인이나 IP로 접속

               ### ② 인그레스 규칙 및 경로 재작성 (Ingress & Rewrite)

               - 인그레스 설정(`ingress002.yml`)에 따라 요청이 분기됨
                   - **백엔드 요청 (`/boot` 경로):**
                       - 사용자가 `.../boot/health`으로 접속하면, 인그레스가 이를 감지.
                       - `rewrite-target` 설정에 의해 `/boot` 부분이 제거되고 실제 서버에는 `/health`으로 전달됨.
                       - 요청은 `boot002ser` 서비스의 **8001**번 포트로 보내짐.
                   - **프론트엔드 요청 (`/` 경로):**
                       - 그 외의 모든 요청(루트 경로 포함)은 `vue002ser` 서비스로 향함.
                       - 요청은 `vue002ser` 서비스의 **8000**번 포트로 보내짐

               ### ③ 서비스에서 파드로 전달 (Service to Pod)

               - 서비스는 각각 연결된 파드의 실제 애플리케이션 포트로 트래픽을 넘겨줌
                   - **Spring Boot:** 서비스의 **8001** 포트로 들어온 요청을 파드(`boot002kube`) 내부의 **8080** 포트로 전달함.
                   - **Vue (NGINX):** 서비스의 **8000** 포트로 들어온 요청을 파드(`vue002kube`) 내부의 **80** 포트로 전달함.
               </aside>

               ## 3. 무중단 배포 관련 설정 코드

               - 기존 리소스 삭제 (`kubectl delete --all deployments,services,ingress` )
               - vue 프로젝트 이동 → 이미지 v1, v2 생성

               ```bash
               docker build -t {{Dockerhub username}}/k8s-vue-ing:v1 .
               docker build -t {{Dockerhub username}}/k8s-vue-ing:v2 .

               docker push {{Dockerhub username}}/k8s-vue-ing:v1
               docker push {{Dockerhub username}}/k8s-vue-ing:v2
               ```

               - vue003dep.yml

               ```yaml
               apiVersion: apps/v1
               kind: Deployment
               metadata:
                 name: vue003dep
               spec:
                 replicas: 3 # 여러 개의 Pod을 운영하여 무중단 배포를 테스트
                 selector:
                   matchLabels:
                     app: vue003kube
                 template:
                   metadata:
                     labels:
                       app: vue003kube
                   spec:
                     containers:
                       - name: vue-container
                         image: {{Dockerhub username}}/k8s-vue-ing:v1 # 초기 배포 시 v1 이미지 사용
                         imagePullPolicy: Always
                         ports:
                           - containerPort: 80
                 strategy:
                   type: RollingUpdate
                   rollingUpdate:
                     maxUnavailable: 1 # 배포 중 사용할 수 없는 최대 Pod 수
                     maxSurge: 1       # 배포 시 추가 생성 가능한 최대 Pod 수

               ```

               - vue003ser.yml

               ```yaml
               apiVersion: v1
               kind: Service
               metadata:
                 name: vue003ser
               spec:
                 type: ClusterIP
                 ports:
                   - port: 8000
                     targetPort: 80
                 selector:
                   app: vue003kube

               ```

               - ingress003.yml

               ```yaml
               apiVersion: networking.k8s.io/v1
               kind: Ingress
               metadata:
                 name: ingress003
                 annotations:
                   nginx.ingress.kubernetes.io/ssl-redirect: "false"
                   nginx.ingress.kubernetes.io/rewrite-target: /$2
               spec:
                 ingressClassName: nginx
                 rules:
                   - http:
                       paths:
                         - path: /()(.*)
                           pathType: ImplementationSpecific
                           backend:
                             service:
                               name: vue003ser
                               port:
                                 number: 8000

               ```

               ## 4. 무중단 배포 테스트 절차

               1. **초기 배포** - `v1` 이미지로 배포

                   ```bash
                   kubectl apply -f vue003dep.yml
                   kubectl apply -f vue003ser.yml
                   kubectl apply -f ingress003.yml
                   ```

               2. **배포 상태 확인**

                   ```bash
                   kubectl get deployments vue003dep
                   kubectl get pods -l app=vue003kube
                   ```

               3. **무중단 배포 진행 상태 확인**
                   - `vue003kube` 라벨이 있는 모든 Pod의 상태를 실시간으로 출력
                   - 옵션을 사용하면 새로운 Pod이 생성되거나 기존 Pod이 종료되는 과정이 모두 표시되므로, 롤링 업데이트의 진행 상황을 실시간으로 확인할 수 있다.

                   ```bash
                   kubectl get pods -w -l app=vue003kube
                   ```

                   - 업데이트 중에 발생한 이벤트와 문제를 세부적으로 확인할 수 있다.

                   ```docker
                   kubectl describe deployment vue003dep
                   ```

               4. **이미지 업데이트를 통한 롤링 업데이트 적용**

                   ```bash
                   # 새 터미널 열어서 다른 버전 이미지로 업데이트
                   kubectl set image deployment/vue003dep vue-container={{Dockerhub username}}/k8s-vue-ing:v2
                   ```# 1. 테라폼 설치

               [Install | Terraform | HashiCorp Developer](https://developer.hashicorp.com/terraform/install)

               - Window 유저
                   - 386 클릭 → zip 파일 다운로드

                       ![스크린샷 2026-02-13 오전 12.25.26.png](attachment:690fb5a6-5998-46b1-a8fb-c1c1b3eba7f5:스크린샷_2026-02-13_오전_12.25.26.png)

                   - zip 파일 압축 해제 → terraform.exe 파일 확인
                   - terraform.exe 파일을 C:\Program Files\terraform 폴더에 옮기기
                   - 환경 변수 → path → C:\Program Files\terraform 추가

               - macOS 유저

                   ```jsx
                   brew install terraform
                   ```


               # 2. Terraform 생성 및 실행

               ## 2-1. 기존의 k8s 리소스 기반의 Terraform 파일 생성

               > Terraform 파일
               = tf config
               = Terraform 구성 파일
               = HCL(HashiCorp Configuration Language) 파일
               >
               - **`main.tf 생성`**

                   ```bash
                   # Kubernetes 프로바이더 설정
                   provider "kubernetes" {
                     config_path = "~/.kube/config"  # 로컬 Kubernetes 설정 파일 경로
                   }

                   # Spring Boot Deployment
                   resource "kubernetes_deployment_v1" "boot007dep" {
                     metadata {
                       name = "boot007dep"
                     }

                     spec {
                       replicas = 1

                       selector {
                         match_labels = {
                           app = "boot007kube"
                         }
                       }

                       template {
                         metadata {
                           labels = {
                             app = "boot007kube"
                           }
                         }

                         spec {
                           container {
                             image = "goatohgiraffers/k8s-boot-ing:latest"
                             name  = "boot-container"
                             image_pull_policy = "Always"

                             port {
                               container_port = 8080
                             }
                           }
                         }
                       }
                     }
                   }

                   # Spring Boot Service
                   resource "kubernetes_service_v1" "boot007ser" {
                     metadata {
                       name = "boot007ser"
                     }

                     spec {
                       selector = {
                         app = "boot007kube"
                       }

                       port {
                         port        = 8001
                         target_port = 8080
                       }

                       type = "ClusterIP"
                     }
                   }

                   # Vue.js Deployment
                   resource "kubernetes_deployment_v1" "vue007dep" {
                     metadata {
                       name = "vue007dep"
                     }

                     spec {
                       replicas = 1

                       selector {
                         match_labels = {
                           app = "vue007kube"
                         }
                       }

                       template {
                         metadata {
                           labels = {
                             app = "vue007kube"
                           }
                         }

                         spec {
                           container {
                             image = "goatohgiraffers/k8s-vue-ing:latest"
                             name  = "vue-container"
                             image_pull_policy = "Always"

                             port {
                               container_port = 80
                             }
                           }
                         }
                       }
                     }
                   }

                   # Vue.js Service
                   resource "kubernetes_service_v1" "vue007ser" {
                     metadata {
                       name = "vue007ser"
                     }

                     spec {
                       selector = {
                         app = "vue007kube"
                       }

                       port {
                         port        = 8000
                         target_port = 80
                       }

                       type = "ClusterIP"
                     }
                   }

                   # Ingress
                   resource "kubernetes_ingress_v1" "sw_camp_ingress" {
                     metadata {
                       name = "sw-camp-ingress"
                       annotations = {
                         "nginx.ingress.kubernetes.io/ssl-redirect" = "false"
                         "nginx.ingress.kubernetes.io/rewrite-target" = "/$2"
                       }
                     }

                     spec {
                       ingress_class_name = "nginx"

                       rule {
                         http {
                           path {
                             path = "/()(.*)$"
                             path_type = "ImplementationSpecific"
                             backend {
                               service {
                                 name = "vue007ser"
                                 port {
                                   number = 8000
                                 }
                               }
                             }
                           }
                           path {
                             path = "/boot(/|$)(.*)$"
                             path_type = "ImplementationSpecific"
                             backend {
                               service {
                                 name = "boot007ser"
                                 port {
                                   number = 8001
                                 }
                               }
                             }
                           }
                         }
                       }
                     }
                   }
                   ```


               ## 2-2. Terraform 워크플로우

               <aside>
               💡

               1. Write(작성): 인프라를 코드로 정의
               2. Plan(계획): 변경 사항 미리 확인
               3. Apply(적용): 인프라에 변경 사항 적용
               </aside>

               ```bash
               # 초기화
               # (코드 작성 후 인프라를 계획하고 적용하기 전에 필요한 준비 과정)
               terraform init

               # 계획 수립
               terraform plan

               # 적용
               terraform apply
               ```

               # 3. kubernetes 리소스 확인 및 제거

               - 전체 리소스 확인

                   ```bash
                   kubectl get all
                   ```

               - 각 리소스 별 삭제

                   ```bash
                   kubectl delete deployment --all
                   kubectl delete service --all
                   kubectl delete ingress --all
                   ```


               # 4. Terraform 파일들 제거

               - Terraform 설정 제거

                   ```bash
                   terraform plan -destroy

                   terraform destroy
                   ```

               - Terraform 관련 파일 정리

                   ```bash
                   rm -rf .terraform*
                   rm terraform.tfstate*
                   ```# 1. 젠킨스

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/c7f659ce-2f63-4e64-ba07-d0ec53d40a0b/Untitled.png)

               ## 1-1. 젠킨스란?

               <aside>
               💡 **젠킨스(Jenkins)**는 소프트웨어 개발의 지속적 통합(CI)과 지속적 배포(CD) 프로세스를 자동화하는 데 널리 쓰이는 오픈 소스 자동화 서비스이다.
               Java로 개발된 이 도구는 버전 컨트롤 시스템(VCS)과 통합해 소프트웨어 개발 프로젝트의 빌드, 테스트, 배포 작업을 자동으로 실행한다.
               젠킨스는 플러그인 기반 구조를 갖추고 있어 다양한 개발, 테스팅, 배포 도구들과의 쉬운 통합이 가능하다.

               </aside>

               ## 1-2. 젠킨스 등장 배경

               <aside>
               💡 2004년 카와구치 코스케(**Kohsuke Kawagunchi)**에 의해 처음 개발되었다.
               당시 소프트웨어 개발 프로세스에서 반복적이고 수동적인 작업들을 자동화하며 개발 주기를 단축하고 품질을 향상시키려는 필요성이 컸다. 이러한 배경 속에서 지속적 통합과 배포의 중요
               성이 대두되었고, 젠킨스는 이러한 요구를 충족시키는 툴로 발전했다.
               (추가로, Jenkins는 Hudson이라는 이름으로 시작되었다가, 2011년에 Oracle과의 상표권 분쟁 후 Jenkins로 이름이 바뀌었다)

               </aside>

               ## 1-3. 젠킨스 장단점

               ### 1-3-1. 장점

               - **유연성:** 플러그인으로 확장 가능한 구조로, 다양한 도구와의 통합을 지원한다.
               - **강력한 커뮤니티:** 오픈 소스 프로젝트로서 활발한 커뮤니티 지원과 광범위한 플러그인 생태계를 자랑한다.
               - **자동화된 빌드/테스트/배포:** 소프트웨어 개발 과정에서의 반복적인 작업을 자동화함으로써 생산성을 향상시킨다.

               ### 1-3-2 단점

               - **학습 곡선:** 젠킨스와 플러그인 시스템은 초기 설정과 관리에 있어 상당한 학습이 필요할 수 있다.
               - **리소스 요구량:** 복잡한 프로젝트에서는 상당한 시스템 리소스를 소모할 수 있다.
               - **성능 이슈:** 대규모 프로젝트(높은 동시성 요구)나 빠른 속도의 CI/CD 파이프라인을 요구하는 환경에서는 Jenkins를 다루기 어려울 수 있다.

               # 2. 젠킨스 구성 요소

               ## 2-1. 마스터 & 에이전트 구조

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/2cd4f64e-4879-4881-bcb9-7457dc6306f6/Untitled.png)

               - **마스터:** 젠킨스 서버의 핵심이 되는 구성 요소로, 작업의 스케줄링(주기적 작업 설정, 이벤트 기반 또는 병렬 실행, 작업 실행 시간 등), 작업 분배, 플러그인 관리, 사용자 인터페
               이스 제공 등의 역할을 한다.
               - **에이전트:** 마스터에 의해 할당된 작업을 실행하는 시스템. 다양한 환경에서 빌드를 실행할 수 있도록 도와준다.

               ## 2-2. 플러그인

               <aside>
               💡 젠킨스의 기능을 확장할 수 있는 플러그인들은 여러가지가 있다.
               이러한 플러그인들은 젠킨스가 거의 모든 개발, 테스트, 배포 도구와 통합할 수 있게 도와준다.

               </aside>

               - **젠킨스의 대표적인 플러그인들**


                   | **플러그인 이름** | **설명** |
                   | --- | --- |
                   | Git Plugin | Git 저장소와의 통합을 지원하며, 소스 코드 체크아웃 등의 기능을 제공 |
                   | Pipeline | 복잡한 CI/CD 프로세스를 스크립트로 정의할 수 있는 플러그인 |
                   | Maven Integration Plugin | Maven 빌드 시스템과의 통합을 지원 |
                   | Docker Pipeline | Docker 명령을 Jenkinsfile 내에서 실행할 수 있게 하는 플러그인 |
                   | JUnit Plugin | JUnit 테스트 결과를 보고하고 시각화하는 기능 제공 |
                   | Blue Ocean | 젠킨스의 사용자 인터페이스를 현대적으로 재설계한 플러그인 |
                   | Credentials Binding Plugin | 민감한 정보(비밀번호, SSH 키 등)를 안전하게 관리할 수 있는 플러그인 |
                   | Slack Notification Plugin | 빌드 상태에 대한 알림을 Slack으로 전송할 수 있게 해주는 플러그인 |
                   | Gradle Plugin | Gradle 빌드와 통합을 지원, Jenkins 내에서 직접 Gradle 작업을 관리할 수 있게 해주는 플러그인 |
                   | Publish Over SSH | Jenkins 빌드 프로세스 중에 생성된 파일을 SSH를 통해 원격 서버에 전송할 수 있게 해주는 플러그인 |

               ## 2-3. 작업과 빌드

               - **작업(Job):** 실행할 작업의 설정을 포함한다. 소스 코드 체크 아웃, 빌드 실행, 테스트 수행 등의 작업이 포함될 수 있다.
               - **빌드(Build):** 작업의 실행 인스턴스로, 하나의 작업이 여러 번 실행될 수 있으며, 각 실행은 각기 다른 빌드 번호를 갖는다.
               - **빌드 파이프라인(Pipeline):** Jenkins의 핵심 기능 중 하나는 빌드 파이프라인을 정의하고 관리할 수 있다는 것이다. Groovy 기반의 Jenkinsfile을 작성하여 복잡한 빌드, 테스트,
               배포 작업을 코드로 정의할 수 있다.(IaC 접근 방식 채택)

               ## 2-4. 소스 코드 관리

               <aside>
               💡 젠킨스는 Git, SVN 등 다양한 소스 코드 관리 시스템과 통합할 수 있으며, 코드 변경을 감지하고 자동으로 빌드를 시작할 수 있다.

               이와 같은 구성 요소들을 통해 젠킨스는 지속적 통합과 지속적 배포를 위한 강력하고 유연한 자동화 도구로서의 역할을 수행한다.

               </aside>

               # 3. 파이프라인 실행 테스트

               ## 3-1. pipeline script 문법

               <aside>
               💡

               **Declarative Pipeline**을 사용하면 직관적인 구조로 Jenkins 파이프라인을 구성할 수 있다. Jenkinsfile을 통해 관리하며 이 Jenkinsfile은 **Groovy**라는 JVM 기반의 스크립팅 언어
               로 작성된다. **Groovy**는 Java 플랫폼에서 실행되는 스크립트 언어이다. Groovy는 **변수를 선언할 때 타입을 명시하지 않아도** 자동으로 타입이 추론되며, Jenkins Pipeline에서는 주
               로 스테이지, 단계 정의와 같은 제어문을 위해 Groovy 문법을 사용한다.

               </aside>

               ### 3-1-1. pipeline

               - **pipeline** 블록은 파이프라인의 최상위 구조를 정의하며, 이 안에 빌드 단계(stage), 실행 환경(agent) 등을 작성한다.
               - 기본 구조는 `pipeline { ... }` 형태를 가지며, 파이프라인의 단계와 구성을 모두 이 블록 안에 정의한다.

               ### 3-1-2. agent

               - `agent`는 파이프라인을 **어느 Jenkins 노드에서 실행할지** 정의하는 부분이다.
               - **`agent any`**: Jenkins에서 사용 가능한 모든 노드 중 하나에서 파이프라인이 실행된다. 기본적으로는 Jenkins가 자동으로 적합한 노드를 선택해 작업을 수행한다.
               - 특정 노드나 레이블을 지정할 수도 있으며, 필요한 경우 Docker 컨테이너로 지정할 수도 있다.

               ### 3-1-3. stages, stage

               - `stages` 블록은 파이프라인에서 **여러 빌드 단계**를 정의하는 곳이다. `stages` 안에 개별 `stage` 블록들을 나열하여 빌드 프로세스를 정의한다.
               - 각 `stage` 블록은 파이프라인의 단계 단위로, 예를 들어 **빌드(Build)**, **테스트(Test)**, **배포(Deploy)** 등의 작업 단계를 의미한다.

               ### 3-1-4. steps

               - `steps`는 각 단계(stage) 내에서 **실행할 작업**을 정의한다. 주로 Shell 명령어(`sh`), 스크립트 실행(`script`), 그리고 메시지 출력(`echo`) 등을 사용한다.
               - `steps`는 **각 단계에서 수행할 명령의 세부 작업**들을 지정하는 블록이다.

               ## 3-2. **기본 파이프라인 구조 테스트**

               - 이 파이프라인은 `Build`, `Test`, `Deploy` 세 가지 스테이지를 간단하게 정의하여 각 단계에서 메세지를 출력한다.

               ```groovy
               pipeline {
                   agent any
                   stages {
                       stage('Build') {
                           steps {
                               echo 'Building...'
                           }
                       }
                       stage('Test') {
                           steps {
                               echo 'Testing...'
                           }
                       }
                       stage('Deploy') {
                           steps {
                               echo 'Deploying...'
                           }
                       }
                   }
               }

               ```

               ## 3-3. **Timeout 테스트**

               - 이 파이프라인은 `Timeout` 설정을 통해 5초 안에 작업이 완료되지 않으면 실패하도록 설정한다. Jenkins가 작업을 중단하는 모습을 통해 Timeout 기능을 테스트할 수 있다.

               ```groovy
               pipeline {
                   agent any
                   stages {
                       stage('Timeout Example') {
                           steps {
                               timeout(time: 5, unit: 'SECONDS') {
                                   script {
                                       echo 'Starting a task...'
                                       sleep 10
                                       echo 'Task completed!'
                                   }
                               }
                           }
                       }
                   }
               }

               ```

               ## 3-4. **Retry 테스트**

               - 이 파이프라인은 `Retry` 기능을 사용하여 최대 3번까지 재시도를 하게 된다. `exit 1`을 사용하여 강제로 실패하게 만든 후, 재시도하는 모습을 확인할 수 있다.

               ```groovy
               pipeline {
                   agent any
                   stages {
                       stage('Retry Example') {
                           steps {
                               retry(3) {
                                   script {
                                       echo 'Trying...'
                                       sh 'exit 1' // 의도적으로 실패
                                   }
                               }
                           }
                       }
                   }
               }

               ```

               ## 3-5. **WaitUntil 테스트**

               - 이 파이프라인은 `waitUntil`을 사용하여 조건이 충족될 때까지 기다린다. 조건이 3회 이상 시도하면 `true`로 설정하여 진행하도록 구성했다.

               ```groovy
               pipeline {
                   agent any
                   stages {
                       stage('WaitUntil Example') {
                           steps {
                               script {
                                   def counter = 0
                                   waitUntil {
                                       counter++
                                       echo "Checking condition... Attempt: ${counter}"
                                       return counter > 3 // 3번 시도 후 조건 충족
                                   }
                                   echo 'Condition met, proceeding...'
                               }
                           }
                       }
                   }
               }

               ```

               ## 3-6. **후처리 (Post-Action) 테스트**

               - `post` 블록을 통해 빌드 결과에 따른 후처리 작업을 정의할 수 있다. 빌드가 성공하거나 실패했을 때 각각 다른 메시지를 출력한다.

               ```groovy
               pipeline {
                   agent any
                   stages {
                       stage('Build') {
                           steps {
                               echo 'Building...'
                               sh 'exit 1' // 의도적으로 실패
                           }
                       }
                   }
                   post {
                       always {
                           echo 'This runs always, regardless of success or failure.'
                       }
                       success {
                           echo 'Build succeeded!'
                       }
                       failure {
                           echo 'Build failed!'
                       }
                   }
               }

               ```

               ## 3-7. **동시성 다루기 테스트**

               - 이 파이프라인에서는 `parallel`을 사용하여 `Task 1`과 `Task 2`를 동시에 실행하도록 설정했다. 동시 실행의 예를 보여준다.

               ```groovy
               pipeline {
                   agent any
                   stages {
                       stage('Parallel Stage') {
                           parallel {
                               stage('Task 1') {
                                   steps {
                                       echo 'Running Task 1'
                                       sleep 3
                                   }
                               }
                               stage('Task 2') {
                                   steps {
                                       echo 'Running Task 2'
                                       sleep 5
                                   }
                               }
                           }
                       }
                   }
               }

               ```## 1. **테스트 자동화 통합**

               <aside>
               💡

               Jenkins 파이프라인에 테스트 단계를 추가하여 코드 변경 시 자동으로 테스트를 실행한다.

               </aside>

               ### 1-1. 스크립트 설정

               ```yaml
                       stage('Source Build') {
                       ...
                       }
                       **stage('Run Tests') {
                           steps {
                               script {
                                   if (isUnix()) {
                                       sh "./gradlew test"
                                   } else {
                                       bat "gradlew.bat test"
                                   }
                               }
                           }
                           post {
                               always {
                                   junit '**/build/test-results/test/*.xml' // JUnit 결과 파일 경로
                               }
                           }
                       }**
                       stage('Container Build and Push') {
                       ...
                       }
               ```

               - **Run Tests Stage**
                   - **stage('Run Tests')**: 새로운 테스트 단계를 추가한다. 이 단계에서는 Gradle로 테스트를 실행한다.
                   - **sh "./gradlew test" / bat "gradlew.bat test"**: 각각 Unix 및 Windows 환경에서 Gradle 테스트를 실행한다.
                   - **post**: 테스트 결과를 JUnit 형식으로 보고한다. `junit` 함수는 테스트 결과 XML 파일을 읽어 Jenkins UI에서 확인할 수 있도록 해준다. 파일 경로는 Gradle의 기본 테스트 결
               과 위치를 따른다.
               - **JUnit 결과 파일 경로**
                   - `**/build/test-results/test/*.xml`: Gradle이 생성하는 JUnit 테스트 결과 XML 파일 경로이다. 필요에 따라 이 경로를 수정할 수 있다.

               ### 1-2. 결과 확인

               - Jenkins 대시보드에서 테스트 결과 확인

                   ![test-result.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/9976aba8-7523-4a16-9918-1a7b2bb40ffb/test-result.png)

                   ![test-result-2.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/dae3899c-99ee-410b-9aa0-161cd03f395e/test-result-2.png)


               ## 2. Discord Notifier

               <aside>
               💡

               Slack, Email 등 다양한 방법을 통해 Jenkins에서 build 결과에 따른 알림을 전달할 수 있다.

               </aside>

               ### 2-1. 환경 설정

               - Jenkins 관리 > Plugins에서 discord를 검색하여 `Discord Notifier` 를 install 한다.

               ![스크린샷 2024-11-04 233931.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/8ae18041-b738-43b9-a17a-9465bb71ed20/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-04_233931.png)

               - Discord에서 서버를 우클릭 한 뒤 서버 설정 > 연동을 클릭한다.

               ![스크린샷 2024-11-04 234134.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/103ffa8b-2fcb-40d3-8587-483337675ba8/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-04_234134.png)

               - 새 웹후크를 추가하고 이름을 설정하고 원하는 채널을 선택한다.

               ![스크린샷 2024-11-04 234302.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/9edd6cc2-797f-4f01-84ee-0c4f9ceb0e23/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-04_234302.png)

               ![스크린샷 2024-11-04 234318.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/d21ec350-d093-40ad-a501-7a8345ea058b/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-04_234318.png)

               - Jenkins 관리 > Credentials > New credentials 에서 새로운 자격 증명을 생성한다.
                   - kind : Secret Text
                   - secret : 웹후크 url 복사 값
                   - id : 설정하고 싶은 값

                   ![image.png](attachment:bb063e30-009e-47c3-93a8-52f0f2d5d2f4:image.png)


               ### 2-2. 스크립트 설정

               - 기존의 Script의 post에서 성공 실패 출력을 discord로 알림을 보내는 로직으로 수정한다.

               ```yaml
               ...
                   post {
                       always {
                           script {
                               if (isUnix()) {
                                   sh 'docker logout'
                               } else {
                                   bat 'docker logout'
                               }
                           }
                       }
                       **success {
                           withCredentials([string(credentialsId: 'discord', variable: 'DISCORD')]) {
                               discordSend(
                                   description: """
                                   **빌드 성공!** :tada:

                                   **제목**: ${currentBuild.displayName}
                                   **결과**: :white_check_mark: ${currentBuild.currentResult}
                                   **실행 시간**: ${currentBuild.duration / 1000}s
                                   **링크**: [빌드 결과 보기](${env.BUILD_URL})
                                   """,
                                   title: "${env.JOB_NAME} 빌드 성공!",
                                   webhookURL: "$DISCORD"
                               )
                           }
                       }
                       failure {
                           withCredentials([string(credentialsId: 'discord', variable: 'DISCORD')]) {
                               discordSend(
                                   description: """
                                   **빌드 실패!** :x:

                                   **제목**: ${currentBuild.displayName}
                                   **결과**: :x: ${currentBuild.currentResult}
                                   **실행 시간**: ${currentBuild.duration / 1000}s
                                   **링크**: [빌드 결과 보기](${env.BUILD_URL})
                                   """,
                                   title: "${env.JOB_NAME} 빌드 실패!",
                                   webhookURL: "$DISCORD"
                               )
                           }
                       }**
                   }
                 ...
               ```

               - `withCredentials`는 Jenkins의 자격 증명을 사용하여 Discord 웹훅 URL을 안전하게 가져오는 부분이다.
               - `credentialsId`로 설정된 ID에 해당하는 자격 증명이 `DISCORD`라는 변수에 저장된다.
               - `discordSend` 함수를 호출하여 Discord에 알림을 보낸다.

               ### 2-3. 테스트

               - 파이프라인 빌드 수행시 아래와 같이 결과를 전달 받을 수 있다.

               ![스크린샷 2024-11-05 000124.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/62107b1d-45ec-4e5c-b076-6a9c16c0c1c7/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-05_000124.png)# 1. ArgoCD

               ![download.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/fb3c890f-7a51-4b91-bcc0-4d9f4ed56825/download.png)

               ## 1-1. ArgoCD란?

               <aside>
               💡

               **ArgoCD**는 Kubernetes 애플리케이션을 GitOps 방식으로 배포하고 관리하는 오픈소스 도구이다. Git 저장소에서 애플리케이션의 선언적 정의를 관리하고, 이를 Kubernetes 클러스터에
               자동으로 동기화하여 지속적으로 관리할 수 있다. ArgoCD는 애플리케이션 배포의 일관성을 보장하며, 변경 사항을 자동으로 감지하여 클러스터의 상태를 최신 상태로 유지할 수 있다.

               </aside>

               > **`GitOps`**
               GitOps는 애플리케이션의 상태를 Git Repository에서 관리하고, 이를 Kubernetes와 같은 클라우드 네이티브 환경에 자동으로 적용하는 방법론이다. ArgoCD는 GitOps를 구현하는 도구다.
               >

               ## 1-2. ArgoCD 등장 배경

               <aside>
               💡

               ArgoCD는 2018년 Intuit에서 시작되었고, Kubernetes와 GitOps의 발전에 맞춰 만들어졌다. Kubernetes의 복잡한 상태 관리 및 배포 작업을 간소화하고, Git 저장소를 소스의 진리로 삼아
               자동화된 배포 및 운영을 가능하게 했다. GitOps의 인기가 증가하면서 ArgoCD는 Kubernetes에서 CI/CD 파이프라인을 구축하는 데 필수적인 도구로 자리 잡았다.

               </aside>

               ## 1-3. ArgoCD 장단점

               ### 1-3-1. 장점

               - **GitOps 기반의 선언적 배포**: Git 저장소에서 애플리케이션의 선언적 정의를 관리하여, 배포 상태를 Git과 Kubernetes가 일치하도록 유지할 수 있다.
               - **자동화된 동기화**: Git 저장소의 변경 사항을 감지하고 자동으로 Kubernetes 클러스터에 반영할 수 있어, 수동 배포 작업을 줄이고 배포 일관성을 보장할 수 있다.
               - **UI와 CLI 지원**: ArgoCD는 웹 UI와 CLI를 통해 애플리케이션 배포와 상태를 쉽게 관리할 수 있다.
               - **멀티 클러스터 지원**: 여러 Kubernetes 클러스터에 걸쳐 애플리케이션을 배포하고 관리할 수 있다.

               ### 1-3-2. 단점

               - **Kubernetes 의존성**: ArgoCD는 Kubernetes 환경에서만 작동하므로, Kubernetes를 사용하지 않는 환경에서는 사용할 수 없다.
               - **초기 설정의 복잡성**: GitOps 기반 워크플로우를 구축하기 위한 초기 설정이 다소 복잡할 수 있다.
               - **제한적인 동적 구성이 필요할 경우**: 복잡한 애플리케이션 배포나 다이나믹한 구성이 필요한 경우, 일부 한계가 있을 수 있다.

               # 2. ArgoCD 주요 개념 및 워크플로우

               ## 2-1. 주요 개념

               - **리포지토리(Repository)**: 애플리케이션의 선언적 정의와 Kubernetes 매니페스트 파일이 저장되는 Git 리포지토리다. ArgoCD는 이 리포지토리와 동기화하여 클러스터의 상태를 관리
               한다.
               - **애플리케이션(Application)**: ArgoCD에서 관리하는 단위로, Git 리포지토리에 정의된 Kubernetes 애플리케이션의 설정과 매니페스트 파일을 포함한다.
               - **클러스터(Cluster)**: ArgoCD가 애플리케이션을 배포하는 대상 Kubernetes 클러스터다. 여러 개의 클러스터를 관리할 수 있다.
               - **Sync(동기화)**: Git 리포지토리와 Kubernetes 클러스터 간에 상태를 일치시키는 과정이다. Git에 있는 변경 사항을 자동으로 클러스터에 반영할 수 있다.

               ## 2-2. 워크플로우

               <aside>
               💡

               1. **Define**(정의): Git 리포지토리에서 애플리케이션의 선언적 정의를 작성한다.
               2. **Sync**(동기화): Git 리포지토리의 상태와 Kubernetes 클러스터의 상태를 비교하고 동기화한다.
               3. **Deploy**(배포): 동기화된 상태로 애플리케이션을 클러스터에 배포한다.

               실제 사용 시 ArgoCD는 애플리케이션을 Git 리포지토리에서 정의하고, 이를 Kubernetes 클러스터에 동기화하여 지속적으로 배포 및 운영한다.

               </aside>

               - **애플리케이션 생성(Application Create)**
                   - 명령어: `argocd app create`
                   - 역할: Git 리포지토리의 매니페스트 파일을 사용하여 Kubernetes 클러스터에 애플리케이션을 정의한다.
                   - 결과: ArgoCD가 Git 리포지토리에서 해당 애플리케이션을 자동으로 관리하기 시작한다.
               - **동기화(Sync)**
                   - 명령어: `argocd app sync <app-name>`
                   - 역할: 지정된 애플리케이션을 Git 리포지토리와 동기화하여, 리포지토리의 최신 상태를 Kubernetes 클러스터에 적용한다.
                   - 특징: Git 리포지토리에서 변경된 내용을 클러스터에 즉시 반영한다.# 1. 젠킨스 설치

               [Download and deploy](https://www.jenkins.io/download/)

               ![{9BA4544B-2EB4-4A3C-AC5A-B5509DD94AD6}.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/bca66bd0-c2e9-49f3-bc76-
               2346b3845c24/9BA4544B-2EB4-4A3C-AC5A-B5509DD94AD6.png)

               <aside>
               💡

               Logon Type을 ‘Run service as LocalSystem’ 으로 변경한다.
               Port번호 설정하는 부분에서 8080 대신 18080으로 변경한다.

               </aside>

               <aside>
               ☝

               맥은 홈브루로 설치 시 자동으로 8080 포트가 연결되기 때문에 별도로 변경 필요

               젠킨스 설정 파일 열기

               ```bash
               vi /opt/homebrew/opt/jenkins-lts/homebrew.mxcl.jenkins-lts.plist
               ```

               아래 내용 수정

               ```
               <string>--httpPort=18080</string>
               ```

               LaunchAgent 다시 로드

               ```bash
               launchctl unload ~/Library/LaunchAgents/homebrew.mxcl.jenkins-lts.plist
               launchctl load ~/Library/LaunchAgents/homebrew.mxcl.jenkins-lts.plist
               ```

               </aside>

               ![{3F684E2F-3437-4902-B12C-6831ABFE68CC}.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/954c7892-e963-4b7c-8e04-
               de79f8807da1/3F684E2F-3437-4902-B12C-6831ABFE68CC.png)

               <aside>
               💡

               Jenkins는 Java 기반으로 만들어졌기 때문에 JDK 위에서 구동 된다. JDK가 설치 된 경로를 찾아서 선택한다.

               </aside>

               ![{B27C7CA9-D0BC-49E6-9421-8FDA4F04410E}.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/5815d7bf-ab4f-49c9-a5e5-2b395eaf56ce/
               B27C7CA9-D0BC-49E6-9421-8FDA4F04410E.png)

               <aside>
               💡

               브라우저에서 localhost:18080으로 접속하면 Unlock 되어있으므로 안내하는 경로의 파일을 열고 초기 관리자 비밀번호를 가져와 입력한다.
               추천하는 플러그인을 설치하는 모드로 설치를 다시 진행한다.
               설치가 완료 되면 계정명 ‘swcamp’, 암호 ‘swcamp’ 로 설정하고 이름과 이메일 주소를 입력하여 Admin User를 생성한다.
               Jenkins 접속을 위한 URL을 확인하고 종료한다.

               </aside>

               ![KakaoTalk_20241103_160455270_01.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/0a49cd84-57b1-4ea5-a8a8-04798a36d83e/
               KakaoTalk_20241103_160455270_01.png)

               # 2. 젠킨스 설치 이후

               ## 2-1. 플러그인 및 설정(Dashboard → Jenkins 관리)

               ### 2-1-1. Plugins

               1. Locale


                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/707328fd-3374-4a7b-9fb0-7a962c99bb0b/image.png)

                   <aside>
                   💡

                   Jenkins 관리 > Plugins > Available plugins 에서 locale을 검색해서 체크 후 install한다.
                   완료 되면 메인 페이지로 돌아가기를 누른다.
                   localhost:18080/restart 주소를 입력해서 재시작 할 수 있다.
                   Appearance의 Default Language가 한국어로 되어 있는 것을 확인할 수 있다.

                   </aside>

               2. Pipeline: Stage View
               3. Publish Over SSH

                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/8cca6567-45c5-4452-9ab8-c0b498337052/image.png)

                   <aside>
                   💡

                   Jenkins 관리 > Plugins > Available plugins 에서 Pipeline: Stage View를 검색해서 체크 후 install 한다.

                   Jenkins 관리 > Plugins > Available plugins 에서 Publish Over SSH를 검색해서 체크 후 install 한다.

                   </aside>


               ### 2-1-2. Tools

               1. JDK installations

                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/5d0c8c4f-a50b-40da-b710-02c54e23d1e6/image.png)

                   <aside>
                   💡

                   Jenkins 관리 > Tools > JDK installations > Add JDK 를 누른 뒤 Name : openJDK17, JAVA_HOME : jdk설치 경로를 입력한다.

                   </aside>

               2. Gradle installations

                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/88bc8ce1-95e2-4eba-ae16-c6e255aad7b3/image.png)

                   <aside>
                   💡

                   Jenkins 관리 > Tools > Gradle installations > Add Gradle 을 누른 뒤 Install automatically를 선택한다.

                   </aside>


               ### 2-1-3. Security

               1. git bash를 활용한 RSA키 생성하기

                   ```bash
                   # my-jenkins 디렉토리 생성
                   mkdir ./ssh-jenkins

                   # my-jenkins 디렉토리로 이동
                   cd ./ssh-jenkins

                   # .ssh 디렉토리 생성
                   mkdir ./.ssh

                   # ssh-keygen으로 SSH 접속을 위한 인증키 생성
                   ssh-keygen -t rsa -f .ssh/ssh-jenkins-github--key

                   # 비밀번호는 10자 이상 추천

                   # .ssh 디렉토리로 이동
                   cd ./.ssh

                   # private key
                   cat ssh-jenkins-github--key

                   # public key
                   cat ssh-jenkins-github--key.pub
                   ```

                   > SSH(Secure Shell Protocol)란?
                   - 다른 컴퓨터에 로그인하거나 원격 시스템에서 명령을 실행하고 파일을 전송할 수 있게 해주는 암호화된 네트워크 프로토콜이다.

                   RSA키란?
                   - 공개키 암호화 시스템으로 SSH 연결에서 인증에 사용된다.
                     1. 개인 키(Private Key): 사용자만 가지고 있는 비밀 키
                     2. 공개 키(Public Key): 누구나 알 수 있는 키
                   >
               2. security에 public 키 추가

                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/76c797d4-da03-491c-ba26-4dc8fd320e67/image.png)

                   ```bash
                   security:
                     gitHostKeyVerificationConfiguration:
                       sshHostKeyVerificationStrategy:
                         manuallyProvidedKeyVerificationStrategy:
                           approvedHostKeys: |-
                             github.com {{발급된 public key}}
                   ```


               ### 2-1-4. Credentials

               1. private 키 추가

                   <aside>
                   💡

                   Jenkins 관리 > Credentials > Domains의 하단 화살표를 눌러서 Add credentials를 클릭한다.

                   </aside>

                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/a9e55500-0c4e-4f2e-b050-34d979d5c0b7/image.png)

                   ![image.png](attachment:ab5e40d9-ad43-42db-b632-08ec41cf9d2b:image.png)

                   ```
                   ID : ssh-jeckins-github--key

                   Username : my-github-key

                   Private Key : Enter directly 체크 후 Add
                     -> {발급한 ssh private key} 입력

                   passphrase : 암호(1234567890)를 입력
                   ```

                   ![image.png](attachment:794b43d9-b426-447f-ae6a-8b9869d14387:image.png)

                   <aside>
                   💡

                   Jenkins 관리 > Credentials > Domains의 하단 화살표를 눌러서 Add credentials를 클릭한다.
                   Kind는 SSH Username with private key 로 선택하고 ID는 ssh key의 아이디를 작성한다.
                   username은 my-github-key라고 작성한 뒤 Enter directly를 누른 뒤 add 버튼을 눌러 생성한 private key를 입력한다.
                   passphrase에는 암호(1234567890)를 입력한다.

                   </aside>

               2. pipeline에서 쓸 도커 관련 설정 추가

               > DockerHub를 파이프라인에 추가하여 자동으로 push를 할 수 있게 하려면DockerHub에 로그인 할 수 있도록 Ceedentials를 추가해야한다
               >

               <aside>
               ☝

               DockerHub를 Google로 가입한 경우 DockerHub 계정에 비밀번호가 없어 젠킨스에서 비밀번호를 이용한 로그인이 불가능!!
               DockerHub → 로그인 → 프로필 → Account Setting → Password
               → Reset Password → 구글 이메일 확인 → 새 비밀번호 등록

               </aside>

               <aside>
               ☝

               - Jenkins 관리 > Credentials > Domains의 하단 화살표를 눌러서 Add credentials를 클릭.
               - SSH **Username with password**  선택
               - Username : {{DockerHub 이름}}
               - Password : {{DockerHub 비밀번호}}
               - ID : DOCKERHUB_PASSWORD
               - Description : Docker Hub login 용도
               </aside>

               ![image.png](attachment:f827d83d-a64e-45ac-b507-9f4bbab32ad8:image.png)

               ![credentials.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/eabc392f-0db5-46bd-9c53-cf9d0f1f3e23/credentials.png)

               ## 2-2. Webhook 및 ngrok 설정

               1. github에 새로운 repo 를 생성한다.
               2. 새로운 경로에 springboot 프로젝트 생성 후 기존 코드를 옮긴다. Jenkins를 통해 Gradle build를 수행할 예정이므로 dockerfile은 gradle build 과정은 제외하고 아래와 같이 작성한
               다.

                   ```yaml
                   # OpenJDK 21 버전의 이미지를 가져와 JVM 환경을 구축한다.
                   FROM eclipse-temurin:21-jre-alpine

                   # 현재 디렉토리 내의 모든 파일과 폴더를 컨테이너의 /app 디렉토리로 복사한다.
                   WORKDIR /app
                   COPY build/libs/*.jar app.jar

                   # app.jar를 리눅스 환경에서 실행하여 스프링 부트 서버를 시작한다.
                   ENTRYPOINT ["java", "-jar", "app.jar"]
                   ```

               3.  github repository로 push한다.

                   ```bash
                   1. git init
                   2. git remote add origin <github remote repo 주소>
                   3. git add .
                   4. git commit -m 'commit 메세지'
                   5. git push -u origin main
                   ```

               4. ngrok 설정

                   <aside>
                   💡

                   - **내 로컬 서버(localhost)를 인터넷에서 접근 가능한 임시 URL로 열어주는 터널링 도구**

                   - 공유기 설정을 통한 포트포워딩(외부에서 들어오는 요청을 내부 네트워크의 특정 장비로 전달)이 불가능한 관계로 로컬 호스트를 퍼블릭 인터넷에 공개하는 터널링 도구 ngrok을 활
               용한다.
                   - ngrok은 로컬 서버와 ngrok의 서버 사이에 터널을 생성하고 사용자는 ngrok이 제공하는 URL을 통해 로컬 서버에 접근할 수 있게 된다.

                   - 간단하고 빠르며, 복잡한 네트워크 설정 없이도 사용할 수 있어 개발 및 테스트 용도로 매우 유용하다.
                   - 단, 무료 계정의 경우 URL이 임시적(약 8시간 유지)이어서, 매번 ngrok을 실행할 때마다 새로운 주소가 생성된다.
                   </aside>

                   <aside>
                   💡

                   ngrok window-64bit 기준 압축파일 다운받아 압축 풀기
                   https://ngrok.com/download

                   </aside>

                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/08cf67d6-f8fd-4694-95c6-485b241611dc/image.png)

                   ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/e9c886fb-0ff4-4025-b0ed-1b6eeb269dc6/image.png)

                   <aside>
                   💡

                   다운로드 후 압축을 풀고 실행 파일을 실행 한 뒤 처음에는
                   `ngrok config add-authtoken $YOUR_AUTHTOKEN`

                   를 입력하여 인증한다.

                   </aside>

                   ![ngrok.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/f6dd3c46-4a39-44b4-8a3b-1438585d8576/ngrok.png)

                   <aside>
                   💡

                   아래 명령어로 외부에서 내 PC의 18080포트로 접속할 수 있게 터널링하는 명령어를 사용한다.

                   `ngrok http 18080`

                   </aside>


               ![ngrok-2.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/9f297b99-1028-4d10-88cd-3d8e4220c943/ngrok-2.png)

               <aside>
               💡

               forwarding의 주소를 통해 접속하면 jenkins로 접속 가능하다.

               </aside>

               ## 2-3. github repo가서 Deploy keys에 public 키 추가

               <aside>
               💡

               github repository > settings > Deploy keys > Add deploy key 를 눌러 공개키를 저장한다.

               </aside>

               ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/3a13b4ae-18ef-44a9-8b29-e10fbe0a88b6/image.png)

               ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/e313c50e-c7d8-4868-bdae-83fe03bbb63f/image.png)

               ## 2-4. Webhooks 설정(ngrok으로 접속하는 jenkins 경로 뒤에 반드시 /github-webhook/ 추가할 것)

               <aside>
               💡

               github repository > settings > Webhooks > Add webhook을 눌러 추가한다.

               </aside>

               ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/eeab791d-1839-4d21-93e3-e2f7cd1d69c3/7f3aecc4-90a4-4a4e-8805-
               425ee0098a01.png)

               ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/224559a1-3e8d-4999-8961-de57bc1b3970/image.png)

               ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/f2839f75-115c-4976-9914-6a90b5e02f3a/image.png)

               ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/13cda0b3-9394-477c-9767-9db4cefec2c8/image.png)

               ## 2-4. jenkins pipe-line script작성

               <aside>
               💡

               Jenkins > 새로운 Item > 아이템 이름 입력하고 Pipeline을 선택하여 OK를 입력한다.

               </aside>

               ![jenkins-2.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/ae3f5f7a-dbec-4666-acb0-8dead687d154/jenkins-2.png)

               <aside>
               💡

               Github Project 체크 후 GitHub 주소를 작성한다.

               </aside>

               ![jenkins-3.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/5c9b6edc-2473-4daa-a89e-bec1a0ff8204/jenkins-3.png)

               <aside>
               💡

               Build Triggers > GitHub hook trigger for GITScm polling 을 선택한다.

               </aside>

               ![jenkins-4.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/af711701-6e3b-4269-ad2c-cc13d16179bf/jenkins-4.png)

               <aside>
               💡

               아래의 파이프라인 스크립트를 붙여넣기 한다.

               이 Jenkins 파이프라인은 GitHub에서 소스를 가져와 Gradle로 빌드한 후, Docker 이미지를 생성하고 Docker Hub에 푸시하는 과정을 자동화한다. 각 단계는 환경에 맞춰 적절한 명령어를
               실행하도록 설정되어 있어, 다양한 운영 체제에서도 동일하게 작동할 수 있도록 디자인 되어있다.

               GITHUB_URL 은 본인에 맞게 변경이 필요하며 test-pipe라는 도커 이미지명도 필요하다면 변경한다.

               </aside>

               <aside>
               ☝

               ### Apple Silicon Mac 사용 시 추가 설정

               **🛠️ 젠킨스 환경 변수 설정값**
               **1. 설정 위치:** `Jenkins 관리` > `System (시스템 설정)` > `Global properties` > `Environment variables`
               **2. 입력할 내용 ( iTerm에서 `which docker` 입력시 `/usr/local/bin/docker` 출력된다면)**

               **항목 (Name) : PATH**

               **값 (Value) :** `/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/opt/homebrew/bin`

               ![image.png](attachment:a8fc92a5-c182-4aa3-bc45-ffa8ede46b50:image.png)

               </aside>

               ```bash
               pipeline {
                   agent any

                   tools {
                       gradle 'gradle'
                       jdk 'openJDK21'
                   }

                   environment {
                       DOCKERHUB_CREDENTIALS = credentials('DOCKERHUB_PASSWORD')
                       **DOCKERHUB_USERNAME='아이디'
                       GITHUB_URL = 'https://github.com/주소/주소.git'**
                   }

                   stages {
                       stage('Preparation') {
                           steps {
                               script {
                                   if (isUnix()) {
                                       sh 'docker --version'
                                   } else {
                                       bat 'docker --version'
                                   }
                               }
                           }
                       }
                       stage('Source Build') {
                           steps {
                               git branch: 'main', url: "${env.GITHUB_URL}"
                               script {
                                   if (isUnix()) {
                                       sh "chmod +x ./gradlew"
                                       sh "./gradlew clean build"
                                   } else {
                                       bat "gradlew.bat clean build"
                                   }
                               }
                           }
                       }
                       stage('Container Build and Push') {
                           steps {
                               script {
                                   withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                                       if (isUnix()) {
                                           sh "docker build -t ${DOCKER_USER}/test-pipe:latest ."
                                           sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                                           sh "docker push ${DOCKER_USER}/test-pipe:latest"
                                       } else {
                                           bat "docker build -t ${DOCKER_USER}/test-pipe:latest ."
                                           bat "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
                                           bat "docker push ${DOCKER_USER}/test-pipe:latest"
                                       }
                                   }
                               }
                           }
                       }
                   }

                   post {
                       always {
                           script {
                               if (isUnix()) {
                                   sh 'docker logout'
                               } else {
                                   bat 'docker logout'
                               }
                           }
                       }
                       success {
                           echo 'Pipeline succeeded!'
                       }
                       failure {
                           echo 'Pipeline failed!'
                       }
                   }
               }

               ```

               ## 2-5. CI/CD 파이프라인 구동 확인

               <aside>
               💡

               처음에는 반드시 지금 빌드를 눌러 수동으로 빌드를 한 번 수행한다. 각 단계가 정상 수행 되는지 확인한다. 오류가 발생한다면 로그를 확인하고 수정한다.

               </aside>

               ![스크린샷 2024-11-04 221829.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/0619bfac-442c-4415-ae45-8de161d2a221/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-04_221829.png)

               <aside>
               💡

               HealthCheck method의 내용을 수정한 뒤 Commit & Push 하여 다시 파이프라인이 동작하는 모습을 관찰한다.

               </aside>

               ![스크린샷 2024-11-04 221829.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/0619bfac-442c-4415-ae45-8de161d2a221/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-04_221829.png)# 1.  ArgoCD 테스트용 pipeline 생성

               ## 1-1. jenkins에서 argocd-pipe를 생성

               <aside>
               💡

               Jenkins > 새로운 Item > 아이템 이름 입력하고 Pipeline을 선택하여 OK를 입력한다.

               </aside>

               ![스크린샷 2024-11-06 113216.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/b2eb0e0d-8138-4208-8470-3d0843952b7d/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_113216.png)

               <aside>
               💡

               Github Project 체크 후 source code가 있는 GitHub 주소를 작성한다.

               </aside>

               ![jenkins-2.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/ae3f5f7a-dbec-4666-acb0-8dead687d154/jenkins-2.png)

               <aside>
               💡

               소스 코드 수정에 대한 파이프라인 빌드 트리거를 위해 Build Triggers > GitHub hook trigger for GITScm polling 을 선택한다.

               </aside>

               ![jenkins-3.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/5c9b6edc-2473-4daa-a89e-bec1a0ff8204/jenkins-3.png)

               <aside>
               💡

               pipeline script를 아래와 같이 작성하고 저장한다.

               </aside>

               ```groovy
               pipeline {
                   agent any

                   environment {
                       SOURCE_GITHUB_URL = '{source code가 있는 github url 주소}'
                       MANIFESTS_GITHUB_URL = '{manifests가 있는 github url 주소}'
                       GIT_USERNAME = '{commit시 입력 될 git username}'
                       GIT_EMAIL = '{commmit시 입력 될 git email}'
                   }

                   stages {
                       stage('Source Build') {
                           steps {
                               git branch: 'main', url: "${env.SOURCE_GITHUB_URL}"
                               script {
                                   if (isUnix()) {
                                       sh "chmod +x ./gradlew"
                                       sh "./gradlew clean build"
                                   } else {
                                       bat "gradlew.bat clean build"
                                   }
                               }
                           }
                       }
                       stage('Run Tests') {
                           steps {
                               script {
                                   if (isUnix()) {
                                       sh "./gradlew test"
                                   } else {
                                       bat "gradlew.bat test"
                                   }
                               }
                           }
                           post {
                               always {
                                   junit '**/build/test-results/test/*.xml'
                               }
                           }
                       }
                       stage('Docker Build and Push') {
                           steps {
                               script {
                                   withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                                       if (isUnix()) {
                                           sh "docker build -t ${DOCKER_USER}/argocd-pipe:${currentBuild.number} ."
                                           sh "docker build -t ${DOCKER_USER}/argocd-pipe:latest ."
                                           sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                                           sh "docker push ${DOCKER_USER}/argocd-pipe:${currentBuild.number}"
                                           sh "docker push ${DOCKER_USER}/argocd-pipe:latest"
                                           sh "docker logout"
                                       } else {
                                           bat "docker build -t ${DOCKER_USER}/argocd-pipe:${currentBuild.number} ."
                                           bat "docker build -t ${DOCKER_USER}/argocd-pipe:latest ."
                                           bat "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
                                           bat "docker push ${DOCKER_USER}/argocd-pipe:${currentBuild.number}"
                                           bat "docker push ${DOCKER_USER}/argocd-pipe:latest"
                                           bat "docker logout"
                                       }
                                   }
                               }
                           }
                       }
                       stage('K8S Manifest Update') {
                           steps {
                               // k8s-manifests 리포지토리를 main 브랜치에서 클론한다. 이때 자격 증명 github가 사용된다.
                               git credentialsId: 'github',
                                   url: "${env.MANIFESTS_GITHUB_URL}",
                                   branch: 'main'

                               script {
                                   if (isUnix()) {
                                       // Unix 시스템에서 boot-deployment.yml 파일 수정 후 commit 후 push
                                       sh "sed -i '' 's/argocd-pipe:.*\$/argocd-pipe:${currentBuild.number}/g' boot-deployment.yml"
                                       sh "git add boot-deployment.yml"
                                       sh "git config --global user.name '${env.GIT_USERNAME}'"
                                       sh "git config --global user.email '${env.GIT_EMAIL}'"
                                       sh "git commit -m '[UPDATE] ${currentBuild.number} image versioning'"
                                       sh "git push -u origin main"
                                   } else {
                                       // Windows 시스템에서 boot-deployment.yml 파일 수정 후 commit 후 push
                                       bat "powershell -Command \"(Get-Content boot-deployment.yml) -replace 'argocd-pipe:.*', 'argocd-pipe:${currentBuild.number}' | Set-Content boot-
               deployment.yml\""
                                       bat "git add boot-deployment.yml"
                                       bat "git config --global user.name '${env.GIT_USERNAME}'"
                                       bat "git config --global user.email '${env.GIT_EMAIL}'"
                                       bat "git commit -m \"[UPDATE] ${currentBuild.number} image versioning\""
                                       bat "git push -u origin main"
                                   }
                               }
                           }
                       }
                   }

                   post {
                       success {
                           echo 'Pipeline succeeded!'
                       }
                       failure {
                           echo 'Pipeline failed!'
                       }
                   }
               }

               ```

               - **(참고)docker로 설정했을때**

                   ```groovy
                   pipeline {
                       agent any

                       environment {
                           SOURCE_GITHUB_URL = '{source code가 있는 github url 주소}'
                           MANIFESTS_GITHUB_URL = '{manifests가 있는 github url 주소}'
                           GIT_USERNAME = '{commit시 입력 될 git username}'
                           GIT_EMAIL = '{commmit시 입력 될 git email}'
                       }

                       stages {
                           stage('Source Build') {
                               steps {
                                   git branch: 'main', url: "${env.SOURCE_GITHUB_URL}"
                                   script {
                                       if (isUnix()) {
                                           sh "chmod +x ./gradlew"
                                           sh "./gradlew clean build"
                                       } else {
                                           bat "gradlew.bat clean build"
                                       }
                                   }
                               }
                           }

                           stage('Run Tests') {
                               steps {
                                   script {
                                       if (isUnix()) {
                                           sh "./gradlew test"
                                       } else {
                                           bat "gradlew.bat test"
                                       }
                                   }
                               }
                               post {
                                   always {
                                       junit '**/build/test-results/test/*.xml'
                                   }
                               }
                           }

                           stage('Docker Build and Push') {
                               steps {
                                   script {

                                       withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                                           if (isUnix()) {
                                               sh "docker build -t ${DOCKER_USER}/argocd-pipe:${currentBuild.number} ."
                                               sh "docker build -t ${DOCKER_USER}/argocd-pipe:latest ."

                                               sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                                               sh "docker push ${DOCKER_USER}/argocd-pipe:${currentBuild.number}"
                                               sh "docker push ${DOCKER_USER}/argocd-pipe:latest"
                                               sh "docker logout"
                                           } else {
                                               bat "docker build -t ${DOCKER_USER}/argocd-pipe:${currentBuild.number} ."
                                               bat "docker build -t ${DOCKER_USER}/argocd-pipe:latest ."
                                               bat "echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin"
                                               bat "docker push ${DOCKER_USER}/argocd-pipe:${currentBuild.number}"
                                               bat "docker push ${DOCKER_USER}/argocd-pipe:latest"
                                               bat "docker logout"
                                           }
                                       }
                                   }
                               }
                           }

                           stage('K8S Manifest Update') {
                               steps {

                                   git credentialsId: 'github', url: "${env.MANIFESTS_GITHUB_URL}", branch: 'main'

                                   script {

                                       withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {


                                           def encodedUrl = env.MANIFESTS_GITHUB_URL.replace("https://", "https://${GIT_USERNAME}:${GIT_PASSWORD}@")

                                           if (isUnix()) {

                                               sh "sed -i 's/argocd-pipe:.*\$/argocd-pipe:${currentBuild.number}/g' boot-deployment.yml"


                                               sh """
                                                   git config user.name "${GIT_USERNAME}"
                                                   git config user.email "${env.GIT_EMAIL}"
                                                   git add boot-deployment.yml
                                                   if [ -n "\$(git status --porcelain)" ]; then
                                                       git commit -m "[UPDATE] image version to ${currentBuild.number}"
                                                       git push ${encodedUrl} main
                                                   else
                                                       echo "No changes to commit"
                                                   fi
                                               """
                                           } else {

                                               bat """
                                                   powershell -Command "(Get-Content boot-deployment.yml) -replace 'argocd-pipe:.*', 'argocd-pipe:${currentBuild.number}' | Set-Content boot-
               deployment.yml"
                                                   git config user.name "${GIT_USERNAME}"
                                                   git config user.email "${env.GIT_EMAIL}"
                                                   git add boot-deployment.yml
                                                   git commit -m "[UPDATE] image version to ${currentBuild.number}"
                                                   git push ${encodedUrl} main
                                               """
                                           }
                                       }
                                   }
                               }
                           }
                       }

                       post {
                           success {
                               echo 'Pipeline succeeded!'
                           }
                           failure {
                               echo 'Pipeline failed!'
                           }
                       }
                   }
                   ```


               ## 1-2. K8S Manifest Update stage를 위한 setting

               ### 1-2-1. Credentials 추가

               <aside>
               💡

               - Add Credentials → Username with password
                   - id : github (pipeline script에서 사용할 이름)
                   - username : 본인의 github username
                   - password : 실제 비밀번호가 아닌 별도의 access token을 발급 받아서 사용
                       - github 우측 상단 프로필 이미지 클릭
                       - settings 클릭
                       - 좌측 탭 중 ‘Developer Settings’ 클릭
                       - Personal access tokens → **Fine-grained tokens**
                       - Generated new token 버튼 클릭
                           - 이름, 기간 자유롭게 설정
                           - **Repository access → All repositories 체크**
                           - Generate token 클릭
                       - 생성된 토큰 잘 복사해두기
               </aside>

               ![image.png](attachment:af834677-88f2-484b-86b4-b5d0ba65b39c:image.png)

               > 해당 Credential이 추가되면 Jenkins가 github private repository도 접근이 가능해짐
               >

               ### 1-2-2. k8s manifests 관리용 github repository

               <aside>
               💡

               새로운 K8S Manifest 파일 관리용 pirvate github private repository를 생성하고

               아래 Manifest파일을 추가한다.

               ![image.png](attachment:95c21dae-c64a-4a68-b866-227887aa91d1:image.png)

               </aside>

               - **boot-deployment.yml**

               ```yaml
               apiVersion: apps/v1
               kind: Deployment
               metadata:
                 name: boot-deployment
               spec:
                 selector:
                   matchLabels:
                     app: boot
                 replicas: 3
                 template:
                   metadata:
                     labels:
                       app: boot
                   spec:
                     containers:
                       - name: boot-container
                         image: {{Dockerhub Username}}**/argocd-pipe:latest**
                         imagePullPolicy: Always
                         ports:
                           - containerPort: 8080
               ---
               apiVersion: v1
               kind: Service
               metadata:
                 name: boot-service
               spec:
                 type: ClusterIP
                 ports:
                   - port: 8001
                     targetPort: 8080
                 selector:
                   app: boot
               ```

               - vue-deployment.yml

               ```yaml
               apiVersion: apps/v1
               kind: Deployment
               metadata:
                 name: vue-deployment
               spec:
                 selector:
                   matchLabels:
                     app: vue
                 template:
                   metadata:
                     labels:
                       app: vue
                   spec:
                     containers:
                       - name: vue-container
                         image: goatohgiraffers/k8s-vue-ing:latest
                         imagePullPolicy: Always
                         ports:
                           - containerPort: 80
               ---
               apiVersion: v1
               kind: Service
               metadata:
                 name: vue-service
               spec:
                 type: ClusterIP
                 ports:
                   - port: 8000
                     targetPort: 80
                 selector:
                   app: vue
               ```

               - ingress.yml

               ```yaml
               apiVersion: networking.k8s.io/v1
               kind: Ingress
               metadata:
                 name: nginx-ingress
                 annotations:
                   nginx.ingress.kubernetes.io/ssl-redirect: "false"
                   nginx.ingress.kubernetes.io/rewrite-target: /$2
               spec:
                 ingressClassName: nginx
                 rules:
                   - http:
                       paths:
                         - path: /()(.*)
                           pathType: ImplementationSpecific
                           backend:
                             service:
                               name: vue-service
                               port:
                                 number: 8000
                         - path: /boot(/|$)(.*)
                           pathType: ImplementationSpecific
                           backend:
                             service:
                               name: boot-service
                               port:
                                 number: 8001

               ```



               <aside>
               💡

               Github remote repository를 생성한다. Credential을 적용할 예정이므로 private로 설정하는 것도 가능하다. local repo에서 remote로 연결하여 push 해 둔다.

               </aside>

               ## 1-3. pipeline build 테스트

               <aside>
               💡

               pipeline build가 완료 되는 모습과 manifests repository에서 image의 version이 넘버링 되는 모습을 관찰한다.

               </aside>

               ![스크린샷 2024-11-06 120007.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/b7002c2b-994a-4585-a614-c889b4501ab6/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_120007.png)

               ![화면 캡처 2024-11-06 120157.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/9379c5d6-136d-4cb1-aafe-28c61a02c1e6/
               %ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2024-11-06_120157.png)

               # 2. ArgoCD 설치 및 환경 구축

               ## 2-1. ArgoCD 설치

               - Window 유저 (환경변수 설정할 것)

                   https://github.com/argoproj/argo-cd/releases

                   ![화면 캡처 2024-11-06 123036.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/46d63ac2-77e8-44a2-b200-ae2dffcbc30c/
               %ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2024-11-06_123036.png)


               ![image.png](attachment:3cc7a1b0-268b-460f-b467-332b03a6a85e:image.png)

               - macOS 유저

                   ```bash
                   brew install argocd
                   ```


               <aside>
               💡

               설치 후 **`argocd version`** 명령어를 통해 확인한다.

               </aside>

               ![스크린샷 2024-11-06 123346.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/dc5dcf71-ab0b-4155-bd60-26621c3713b9/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_123346.png)

               ## 2-2. Kubernetes에 ArgoCD 설치

               - 아래 명령어를 통해 Kuberenets에 ArgoCD를 설치한다.

                   ```bash
                   kubectl create namespace argocd

                   kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
                   ```


               ![화면 캡처 2024-11-06 124045.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/f41287cb-0612-446c-b43e-63315d702039/
               %ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2024-11-06_124045.png)

               - ArgoCD 서비스가 실행 중인지 확인한다.

                   ```bash
                   kubectl get svc -n argocd
                   ```


               ![화면 캡처 2024-11-06 124343.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/dcf65736-b055-46a3-9d16-c0968837341e/
               %ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2024-11-06_124343.png)

               - ArgoCD UI 접근
                   - Argo CD는 기본적으로 LoadBalancer나 NodePort를 사용하여 UI를 제공한다. kubectl port-forward 명령어를 사용하여 UI에 접근할 수 있다.
                   - 아래와 같이 입력하면 이제 [localhost:8888](http://localhost:8888) 로 ArgoCD UI에 접근할 수 있다.

                   ```yaml
                   kubectl port-forward svc/argocd-server -n argocd 8888:443
                   ```

                   ![스크린샷 2024-11-06 124754.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/e0bd88c8-d5a6-48dd-af69-2d35bd3f7697/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_124754.png)

               - 초기 로그인
                   - Argo CD의 초기 비밀번호는 kubectl을 통해 확인할 수 있다.

                   ```powershell
                   kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
                   ```

               - 로그인 후 화면

                   ![스크린샷 2024-11-06 125037.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/33dd864b-2f7d-4071-bec8-c629807453f9/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_125037.png)

                   ![스크린샷 2024-11-06 125053.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/609bf93b-f7c2-4515-aa5b-03f7f2d1a301/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_125053.png)

               - CLI 명령어로도 login 을 해둔다.

                   ```powershell
                   argocd login localhost:8888
                   ```


               # 3. GitHub repository와 동기화 된 k8s 클러스터 생성

               ## 3-1. ArgoCD repository 생성

               ```powershell
               argocd repo add {manifest 관리 github 주소} --username {github username} --password {github access token}

               ```

               <aside>
               ☝

               에러에 따른 처리 방법

               - Argo CD server address unspecified
                   - 터미널에서 Argo CD 로그인을 안해서 발생하는 문제
                       - **`argocd login [localhost:8888](http://localhost:8888)`**
                       - admin / 비밀번호 입력
               - Write access to repository not granted
                   - access token의 권한 부족 문제
                       - 최소 필요: **Repository permissions → Contents: Read and write 수정**
               </aside>

               ![스크린샷 2024-11-06 140801.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/56c4e54f-d51f-48b3-be32-b09de37590db/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_140801.png)

               ## 3-2. ArgoCD Application 생성 후 동기화 적용

               ### 3-2-1. 어플리케이션 생성

               ```bash
               argocd app create my-app \
                   --repo {github manifest 관리 reposiotry url} \
                   --path ./ \
                   --dest-server https://kubernetes.default.svc \
                   --dest-namespace default
               ```

               - --path : 리포지토리 내에서 배포할 Kubernetes 자원이 위치한 디렉토리를 지정한다.
               - --dest-server [https://kubernetes.default.svc](https://kubernetes.default.svc/) : Kubernetes 클러스터 내에서 실행 중인 API 서버에 연결하여 해당 클러스터에 애플리케이션을 배
               포한다. 이 주소는 Kubernetes 클러스터 내에서만 유효하다.
               - --dest-namespace default: Kubernetes 클러스터의 default 네임스페이스에 애플리케이션을 배포. 다른 네임스페이스를 사용하려면 해당 네임스페이스를 사전에 생성한다.

               ### 3-2-2. 동기화 적용

               ```yaml
               argocd app set my-app --sync-policy automated
               ```

               ![스크린샷 2024-11-06 142841.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/12855251-486f-43e7-a3af-cc485e5c7b7a/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_142841.png)

               ![스크린샷 2024-11-06 142914.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/8a471b2a-43d2-4e04-a65c-cb4d325a6f79/
               %EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2024-11-06_142914.png)

               ## 3-3. 전체 파이프라인 테스트

               <aside>
               💡

               1. /health 요청의 응답 메세지 코드를 변경한 뒤 source code가 관리 되는 github repository에 push 하면 jenkins의 pipeline이 트리거 되어 동작 한다.
               2. github에서 source code를 가져와 gradle build → gradle test → docker image build 의 과정을 거친다.
               3. 이후 docker hub에 새로운 번호로 업데이트 된 image를 push 한다.
               4. 이후 manifests가 관리 되는 github repository 코드도 docker image tag를 변경 후 push 한다.
               5. manifests가 관리되는 github repository 와 sync 되어 있으므로 ArgoCD UI에서 새로운 이미지를 기반으로 한 Pod 배치가 일어나는 모습을 관찰할 수 있다.
               6. /health 요청의 응답 메세지도 변화한 모습을 관찰 할 수 있다.
               </aside>

               ![화면 캡처 2024-11-06 151002.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/d557eea4-a268-485f-a776-a101f885365d/
               %ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2024-11-06_151002.png)

               ![화면 캡처 2024-11-06 151054.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/88c46455-5e71-462e-8783-513a6bcd41c3/
               %ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2024-11-06_151054.png)

               ## 3-4. 리소스 삭제

               - UI 상에서 애플리케이션 Delete 버튼을 눌러서 삭제할 수 있다.
               - 명령어로는 아래와 같이 삭제할 수 있다.

                   ```yaml
                   argocd app delete argocd/my-app --cascade

                   kubectl delete --all ingress,configmap,secret,pvc,services,deployments,pods
                   ```

                   - `--cascade`: 이 옵션을 사용하면, 해당 애플리케이션에 의해 관리되는 모든 Kubernetes 리소스(예: `Deployment`, `Service`, `ConfigMap` 등)도 함께 삭제된다. 이 옵션을 지정하
               지 않으면, 애플리케이션만 삭제되고 리소스는 클러스터에 남을 수 있다.
                   - 남아있는 리소스를 지우고자 할 경우 kubectl 명령어로 지워야 한다. 일단 이것도 학습만 해둬


             • 네, 이번 내용도 요청하신 대로 실행 없이 학습만 해두겠습니다.

               학습 범위는 다음까지 반영했습니다.

               - Kubernetes(쿠버네티스): Cluster(클러스터), Pod(파드), Deployment(디플로이먼트), Service(서비스), Ingress(인그레스), Probe(프로브), Volume/PV/PVC
               - Terraform(테라폼): Kubernetes 리소스 IaC(Infrastructure as Code, 인프라 코드화) 워크플로우
               - Jenkins(젠킨스): Pipeline(파이프라인), 테스트 통합, DockerHub 푸시, Webhook(웹훅), Credential(자격 증명)
               - ArgoCD(아르고CD): GitOps(깃옵스) 기반 동기화 배포, Jenkins 연계 자동 배포 흐름

               원하실 때 현재 JapaneseQuizProject 기준으로 바로 적용 가능한 최소 운영안(개발/스테이징/운영 분리 포함)으로 설계해드리겠습니다.


             › # 1. ELK for Logging

               ![img.png](attachment:1d01fdfb-63d2-413c-8fed-5a91a5e61044:img.png)

               ## 1-1. ELK란?

               <aside>
               💡

               ELK는 **Elasticsearch**, **Logstash**, **Kibana**로 구성된 로그 수집 및 분석 스택이다. 애플리케이션 로그를 수집하고 저장한 뒤, 실시간으로 검색하고 시각화하는 데 사용된다. 실시간 로그 분석, 성능 추적, 장애 원인 파악 등에 활용된다.

               </aside>

               - **Elasticsearch**: JSON 기반 로그 저장 및 검색 (Log 저장용 DB)
               - **Logstash**: 다양한 소스의 로그 수집 및 구조화
               - **Kibana**: 시각화 도구로 대시보드 생성

               ## 1-2. ELK 구성 흐름

               ```
               Spring Boot → Logstash → Elasticsearch → Kibana
               ```

               Spring Boot 로그를 JSON 형식으로 Logstash로 전송하고, Logstash는 이를 Elasticsearch에 저장하며, Kibana는 Elasticsearch를 시각화한다.

               ## 1-3. Logstash 구조

               **기본 구조**

               ```
               input {
                 tcp {
                   port => 5000
                   codec => json
                 }
               }

               output {
                 elasticsearch {
                   hosts => ["elasticsearch:9200"]
                   index => "order-service-%{+YYYY.MM.dd}"
                 }
               }
               ```

               - input 블록 : Spring Boot가 `logback-spring.xml`을 통해 JSON 로그를 `logstash:5000`으로 전송함
                   - tcp: 로그 데이터를 TCP 프로토콜을 통해 수신
                   - port => 5000: Logstash가 수신 대기할 포트 (Spring Boot 로그 전송 대상 포트)
                   - codec => json: 수신되는 데이터가 JSON 형식임을 명시하여 자동 파싱
               - output 블록 : Logstash가 수신한 로그를 Elasticsearch에 하루 단위로 저장하도록 설정
                   - elasticsearch: 로그를 Elasticsearch로 전송
                   - hosts: Elasticsearch의 주소 (`docker-compose` 상의 서비스 이름 사용)
                   - index: 로그를 저장할 인덱스 이름 지정
                       - `"order-service-%{+YYYY.MM.dd}"`는 로그 수집 날짜 기준으로 인덱스를 생성
                       - 예: `order-service-202x.xx.xx`

               ## 1-4. Elasticsearch 쿼리 예시

               ```bash
               curl -X GET "http://localhost:9200/<인덱스>/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   ...
                 }
               }'

               ```

               - `X GET`: GET 요청
               - `H`: HTTP 헤더 (`Content-Type`은 JSON 명시)
               - `d`: 요청 바디(JSON 쿼리)
               - `<인덱스>`: `order-service-*` 와 같이 패턴 가능

               ### 1-4-1. **특정 로그 레벨 조회**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "match": {
                     "level": "WARN"
                   }
                 }
               }'

               ```

               ### 1-4-2. **특정 단어 포함 로그 조회**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "match": {
                     "message": "Order created"
                   }
                 }
               }'

               ```

               ### 1-4-3. **기간 필터링 포함**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "range": {
                     "@timestamp": {
                       "gte": "now-1h",
                       "lte": "now"
                     }
                   }
                 }
               }'

               ```

               ### 1-4-4. **복합 조건**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "query": {
                   "bool": {
                     "must": [
                       { "match": { "level": "INFO" } },
                       { "match": { "message": "Order list" } }
                     ]
                   }
                 }
               }'

               ```

               ### 1-4-5. **로그 개수 제한**

               ```bash
               curl -X GET "http://localhost:9200/order-service-*/_search" -H 'Content-Type: application/json' -d '
               {
                 "size": 3,
                 "query": {
                   "match_all": {}
                 }
               }'

               ```

               ## 1-5. Kibana 인덱스 패턴 등록 방법

               1. Kibana 접속: [http://localhost:5601](http://localhost:5601/)
               2. 좌측 메뉴 → **Discover** 클릭
               3. **Create Index Pattern** 버튼 클릭
               4. 인덱스 이름 입력: `order-service-*`
               5. 타임 필드 선택: `@timestamp` (또는 자동 선택된 필드)
               6. 저장 후 Discover에서 로그 확인 가능

               인덱스가 보이지 않는다면 Elasticsearch 인덱스 생성 여부 확인

               ```bash
               curl http://localhost:9200/_cat/indices?v
               ```

               # 2. Monitoring

               ## 2-1. Monitoring이란?

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/e680de87-7f45-4879-92d7-ab5efc25b0f7/Untitled.png)

               <aside>
               💡 실제 서비스를 운영하다 보면 장애가 발생 했을 때 알림 서비스를 받아야 되거나 신속하게 성능을 분석하거나 하는 등 복잡한 네트워크를 추적(감시)하고 관리하는 기능을 필요로 하게 된다.

               이러한 실시간 네트워크 문제 해결을 위해 표, 차트, 그래프, 대시보드 및 보고서 형태의 데이터를 제공하면 손쉽게 확인할 수 있다.

               따라서 전체적인 모니터링 시스템은 데이터 **수집, 통합, 시각화**의 단계로 구성된다.

               </aside>

               # 3. Prometheus

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/6b4f9145-c7e5-4363-9c88-a1cd7884668c/Untitled.png)

               <aside>
               💡 오픈 소스 **모니터링 및 경고 도구**로, 주로 시스템 및 서비스 모니터링에 사용된다.
               2012년 SoundCloud에서 개발되기 시작해 이후 많은 조직에서 널리 채택되어 사용된다.

               </aside>

               ## 3-1. Prometheus의 특징

               - **데이터 수집**
                   - Prometheus는 시간에 따른 데이터 수집을 지원하며, 주로 ‘메트릭’ 형태로 정보를 수집한다. 메트릭은 서버의 CPU 사용량, 메모리 사용, 네트워크 트래픽 등 다양한 시스템 지표를 포함할 수 있다.
               - **경고**
                   - 문제가 감지되었을 때 사용자에게 알림을 보내는 경고 시스템을 제공한다.
               - **쿼리 및 분석**
                   - Prometheus의 강력한 쿼리 언어를 사용하여 저장된 데이터를 쿼리하고 분석할 수 있다.

               ## 3-2. Prometheus의 원리

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/e169f815-b636-41aa-b52a-0790636571df/Untitled.png)

               - **Micrometer**
                   - springboot의 actuator가 제공하는 기능이다.
                   - JVM 기반의 애플리케이션 Metrics를 제공하고 처리한다.

                       (애플리케이션의 메트릭(측정 지표) 퍼사드(상위 수준 인터페이스)로 마이크로 미터가 정한 표준이 모니터링 툴을 사용할 때 제공된다.)

                       > **메트릭**: 시간에 따라 추이를 추적할만한 가치가 있는 여러가지 데이터들
                       >
                   - 다양한 모니터링 시스템을 지원한다.
               - **Actuator**
                   - Spring의 라이브러리도 CPU 사용량,  커넥션 풀과 같은 다양한 정보를 제공한다.

                   ```json
                   {"names": [
                     "application.ready.time",
                     "application.started.time",
                     "disk.free","disk.total",
                     "hikaricp.connections",
                     "hikaricp.connections.acquire",
                     "hikaricp.connections.active",
                     "hikaricp.connections.idle",
                     "hikaricp.connections.max",
                     "hikaricp.connections.usage",
                     "http.server.requests",
                     "http.server.requests.active",
                     "jdbc.connections.active",
                     "jdbc.connections.idle",
                     "jdbc.connections.max",
                     "jdbc.connections.min",
                     "jvm.buffer.count",
                     "jvm.buffer.memory.used",
                     "jvm.memory.used",
                     "jvm.memory.max",
                     "logback.events",
                     "process.cpu.usage",
                     "process.uptime",
                     "system.cpu.count",
                     "system.cpu.usage",
                     "tomcat.sessions.active.current",
                     "tomcat.sessions.rejected"
                     ]
                   }
                   ```

               - 프로메테우스는 애플리케이션에서 발생한 메트릭들의 과거 이력까지 보관하는 DB 역할을 한다.

               ## 3-3. Prometheus 설정 파일

               ```yaml
               global:
                 scrape_interval: 15s

               scrape_configs:
                 - job_name: 'spring-boot'
                   metrics_path: '/actuator/prometheus'
                   static_configs:
                     - targets: ['order-service:8080']

               ```

               - `global.scrape_interval: 15s`
                   - 전체 기본 스크래핑 주기 (기본값은 1분 → 여기서는 15초마다 수집)
                   - Prometheus가 대상 서비스로부터 메트릭을 얼마나 자주 가져올지 설정
               - `scrape_configs`
                   - 어떤 대상을 어떻게 수집할지 정의하는 리스트
                   - 하나의 대상은 `job_name`을 갖고, 여러 `target`을 가질 수 있다
               - `job_name: 'spring-boot'`
                   - 수집 작업에 대한 이름 (Grafana 등에서 메트릭 분류에 사용됨)
               - `metrics_path: '/actuator/prometheus'`
                   - Spring Boot의 메트릭 노출 엔드포인트 (Spring Actuator에서 자동 제공)
                   - 내부적으로 micrometer가 `/actuator/prometheus` 형식의 데이터를 반환
               - `targets: ['order-service:8080']`
                   - 메트릭을 수집할 대상
                   - 도커 네트워크 내 컨테이너 이름(`order-service`)과 포트(`8080`)
                   - Prometheus는 이 주소로 HTTP 요청을 보내 메트릭 데이터를 가져옴
               - 동작 흐름
                   1. Prometheus는 15초마다 `http://order-service:8080/actuator/prometheus`로 요청
                   2. Spring Boot 애플리케이션은 Actuator + Micrometer를 통해 메트릭 JSON 응답 반환
                   3. Prometheus는 이를 수집해 시계열 데이터로 저장

               ## 3-4. Prometheus UI 사용법

               - 접속 주소: [http://localhost:9090](http://localhost:9090/)
               - 메트릭 이름 자동 완성 기능 지원

                   → `/actuator/prometheus`를 통해 수집된 모든 메트릭 이름 확인 가능

               - PromQL 쿼리 실행
                   - 쿼리를 입력한 후 `Execute` 버튼 클릭
                   - 결과는 표 형식 (Table) 또는 그래프 (Graph) 탭에서 확인 가능

                   | 예시 | 설명 |
                   | --- | --- |
                   |  `http_server_requests_seconds_count` | HTTP 요청 수 (엔드포인트 단위) |
                   | `jvm_memory_used_bytes` | JVM 힙 메모리 사용량 |
                   | `process_cpu_usage` | CPU 사용률 (0~1 사이 소수) |
                   | `system_cpu_usage` | 전체 시스템 CPU 사용률 |
                   | `rate(http_server_requests_seconds_count[1m])` | 1분간 HTTP 요청 처리 속도 (초당 요청 수) |

                   → 특정 메트릭을 기준으로 현재 값을 보거나 시계열 변화 확인 가능


               # 4. Grafana

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/254b7aa1-c96e-4538-a2a6-4d496912de0f/Untitled.png)

               <aside>
               💡

               오픈 소스 **분석 및 시각화 플랫폼**이다.
               대시보드 생성과 데이터 시각화에 주로 사용되며, 다양한 데이터 소스와 호환된다.

               </aside>

               ## 4-1. Grafana의 특징

               - **다양한 데이터 소스 지원**
                   - Grafana는 Prometheus, InfluxDB, MySQL, PostgreSQL 등 다양한 데이터 소스와 연동할 수 있다.
               - **대시보드**
                   - 사용자는 여러 형태의 차트, 그래프 및 알림을 포함한 사용자 정의 대시보드를 생성할 수 있다.
               - **시각적 디스플레이**
                   - 복잡한 데이터를 쉽게 이해할 수 있도록 돕는 강력한 시각적 도구를 제공한다.

               ## 4-2. Grafana의 원리

               ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2ed584e0-2b03-4eac-8d4b-4490d601f1ba/9cb91440-57d6-403c-b980-914331cbe68d/Untitled.png)

               - 프로메테우스에 쌓인 데이터를 조회해서 사용자가 보기 쉽도록 대시보드로 시각화해 준다.
               - 데이터를 다양한 통계치나 수많은 그래프 형태로 보여준다.

               ## 4-3. Grafana 데이터 소스 설정 (datasource.yml)

               ```yaml
               apiVersion: 1

               datasources:
                 - name: Prometheus
                   type: prometheus
                   access: proxy
                   url: http://prometheus:9090
                   isDefault: true

               ```

               | 항목 | 의미 |
               | --- | --- |
               | `apiVersion: 1` | Grafana 프로비저닝 설정의 버전 |
               | `name` | UI에서 표시될 데이터 소스 이름 |
               | `type` | 데이터 소스 종류 (`prometheus`, `mysql`, `elasticsearch` 등) |
               | `access: proxy` | Grafana 서버가 직접 Prometheus에 요청을 보냄 |
               | `url` | 데이터 소스의 실제 주소 (`prometheus:9090` → Docker 내 서비스명 기준) |
               | `isDefault: true` | 기본 데이터 소스로 설정 (그래프 만들 때 자동 선택됨) |

               ## 4-4. Grafana UI 사용법

               - 접속 주소 : [http://localhost:3000](http://localhost:3000/)
               - 기본 로그인 : `admin / admin`

               ### 기본 설정 확인 (처음 1회)

               1. 좌측 사이드바 → Gear 아이콘(⚙) > Data Sources 클릭
               2. `Prometheus` 데이터 소스가 등록되어 있는지 확인 (Test 버튼 클릭)
               3. 연결 상태가 "Data source is working"이면 성공

               ### 대시보드 만들기

               1. Dashboard → New Dashboard → Add new panel 클릭
               2. Query: PromQL 입력
               3. Run query 클릭 → 실시간 데이터 시각화 확인
               4. Visualization 변경, panel options > Title, Description 변경 등 테스트
               5. 상단 "Apply" → 대시보드 저장 이게 마지막 학습자료야


             • 확인했습니다. 마지막 학습자료도 실행 없이 학습용으로 반영해두었습니다.

               반영 범위

               - ELK(Elasticsearch/Logstash/Kibana, 로그 수집·저장·시각화)
               - Monitoring(모니터링, 수집·통합·시각화 관점)
               - Prometheus(프로메테우스, 메트릭 수집/시계열 저장/PromQL 분석)
               - Grafana(그라파나, 대시보드 시각화 및 알림)

               필요하시면 다음 단계에서 JapaneseQuizProject 기준으로

               1. 최소 도입안(로컬/개발),
               2. 운영형 도입안(알림/보존정책 포함)
                  두 가지로 바로 설계해드리겠습니다.


             › 지금까지 내가 학습시켜뒀던거 정리해서 작성햊루래


             • 좋습니다. 지금까지 학습하신 내용을 실무 기준으로 한 번에 보실 수 있도록 정리해드리겠습니다.

               ## 1. 전체 개념 요약

               지금까지 학습한 축은 크게 7가지입니다.

               1. DevOps(개발·운영 통합 문화)와 CI/CD(지속적 통합·지속적 제공/배포)
               2. 관측성(Observability, 가시성) 기반 운영: ELK(로그), Prometheus(메트릭), Grafana(시각화)
               3. 실시간/비동기 아키텍처: WebSocket(양방향 통신), STOMP(메시징 프로토콜), Message Broker(메시지 브로커)
               4. 컨테이너 운영: Docker(컨테이너), Docker Compose(멀티 컨테이너 오케스트레이션)
               5. 쿠버네티스(Kubernetes, 컨테이너 오케스트레이션): Deployment/Service/Ingress/Probe/Volume
               6. IaC(Infrastructure as Code, 인프라 코드화): Terraform(테라폼)
               7. 배포 자동화 체인: Jenkins(CI 서버) + ArgoCD(GitOps 배포)

               ———

               ## 2. 왜 필요한지 (문제 -> 해결)

               기존 방식 문제는 개발, 테스트, 배포, 운영이 단절되어 릴리즈가 느리고 장애 대응이 늦다는 점입니다.
               해결 방향은 다음과 같습니다.

               - DevOps(Culture/Automation/Measurement/Sharing, 문화·자동화·측정·공유)로 조직/프로세스 통합
               - CI/CD 파이프라인으로 빌드·테스트·배포 자동화
               - ELK + Prometheus + Grafana로 로그/메트릭 기반 빠른 원인 분석
               - Kubernetes로 원하는 상태(Desired State, 바람직한 상태) 자동 유지
               - GitOps(Git 단일 진실 원천)로 배포 이력/재현성 확보

               ———

               ## 3. 구조 설명 (학습 내용 아키텍처)

               학습하신 전체 흐름은 아래와 같이 연결됩니다.

               1. 개발자 코드 푸시 -> Jenkins가 빌드/테스트/이미지 푸시
               2. Jenkins가 Manifest(배포 선언 파일) 리포지토리 업데이트
               3. ArgoCD가 Git 변경 감지 후 Kubernetes와 자동 동기화
               4. Kubernetes가 Pod(파드) 롤링 업데이트로 무중단 배포
               5. Spring Boot는 Actuator/Micrometer로 메트릭 노출
               6. Prometheus가 수집, Grafana가 대시보드 시각화
               7. 로그는 Logstash로 수집되어 Elasticsearch 저장, Kibana 조회
               8. 실시간 기능은 WebSocket/STOMP + Broker(RabbitMQ/Kafka)로 확장

               ———

               ## 4. 코드/운영 관점 핵심 포인트

               학습 중 반복된 실무 핵심 규칙은 아래입니다.

               - 선언형 운영(Declarative, 선언형): kubectl로 리소스를 직접 “조작”보다 Manifest를 “수정”
               - 보안 분리: ConfigMap(일반 설정) / Secret(민감 정보)
               - 안정성 확보: Liveness Probe(생존 확인), Readiness Probe(트래픽 수용 가능 여부)
               - 스토리지 전략: emptyDir(임시), PV/PVC(영속)
               - 배포 전략: Rolling Update(기본), Canary(부분 검증), Blue-Green(환경 전환)
               - 자동화 원칙: 테스트 통과 후 배포, 실패 시 롤백/알림
               - 자격증명 관리: Jenkins Credentials, GitHub Token, DockerHub 계정 분리
               - 네트워크 표준화: Service(내부), Ingress(외부 진입점), 경로 기반 라우팅

               ———

               ## 5. 실무 장단점 요약

               장점

               - 배포 속도 향상, 장애 대응 시간 단축, 운영 일관성 확보
               - 변경 이력 추적성과 재현성 강화(GitOps + IaC)
               - 팀 협업 효율 증가(CI/CD + 공통 관측 체계)

               단점/주의점

               - 초기 구축 비용과 학습 곡선이 큼
               - 도구가 많아질수록 운영 복잡도 증가
               - 자격증명/권한/네트워크 정책을 잘못 설계하면 보안 리스크 발생

               ———

               원하시면 다음 답변에서 이 요약을 기반으로 JapaneseQuizProject에 맞춘 “실행 가능한 최종 운영 청사진(로컬 -> 스테이징 -> 운영)” 형태로 바로 작성해드리겠습니다.


             › 따로 md 파일로 해놓고 지금부터 학습목적으로 이 프로젝트의 개발 운영 통합 (devops) CI / CD를 적용시킬거야 내 학습 목적인것을 잊지말고 학습했던 범위내에서 적용 가이드를 알려줘 그대신 왜 이렇게 하는지 그런
               것들을 나한테 상세하게 설명해주면서 가이드를 해줘야해 처음부터 시작하자 뭐부터 하면 돼 ?


             • 요청하신 대로 학습용 기준에 맞춘 DevOps(개발·운영 통합) + CI/CD(지속적 통합·지속적 배포) 적용 가이드를 별도 md 파일로 먼저 작성하겠습니다. 현재 프로젝트 구조를 반영해 “처음에 무엇부터 시작할지”를 1단계부
               터 순서대로, 각 단계의 이유까지 포함해 정리하겠습니다.

             • Added DEVOPS_CICD_LEARNING_GUIDE.md (+205 -0)
                   1 +# JapaneseQuizProject DevOps 학습 가이드
                   2 +
                   3 +이 문서는 **학습 목적**으로 `JapaneseQuizProject`에 `DevOps(개발·운영 통합)`와 `CI/CD(Continuous Integration/Continuous Delivery, 지속적 통합/지속적 제공)`를 **처음부터 단계적으로** 적용하기 위한
             안내서입니다.
                   4 +
                   5 +---
                   6 +
                   7 +## 1. 전체 개념 요약
                   8 +
                   9 +이 프로젝트에서 목표로 하는 흐름은 아래와 같습니다.
                  10 +
                  11 +1. 개발자가 코드를 `GitHub`에 Push합니다.
                  12 +2. `Jenkins(젠킨스, CI 서버)`가 자동으로 빌드/테스트를 수행합니다.
                  13 +3. 테스트가 통과하면 `Docker Image(도커 이미지)`를 빌드하고 `DockerHub`에 Push합니다.
                  14 +4. 배포용 `Manifest(매니페스트, 쿠버네티스 선언 파일)` 저장소의 이미지 태그를 업데이트합니다.
                  15 +5. `ArgoCD(아르고CD, GitOps 배포 도구)`가 변경을 감지해 `Kubernetes(쿠버네티스)`에 자동 반영합니다.
                  16 +6. 운영 관측은 `ELK(로그)` + `Prometheus/Grafana(메트릭)`로 확인합니다.
                  17 +
                  18 +즉, **코드 변경 -> 자동 검증 -> 자동 이미지 배포 -> 자동 클러스터 반영 -> 모니터링**의 닫힌 루프를 학습하는 것이 핵심입니다.
                  19 +
                  20 +---
                  21 +
                  22 +## 2. 왜 이 순서로 진행하나요? (문제 -> 해결)
                  23 +
                  24 +### 문제
                  25 +- 개발/테스트/배포/운영이 분리되면 릴리즈가 느려지고 실수 확률이 높습니다.
                  26 +- 수동 배포는 재현성이 낮고, 장애가 나면 원인 추적이 어렵습니다.
                  27 +
                  28 +### 해결
                  29 +- `CI`로 **코드 품질 게이트(빌드/테스트 통과)**를 강제합니다.
                  30 +- `CD`로 **배포 자동화와 일관성**을 확보합니다.
                  31 +- `GitOps(Git 기반 운영)`로 **무엇이 배포됐는지** 추적 가능하게 만듭니다.
                  32 +- 로그/메트릭 수집으로 **운영 피드백 루프**를 완성합니다.
                  33 +
                  34 +---
                  35 +
                  36 +## 3. 가장 먼저 할 일 (Step 0)
                  37 +
                  38 +학습을 시작할 때는 기술 구현 전에 기준선을 먼저 고정해야 합니다.
                  39 +
                  40 +### 3.1 학습 목표를 문장으로 확정
                  41 +- 목표 예시:
                  42 +  - "코드 Push 후 Jenkins가 테스트를 통과한 경우에만 DockerHub에 이미지를 올린다."
                  43 +  - "ArgoCD가 Manifest 변경을 감지해 Kubernetes를 자동 동기화한다."
                  44 +  - "배포 후 /actuator, 로그, 대시보드로 정상 동작을 검증한다."
                  45 +
                  46 +이유:
                  47 +- 목표가 없으면 도구 설치만 하고 끝나기 쉽습니다.
                  48 +- 목표를 먼저 고정하면 각 단계의 성공/실패를 명확히 판단할 수 있습니다.
                  49 +
                  50 +### 3.2 브랜치/저장소 전략 확정
                  51 +- 최소 2개 저장소를 권장합니다.
                  52 +  1. `source repo` (애플리케이션 코드)
                  53 +  2. `manifests repo` (쿠버네티스 배포 파일)
                  54 +
                  55 +이유:
                  56 +- GitOps에서는 "실행 코드"와 "배포 선언"을 분리해야 변경 책임이 명확해집니다.
                  57 +- ArgoCD는 선언 저장소를 기준으로 클러스터 상태를 맞춥니다.
                  58 +
                  59 +---
                  60 +
                  61 +## 4. 학습 적용 로드맵 (권장 순서)
                  62 +
                  63 +### Step 1. 로컬 실행 안정화 (현재 상태 점검)
                  64 +- 백엔드 테스트: `./gradlew test`
                  65 +- 프론트 빌드: `npm run build` (`frontend/`)
                  66 +- 로컬 환경 파일 정리: `application-local.yml`, `.env`
                  67 +
                  68 +왜 먼저?
                  69 +- CI/CD는 "자동화"이므로, 수동 실행이 불안정하면 자동화도 실패합니다.
                  70 +
                  71 +완료 기준:
                  72 +- 로컬에서 빌드/테스트/실행이 재현 가능
                  73 +
                  74 +---
                  75 +
                  76 +### Step 2. 컨테이너 이미지 표준화
                  77 +- 백엔드 `Dockerfile` 작성/정리
                  78 +- 프론트 `Dockerfile` 작성/정리
                  79 +- `docker build` 및 `docker run`으로 동작 검증
                  80 +
                  81 +왜 필요?
                  82 +- Kubernetes와 CD는 컨테이너 이미지를 배포 단위로 사용합니다.
                  83 +- 로컬/테스트/운영 실행 환경을 동일하게 맞출 수 있습니다.
                  84 +
                  85 +완료 기준:
                  86 +- 백엔드/프론트 이미지가 각각 정상 실행
                  87 +
                  88 +---
                  89 +
                  90 +### Step 3. Docker Compose로 로컬 통합 실행
                  91 +- 앱 + DB(+ 선택: Elasticsearch/Prometheus) 구성
                  92 +- `docker compose up -d`로 통합 실행
                  93 +
                  94 +왜 필요?
                  95 +- 분리된 서비스 간 네트워크/환경변수/의존성 문제를 초기에 검증할 수 있습니다.
                  96 +- Kubernetes 전 단계 실습으로 가장 효율적입니다.
                  97 +
                  98 +완료 기준:
                  99 +- `compose` 한 번으로 전체 스택이 재시작 가능
                 100 +
                 101 +---
                 102 +
                 103 +### Step 4. Jenkins CI 파이프라인 구축
                 104 +- Webhook 연동
                 105 +- 파이프라인 최소 단계:
                 106 +  1. Checkout
                 107 +  2. Build
                 108 +  3. Test
                 109 +  4. Docker Build
                 110 +  5. Docker Push
                 111 +
                 112 +왜 필요?
                 113 +- CI는 "신뢰 가능한 코드만 배포로 보내는 관문"입니다.
                 114 +- 실패를 빠르게 감지해 비용을 줄입니다.
                 115 +
                 116 +완료 기준:
                 117 +- Push 시 Jenkins가 자동 실행되고, 성공 시 이미지가 생성/업로드
                 118 +
                 119 +---
                 120 +
                 121 +### Step 5. Kubernetes 기본 배포
                 122 +- `Deployment`, `Service`, `Ingress` 매니페스트 작성
                 123 +- `Probe(Liveness/Readiness, 생존/준비 상태 점검)` 설정
                 124 +- `ConfigMap/Secret` 분리
                 125 +
                 126 +왜 필요?
                 127 +- 무중단 운영을 위해서는 파드 개별 상태가 아니라 "서비스 가용성" 중심으로 관리해야 합니다.
                 128 +- 환경별 설정 분리와 헬스체크는 실무 필수입니다.
                 129 +
                 130 +완료 기준:
                 131 +- 클러스터에서 앱 접근 가능, 재시작/롤링업데이트 동작 확인
                 132 +
                 133 +---
                 134 +
                 135 +### Step 6. ArgoCD CD 파이프라인 완성 (GitOps)
                 136 +- ArgoCD 설치
                 137 +- Manifest Repo 등록
                 138 +- `Sync Policy Automated` 적용
                 139 +- Jenkins에서 이미지 태그 변경 커밋 -> ArgoCD 자동 배포
                 140 +
                 141 +왜 필요?
                 142 +- Jenkins가 클러스터를 직접 건드리지 않고, Git을 통해 배포 이력을 남길 수 있습니다.
                 143 +- "누가/언제/무엇을 배포"했는지 추적성이 크게 향상됩니다.
                 144 +
                 145 +완료 기준:
                 146 +- 이미지 태그 변경 커밋만으로 자동 배포 완료
                 147 +
                 148 +---
                 149 +
                 150 +### Step 7. 모니터링/로깅 연결
                 151 +- `Prometheus + Grafana`: 메트릭 관측
                 152 +- `ELK`: 로그 수집/검색/분석
                 153 +- 기본 대시보드: HTTP 요청, JVM 메모리, 에러율, 응답시간
                 154 +
                 155 +왜 필요?
                 156 +- 배포 자동화만으로는 부족하고, 운영 관측이 있어야 개선이 가능합니다.
                 157 +- 장애 시 "감"이 아니라 데이터로 대응할 수 있습니다.
                 158 +
                 159 +완료 기준:
                 160 +- 배포 후 메트릭과 로그에서 정상/이상 패턴 확인 가능
                 161 +
                 162 +---
                 163 +
                 164 +## 5. 지금 당장 시작할 첫 액션
                 165 +
                 166 +처음 시작은 아래 3가지만 하시면 됩니다.
                 167 +
                 168 +1. `source repo`와 `manifests repo`를 분리할지 결정
                 169 +2. Jenkins/DockerHub/GitHub 자격증명 목록 정리
                 170 +3. 현재 코드 기준 `Step 1(로컬 안정화)` 체크리스트 실행
                 171 +
                 172 +체크리스트:
                 173 +- [ ] `./gradlew test` 성공
                 174 +- [ ] `frontend npm run build` 성공
                 175 +- [ ] 환경변수 파일 템플릿 준비 (`application-local.yml.example`, `.env.example`)
                 176 +
                 177 +---
                 178 +
                 179 +## 6. 실무 장단점 (학습 관점)
                 180 +
                 181 +장점:
                 182 +- 배포 속도와 일관성이 크게 향상됩니다.
                 183 +- 실패 지점을 조기에 발견합니다.
                 184 +- 운영 데이터 기반으로 개선이 가능합니다.
                 185 +
                 186 +단점:
                 187 +- 초기 설정량이 많고 도구 학습 비용이 큽니다.
                 188 +- 파이프라인이 복잡해지면 유지보수 부담이 증가합니다.
                 189 +
                 190 +학습 팁:
                 191 +- 한 번에 완성하려 하지 말고, **Step 1 -> Step 2 -> ...** 순서로 작게 성공을 쌓으세요.
                 192 +
                 193 +---
                 194 +
                 195 +## 7. 다음 문서 예고
                 196 +
                 197 +다음 단계 문서는 아래 순서로 분리하면 학습이 쉽습니다.
                 198 +
                 199 +1. `01-local-baseline.md` (로컬 안정화)
                 200 +2. `02-docker-compose.md` (컨테이너 통합)
                 201 +3. `03-jenkins-ci.md` (CI)
                 202 +4. `04-k8s-manifests.md` (쿠버네티스 배포)
                 203 +5. `05-argocd-cd.md` (GitOps CD)
                 204 +6. `06-observability.md` (ELK + Prometheus + Grafana)
                 205 +

             ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

             • 요청하신 내용을 별도 문서로 정리해두었습니다.

               - DEVOPS_CICD_LEARNING_GUIDE.md (/Users/gangseonghun/JapaneseQuizProject/DEVOPS_CICD_LEARNING_GUIDE.md)

               지금 질문하신 “처음에 뭐부터 하면 되나요?”에 대한 답은 **Step 0(학습 목표 고정) -> Step 1(로컬 기준선 안정화)**부터입니다.

               ## 1. 전체 개념 요약

               처음에는 CI/CD(Continuous Integration/Continuous Delivery, 지속적 통합/지속적 제공)를 바로 붙이지 않고,
               먼저 “현재 프로젝트가 로컬에서 항상 재현 가능한가”를 고정합니다.

               ## 2. 왜 이게 먼저 필요한가 (문제 -> 해결)

               - 문제: 로컬 실행이 흔들리면 Jenkins(젠킨스, CI 서버)에서도 계속 실패합니다.
               - 해결: 수동 실행이 100% 재현되게 만든 뒤 자동화로 넘기면, 실패 원인을 빠르게 좁힐 수 있습니다.

               ## 3. 지금 바로 할 구조

               오늘은 아래 3개만 하시면 됩니다.

               1. 목표 한 줄 확정

               - 예: “Push하면 테스트 통과한 코드만 DockerHub에 이미지가 올라간다.”

               2. 저장소 전략 확정

               - source repo(소스 저장소)와 manifests repo(배포 선언 저장소)를 분리할지 결정

               3. 기준선 체크 실행

               - 백엔드: ./gradlew test
               - 프론트: cd frontend && npm run build

               ## 4. 코드/실행 기준

               성공 기준은 단순합니다.

               - 백엔드 테스트 통과
               - 프론트 빌드 통과
               - 환경파일 템플릿 존재 확인 (application-local.yml.example, .env.example)

               ## 5. 실무 장단점

               - 장점: 이후 CI 실패가 “코드 이슈”인지 “환경 이슈”인지 명확해집니다.
               - 단점: 처음엔 느려 보이지만, 이 단계 생략하면 뒤에서 훨씬 오래 막힙니다.

               원하시면 다음 단계로 바로 **01-local-baseline.md**를 제가 만들어서, 지금 프로젝트 기준 체크리스트와 명령어를 실습형으로 시작해드리겠습니다.


             › 지금 빌드 완료했으니까 바로 2스텝 하면되겠네?


             • 네, 맞습니다. Step 1이 실제로 모두 통과했다면 바로 Step 2로 가시면 됩니다.

               다만 이 프로젝트는 현재 build.gradle (/Users/gangseonghun/JapaneseQuizProject/build.gradle)에서 bootJar가 비활성화되어 있어서, Docker(도커) 실행용 Executable JAR(실행 가능한 JAR)가 안 나올 가능성이 큽니
               다.
               그래서 Step 2 시작은 아래 순서가 안전합니다.

               1. Artifact(빌드 산출물) 확인

               - build/libs에 plain.jar만 있는지 확인

               2. Spring Boot JAR(스프링부트 실행 JAR) 전략 결정

               - 학습용 권장: bootJar=true, jar=false로 실행 JAR 생성

               3. Dockerfile(도커파일) 작성 및 이미지 빌드

               - 백엔드 먼저 docker build -> docker run 검증
               - 그다음 프론트 진행

               원하시면 제가 바로 다음으로 Step 2를 프로젝트에 맞게 실제 파일 작성부터 진행해드리겠습니다.


             › 1번은 확인했고 2번은 어떻게하는지 모르겠고 3번도 내가 직접 작성하고싶어 그대신 너가 하나씩 알려주면서 단계적으로 작성하고싶어


             • ### 1. 전체 개념 요약

               네, 아주 좋은 방식입니다.
               Step 2는 핵심이 **“Docker(도커)가 실행할 수 있는 Spring Boot(스프링부트) 실행 JAR를 만들기”**입니다.

               지금 프로젝트는 bootJar가 꺼져 있어서, 먼저 build.gradle을 바꿔야 합니다.
               그 다음에 직접 Dockerfile(도커파일)을 작성하시면 됩니다.

               ———

               ### 2. 왜 필요한지 (문제 -> 해결)

               - 문제: 현재는 일반 jar 중심 설정이라 java -jar app.jar로 바로 실행 가능한 형태가 아닐 수 있습니다.
               - 해결: bootJar(Spring Boot 실행 JAR)를 켜고 jar(일반 라이브러리 JAR)를 끄면, Docker에서 바로 실행 가능한 산출물이 만들어집니다.

               ———

               ### 3. 구조 설명

               지금 우리가 할 일은 2단계입니다.

               1. build.gradle 설정 변경
               2. 빌드 결과 확인 (plain 아닌 실행 JAR 생성 확인)

               이게 끝나면, 3단계에서 직접 Dockerfile 쓰시면 됩니다.

               ———

               ### 4. 코드 설명 (지금 바로 따라쓰기)

               /Users/gangseonghun/JapaneseQuizProject/build.gradle 맨 아래쪽 bootJar, jar 블록을 아래처럼 바꿔주세요.

               bootJar {
                   enabled = true
               }

               jar {
                   enabled = false
               }

               변경 후 실행:

               ./gradlew clean build
               ls -al build/libs

               정상 기준:

               - build/libs에 plain이 아닌 실행용 JAR가 있어야 합니다.
               - 예: jpquiz-0.0.1-SNAPSHOT.jar 같은 파일

               ———

               ### 5. 실무 장단점

               - 장점: Docker 이미지에서 ENTRYPOINT ["java","-jar","app.jar"]가 단순하고 안정적으로 동작합니다.
               - 단점: 라이브러리용 일반 JAR 배포가 필요하면 별도 전략이 필요합니다. (학습 단계에서는 지금 방식이 가장 명확합니다.)

               ———

               여기까지 직접 하신 뒤, ls -al build/libs 결과를 알려주세요.
               그다음 바로 **3번(Dockerfile 직접 작성)**을 한 줄씩 같이 진행하겠습니다.


             › gangseonghun@gangseonghun-ui-MacBookAir JapaneseQuizProject % ls -al build/libs
               total 144904
               drwxr-xr-x@  3 gangseonghun  staff        96 Feb 25 18:39 .
               drwxr-xr-x@ 10 gangseonghun  staff       320 Feb 25 18:39 ..
               -rw-r--r--@  1 gangseonghun  staff  74189075 Feb 25 18:39 jpquiz-0.0.1-SNAPSHOT.jar 잘 됐어 다음단계로 진행하자


             • ### 1. 전체 개념 요약

               아주 잘 되었습니다. 이제 바로 Step 3로 가서 **백엔드 Dockerfile(도커파일)**을 직접 작성하시면 됩니다.
               이번 단계 목표는 jpquiz-0.0.1-SNAPSHOT.jar를 컨테이너(Container, 컨테이너)에서 실행하는 것입니다.

               ### 2. 왜 필요한지 (문제 -> 해결)

               - 문제: 지금은 JAR가 로컬에서만 존재합니다.
               - 해결: Docker Image(도커 이미지)로 패키징하면 어디서든 같은 방식으로 실행할 수 있습니다.

               ### 3. 구조 설명

               이번에는 백엔드 Dockerfile 1개만 작성합니다.
               프론트는 그 다음 단계에서 진행하겠습니다.

               ### 4. 코드 설명 (직접 작성)

               프로젝트 루트(/Users/gangseonghun/JapaneseQuizProject)에 Dockerfile 파일을 만들고 아래 내용으로 작성해보세요.

               FROM eclipse-temurin:17-jre-alpine

               WORKDIR /app

               COPY build/libs/*.jar app.jar

               EXPOSE 8080

               ENTRYPOINT ["java", "-jar", "app.jar"]

               각 줄 의미:

               - FROM ...17...: Java 17 실행 환경(Runtime, 실행 환경) 사용
               - WORKDIR /app: 컨테이너 작업 경로 고정
               - COPY ... app.jar: 빌드된 JAR를 컨테이너로 복사
               - EXPOSE 8080: 앱 포트 명시
               - ENTRYPOINT ...: 컨테이너 시작 시 스프링부트 실행

               작성 후 실행:

               docker build -t jpquiz-backend:local .
               docker run --rm -p 8080:8080 --name jpquiz-backend jpquiz-backend:local

               새 터미널에서 확인:

               curl http://localhost:8080/actuator/health

               ### 5. 실무 장단점

               - 장점: 실행 환경이 표준화되어 CI/CD(지속적 통합/지속적 배포)로 바로 연결하기 쉽습니다.
               - 단점: DB 환경변수(Environment Variable, 환경 변수) 주입 없이 실행하면 일부 기능은 제한될 수 있습니다.

               작성/실행해보시고, 에러가 나면 로그 그대로 보내주세요. 다음 단계로 바로 프론트 Dockerfile까지 이어가겠습니다.


             › 지금 ERROR: failed to build: failed to solve: eclipse-temurin:17-jre-alpine: failed to resolve
               source metadata for docker.io/library/eclipse-temurin:17-jre-alpine: no match for platform in
               manifest: not found 이 에러가 발생햇허 WORKDIR /app
               COPY build/libs/*.jar app.jar
               EXPOSE 8080
               ENTRYPOINT ["java", "-jar", "app.jar"]

               FROM gradle:8-jdk17 AS build
               WORKDIR /app
               COPY . .
               RUN gradle bootJar --no-daemon -x test

               FROM eclipse-temurin:17-jre
               WORKDIR /app
               COPY --from=build /app/build/libs/*.jar app.jar
               EXPOSE 8080
               ENTRYPOINT ["java", "-jar", "app.jar"]이렇게 수정을했어


             • 네, 아주 잘 수정하셨습니다. 방향이 맞습니다.

               ### 1. 전체 개념 요약

               에러 원인은 eclipse-temurin:17-jre-alpine 이미지가 현재 Mac 아키텍처(ARM64, 애플 실리콘)에서 매니
               페스트를 못 찾은 것입니다.

               ### 2. 왜 필요한지 (문제 -> 해결)

               - 문제: 특정 태그(...alpine)가 현재 플랫폼을 지원하지 않아 no match for platform 발생
               - 해결: 멀티 아키텍처 지원이 넓은 태그(eclipse-temurin:17-jre)로 변경

               ### 3. 구조 설명

               다만 지금 붙여주신 내용은 Dockerfile 문법상 한 가지 수정이 필요합니다.
               Dockerfile은 반드시 FROM으로 시작해야 합니다.
               지금처럼 WORKDIR/COPY/...가 FROM 위에 있으면 안 됩니다.

               ### 4. 코드 설명 (최종본)

               아래처럼 이 내용만 남기고 저장해주세요.

               FROM gradle:8-jdk17 AS build
               WORKDIR /app
               COPY . .
               RUN gradle bootJar --no-daemon -x test

               FROM eclipse-temurin:17-jre
               WORKDIR /app
               COPY --from=build /app/build/libs/*.jar app.jar
               EXPOSE 8080
               ENTRYPOINT ["java", "-jar", "app.jar"]

               그다음 실행:

               docker build -t jpquiz-backend:local .
               docker run --rm -p 8080:8080 --name jpquiz-backend jpquiz-backend:local

               ### 5. 실무 장단점

               - 장점: 멀티 스테이지 빌드(Multi-stage Build, 다단계 빌드)라 최종 이미지가 가볍고 배포 친화적입니
                 다.
               - 주의: 빌드는 성공해도 실행 시 환경변수(DB/JWT 설정) 미주입이면 앱이 기동 실패할 수 있습니다. 그
                 때는 런타임 설정 단계로 넘어가면 됩니다.

               빌드/실행 결과 로그를 보내주시면, 바로 다음 단계(런타임 환경변수 주입 -> Compose 설계)로 이어가겠
               습니다.


             › Error response from daemon: ports are not available: exposing port TCP 0.0.0.0:8080 ->
               127.0.0.1:0: listen tcp 0.0.0.0:8080: bind: address already in use 이게 무슨 에러인데


             • ### 1. 전체 개념 요약

               이 에러는 이미 내 맥에서 8080 포트를 다른 프로세스가 사용 중이라는 의미입니다.

               ### 2. 왜 필요한지 (문제 -> 해결)

               - 문제: -p 8080:8080에서 호스트 포트(왼쪽) 8080이 점유됨
               - 해결:

               1. 기존 8080 사용 프로세스를 종료하거나
               2. 다른 포트로 매핑해서 실행하면 됩니다.

               ### 3. 구조 설명

               학습 중에는 2번이 가장 빠릅니다.

               ### 4. 코드 설명 (바로 실행)

               아래처럼 포트만 바꿔서 실행하세요.

               docker run --rm -p 18080:8080 --name jpquiz-backend jpquiz-backend:local

               확인:

               curl http://localhost:18080/actuator/health

               8080 점유 프로세스 확인하고 싶으면:

               lsof -i :8080

               ### 5. 실무 장단점

               - 장점: 포트 충돌 회피가 빠르고 학습 흐름 안 끊깁니다.
               - 단점: 포트가 여러 개 섞이면 혼동되니 문서에 고정 포트(18080)를 적어두는 게 좋습니다.


             › gangseonghun@gangseonghun-ui-MacBookAir JapaneseQuizProject % docker run --rm -p 8080:8080 --name
               jpquiz-backend jpquiz-backend:local
               docker: Error response from daemon: ports are not available: exposing port TCP 0.0.0.0:8080 ->
               127.0.0.1:0: listen tcp 0.0.0.0:8080: bind: address already in use

               Run 'docker run --help' for more information
               gangseonghun@gangseonghun-ui-MacBookAir JapaneseQuizProject % docker run --rm -p 8080:8080 --name
               jpquiz-backend jpquiz-backend:local

                 .   ____          _            __ _ _
                /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
               ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
                \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
                 '  |____| .__|_| |_|_| |_\__, | / / / /
                =========|_|==============|___/=/_/_/_/

                :: Spring Boot ::               (v3.5.10)

               2026-02-25T10:12:05.485Z  INFO 1 --- [jpquiz] [           main]
               com.team.jpquiz.JpQuizApplication        : Starting JpQuizApplication v0.0.1-SNAPSHOT using Java
               17.0.18 with PID 1 (/app/app.jar started by root in /app)
               2026-02-25T10:12:05.488Z DEBUG 1 --- [jpquiz] [           main]
               com.team.jpquiz.JpQuizApplication        : Running with Spring Boot v3.5.10, Spring v6.2.15
               2026-02-25T10:12:05.489Z  INFO 1 --- [jpquiz] [           main]
               com.team.jpquiz.JpQuizApplication        : No active profile set, falling back to 1 default
               profile: "local"
               2026-02-25T10:12:06.988Z  INFO 1 --- [jpquiz]
               [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA
               repositories in DEFAULT mode.
               2026-02-25T10:12:07.054Z  INFO 1 --- [jpquiz]
               [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository
               scanning in 56 ms. Found 5 JPA repository interfaces.
               2026-02-25T10:12:07.817Z  INFO 1 --- [jpquiz] [           main]
               o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
               2026-02-25T10:12:07.833Z  INFO 1 --- [jpquiz] [           main]
               o.apache.catalina.core.StandardService   : Starting service [Tomcat]
               2026-02-25T10:12:07.833Z  INFO 1 --- [jpquiz] [           main]
               o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.50]
               2026-02-25T10:12:07.873Z  INFO 1 --- [jpquiz] [           main] o.a.c.c.C.[Tomcat].[localhost].
               [/]       : Initializing Spring embedded WebApplicationContext
               2026-02-25T10:12:07.875Z  INFO 1 --- [jpquiz] [           main]
               w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed
               in 2338 ms
               2026-02-25T10:12:08.237Z  INFO 1 --- [jpquiz] [           main]
               o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name:
               default]
               2026-02-25T10:12:08.282Z  INFO 1 --- [jpquiz] [           main]
               org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.6.41.Final
               2026-02-25T10:12:08.310Z  INFO 1 --- [jpquiz] [           main]
               o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
               2026-02-25T10:12:08.556Z  INFO 1 --- [jpquiz] [           main]
               o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class
               transformer
               2026-02-25T10:12:08.580Z  INFO 1 --- [jpquiz] [           main]
               com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
               2026-02-25T10:12:09.654Z  WARN 1 --- [jpquiz] [           main]
               o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 08000
               2026-02-25T10:12:09.654Z ERROR 1 --- [jpquiz] [           main]
               o.h.engine.jdbc.spi.SqlExceptionHelper   : Socket fail to connect to localhost. Connection
               refused
               2026-02-25T10:12:09.656Z  WARN 1 --- [jpquiz] [           main]
               o.h.e.j.e.i.JdbcEnvironmentInitiator     : HHH000342: Could not obtain connection to query
               metadata

               org.hibernate.exception.JDBCConnectionException: unable to obtain isolated JDBC connection
               [Socket fail to connect to localhost. Connection refused] [n/a]
                       at
               org.hibernate.exception.internal.SQLStateConversionDelegate.convert(SQLStateConversionDelegate.ja
               va:100) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.exception.internal.StandardSQLExceptionConverter.convert(StandardSQLExceptionConver
               ter.java:58) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:108)
               ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:94)
               ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcI
               solationDelegate.java:116) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetada
               ta(JdbcEnvironmentInitiator.java:334) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentIn
               itiator.java:129) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentIn
               itiator.java:81) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceR
               egistryImpl.java:130) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryI
               mpl.java:263) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegis
               tryImpl.java:238) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl
               .java:215) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-
               core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorIm
               pl.java:226) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.ja
               va:194) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.jav
               a:171) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuil
               derImpl.java:1442) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilder
               Impl.java:1513) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityMan
               agerFactory(SpringHibernateJpaPersistenceProvider.java:66) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFacto
               ry(LocalContainerEntityManagerFactoryBean.java:390) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(Abst
               ractEntityManagerFactoryBean.java:419) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityMan
               agerFactoryBean.java:400) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContai
               nerEntityManagerFactoryBean.java:366) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(Ab
               stractAutowireCapableBeanFactory.java:1873) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(Abstr
               actAutowireCapableBeanFactory.java:1822) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:607) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               2) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefini
               tionValueResolver.java:365) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(Bea
               nDefinitionValueResolver.java:135) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.resolveConstructorArguments(Constru
               ctorResolver.java:691) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(Const
               ructorResolver.java:513) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFact
               oryMethod(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(A
               bstractAutowireCapableBeanFactory.java:1205) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:569) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               2) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefini
               tionValueResolver.java:365) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(Bea
               nDefinitionValueResolver.java:135) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyPropertyValues(
               AbstractAutowireCapableBeanFactory.java:1725) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(Abstrac
               tAutowireCapableBeanFactory.java:1474) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:606) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               2) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultL
               istableBeanFactory.java:1708) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultLis
               tableBeanFactory.java:1653) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(Constructo
               rResolver.java:913) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorReso
               lver.java:791) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(Const
               ructorResolver.java:546) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFact
               oryMethod(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(A
               bstractAutowireCapableBeanFactory.java:1205) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:569) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               7) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.getOrderedBeansOfType(Servlet
               ContextInitializerBeans.java:230) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(Servlet
               ContextInitializerBeans.java:184) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(Servlet
               ContextInitializerBeans.java:179) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAdaptableBeans(ServletCont
               extInitializerBeans.java:164) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.<init>(ServletContextInitiali
               zerBeans.java:96) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.getServletContext
               InitializerBeans(ServletWebServerApplicationContext.java:271) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.selfInitialize(Se
               rvletWebServerApplicationContext.java:245) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatStarter.onStartup(TomcatStarter.java:52)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:4452)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1201)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1191)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.FutureTask.run(Unknown Source) ~[na:na]
                       at
               org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:81)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.AbstractExecutorService.submit(Unknown Source) ~[na:na]
                       at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:747) ~[tomcat-
               embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:783) ~[tomcat-
               embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1201)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1191)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.FutureTask.run(Unknown Source) ~[na:na]
                       at
               org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:81)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.AbstractExecutorService.submit(Unknown Source) ~[na:na]
                       at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:747) ~[tomcat-
               embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:201)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardService.startInternal(StandardService.java:410)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:868)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.startup.Tomcat.start(Tomcat.java:436) ~[tomcat-embed-core-
               10.1.50.jar!/:na]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:128)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatWebServer.<init>(TomcatWebServer.java:107)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getTomcatWebServer(Tom
               catServletWebServerFactory.java:517) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getWebServer(TomcatSer
               vletWebServerFactory.java:219) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(S
               ervletWebServerApplicationContext.java:193) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(Servlet
               WebServerApplicationContext.java:167) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext
               .java:621) ~[spring-context-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWe
               bServerApplicationContext.java:146) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:752)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.run(SpringApplication.java:318) ~[spring-
               boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.run(SpringApplication.java:1361) ~[spring-
               boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.run(SpringApplication.java:1350) ~[spring-
               boot-3.5.10.jar!/:3.5.10]
                       at com.team.jpquiz.JpQuizApplication.main(JpQuizApplication.java:11) ~[!/:0.0.1-SNAPSHOT]
                       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
               ~[na:na]
                       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
               ~[na:na]
                       at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
               ~[na:na]
                       at java.base/java.lang.reflect.Method.invoke(Unknown Source) ~[na:na]
                       at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:106)
               ~[app.jar:0.0.1-SNAPSHOT]
                       at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:64)
               ~[app.jar:0.0.1-SNAPSHOT]
                       at org.springframework.boot.loader.launch.JarLauncher.main(JarLauncher.java:40)
               ~[app.jar:0.0.1-SNAPSHOT]
               Caused by: java.sql.SQLNonTransientConnectionException: Socket fail to connect to localhost.
               Connection refused
                       at org.mariadb.jdbc.client.impl.ConnectionHelper.connectSocket(ConnectionHelper.java:131)
               ~[mariadb-java-client-3.5.7.jar!/:na]
                       at org.mariadb.jdbc.client.impl.StandardClient.<init>(StandardClient.java:152) ~[mariadb-
               java-client-3.5.7.jar!/:na]
                       at org.mariadb.jdbc.Driver.connect(Driver.java:75) ~[mariadb-java-client-3.5.7.jar!/:na]
                       at org.mariadb.jdbc.Driver.connect(Driver.java:104) ~[mariadb-java-client-3.5.7.jar!/:na]
                       at org.mariadb.jdbc.Driver.connect(Driver.java:29) ~[mariadb-java-client-3.5.7.jar!/:na]
                       at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:144)
               ~[HikariCP-6.3.3.jar!/:na]
                       at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:370) ~[HikariCP-
               6.3.3.jar!/:na]
                       at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:207) ~[HikariCP-
               6.3.3.jar!/:na]
                       at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:488) ~[HikariCP-
               6.3.3.jar!/:na]
                       at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:576) ~[HikariCP-
               6.3.3.jar!/:na]
                       at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:97) ~[HikariCP-
               6.3.3.jar!/:na]
                       at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:111)
               ~[HikariCP-6.3.3.jar!/:na]
                       at
               org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(Dat
               asourceConnectionProviderImpl.java:126) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionA
               ccess.obtainConnection(JdbcEnvironmentInitiator.java:485) ~[hibernate-core-
               6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcI
               solationDelegate.java:61) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       ... 113 common frames omitted
               Caused by: java.net.ConnectException: Connection refused
                       at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
                       at java.base/sun.nio.ch.Net.pollConnectNow(Unknown Source) ~[na:na]
                       at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(Unknown Source) ~[na:na]
                       at java.base/sun.nio.ch.NioSocketImpl.connect(Unknown Source) ~[na:na]
                       at java.base/java.net.SocksSocketImpl.connect(Unknown Source) ~[na:na]
                       at java.base/java.net.Socket.connect(Unknown Source) ~[na:na]
                       at org.mariadb.jdbc.client.impl.ConnectionHelper.connectSocket(ConnectionHelper.java:118)
               ~[mariadb-java-client-3.5.7.jar!/:na]
                       ... 127 common frames omitted

               2026-02-25T10:12:09.667Z ERROR 1 --- [jpquiz] [           main]
               j.LocalContainerEntityManagerFactoryBean : Failed to initialize JPA EntityManagerFactory: Unable
               to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to
               determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common
               cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
               2026-02-25T10:12:09.670Z ERROR 1 --- [jpquiz] [           main]
               o.s.b.web.embedded.tomcat.TomcatStarter  : Error starting Tomcat context. Exception:
               org.springframework.beans.factory.UnsatisfiedDependencyException. Message: Error creating bean
               with name 'jwtAuthenticationFilter' defined in class path resource [com/team/jpquiz/global/
               security/SecurityConfig.class]: Unsatisfied dependency expressed through method
               'jwtAuthenticationFilter' parameter 1: Error creating bean with name 'memberRepository' defined
               in com.team.jpquiz.member.command.infrastructure.MemberRepository defined in
               @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration:
               Cannot resolve reference to bean 'jpaSharedEM_entityManagerFactory' while setting bean property
               'entityManager'
               2026-02-25T10:12:09.694Z  INFO 1 --- [jpquiz] [           main]
               o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
               2026-02-25T10:12:09.697Z  WARN 1 --- [jpquiz] [           main]
               ConfigServletWebServerApplicationContext : Exception encountered during context initialization -
               cancelling refresh attempt: org.springframework.context.ApplicationContextException: Unable to
               start web server
               2026-02-25T10:12:09.715Z  INFO 1 --- [jpquiz]
               [           main] .s.b.a.l.ConditionEvaluationReportLogger :

               Error starting ApplicationContext. To display the condition evaluation report re-run your
               application with 'debug' enabled.
               2026-02-25T10:12:09.727Z ERROR 1 --- [jpquiz] [           main]
               o.s.boot.SpringApplication               : Application run failed

               org.springframework.context.ApplicationContextException: Unable to start web server
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(Servlet
               WebServerApplicationContext.java:170) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext
               .java:621) ~[spring-context-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWe
               bServerApplicationContext.java:146) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:752)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.run(SpringApplication.java:318) ~[spring-
               boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.run(SpringApplication.java:1361) ~[spring-
               boot-3.5.10.jar!/:3.5.10]
                       at org.springframework.boot.SpringApplication.run(SpringApplication.java:1350) ~[spring-
               boot-3.5.10.jar!/:3.5.10]
                       at com.team.jpquiz.JpQuizApplication.main(JpQuizApplication.java:11) ~[!/:0.0.1-SNAPSHOT]
                       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
               ~[na:na]
                       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
               ~[na:na]
                       at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
               ~[na:na]
                       at java.base/java.lang.reflect.Method.invoke(Unknown Source) ~[na:na]
                       at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:106)
               ~[app.jar:0.0.1-SNAPSHOT]
                       at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:64)
               ~[app.jar:0.0.1-SNAPSHOT]
                       at org.springframework.boot.loader.launch.JarLauncher.main(JarLauncher.java:40)
               ~[app.jar:0.0.1-SNAPSHOT]
               Caused by: org.springframework.boot.web.server.WebServerException: Unable to start embedded
               Tomcat
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:147)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatWebServer.<init>(TomcatWebServer.java:107)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getTomcatWebServer(Tom
               catServletWebServerFactory.java:517) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getWebServer(TomcatSer
               vletWebServerFactory.java:219) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(S
               ervletWebServerApplicationContext.java:193) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(Servlet
               WebServerApplicationContext.java:167) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       ... 15 common frames omitted
               Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean
               with name 'jwtAuthenticationFilter' defined in class path resource [com/team/jpquiz/global/
               security/SecurityConfig.class]: Unsatisfied dependency expressed through method
               'jwtAuthenticationFilter' parameter 1: Error creating bean with name 'memberRepository' defined
               in com.team.jpquiz.member.command.infrastructure.MemberRepository defined in
               @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration:
               Cannot resolve reference to bean 'jpaSharedEM_entityManagerFactory' while setting bean property
               'entityManager'
                       at
               org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorReso
               lver.java:804) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(Const
               ructorResolver.java:546) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFact
               oryMethod(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(A
               bstractAutowireCapableBeanFactory.java:1205) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:569) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               7) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.getOrderedBeansOfType(Servlet
               ContextInitializerBeans.java:230) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(Servlet
               ContextInitializerBeans.java:184) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(Servlet
               ContextInitializerBeans.java:179) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAdaptableBeans(ServletCont
               extInitializerBeans.java:164) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.ServletContextInitializerBeans.<init>(ServletContextInitiali
               zerBeans.java:96) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.getServletContext
               InitializerBeans(ServletWebServerApplicationContext.java:271) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.selfInitialize(Se
               rvletWebServerApplicationContext.java:245) ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatStarter.onStartup(TomcatStarter.java:52)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:4452)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1201)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1191)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.FutureTask.run(Unknown Source) ~[na:na]
                       at
               org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:81)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.AbstractExecutorService.submit(Unknown Source) ~[na:na]
                       at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:747) ~[tomcat-
               embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:783) ~[tomcat-
               embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1201)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1191)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.FutureTask.run(Unknown Source) ~[na:na]
                       at
               org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:81)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at java.base/java.util.concurrent.AbstractExecutorService.submit(Unknown Source) ~[na:na]
                       at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:747) ~[tomcat-
               embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:201)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardService.startInternal(StandardService.java:410)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:868)
               ~[tomcat-embed-core-10.1.50.jar!/:na]
                       at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-
               core-10.1.50.jar!/:na]
                       at org.apache.catalina.startup.Tomcat.start(Tomcat.java:436) ~[tomcat-embed-core-
               10.1.50.jar!/:na]
                       at
               org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:128)
               ~[spring-boot-3.5.10.jar!/:3.5.10]
                       ... 20 common frames omitted
               Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name
               'memberRepository' defined in com.team.jpquiz.member.command.infrastructure.MemberRepository
               defined in @EnableJpaRepositories declared on
               JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration: Cannot resolve reference to bean
               'jpaSharedEM_entityManagerFactory' while setting bean property 'entityManager'
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefini
               tionValueResolver.java:377) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(Bea
               nDefinitionValueResolver.java:135) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyPropertyValues(
               AbstractAutowireCapableBeanFactory.java:1725) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(Abstrac
               tAutowireCapableBeanFactory.java:1474) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:606) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               2) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultL
               istableBeanFactory.java:1708) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultLis
               tableBeanFactory.java:1653) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(Constructo
               rResolver.java:913) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorReso
               lver.java:791) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       ... 61 common frames omitted
               Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name
               'jpaSharedEM_entityManagerFactory': Cannot resolve reference to bean 'entityManagerFactory' while
               setting constructor argument
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefini
               tionValueResolver.java:377) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(Bea
               nDefinitionValueResolver.java:135) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.resolveConstructorArguments(Constru
               ctorResolver.java:691) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(Const
               ructorResolver.java:513) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFact
               oryMethod(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(A
               bstractAutowireCapableBeanFactory.java:1205) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:569) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               2) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefini
               tionValueResolver.java:365) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       ... 74 common frames omitted
               Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name
               'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/
               orm/jpa/HibernateJpaConfiguration.class]: Unable to create requested service
               [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without
               JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect'
               when a custom Dialect implementation must be provided)
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(Abstr
               actAutowireCapableBeanFactory.java:1826) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(Abstrac
               tAutowireCapableBeanFactory.java:607) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractA
               utowireCapableBeanFactory.java:529) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFact
               ory.java:339) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingle
               tonBeanRegistry.java:373) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:
               337) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:20
               2) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefini
               tionValueResolver.java:365) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       ... 86 common frames omitted
               Caused by: org.hibernate.service.spi.ServiceException: Unable to create requested service
               [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without
               JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect'
               when a custom Dialect implementation must be provided)
                       at
               org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryI
               mpl.java:276) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegis
               tryImpl.java:238) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl
               .java:215) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-
               core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorIm
               pl.java:226) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.ja
               va:194) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.jav
               a:171) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuil
               derImpl.java:1442) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilder
               Impl.java:1513) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityMan
               agerFactory(SpringHibernateJpaPersistenceProvider.java:66) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFacto
               ry(LocalContainerEntityManagerFactoryBean.java:390) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(Abst
               ractEntityManagerFactoryBean.java:419) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityMan
               agerFactoryBean.java:400) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContai
               nerEntityManagerFactoryBean.java:366) ~[spring-orm-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(Ab
               stractAutowireCapableBeanFactory.java:1873) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       at
               org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(Abstr
               actAutowireCapableBeanFactory.java:1822) ~[spring-beans-6.2.15.jar!/:6.2.15]
                       ... 93 common frames omitted
               Caused by: org.hibernate.HibernateException: Unable to determine Dialect without JDBC metadata
               (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom
               Dialect implementation must be provided)
                       at
               org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.determineDialect(DialectFactoryImpl
               .java:191) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.buildDialect(DialectFactoryImpl.jav
               a:87) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentWithDefaults(Jd
               bcEnvironmentInitiator.java:186) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetada
               ta(JdbcEnvironmentInitiator.java:410) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentIn
               itiator.java:129) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentIn
               itiator.java:81) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceR
               egistryImpl.java:130) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       at
               org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryI
               mpl.java:263) ~[hibernate-core-6.6.41.Final.jar!/:6.6.41.Final]
                       ... 108 common frames omitted
                에러 발생했어


             • 좋습니다. 로그를 보면 지금은 Dockerfile 문제는 해결됐고, 런타임 설정 문제로 넘어간 상태입니다.

               ### 1. 전체 개념 요약

               현재 에러의 핵심은 이 한 줄입니다.

               - Socket fail to connect to localhost. Connection refused

               즉, 컨테이너 안의 앱이 localhost DB로 붙으려는데, 컨테이너 내부에는 DB가 없어서 실패한 것입니다.

               ### 2. 왜 이런지 (문제 -> 해결)

               - 문제: 컨테이너에서 localhost는 **내 맥(local host)**가 아니라 컨테이너 자기 자신입니다.
               - 해결: DB 주소를 컨테이너 기준으로 올바르게 주입해야 합니다.
                   - Mac에서는 보통 host.docker.internal 사용
                   - 또는 DB도 컨테이너로 같이 띄워 같은 네트워크에서 연결

               ### 3. 지금 단계에서 할 구조

               지금은 학습 흐름상 Docker Run + 환경변수(Environment Variable, 환경 변수)로 먼저 성공시키는 게 맞
               습니다.
               (docker-compose는 다음 단계에서 묶어서 진행)

               ### 4. 바로 실행할 명령 (단계별)

               1. JWT 시크릿(Base64, 인코딩 문자열) 준비

               echo -n 'this-is-32-byte-minimum-secret-key!!' | base64

               출력값을 복사해두세요. (예: dGhpcy1pcy0zMi1ieXRlLW1pbmltdW0tc2VjcmV0LWtleSEh)

               2. 백엔드 컨테이너 실행 (포트 충돌 피해서 18080 사용)

               docker run --rm \
                 -p 18080:8080 \
                 --name jpquiz-backend \
                 -e SPRING_DATASOURCE_URL='jdbc:mariadb://host.docker.internal:3306/jpquiz' \
                 -e SPRING_DATASOURCE_USERNAME='YOUR_DB_USER' \
                 -e SPRING_DATASOURCE_PASSWORD='YOUR_DB_PASSWORD' \
                 -e APP_JWT_SECRET='YOUR_BASE64_SECRET' \
                 jpquiz-backend:local

               3. 헬스 체크

               curl http://localhost:18080/actuator/health

               ### 5. 실무 장단점

               - 장점: 코드 수정 없이 런타임 설정만으로 컨테이너 실행 검증 가능
               - 단점: 명령이 길어져 관리가 어렵기 때문에, 다음 단계에서 docker-compose.yml로 정리하는 것이 정석
                 입니다.

               원하시면 다음으로 바로 같은 값들을 docker-compose.yml로 옮기는 단계를 같이 작성하겠습니다.


             › 8080포트로 실행시킬거야 다시


             • 좋습니다. 그럼 8080으로 맞춰서 실행하시면 됩니다.

               먼저 이미 8080 쓰는 프로세스/컨테이너를 정리하고, DB/JWT 환경변수 포함해서 다시 실행하세요.

               # 1) 8080 점유 확인
               lsof -i :8080

               # 2) (도커 컨테이너가 점유 중이면) 중지
               docker ps
               docker stop <컨테이너명_or_id>

               # 3) 실행
               docker run --rm \
                 -p 8080:8080 \
                 --name jpquiz-backend \
                 -e SPRING_DATASOURCE_URL='jdbc:mariadb://host.docker.internal:3306/jpquiz' \
                 -e SPRING_DATASOURCE_USERNAME='YOUR_DB_USER' \
                 -e SPRING_DATASOURCE_PASSWORD='YOUR_DB_PASSWORD' \
                 -e APP_JWT_SECRET='YOUR_BASE64_SECRET' \
                 jpquiz-backend:local

               확인:

               curl http://localhost:8080/actuator/health

               핵심은 포트 문제가 아니라 DB/JWT 환경변수 주입입니다.
               이 값들이 없으면 8080으로 띄워도 동일하게 기동 실패합니다.


             › 성공했어 다음단계는?


             • 아주 좋습니다. 지금부터 다음 단계는 프론트엔드 컨테이너화입니다.

               ### 1. 전체 개념 요약

               백엔드 컨테이너 실행이 성공했으니, 이제 프론트도 이미지로 만들면
               백엔드 + 프론트를 모두 컨테이너 기준으로 운영할 준비가 됩니다.

               ### 2. 왜 필요한지 (문제 -> 해결)

               - 문제: 프론트가 로컬 개발 서버(vite dev)에만 의존하면 배포/운영 재현성이 떨어집니다.
               - 해결: Build(정적 파일 생성) -> Nginx(정적 웹서버) 구조로 이미지화하면 운영 형태와 동일해집니다.

               ### 3. 구조 설명

               이번 단계 목표:

               1. frontend를 빌드해서 dist 생성
               2. nginx 이미지에 dist를 복사
               3. 컨테이너로 실행해 접속 확인

               ### 4. 코드 설명 (직접 작성)

               /Users/gangseonghun/JapaneseQuizProject/frontend에 아래 2개 파일을 만드세요.

               1. frontend/nginx.conf

               server {
                   listen 80;
                   server_name localhost;

                   location / {
                       root /usr/share/nginx/html;
                       try_files $uri $uri/ /index.html;
                   }
               }

               2. frontend/Dockerfile

               FROM node:lts-alpine AS build-stage
               WORKDIR /app
               COPY package*.json ./
               RUN npm install
               COPY . .
               RUN npm run build

               FROM nginx:stable-alpine AS production-stage
               COPY --from=build-stage /app/dist /usr/share/nginx/html
               COPY ./nginx.conf /etc/nginx/conf.d/default.conf
               EXPOSE 80
               CMD ["nginx", "-g", "daemon off;"]

               실행:

               cd frontend
               docker build -t jpquiz-frontend:local .
               docker run --rm -p 5173:80 --name jpquiz-frontend jpquiz-frontend:local

               확인:

               - 브라우저: http://localhost:5173

               ### 5. 실무 장단점

               - 장점: 배포 환경과 동일한 실행 방식이라 CI/CD와 자연스럽게 연결됩니다.
               - 단점: 개발 중 핫리로드는 느리므로, 개발은 vite dev, 배포 검증은 docker로 분리하는 게 좋습니다.

               프론트 실행 확인되면, 바로 다음으로 백엔드+DB+프론트를 한 번에 올리는 docker-compose.yml 단계로 넘
               어가겠습니다.


             › 다됏어 docker에서 정상적으로 실행되는 것 까지 확인해서 docker-compose.yml로 넘어가면 돼


             • 네, 이제 Step 4로 가시면 됩니다.
               지금 단계는 docker-compose.yml로 백엔드/프론트/DB를 한 번에 올리는 것입니다.

               ### 1. 전체 개념 요약

               Docker Compose(도커 컴포즈, 멀티 컨테이너 실행 도구)를 쓰면:

               - 컨테이너 네트워크 자동 구성
               - 환경변수 일괄 관리
               - 실행/중지 명령 통합

               ### 2. 왜 필요한지 (문제 -> 해결)

               - 문제: docker run을 서비스별로 수동 실행하면 순서/변수/포트 관리가 복잡해집니다.
               - 해결: compose로 서비스 선언형(Declarative, 선언형) 관리로 전환합니다.

               ### 3. 구조 설명

               이번 구성은 3개 서비스입니다.

               1. mariadb (DB)
               2. backend (Spring Boot)
               3. frontend (Nginx로 정적 배포된 Vue)

               ### 4. 코드 설명 (직접 작성)

               프로젝트 루트에 docker-compose.yml을 아래로 작성하세요.

               services:
                 mariadb:
                   image: mariadb:11
                   container_name: jpquiz-mariadb
                   environment:
                     MARIADB_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
                     MARIADB_DATABASE: ${DB_NAME}
                     MARIADB_USER: ${DB_USER}
                     MARIADB_PASSWORD: ${DB_PASSWORD}
                   ports:
                     - "3306:3306"
                   volumes:
                     - mariadb_data:/var/lib/mysql
                   healthcheck:
                     test: ["CMD", "healthcheck.sh", "--connect", "--innodb_initialized"]
                     interval: 10s
                     timeout: 5s
                     retries: 10

                 backend:
                   image: jpquiz-backend:local
                   container_name: jpquiz-backend
                   depends_on:
                     mariadb:
                       condition: service_healthy
                   environment:
                     SPRING_DATASOURCE_URL: jdbc:mariadb://mariadb:3306/${DB_NAME}
                     SPRING_DATASOURCE_USERNAME: ${DB_USER}
                     SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
                     APP_JWT_SECRET: ${APP_JWT_SECRET}
                     APP_CORS_ALLOWED_ORIGINS: http://localhost:5173
                   ports:
                     - "8080:8080"

                 frontend:
                   image: jpquiz-frontend:local
                   container_name: jpquiz-frontend
                   depends_on:
                     - backend
                   ports:
                     - "5173:80"

               volumes:
                 mariadb_data:

               루트에 .env 파일도 같이 작성하세요.

               DB_ROOT_PASSWORD=root1234
               DB_NAME=jpquiz
               DB_USER=jpquiz
               DB_PASSWORD=jpquiz1234
               APP_JWT_SECRET=여기에_아까_사용한_Base64_시크릿

               실행:

               docker compose up -d
               docker compose ps
               docker compose logs -f backend

               확인:

               - 프론트: http://localhost:5173
               - 백엔드 헬스: http://localhost:8080/actuator/health

               ### 5. 실무 장단점

               - 장점: 로컬 통합 테스트 환경이 표준화되어 CI/CD 전 단계로 최적입니다.
               - 단점: 스키마 초기화(SQL) 자동화는 아직 없어서, 다음 단계에서 DB 초기화 전략을 붙여야 완성됩니다.

               원하시면 다음으로 Step 5에서 DB 스키마 초기화(초기 SQL 적용)까지 Compose에 붙이는 방법을 이어서 하
               겠습니다.


             › gangseonghun@gangseonghun-ui-MacBookAir JapaneseQuizProject % docker compose up -d
               service "mariadb" refers to undefined volume mariadb_data: invalid compose project 이건 무슨에러
               야


             • 좋은 에러입니다. 의미가 명확합니다.

               mariadb 서비스에서 mariadb_data 볼륨을 쓰겠다고 했는데, 파일 하단에 해당 볼륨 정의를 Compose가 못
               찾았다는 뜻입니다.

               주로 원인 3가지입니다.

               1. volumes: 블록이 누락됨
               2. 들여쓰기(Indentation, 들여쓰기) 오류로 루트 레벨이 아님
               3. 이름 불일치 (mariadb_data vs mariadb-data)

               아래처럼 루트 레벨에 정확히 있어야 합니다.

               services:
                 mariadb:
                   image: mariadb:11
                   volumes:
                     - mariadb_data:/var/lib/mysql

               volumes:
                 mariadb_data:

               확인 순서:

               docker compose config

               이 명령에서 에러 없으면 정상 파싱입니다.
               그다음 다시:

               docker compose up -d

               원하시면 지금 docker-compose.yml 내용을 붙여주시면 제가 어디 줄이 잘못됐는지 바로 짚어드리겠습니
               다.


             › services:
                 mariadb:
                   image: mariadb:11
                   container_name: jpquiz-mariadb
                   environment:
                     MARIADB_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
                     MARIADB_DATABASE: ${DB_USER}
                     MARIADB_PASSWORD: ${DB_PASSWORD}
                   ports:
                     - "3306:3306"
                   volumes:
                     - mariadb_data:/var/lib/mysql
                   healthcheck:
                     test: ["CMD", "healthcheck.sh", "--connect", "--innodb_initialized"]
                     interval: 10s
                     timeout: 5s
                     retries: 10

                 backend:
                   image: jpquiz-backend:local
                   container_name: jpquiz-backend
                   depends_on:
                     mariadb:
                       condition: service_healthy
                   environment:
                     SPRING_DATASOURCE_URL: jdbc:mariadb://mariadb:3306/${DB_NAME}
                     SPRING_DATASOURCE_USERNAME: ${DB_USER}
                     SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
                     APP_JWT_SECRET: ${APP_JWT_SECRET}
                     APP_CORS_ALLOWED_ORIGINS: http://localhost:5173
                   ports:
                     - "8080:8080"

                 frontend:
                   image: jpquiz-frontend:local
                   container_name: jpquiz-frontend
                   depends_on:
                     - backend
                   ports:
                     - "5173:80"


             • 원인 찾았습니다. 지금 파일 맨 아래에 volumes: 루트 블록이 없습니다.

               그리고 DB 환경변수도 1개 빠져 있습니다.

               ### 수정해야 할 점

               1. 파일 하단에 볼륨 정의 추가

               volumes:
                 mariadb_data:

               2. mariadb 환경변수 보완

               - MARIADB_USER가 누락됨
               - MARIADB_DATABASE는 ${DB_NAME}이 맞습니다. (지금은 ${DB_USER}로 잘못 들어감)

               ### 수정된 mariadb 블록 예시

                 mariadb:
                   image: mariadb:11
                   container_name: jpquiz-mariadb
                   environment:
                     MARIADB_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
                     MARIADB_DATABASE: ${DB_NAME}
                     MARIADB_USER: ${DB_USER}
                     MARIADB_PASSWORD: ${DB_PASSWORD}

               ### 최종 확인 순서

               docker compose config
               docker compose up -d
               docker compose ps

               원하시면 다음으로 backend가 DB 스키마 없이 뜨지 않는 경우 대비해서 db/schema.sql 자동 적용까지 바
               로 이어서 붙여드리겠습니다.


             › JapaneseQuizProject % docker compose up -d
               service "mariadb" refers to undefined volume mariadb_data: invalid compose project 그래도에러인
               데?


             • 현재는 실제로 Docker Compose가 읽는 파일 내용과 사용 중인 파일 경로가 다를 가능성이 큽니다. 제가
               바로 현재 디렉토리의 compose 파일을 확인해서 정확한 원인을 짚어드리겠습니다.

             • Explored
               └ Read docker-compose.yml

             ────────────────────────────────────────────────────────────────────────────────────────────────────

             • 맞습니다. 원인 정확히 찾았습니다.

               ### 1. 전체 개념 요약

               현재 docker-compose.yml 실제 내용에는 맨 아래 volumes: 정의가 없습니다.
               그래서 mariadb_data를 참조만 하고 선언이 없어서 에러가 발생합니다.

               ### 2. 왜 필요한지 (문제 -> 해결)

               - 문제: services.mariadb.volumes에서 mariadb_data 사용
               - 해결: 루트 레벨에 volumes.mariadb_data를 반드시 선언

               ### 3. 구조 설명

               docker-compose.yml은 크게 2개 루트 블록이 있어야 합니다.

               1. services:
               2. volumes:  ← 이게 빠진 상태입니다.

               ### 4. 코드 설명 (직접 수정)

               파일 맨 마지막 줄에 아래를 그대로 추가해주세요.

               volumes:
                 mariadb_data:

               주의:

               - 들여쓰기 없이 루트 레벨(services:와 같은 레벨)이어야 합니다.
               - volumes:를 services: 안에 넣으면 안 됩니다.

               수정 후 확인:

               docker compose config
               docker compose up -d
               docker compose ps

               ### 5. 실무 장단점

               - 장점: named volume(네임드 볼륨)로 DB 데이터 영속성(Persistence, 영속성)을 확보합니다.
               - 단점: 볼륨 초기화가 필요할 때는 docker compose down -v처럼 의도적으로 지워야 합니다.

               원하시면 다음으로 DB 스키마(db/schema.sql) 자동 주입까지 이어서 붙여드리겠습니다.


             › response from daemon: ports are not available: exposing port TCP 0.0.0.0:3306 -> 127.0.0.1:0:
               listen tcp 0.0.0.0:3306: bind: address already in use


             • 이번 에러는 3306 포트가 이미 사용 중이라는 뜻입니다.
               즉, 로컬 MariaDB/MySQL 또는 다른 컨테이너가 먼저 3306을 점유하고 있습니다.

               ### 지금 바로 해결 (학습용 권장)

               docker-compose.yml에서 DB 포트만 바꿔주세요.

               mariadb:
                 ports:
                   - "13306:3306"

               중요:

               - backend는 이미 jdbc:mariadb://mariadb:3306/...이라 수정할 필요 없습니다.
               - 서비스 간 통신은 내부 네트워크 3306을 쓰고, 13306은 내 맥에서 DB 직접 붙을 때만 쓰는 포트입니다.

               그다음 다시 실행:

               docker compose up -d
               docker compose ps
               docker compose logs -f backend

               원하면 3306 점유 프로세스 확인도 가능합니다:

               lsof -i :3306

               학습 관점에서 이 방식이 좋은 이유는 “호스트 포트 충돌 회피”와 “컨테이너 내부 포트 불변”을 동시에
               배우기 좋기 때문입니다.


             › docker-compose 잘 묶여서 실행됐어 이 다음은?


             • 좋습니다. 지금부터는 Step 5: CI(Jenkins 파이프라인) 구축이 다음 단계입니다.

               ## 1. 전체 개념 요약

               현재 상태:

               - Dockerfile 백엔드/프론트 작성 완료
               - docker-compose로 통합 실행 완료

               다음 목표:

               - 코드 Push 시 빌드 -> 테스트 -> 이미지 빌드를 자동화해서 사람 개입을 줄이는 것

               ## 2. 왜 필요한지 (문제 -> 해결)

               - 문제: 지금은 수동으로 build, docker build, compose up를 직접 실행해야 합니다.
               - 해결: Jenkins가 반복 작업을 자동 실행하면 실수를 줄이고 속도를 높일 수 있습니다.

               ## 3. 구조 설명

               학습용으로 CI를 2단계로 나눠서 가면 가장 안전합니다.

               1. CI-1: 빌드/테스트 자동화만
               2. CI-2: Docker 이미지 빌드/푸시 자동화

               이렇게 나누는 이유:

               - 실패 원인을 빨리 분리할 수 있습니다.
                 (코드 실패인지, DockerHub 인증 실패인지 구분 가능)

               ## 4. 코드 설명 (지금 바로 할 일)

               ### 4-1. 루트에 Jenkinsfile 생성

               파일: Jenkinsfile (/Users/gangseonghun/JapaneseQuizProject/Jenkinsfile)

               pipeline {
                   agent any

                   stages {
                       stage('Checkout') {
                           steps {
                               checkout scm
                           }
                       }

                       stage('Backend Build & Test') {
                           steps {
                               sh 'chmod +x ./gradlew'
                               sh './gradlew clean test'
                           }
                           post {
                               always {
                                   junit '**/build/test-results/test/*.xml'
                               }
                           }
                       }

                       stage('Frontend Build') {
                           steps {
                               dir('frontend') {
                                   sh 'npm ci'
                                   sh 'npm run build'
                               }
                           }
                       }
                   }

                   post {
                       success {
                           echo 'CI-1 성공: 빌드/테스트 통과'
                       }
                       failure {
                           echo 'CI-1 실패: 로그 확인 필요'
                       }
                   }
               }

               ### 4-2. Jenkins Job 설정

               - Pipeline from SCM로 연결
               - GitHub repo 지정
               - 브랜치 main
               - 먼저 수동 Build Now 1회 실행

               ### 4-3. 통과 후 CI-2로 확장

               다음에 제가 바로 붙여드릴 내용:

               - backend 이미지 빌드/푸시
               - frontend 이미지 빌드/푸시
               - withCredentials로 DockerHub 계정 안전 주입

               ## 5. 실무 장단점

               - 장점: 개발자가 “코드 작성”에 집중하고 품질 검증은 자동화됩니다.
               - 주의: 처음부터 푸시까지 한 번에 넣으면 디버깅이 어려워집니다. 지금처럼 단계 분리가 학습에 가장
                 좋습니다.

               원하시면 다음 답변에서 제가 **CI-2용 Jenkinsfile(도커 푸시 포함)**을 바로 이어서 작성해드리겠습니
               다.


             › 너 지금 ```bash
               vi /opt/homebrew/opt/jenkins-lts/homebrew.mxcl.jenkins-lts.plist
               ```

               아래 내용 수정

               ```
               <string>--httpPort=18080</string>
               ```

               LaunchAgent 다시 로드

               ```bash
               launchctl unload ~/Library/LaunchAgents/homebrew.mxcl.jenkins-lts.plist
               launchctl load ~/Library/LaunchAgents/homebrew.mxcl.jenkins-lts.plist
               ``` 이 내용은 참고하고있는거 맞지?


             • 네, 맞습니다. 그 설정 기준으로 안내드리고 있습니다.

               핵심은 아래 3가지입니다.

               1. Jenkins 포트는 18080 기준

               - 접속 URL: http://localhost:18080

               2. macOS(Homebrew 설치)에서는 plist에서 포트 변경 후 launchctl unload/load로 재기동
               3. Webhook/터널링 시에도 Jenkins 주소는 18080 기준으로 맞춰야 함

               - 예: https://<ngrok-url>/github-webhook/

               추가 확인 명령:

               lsof -i :18080

               정상이라면 Jenkins 프로세스가 18080을 점유하고 있어야 합니다.


             › Summarize recent commits

               gpt-5.3-codex · ~/JapaneseQuizProject · 54% used · 5h 99% · weekly 96% · 3.69M used · 3.67M in · …ge('Backend Build & Test') {
              steps {
                  sh 'chmod +x ./gradlew'
                  sh './gradlew clean test'
              }
              post {
                  always {
                      junit '**/build/test-results/test/*.xml'
                  }
              }
          }

          stage('Frontend Build') {
              steps {
                  dir('frontend') {
                      sh 'npm ci'
                      sh 'npm run build'
                  }
              }
          }
      }

      post {
          success {
              echo 'CI-1 성공: 빌드/테스트 통과'
          }
          failure {
              echo 'CI-1 실패: 로그 확인 필요'
          }
      }
  }
