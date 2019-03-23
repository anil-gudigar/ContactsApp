pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''sudo ./gradlew clean
sudo ./gradlew assembleDebug'''
      }
    }
    stage('Test') {
      steps {
        sh './gradlew testDebugUnitTest'
      }
    }
  }
}