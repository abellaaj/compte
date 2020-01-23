pipeline {

    agent any
    tools {
        maven 'maven3' 
    }
    stages {
        stage('scm') {
				steps {
					checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '260f9667-6d49-4e80-9593-2c4312be0b53', url: 'https://github.com/abellaaj/git-test.git']]])
				}
			}
        stage('Compile stage') {
            steps {
                sh "mvn clean install" 
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
