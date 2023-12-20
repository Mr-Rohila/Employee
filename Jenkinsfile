pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool 'Maven'
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
                bat "copy /Y target\\Employee.jar G:\\HRMS API\\Employee"
            }
        }

        stage('Run Employee Service') {
            steps {            
                bat "nssm start Employee"
            }
        }
    }
}