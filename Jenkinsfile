pipeline {
  agent none
  stages {
    stage('Fluffy Build') {
      parallel {
        stage('Build Java 8') {
          agent {
            node {
              label 'java8'
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
              label 'java11'
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
    stage('Fluffy Test') {
      parallel {
        stage('Backend Java 8') {
          agent {
            node {
              label 'java8'
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
        stage('Frontend') {
          agent {
            node {
              label 'java8'
            }

          }
          post {
            always {
              junit 'target/test-results/**/TEST*.xml'

            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-frontend.sh'
          }
        }
        stage('Static Java 8') {
          agent {
            node {
              label 'java8'
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
              label 'java11'
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
        stage('Frontend Java 11') {
          agent {
            node {
              label 'java11'
            }

          }
          post {
            always {
              junit 'target/test-results/**/TEST*.xml'

            }

          }
          steps {
            unstash 'Java 11'
            sh './jenkins/test-frontend.sh'
          }
        }
        stage('Static Java 11') {
          agent {
            node {
              label 'java11'
            }

          }
          steps {
            unstash 'Java 11'
            sh './jenkins/test-static.sh'
          }
        }
      }
    }
    stage('Fluffy Performance') {
      parallel {
        stage('Performance Java 8') {
          agent {
            node {
              label 'java8'
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
              label 'java11'
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
        checkpoint 'Finished tests'
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
          label 'java8'
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
