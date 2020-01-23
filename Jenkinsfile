pipeline {

    agent any
    tools {
        maven 'maven3' 
    }
    stages {
        stage('scm') {
				steps {
					checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: scm.userRemoteConfigs])
				}
			}
        stage('Compile stage') {
            steps {
                sh "mvn clean compile" 
        }
    }
/*
         stage('testing stage') {
             steps {
                sh "mvn test"
        }
    }

          stage('deployment stage') {
              steps {
                sh "mvn install"
                echo "FIN ...aaaoaaa"  
        }
    }
*/
  }

}
