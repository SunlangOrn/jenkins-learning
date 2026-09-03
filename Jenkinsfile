pipeline {

    agent any // allow any avaiable agent

    // define env variables
    environment {
        APP_NAME = 'jenkins-demo'
    }

    stages {
        stage('Build'){
            steps {
                echo "Compliling ${APP_NAME}"
                sh "chmod +x ./mvnw && ./mvnw clean compile"
            }
        }

        //use parallel stage for synoc
        stage('test and package'){
            parallel{
                stage('Test'){
                    steps {
                        echo 'Running unit test'
                        sh './mvnw test'
                        echo 'Unit tet passed'
                    }
                    // use 'post' to ensure the report is generate even if test is fail
                    post {
                        always {
                            echo 'Capturing Test Result'
                            junit 'target/surefire-reports/*.xml'
                        }
                    }
                }
                stage('Package'){
                    steps {
                        echo 'Package app into JAR'
                        // We skip tests here because we already ran them in the 'Test' stage
                        sh './mvnw package -DskipTests'
                    }
                }

            }
        }

        //Input prompt stage
        stage('Approval for Production'){
            steps {
                timeout(time: 30, unit:'MINUTES'){
                    input(
                        message: "Do you want to deploy ?",
                        ok: 'Deploy now'
                    )
                }

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