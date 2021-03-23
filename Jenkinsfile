pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                sh '''
                    bash -x source-git.sh clone --all
                    bash -x source-git.sh pull --all
                '''
            }
        }

        stage('Build') {
            steps {
                sh '''
                    PROJECT_VERSION_PREFIX='1.0.'
                    PROJECT_VERSION=$PROJECT_VERSION_PREFIX$BUILD_NUMBER
                    mvn versions:set -DnewVersion=$PROJECT_VERSION
                    mvn clean install -DskipTests -DskipDocker
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh '''
                    mvn test -DskipDocker
                '''
            }
        }

        stage('Publish Artifacts') {
            steps {
                echo '''TODO'''
            }
        }

        stage('Automation') {
            steps {
                echo '''TODO'''
            }
        }

        stage('Deploy Production') {
            steps {
                echo '''TODO'''
            }
        }
    }
}