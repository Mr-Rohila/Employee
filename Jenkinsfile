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

        stage('Stop Service') {
            steps {
                bat "nssm stop Employee"
            }
        }

 		stage('Copy Jar File') {
            steps {
                bat "copy /Y target\Employee.jar D:\Temp"
            }
        }

        stage('Run Employee Service') {
            steps {            
                bat "nssm start Employee"
            }
        }
    }
}