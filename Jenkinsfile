pipeline {
    agent any


    parameters {
        choice(
            name: 'ENVIRONMENT' ,
            choices: ['dev', 'staging', 'prod'] ,
            description: 'which env should this be deploy to?'
        )
        string(
            name: 'CUSTOM_MESSAGE' ,
            defaultValue: 'deploying new version',
            description: 'a message to print during deploy'
        )
    }

    environment {
        // REPLACE with your actual Docker Hub username if different
        DOCKER_IMAGE = 'ornsunlang/jenkins-demo'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
    }

    stages {
        stage('Build & Test') {
            steps {
                echo "Compiling and Testing..."
                // Single quotes are fine here because there are no variables
                sh 'chmod +x mvnw && ./mvnw clean package'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker Image: ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                sh "docker build -t ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ."
                sh "docker tag ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Docker Push') {
            steps {
                echo "Pushing to Docker Hub..."
                withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
                    retry(3) {
                        sh "docker push ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                    }
                }
            }
        }

        stage('Deploy to Server') {
            when {
                branch 'main'
            }
            steps {
                echo "Starting deploy"
                script {
                    try {
                        echo "target env: ${params.ENVIRONMENT}"
                        echo "message: ${params.CUSTOM_MESSAGE}"

                        if (params.ENVIRONMENT == 'prod'){
                            echo "WARNING: deploying to production"
                            // in real: sh 'docker-compose -f docker-compose.prod.yml up -d'
                        } else {
                            echo "deploy to ${params.ENVIRONMENT} env"
                            //in real:sh 'docker-compose -f docker-compose.dev.yml up -d'
                        }

                        sh 'echo "Deployment commands executed successfully!"'
                    } catch (Exception e) {
                        echo "Deployment failed! Error: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }

       stage('Health Check') {

           when {
               branch 'main'
           }
           steps {
               echo 'Verify app health'
               script {
                   sh 'sleep 5'
                   sh 'curl -f --retry 3 --retry-delay 2 http://localhost:1200/hello'
                   echo "✅ Health check passed!."
               }
           }
       }
    }

    post {
        always {
            echo "Pipeline finished."
        }
    }
}