pipeline {
  agent none
  stages {
    stage('Fluffy Build') {
      parallel {
        stage('Build Java 8') {
          agent {
            node {
              label 'java8 && !windows'
            }

          }
          post {
            success {
              archiveArtifacts 'target/*.jar'
              stash(name: 'Java 8', includes: 'target/**')

            }

          }
          steps {
            sh './jenkins/build.sh'
          }
        }
        stage('Build Java 11') {
          agent {
            node {
              label 'java11 && !windows'
            }

          }
          post {
            success {
              stash(name: 'Java 11', includes: 'target/**')

            }

          }
          steps {
            sh './jenkins/build.sh'
          }
        }
      }
    }
    stage('Fluffy Unit Test') {
      parallel {
        stage('Backend Java 8') {
          agent {
            node {
              label 'java8 && !windows'
            }

          }
          post {
            always {
              junit 'target/surefire-reports/**/TEST*.xml'

            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-backend.sh'
          }
        }
        stage('Static Java 8') {
          agent {
            node {
              label 'java8 && !windows'
            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-static.sh'
          }
        }
        stage('Backend Java 11') {
          agent {
            node {
              label 'java11 && !windows'
            }

          }
          post {
            always {
              junit 'target/surefire-reports/**/TEST*.xml'

            }

          }
          steps {
            unstash 'Java 11'
            sh './jenkins/test-backend.sh'
          }
        }
        stage('Static Java 11') {
          agent {
            node {
              label 'java11 && !windows'
            }

          }
          steps {
            unstash 'Java 11'
            sh './jenkins/test-static.sh'
          }
        }
      }
    }
    stage('Fluffy Integration Test') {
      parallel {
        stage('Frontend Integration Test') {
          agent {
            node {
              label 'java8 && !windows'
            }

          }
          steps {
            sh 'jenkins/test-frontend.sh'
          }
        }
        stage('Frontend Integration Java 11') {
          agent {
            node {
              label 'java11 && !windows'
            }

          }
          steps {
            sh 'jenkins/test-frontend.sh'
          }
        }
      }
    }
    stage('Fluffy Performance') {
      parallel {
        stage('Performance Java 8') {
          agent {
            node {
              label 'java8 && !windows'
            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-performance.sh'
          }
        }
        stage('Performance Java 11') {
          agent {
            node {
              label 'java11 && !windows'
            }

          }
          steps {
            unstash 'Java 11'
            sh './jenkins/test-performance.sh'
          }
        }
      }
    }
    stage('Tests Complete Checkpoint') {
      when {
        branch 'master'
      }
      steps {
        echo "checkpoint 'Finished tests'"
      }
    }
    stage('Confirm Deploy') {
      when {
        branch 'master'
      }
      steps {
        input(message: 'Okay to Deploy to Staging?', ok: 'Let\'s Do it!')
      }
    }
    stage('Fluffy Deploy') {
      agent {
        node {
          label 'java8 && !windows'
        }

      }
      when {
        branch 'master'
      }
      steps {
        unstash 'Java 8'
        sh "./jenkins/deploy.sh ${params.DEPLOY_TO}"
      }
    }
  }
  parameters {
    string(name: 'DEPLOY_TO', defaultValue: 'dev', description: '')
  }
}
