pipeline {
	agent any
	tools { 
        maven 'Maven'  
          }
	environment {
     DOCKERHUB_USERNAME = "ravi338"
     APP_NAME = "msa-banking-aws"
     SERVICE_NAME = "user"
     REPOSITORY_TAG="${DOCKERHUB_USERNAME}/${APP_NAME}-${SERVICE_NAME}:${BUILD_ID}"
	 }
	stages {
	         stage ('scm checkout') {
			       steps {
				    cleanWs()
					checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitHub-Ravi', url: 'https://github.com/Ravikiran338/k8s-cicd.git']]])
	                      }
                    }						  
			 stage ('build') {
			       steps {
				           sh label: '', script: '''cd ${WORKSPACE}
                                                    mvn clean install
			                                     '''
					     }
					}					
			 stage ('deploy') {
			      steps {
				     sh label: '', script: '''
                                              cd ${WORKSPACE}					 
					      #docker rmi -f ${SERVICE_NAME}
					      docker build -t ${REPOSITORY_TAG} .
					      docker login
                                              #docker tag user ${REPOSITORY_TAG}
                                              docker push ${REPOSITORY_TAG}
					      export KUBECONFIG=~/.kube/kube-config-eks	
					      export PATH=$HOME/bin:$PATH
					      echo `kubectl get svc`
					      echo `kubectl get nodes`
											  envsubst < ${WORKSPACE}/${SERVICE_NAME}.yaml | kubectl apply -f -
										   '''										   
						}
					}
				}
			}
