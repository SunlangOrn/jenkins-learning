pipeline {

    agent any // allow any avaiable agent

    // define env variables
    environment {
        APP_NAME = 'jenkins-demo'
    }

    stages {
        stage('checkout'){
            steps {
                echo 'checkout code for ${APP_NAME}...'
            }
        }

        stage('Build'){
            steps {
                echo 'Running Maven Wrapper....'
                sh "chmod +x ./mvnw && ./mvnw clean package"
            }
        }

        //use parallel stage for synoc
        stage('test and analysis'){
            parallel{
                stage('Unit test'){
                    steps {
                        echo 'Running unit test'
                        sh 'sleep 5' //mean take 5 min to test
                        echo 'Unit tet passed'
                    }
                }
                stage('analysis code'){
                    steps {
                        echo 'Runing code analysis'
                        sh 'sleep 5'
                        echo 'Code analysis passed'
                    }
                }

            }
        }

        //Input prompt stage
        stage('Approval for Production'){
            steps {
                input message: "Do you want to deploy ?", Ok: 'Deploy now'
            }
        }

        stage('Deploy'){
            steps {
                echo 'Deploying to Prod'
                sh 'sleep 3'
                echo 'Completed'
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