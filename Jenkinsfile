pipeline {
    agent any
    tools {
        maven "MAVEN"
        jdk "JDK"
    }
    stages {
        stage('Initialize'){
            steps{               
            }
        }
        stage('Build') {
            steps {
                
                sh 'mvn clean install'
                
            }
        }
     }    
}