pipeline {
    agent any

    environment {
        PROJECT_VERSION_PREFIX='1.0.'
    }

    stages {
        stage('Checkout') {
            steps {
                sh '''
                    echo "***************************************"
                    echo "**          CLONE SOURCE             **"
                    echo "***************************************"
                    ./source-git.sh clone --all

                    ./source-git.sh pull --all
                '''
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "***************************************"
                    echo "**          BUILD SOURCE             **"
                    echo "***************************************"
                    PROJECT_VERSION=$PROJECT_VERSION_PREFIX$BUILD_NUMBER
                    echo Current project version $PROJECT_VERSION

                    echo Settings version for all project $PROJECT_VERSION
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