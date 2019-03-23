pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''./gradlew clean
./gradlew assembleDebug'''
      }
    }
    stage('Test') {
      steps {
        sh './gradlew testDebugUnitTest'
      }
    }
  }
}