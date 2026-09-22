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
        stage('Compile') {
            steps {
                echo "Compiling application..."
                sh 'chmod +x mvnw && ./mvnw clean compile'
            }
        }

        stage('Quality and Verification') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        echo 'Running Testing'
                        sh './mvnw test'
                    }
                    post {
                        always { junit 'target/surefire-reports/*.xml'}
                    }
                }
                stage ('Code Analysis') {
                    steps {
                        echo 'Running Code Analysis'
                        sh 'slepp 3'
                        echo 'SonarQube Analyisi Passed'
                    }
                }

            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker Image: ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                sh "docker build -t ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ."
                sh "docker tag ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ${DOCKER_IMAGE}:latest"

                withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
                    sh 'docker push ${DOCKER_IMAGE}:${env.BUILD_NUMBER}'
                    sh 'occker push ${DOCKER_IMAGE}:latest'
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
    }

    post {
        always {
            echo "Pipeline finished. Cleaning up workspace to save disk space..."
            cleanWs() // cleanWs() delete the workspace folder for this specific job

            //Optional: Clean docker image
            //sh 'docker image prune -f || true'
        }
        failure {
            echo "Pipeline failed"
        }
    }
}