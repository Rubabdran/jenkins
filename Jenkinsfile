pipeline {
	agent any

	tools {
		maven 'M3'
		jdk 'JDK21'
	}

	stages {
		stage('Checkout') {
			steps {
				git branch: 'main',
				url: 'https://github.com/Rubabdran/jenkins'
			}
		}

		stage('Build') {
			steps {
				bat 'mvn clean compile'
			}
		}

		stage('Test') {
			steps {
				bat 'mvn test'
			}
			post {
				always {
					junit 'target/surefire-reports/*.xml'
				}
			}
		}

		stage('Package') {
			steps {
				bat 'mvn package -DskipTests'
			}
		}
	}

	post {
		always {
			cleanWs()
		}
		success {
			echo 'Pipeline completed successfully!'
		}
		failure {
			echo 'Pipeline failed!'
		}
	}
}