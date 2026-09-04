pipeline {

    agent any // allow any avaiable agent

    // define env variables
    environment {
        DOCKER_IMAGE = 'ornsunlang/jenkins-demo'
        DOCKER_CREDENTAILS_ID = 'docker-hub-credentails'
    }

    stages {

        stage('Biuld and Test'){
            steps{
                echo "Compiling and Testing"
                sh 'chmod +x mvnw && ./mvnw clean test'
                sh 'find target -type f'
            }
            post{
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Dcoker Build'){
            steps{
                echo "Docker build image: ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                sh "docker build -t ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ."
                sh "docker tag ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Docker push'){
            steps{
                echo "Push to docker hub"
                //withDockerRegistry securely logs in and out
                withDockerRegistry([credentailsId: DOCKER_CREDENTAILS_ID, url: '']){
                    sh "docker push ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }
    }
    post{
        always {
            echo "Pipeline finished"
        }
    }
}