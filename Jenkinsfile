pipeline {
    agent any

    tools {
        jdk 'jdk 17'
        maven 'maven 3.9'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }


        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'allure-results/*.*', allowEmptyArchive: true
        }
    }
}