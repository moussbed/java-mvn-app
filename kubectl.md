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



