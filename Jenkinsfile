pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                sh '''
                    ./source-git.sh clone --all
                    ./source-git.sh pull --all
                '''
            }
        }

        stage('Build') {
            steps {
                sh '''
                    PROJECT_VERSION_PREFIX='1.0.'
                    PROJECT_VERSION=$PROJECT_VERSION_PREFIX$BUILD_NUMBER
                    mvn versions:set -DnewVersion=$PROJECT_VERSION
                    mvn clean install -DskipTests
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh '''
                    mvn test
                '''
            }
        }

        stage('Publish Artifacts') {
            steps {
                sh '''
                    docker login -u  tranvanhoa2810 -p $DOCKER_HUB_PASS
                    mvn deploy -Dmaven.install.skip=true -DskipTests
                '''
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
    post {
        always {
            sh '''
                 mvn versions:revert
            '''
        }
    }
}