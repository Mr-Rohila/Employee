pipeline {
    agent any
    
    environment {
        PORT = '8081' 
    }

    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    def existingProcess = sh(script: "lsof -i :${PORT} -t", returnStatus: true, scriptOnError: false) ?: bat(script: "netstat -ano | findstr :${PORT} | findstr LISTENING | awk '{print $5}'", returnStatus: true, scriptOnError: false)

                    if (existingProcess == 0) {
                        echo "Terminating existing process on port ${PORT}"
                        sh "kill -9 \$(lsof -i :${PORT} -t)"
                    }
                }
            }
        }

        stage('Run Application') {
            steps {
                script {
                    sh "nohup java -jar target/Employee.jar > /dev/null 2>&1 &"
                }
            }
        }
    }
}
