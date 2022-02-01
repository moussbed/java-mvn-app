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
     IMAGE_NAME = 'moussbed/java-mvn'

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
                  // gv.deployAppByDocker("$IMAGE_NAME:$IMAGE_VERSION")
                  // gv.deployAppByDockerCompose("$IMAGE_VERSION")
                  gv.deployAppByKubernetes()
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
