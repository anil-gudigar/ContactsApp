pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        sh './gradlew assembleDebug'
      }
    }
    stage('Unit test') {
      steps {
        sh './gradlew testDebugUnitTest'
        junit '**/TEST-*.xml'
      }
    }
    stage('Build APK') {
      steps {
        sh './gradlew assembleDebug'
        archiveArtifacts '**/*.apk'
      }
    }
    stage('Static analysis') {
      steps {
        sh './gradlew lintDebug'
        androidLint(pattern: '**/lint-results-*.xml')
      }
    }
  }
}