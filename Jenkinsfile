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
     IMAGE_NAME = 'moussbed/java-mvn:1.4'
   }

   stages{

       stage('init'){
          steps{
              script{
                gv = load "script.groovy"
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
                  buildImage "$IMAGE_NAME" // Come from jenkins-shared-library
                }
             }
       }

       stage('push image'){
             steps{
                script{
                   dockerLogin()
                   pushImage "$IMAGE_NAME"  // Come from jenkins-shared-library
                }
             }
       }

       stage('deploy'){
            steps{
               script{
                  gv.deployApp()
               }
            }
       }
   }

}
