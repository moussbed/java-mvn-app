#!/usr/bin/env groovy
// @Library('jenkins-shared-library')   We use it when we define shared library in UI on serve jenkins. In this case library is accessible globally
// How to scope library for some projects. This configuration is used
library  identifier: 'jenkins-shared-library@main' , retriever: modernSCM(
                      [$class: 'GitSCMSource',
                      remote: 'https://github.com/moussbed/jenkins-shared-library.git',
                      credentialsId: 'Github-Credential'
                      ]
                      )

def gv

pipeline{
   agent any

   tools{
     maven  'Maven-3.8.4'
   }

   environment{
   //  IMAGE_NAME = 'moussbed/java-mvn'
       REPO_SERVER= '637771966635.dkr.ecr.us-east-2.amazonaws.com'
       IMAGE_NAME = "${REPO_SERVER}/my-app"
   }

   stages{

       stage('init'){
          steps{
              script{
                gv = load "script.groovy"
              }
          }

       }
       stage('increment version'){
          steps{
            script{
               incrementVersion() // // Come from jenkins-shared-library
            }
          }
       }

       stage('build jar'){
           steps{
             script{
                 buildJar() // Come from jenkins-shared-library
             }
           }
       }
       stage('build image'){
             steps{
                script{
                  
                  buildImage "$IMAGE_NAME:$IMAGE_VERSION" // Come from jenkins-shared-library

                }
             }
       }

       stage('push image'){
              when {
                    expression{
                       BRANCH_NAME == 'main'
                    }
              }
             steps{
                script{
                   dockerLogin()
                   pushImage "$IMAGE_NAME:$IMAGE_VERSION"  // Come from jenkins-shared-library
                //  dockerLoginECR()
                //  pushImageECR "$IMAGE_NAME:$IMAGE_VERSION"
                }
             }
       }

       stage('provision server'){

           // Terraform provision server
            when {
                  expression{
                      BRANCH_NAME == 'main'
                }
            }
             environment{
                     AWS_ACCESS_KEY_ID = credentials('Jenkins_aws_access_key_id')
                     AWS_SECRET_ACCESS_KEY = credentials('Jenkins_aws_secret_access_key')
                     // Set variable values from CI/CD pipeline
                     TF_VAR_env_prefix= "prod"
             }
            steps{
                script{
                  gv.provisionEC2Instance()
                }
            }

       }

       stage('deploy'){
             when {
                   expression{
                       BRANCH_NAME == 'main'
                   }
             }
             environment{
                 AWS_ACCESS_KEY_ID = credentials('Jenkins_aws_access_key_id')
                 AWS_SECRET_ACCESS_KEY = credentials('Jenkins_aws_secret_access_key')
                 APP_NAME="java-maven-app"
             }
            steps{
               script{
                  // Give time to provision stage to setup instance and install tools(docker, docker compose)
                  // You can also execute "server provisioning" as the first stage
                  echo "Waiting for EC2 Server to initialize"
                  sleep(time:90, unit:"SECONDS")

                  // gv.deployAppByDocker("$IMAGE_NAME:$IMAGE_VERSION")
                  gv.deployAppByDockerCompose("$IMAGE_VERSION")
                  //gv.deployAppByKubernetes()
               }
            }
       }

       stage('commit version update'){
            when {
                  expression{
                      BRANCH_NAME == 'main'
                  }
            }
            steps{
                script{
                    commitVersionUpdate()
                }
            }
       }
   }

}
