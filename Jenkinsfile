#!/usr/bin/env groovy
@Library('jenkins-shared-library')
def gv

pipeline{
   agent any

   tools{
     maven  'Maven-3.8.4'
   }

   environment{
     IMAGE_NAME = 'moussbed/java-mvn:1.1'
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
                  buildImage() // Come from jenkins-shared-library
                }
             }
       }

       stage('push image to docker hub repository'){
             steps{
                script{
                   pushImage()  // Come from jenkins-shared-library
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
