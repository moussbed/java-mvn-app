#### Create cluster with eksctl
```bash
$ eksctl create cluster \                                                                                                                                   ░▒▓ ✔  system   19:41:39  
> --name demo-cluster \          
> --version 1.21 \               
> --region us-east-2 \           
> --nodegroup-name demo-nodes \  
> --node-type t2.micro \         
> --nodes 2 \                     
> --nodes-min 1 \                
> --nodes-max 3 \
> --profile admin 
```

#### Create the Secret

This secret is useful when we want to pull image from a private registry.
Why we don't create a secret yaml file? Because this secret will be available for all deployments.
It isn't necessary to apply it on the CI. We apply once in the command line  as follows

```bash
   $ kubectl create secret docker-registry my-registry-key \
   > --docker-server=docker.io \
   > --docker-username=xxxxx \ 
   > --docker-password=xxxxx
```

or 
```bash
  $ kubectl create secret docker-registry ecr-registry-key \                                                       ░▒▓ ✔  system   admin@demo-cluster.us-east-2.eksctl.io ⎈  17:15:37  
  > --docker-server=xxxxx.dkr.ecr.us-east-2.amazonaws.com \
  > --docker-username=AWS \   
  > --docker-password=$(aws ecr get-login-password --region us-east-2)
```



