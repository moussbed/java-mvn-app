#!/usr/bin/env groovy
@Library('jenkins-shared-library')
def gv

pipeline{
   agent any

   tools{
     maven  'Maven-3.8.4'
   }

   environment{
     IMAGE_NAME = 'moussbed/java-mvn:1.2'
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

       stage('push image to docker hub repository'){
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
