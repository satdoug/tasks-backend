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
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=537d521170f22544759024259da99f7ff32f5ef1 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**/RootController.java,**/TaskBackendApplication.java,**/ValidationException.java"
               }
            }
        }
         stage ('Quality Gate') {
            steps {
                sleep(5)
                timeout(time: 1, unit: 'MINUTES'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }
         stage ('Deploy Backend') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'Tomcat_login', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
         stage ('Api Test') {
             steps {
                 dir('api-test'){
                    git credentialsId: 'github_login', url: 'https://github.com/satdoug/tasks-api-test'
                    bat 'mvn test'
                 }
             }
         }
    }
}

