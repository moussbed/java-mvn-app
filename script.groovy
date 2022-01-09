/* def buildJar(){
    echo 'Building jar artefact ....'
    sh 'mvn package'
}

def buildImage(){
    echo 'Building docker image ....'
    sh  "docker build -t $IMAGE_NAME ."
}

def pushImage(){
    echo 'Pushing docker image to docker hub repository ....'
    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials',
            passwordVariable: 'PASS',
            usernameVariable: 'USER'
    )]){
        sh ('echo $PASS | docker login -u $USER --password-stdin ') // Use '' to prevent Interpolation of sensitive environment variables
        sh "docker push $IMAGE_NAME"
    }
}*/

def deployApp(){
    echo 'Deploying application  ....'
}

return  this
