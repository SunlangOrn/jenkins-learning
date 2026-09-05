pipeline {
    agent any

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
                    sh "docker push ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                echo 'Starting Deployment...'
                // In a real scenario, we would use: sshagent(['ssh-credential-id']) { sh 'ssh user@server "bash deploy.sh"' }
                // For local learning, we execute the deployment script directly in the workspace:
                withCredentials([string(credentialsId: 'deploy-script', variable: 'DEPLOY_CMD')]) {
                    sh """
                        echo "Executing deployment commands..."

                        docker-compose down || true
                        docker-compose up -d

                        echo "Application deployed successfully!"
                    """
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