pipeline {
  agent any

  environment {
    IMAGE_REPO_BACKEND  = 'zkfmak9257/jpquiz-backend'
    IMAGE_REPO_FRONTEND = 'zkfmak9257/jpquiz-frontend'
    IMAGE_TAG = "${env.BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Backend Test') {
      steps {
        sh 'chmod +x ./gradlew'
        sh './gradlew clean test'
      }
      post { always { junit '**/build/test-results/test/*.xml' } }
    }

    stage('Frontend Build') {
      steps {
        dir('frontend') {
          sh 'npm ci'
          sh 'npm run build'
        }
      }
    }

    stage('Docker Login') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD',
          usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
        }
      }
    }

    stage('Build & Push Backend Image') {
      steps {
        sh """
          docker build \
            -t ${IMAGE_REPO_BACKEND}:${IMAGE_TAG} \
            -t ${IMAGE_REPO_BACKEND}:latest \
            .
        """
        sh "docker push ${IMAGE_REPO_BACKEND}:${IMAGE_TAG}"
        sh "docker push ${IMAGE_REPO_BACKEND}:latest"
      }
    }

    stage('Build & Push Frontend Image') {
      steps {
        dir('frontend') {
          sh """
            docker build \
              -t ${IMAGE_REPO_FRONTEND}:${IMAGE_TAG} \
              -t ${IMAGE_REPO_FRONTEND}:latest \
              .
          """
          sh "docker push ${IMAGE_REPO_FRONTEND}:${IMAGE_TAG}"
          sh "docker push ${IMAGE_REPO_FRONTEND}:latest"
        }
      }
    }
  }

  post {
    always { sh 'docker logout || true' }
    success { echo "CI-2 성공: DockerHub push 완료" }
    failure { echo "CI-2 실패: 로그 확인 필요" }
  }
}