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
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=04ffa93374c393630cf99589c7058a988fde4567 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**/RootController.java,**/TaskBackendApplication.java,**/ValidationException.java"
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
         stage ('Deploy Frontend') {
             steps {
                dir('frontend') {
                    git credentialsId: 'github_login', url: 'https://github.com/satdoug/tasks-frontend'
                    bat 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'Tomcat_login', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks', war: 'target/tasks.war'     
                } 
             }
         }
         stage ('Functional Test') {
             steps {
                 dir('functional-test'){
                    git credentialsId: 'github_login', url: 'https://github.com/satdoug/tasks-functional-test'
                    bat 'mvn test'
                 }
             }
         }
         stage ('Deploy Prod') {
             steps {
                 bat 'docker-compose build'
                 bat 'docker-compose up -d'
             }
         }
         stage ('Health Check') {
             steps {
                 sleep(5)
                 dir('functional-test'){
                    bat 'mvn verify -Dskip.sufire.tests'
                 }
             }
         }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
            archiveArtifacts artifacts: 'target/*.war, frontend/target/*.war', onlyIfSuccessful: true
        }
    }
}

