pipeline {
    agent any    
    stages {        
        stage('Build') {
            steps {                
                bat 'mvn clean install'                
            }
        }
		stage('Test') {
            steps {                
                httpRequest "http://localhost:8081/stat/all"    				
            }
        }
     }    
}