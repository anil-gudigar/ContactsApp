pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './gradlew clean'
        sh './gradlew assembleDebug'
      }
    }
    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }
  }
}