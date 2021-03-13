pipeline {
    agent any
    stages {
        stage ('Build Backend') {
            steps {
                bat 'mvn clean package -DskipTests=true'
            }
        }
        stage ('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }
         stage ('Sonar Analysis') {
           environment {
               scannerHome = tool 'SONAR_SCANNER'
           }  
           steps {
               withSonarQubeEnv('SONAR_LOCAL'){
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://127.0.0.1:9000 -Dsonar.login=d4de17ea8b4c368eaf83c53818a8193410645ca5 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**/RootController.java,**/TaskBackendApplication.java,**/ValidationException.java"
               }
            }
        }
    }
}

