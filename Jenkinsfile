pipeline {
    agent any
    
    stages {
        stage('Gradle Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew clean build'
                    } else {
                        bat 'gradlew.bat clean build'
                    }
                }
            }
        }
    }
}
