pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'sudo ./gradlew assembleDebug'
      }
    }
  }
}