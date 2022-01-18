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
// We can move it to shared library
def deployAppByDocker(String image){
    echo 'Deploying application ....'
    def dockerCmd = "docker run -d -p 8080:8080 $image"
    sshagent(['ec2-server-key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.144.219.193 ${dockerCmd}"
    }
}
def deployAppByDockerCompose(){
    echo 'Deploying application ....'
    def dockerComposeCmd = "docker-compose -f docker-compose.yaml up --detach"
    sshagent(['ec2-server-key']) {
        sh 'scp docker-compose.yaml ec2-user@3.144.219.193:/home/ec2-user'
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.144.219.193 ${dockerComposeCmd}"
    }

}

return  this
