pipeline{
   agent any

   tools{
     maven  'Maven-3.8.4'
   }

   environments{
     IMAGE_NAME = 'moussbed/java-mvn:1.1'
   }

   stages{

       stage('build jar'){
           steps{
              echo 'Building jar artefact ....'
              sh 'mvn package'
           }
       }
       stage('build image'){
             steps{
                echo 'Building docker image ....'
                sh  "docker build -t $IMAGE_NAME ."
             }
       }

       stage('push image to docker hub repository'){
             steps{
                echo 'Pushing docker image to docker hub repository ....'
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials',
                  passwordVariable: 'PASS',
                  usernameVariable: 'USER'
                 )]){
                   sh "echo $PASS | docker login -u $USER --password-stdin "
                   sh "docker push $IMAGE_NAME"
                }

             }
       }

       stage('deploy'){
            steps{
               echo 'Deploying application ....'
            }
       }
   }

}
