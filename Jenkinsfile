pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Unit Tests') {
            steps {
                sh 'mvn test -Punit-tests'
            }
        }
        stage('Integration Tests') {
            steps {
                sh 'mvn verify -Pintegration-tests'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn clean verify sonar:sonar \
                        -Dsonar.scanner.forceAnalysis=true \
                        -Dsonar.projectKey=social_media_plateform_springboot \
                        -Dsonar.projectName='social_media_plateform_springboot' \
                        -Dsonar.java.binaries=target/classes
                    """                }
            }
        }
    }
}