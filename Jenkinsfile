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
		    script{
		    env.STAGE = "scm checkout"
		    }
                    cleanWs()
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitHub-Ravi', url: 'https://github.com/Ravikiran338/k8s-cicd-user.git']]])
                          }
                    }
             stage ('build') {
                   steps {
			   script{
			   env.STAGE = "build stage"
			   }
                           sh label: '', script: '''cd ${WORKSPACE}
                                                    mvn clean install
                                                 '''
                         }
                    }
             stage ('deploy') {
                  steps {
		   script{
	             env.STAGE = "deploy"
		   }
                     sh label: '', script: '''
                          cd ${WORKSPACE}

                          docker build -t ${REPOSITORY_TAG} .
                          docker login
                          docker push ${REPOSITORY_TAG}

                          export KUBECONFIG=~/.kube/kube-config-eks
                          export PATH=$HOME/bin:$PATH
                          echo `kubectl get svc`
                          echo `kubectl get nodes`

                          envsubst < ${WORKSPACE}/${SERVICE_NAME}.yaml | kubectl apply -f -
						                    '''
                      }
                }
        stage ('testing scm checkout') {
                      steps {
                script{
                env.STAGE = "testing scm checkout"
                }
                       cleanWs()
                       checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitHub-Ravi', url: 'https://github.com/Ravikiran338/K8s-user-testscripts.git']]])
                             }
                       }

		   stage ('test automation execution') {
                   steps {
			   script{
			   env.STAGE = "test automation execution"
						  try{
                           sh label: '', script: '''
						                            cd ${WORKSPACE}
						                            chmod +x drivers/*
                                                    mvn clean install
                                                    exit 1
                                                 '''
												 currentBuild.result = 'SUCCESS'
												                      echo "Service Deployed Successfully"
							}
				                                 catch(Exception e){
					                             currentBuild.result = 'FAILURE'

					                             sh label: '', script: '''
					                                                   echo "Rolling back Deployment "
		                                                               export KUBECONFIG=~/.kube/kube-config-eks
		                                                               export PATH=$HOME/bin:$PATH
		                                                               kubectl rollout undo deployment.apps/userservice
                                                                       '''
				 }
				        echo "${currentBuild.result}"
								 }
						 }
				 }
		 }
    post
   {
     success
       {
         slackSend color: "good", message: "Hi , Microservices Build SUCCESS with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild("SUCCESS", env.STAGE)
         // Send notification to put-global-channel only for regression test build which uses release-xx.xx.xx.xx branch
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
