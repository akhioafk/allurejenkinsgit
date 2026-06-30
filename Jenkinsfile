pipeline {
    agent any

    tools {
        maven 'maven 3.9'          
        jdk 'jdk 17'              
    }

    stages {
        stage('checkout git') {
            steps {
                checkout scm
            }
        }

        stage('build test') {
            steps {
                sh 'mvn clean test -Dtest=AllureTestExample'
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo 'Publishing Allure Report'
            }
            post {
                always {
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/allure-report/**', allowEmptyArchive: true
        }
    }
}