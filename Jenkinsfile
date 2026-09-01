pipeline {

    any agent

    stages {
        stage('checkout'){
            steps {
                echo 'checkout code from github...'
            }
        }

        stage('Build and Test'){
            steps {
                echo 'Running Maven Wrapper...'
                sh 'chmod +x mvnw && ./mvnw clean package'
            }
        }

        stage('Archive'){
            steps{
                echo 'Archive the JAR file ...'
                archiveArtifacts artifacets: 'target/*.jar',
                allowEmptyArchive: false
            }
        }
    }
}