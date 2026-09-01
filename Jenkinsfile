pipeline {

    agent any // allow any avaiable agent

    // define env variables
    environment {
        APP_NAME = 'jenkins-demo'
        MAVEN_CMD = './mvnw'
    }

    stages {
        stage('checkout'){
            steps {
                echo 'checkout code for ${APP_NAME}...'
            }
        }

        stage('Build and Test'){
            steps {
                echo 'Running Maven Wrapper....'
                sh "chmod +x ${MAVEN_CMD} && ${MAVEN_CMD} clean package"
            }
        }

        stage('Archive'){
            steps{
                echo 'Archive the JAR file ...'
                archiveArtifacts artifacts: 'target/*.jar',
                allowEmptyArchive: false
            }
        }
    }

    // define post-build action
    post{
        always {
            echo "pipeline exec completed for ${APP_NAME}"
        }
        success {
            echo "success: buid and test is perfect"
        }
        failure {
            echo "failure: please check the log"
        }
    }
}