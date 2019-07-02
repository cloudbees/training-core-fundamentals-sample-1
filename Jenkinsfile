pipeline {
  agent {
    node {
      label 'jdk8'
    }

  }
  stages {
    stage('Fluffy Build') {
      steps {
        sh 'jenkins/build.sh'
        archiveArtifacts 'target/*.jar'
      }
    }
    stage('Fluffy Test') {
      parallel {
        stage('Performance') {
          steps {
            sh 'jenkins/test-performance.sh'
          }
        }
        stage('Backend') {
          steps {
            sh 'jenkins/test-backend.sh'
            junit 'target/surefire-reports/**/TEST*.xml'
          }
        }
        stage('Frontend') {
          steps {
            sh 'jenkins/test-frontend.sh'
            junit 'target/test-results/**/TEST*.xml'
          }
        }
        stage('Static') {
          steps {
            sh 'jenkins/test-static.sh'
          }
        }
      }
    }
    stage('Fluffy Deploy') {
      steps {
        sh 'jenkins/deploy.sh staging'
      }
    }
  }
}