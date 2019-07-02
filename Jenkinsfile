pipeline {
  agent {
    node {
      label 'jdk8'
    }

  }
  stages {
    stage('Fluffy Build') {
      steps {
        echo 'Fluffy build message'
        sh 'echo Another Placeholder in Fluffy Build stage'
      }
    }
    stage('Fluffy Test') {
      steps {
        sh 'sleep 5'
        sh 'echo Success!'
      }
    }
    stage('Fluffy Deploy') {
      steps {
        echo 'Fluffy Deploy Message'
      }
    }
  }
}