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
          sh 'node -v'
          sh 'npm -v'
          sh 'npm ci'
          sh 'npm run build'
        }
      }
    }
  }

  post {
    success { echo 'CI-1 성공: 빌드/테스트 통과' }
    failure { echo 'CI-1 실패: 콘솔 로그 확인 필요' }
  }
}