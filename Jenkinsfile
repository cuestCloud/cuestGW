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
                httpRequest httpMode: 'POST', requestBody: "{ num:1 }", url:"http://localhost:8081/invoke/someApp/automationExmpl"    				
            }
        }
     }    
}