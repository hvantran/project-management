pipeline {
    agent any

    environment {
        PROJECT_VERSION_PREFIX='1.0.'
    }

    stages {
        stage('Checkout') {
            steps {
                git scm
            }
        }

        stage('Build') {
            steps {
                sh '''
                    mvn
                    ./ee-environment/source-git.sh clone --all
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