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
    echo 'Deploying application ....'
    def dockerCmd = 'docker run -d -p 3000:80 mmoussbed/react-node:1.0'
    sshagent(['ec2-server-key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.144.219.193 ${dockerCmd}"
    }
}

return  this
