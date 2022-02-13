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
def deployAppByDockerCompose(String tag){
    echo 'Deploying application ....'
    echo "${EC2_PUBLIC_IP}"
   // def dockerComposeCmd = "docker-compose -f docker-compose.yaml up --detach"
    def shellCmd= "bash ./server-cmds.sh ${tag}"
    def server = "ec2-user@${EC2_PUBLIC_IP}"
    def directory= "/home/ec2-user"
    sshagent(['ec2-server-key']) {
        sh  "scp -o StrictHostKeyChecking=no server-cmds.sh ${server}:${directory}"
        sh  "scp -o StrictHostKeyChecking=no docker-compose.yaml ${server}:${directory}"
        sh "ssh -o StrictHostKeyChecking=no ${server} ${shellCmd}"
    }

}
def deployAppByDockerCompose(String tag,String userImageRepository, String passwordImageRepository){
    echo 'Deploying application ....'
    echo "${EC2_PUBLIC_IP}"
    // def dockerComposeCmd = "docker-compose -f docker-compose.yaml up --detach"
    def shellCmd= "bash ./server-cmds.sh ${tag} ${userImageRepository} ${passwordImageRepository}"
    def server = "ec2-user@${EC2_PUBLIC_IP}"
    def directory= "/home/ec2-user"
    sshagent(['ec2-server-key']) {
        sh  "scp -o StrictHostKeyChecking=no server-cmds.sh ${server}:${directory}"
        sh  "scp -o StrictHostKeyChecking=no docker-compose.yaml ${server}:${directory}"
        sh "ssh -o StrictHostKeyChecking=no ${server} ${shellCmd}"
    }

}

def deployAppByKubernetes(){
    echo 'Deploying application ....'
    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f -'
}

def provisionEC2Instance(){
    echo 'Provision EC2 Instance ....'
    dir("terraform"){
        sh "terraform init"
        sh "terraform apply --auto-approve"
      EC2_PUBLIC_IP = sh(
              script: "terraform output ec2_public_ip",
              returnStdout: true
      ).trim()
    }
}

return  this
