@Library('pipeline-shared-library') _
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
                          docker push ${REPOSITORY_TAG}
                          
                          export KUBECONFIG=~/.kube/kube-config-eks 
                          export PATH=$HOME/bin:$PATH
                          echo `kubectl get svc`
                          echo `kubectl get nodes`
                          
                          envsubst < ${WORKSPACE}/${SERVICE_NAME}.yaml | kubectl apply -f - '''
						  script{
                      
							  def status = sh(returnStatus: true, script: "python app.py")
							  sh """
								if [ "${status}" == "0" ]; then
								   echo "deployed service is running successfully"
								else
								   echo "service status chack failed, please check, "
								   echo "rolling back deployment "
								   export KUBECONFIG=~/.kube/kube-config-eks
								   export PATH=$HOME/bin:$PATH
								   kubectl rollout undo deployment.apps/userservice
								fi 
								 """
		         post
   {
     success
       {
         slackSend color: "good", message: "Hi , Microservices Build SUCCESS with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild("SUCCESS", env.STAGE)
       }
     failure
       {
         slackSend color: "danger", message: "Hi Microservices Build FAILED with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild('FAILED', env.STAGE)
       }
     aborted
       {
         slackSend color: "danger", message: "Hi , Microservices Build ABORTED with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild('ABORTED', env.STAGE)
       }
     unstable
       {
         slackSend color: "warning", message: "Hi , Microservices Build FAILED with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild('UNSTABLE', env.STAGE)
       }
   }
                                }                                               
                        }
                    }
                }
            }
