
pipeline{
   agent any

   environment {
      ANSIBLE_SERVER= "167.99.136.123"
   }

   stages{
        stage ("Copy files to the ansible server"){
            steps{
                script{
                     echo "Copy all necessary files to ansible control node"
                     sshagent(['ansible-server-key-pair']){
                        sh 'scp -o StrictHostKeyChecking=no ansible/* root@${ANSIBLE_SERVER}:/root'

                        // Copy the key pair to ec2 instances to ansible control node
                        withCredentials([sshUserPrivateKey(credentialsId: 'ec2-server-pair-key', keyFileVariable: 'keyfile', usernameVariable: 'user')]){
                            sh 'scp $keyfile root@$ANSIBLE_SERVER:/root/my_key' // The same directory which is defined to ansible config (ansible.cfg)
                        }
                     }

                }

            }
        }

     // Before install ssh pipeline steps
        stage("Execute ansible playbook"){
            steps{
                script {
                   echo "Calling ansible playbook to configure ec2 instances"
                   def remote = [:]
                   remote.name = "ansible-node-control"
                   remote.host = ANSIBLE_SERVER
                   remote.allowAnyHosts = true
                   withCredentials([sshUserPrivateKey(credentialsId: 'ansible-server-key-pair', keyFileVariable: 'keyfile', usernameVariable: 'user')]){
                      remote.user = user
                      remote.identityFile = keyfile
                      sshScript remote: remote, script: "ansible/prepare-ansible-server.sh" // It's useful if tools aren't already installed
                      sshCommand remote: remote, command: "ansible-playbook deploy-docker.yaml"
                   }

                }
            }
        }
   }

}