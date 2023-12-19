pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool 'Maven'
        JAVA_HOME = tool 'Java'
        PORT = '8081'
    }

    stages {
        stage('Build') {
            steps {
                bat "\"${MAVEN_HOME}\\bin\\mvn\" clean package"
            }
        }

        stage('Prepare Environment') {
            steps {
                script {
                    def existingProcess = bat(script: "netstat -ano | findstr :${PORT} | findstr LISTENING", returnStatus: true) ?: 1

                    if (existingProcess == 0) {
                        echo "Terminating existing process on port ${PORT}"
                        bat "taskkill /F /PID \$(netstat -ano | findstr :${PORT} | findstr LISTENING | awk '{print \$5}')"
                        bat 'timeout /t 2 /nobreak > nul'
                    }
                }
            }
        }

        stage('Run Application') {
            steps {            
                bat "start /B \"${JAVA_HOME}\\bin\\java\" -jar target\\Employee.jar"
            }
        }
    }
}