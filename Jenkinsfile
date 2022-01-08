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
                gv.buildJar()
             }
           }
       }
       stage('build image'){
             steps{
                script{
                  gv.buildImage()
                }
             }
       }

       stage('push image to docker hub repository'){
             steps{
                script{
                   gv.pushImage()
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
