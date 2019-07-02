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
      steps {
        sh 'jenkins/test-all.sh'
        junit 'target/**/TEST*.xml'
      }
    }
    stage('Fluffy Deploy') {
      steps {
        sh 'jenkins/deploy.sh staging'
      }
    }
  }
}