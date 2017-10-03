pipeline {
  agent {
    docker {
      image 'bitwiseman/training-blueocean-sample'
      args '-u root -v /home/jenkins/docker/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh './jenkins/build.sh'
        archiveArtifacts 'target/*.war'
      }
    }
    stage('Test') {
      steps {
        parallel(
          "Backend": {
            sh './jenkins/test-backend.sh'
            junit '**/surefire-reports/**/*.xml'

          },
          "Frontend": {
            sh './jenkins/test-frontend.sh'
            junit '**/test-results/karma/*.xml'
            // publishHTML target: [
            //   allowMissing: false,
            //   alwaysLinkToLastBuild: false,
            //   keepAll: true,
            //   reportDir: 'target/test-results/coverage',
            //   reportFiles: 'index.html',
            //   reportName: 'RCov Report'
            // ]
          },
          "Static": {
            sh './jenkins/test-static.sh'

          },
          "Performance": {
            sh './jenkins/test-performance.sh'

          }
        )
      }
    }
    stage('Deploy to Staging') {
      // when {
      //   branch 'master'
      // }
      steps {
        sh './jenkins/deploy.sh staging'
      }
    }
    stage('Deploy to Production') {
      // when {
      //   branch 'master'
      // }
      steps {
        input(message: 'Deploy to staging?', ok: 'Fire away!')
        sh './jenkins/deploy.sh production'
        sh 'echo Notifying appropriate team members!'
      }
    }
  }
}
pipeline {
  agent {
    dockerfile {
      args '-u root -v /home/jenkins/docker/.m2:/root/.m2'
    }
  }
  stages {
    stage('Build') {
      steps {
        sh './jenkins/build.sh'
      }
    }
    stage('Test') {
      steps {
        parallel(
         'Backend' : {
           sh './jenkins/test-backend.sh'
           junit '**/surefire-reports/**/*.xml'
          },
          'Frontend' : {
            sh './jenkins/test-frontend.sh'
            junit '**/test-results/karma/*.xml'
            // publishHTML target: [
            //   allowMissing: false,
            //   alwaysLinkToLastBuild: false,
            //   keepAll: true,
            //   reportDir: 'target/test-results/coverage',
            //   reportFiles: 'index.html',
            //   reportName: 'RCov Report'
            // ]
         },
         'Static' : {
           sh './jenkins/test-static.sh'
        })
       }
    }
    stage('Performance') {
      steps {
        sh './jenkins/test-performance.sh'
      }
    }
    stage('Deploy to Staging') {
      when {
        branch 'master'
      }
      steps {
        sh './jenkins/deploy.sh staging'
        sh 'echo Notifying the Team!'
      }
    }

    stage('Deploy to Production') {
      when {
        branch 'master'
      }
      steps {
        input message: 'Deploy to production?',
                   ok: 'Fire away!'
        sh './jenkins/deploy.sh production'
        sh 'echo Notifying the Team!'
      }
    }
  }
}
