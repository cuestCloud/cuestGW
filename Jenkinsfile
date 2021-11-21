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
                curl http://localhost:8081/stat/all                
            }
        }
     }    
}