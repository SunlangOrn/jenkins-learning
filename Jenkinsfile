pipeline {
    agent any


    parameters {
        choice(
            name: 'ENVIRONMENT' ,
            choices: ['dev', 'prod'] ,
            description: 'target deployment enviroment'
        )
    }

    environment {
        // REPLACE with your actual Docker Hub username if different
        DOCKER_IMAGE = 'ornsunlang/jenkins-demo'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        SONARQUBE_ENV = "SonarQube"
    }

    stages {
        stage('checkout') {
            steps{
                echo "checking out code from ${env.BRANCH_NAME}"
            }
        }
        stage('Build and Test') {
            steps {
                echo "Compiling application..."
                sh 'chmod +x mvnw && ./mvnw clean test'
            }
            post{
                always { junit 'target/surefire-reports/*.xml' }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonaQube Analysis'
                withSonarQubeEnv(SONARQUBE_ENV) {
                    sh './mvnw sonar:sonar -Dsonar.projectKey=jenkins-demo -Dsonar.projectName=jenkins-demo'
                }
            }
        }

        stage('Quality Gate') {
            steps{
                echo 'Check SonarQube Quality Gate'
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build and Push') {
            when {
                branch 'main'
            }
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
                        sh """
                            export IMAGE_TAG=${env.BRANCH_NUMBER}

                            if command -v docker compose >/dev/null 2>&1; then
                                docker compose -f docker-compose.yml down || true
                                docker compose -f docker-compose.yml up -d
                            elif docker compose version >/dev/null 2>&1; then
                                docker compose -f docker-compose.yml down || true
                                docker compose -f docker-compose.yml up -d
                            else
                                exit 1
                            fi
                        """
                    } catch (Exception e) {
                        echo "Message: Deployment failed"
                        echo "Error : ${e.getMessage()}"
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
                echo 'Verifying app health'
                script {
                    sh 'sleep 10'
                    sh 'curl -f --retry 3 --retry-delay 2 http://localhost:1200/hello'
                    echo "Health check passed!"
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