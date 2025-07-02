pipeline {
    agent any

    stages {
        stage('Detect Platform') {
            steps {
                script {
                    def branchName = env.BRANCH_NAME ?: 'unknown/unknown'
                    if (branchName.contains('fabric')) {
                        env.PLATFORM = 'fabric'
                    } else if (branchName.contains('neoforge')) {
                        env.PLATFORM = 'neoforge'
                    } else if (branchName.contains('forge')) {
                        env.PLATFORM = 'forge'
                    } else {
                        env.PLATFORM = 'unknown'
                    }
                    def mcVersion = branchName.split('/')[0]
                    echo "Building for platform: ${env.PLATFORM}"
                    echo "Minecraft version: ${mcVersion}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew clean build --no-daemon'
                    } else {
                        bat 'gradlew.bat clean build --no-daemon'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew test --no-daemon'
                    } else {
                        bat 'gradlew.bat test --no-daemon'
                    }
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
