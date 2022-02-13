##   EC2
#### Create Security group 
Note : --profile admin at the end of command doesn't mandatory. I use it because I have admin IAM user 
```bash 
$ aws ec2 create-security-group --group-name my-sg --description "My SG description" --vpc-id vpc-fb2bfb90 --profile admin
```
Result of this command it use to create EC2. In this case it return sg-05a3c1591fa230e3f.
If you need more information about this security group you can type this command
```bash
 $ aws ec2 describe-security-groups --group-ids sg-05a3c1591fa230e3f --profile admin
```
We need to configure a firewall rule to access to EC2 instance through security group on port 22

```bash
$ aws ec2 authorize-security-group-ingress \
 > --group-id sg-05a3c1591fa230e3f \ 
 > --protocol tcp \
 > --port 22 \
 > --cidr 102.162.116.237/32 \
 > --profile admin
```

#### Create Key Pair
This key pair is useful when we want to login without using a password.
Usually we create this key pair on the user's machine|server who|which need to connect on another machine.
AWS creates it for us we just need to download private key and put on our machine or server in the directory ~/.ssh  
```bash
$ aws ec2 create-key-pair \
> --key-name my-kp-cli \
> --query 'KeyMaterial' \
> --output text > my-kp-cli.pem \
> --profile admin
```

#### List the subnet to use it to create EC2
```bash
$ aws ec2 describe-subnets --profile admin
```

#### Create EC2 Instance
```bash 
$ aws ec2 run-instances \
>  --image-id ami-001089eb624938d9f \
>  --count 1 \
>  --instance-type t2.micro \
>  --key-name my-kp-cli \
>  --security-group-ids sg-05a3c1591fa230e3f \
>  --subnet-id subnet-5f744525 \
>  --profile admin
```

#### List EC2 instances
```bash
$  aws ec2 describe-instances --profile admin
```

#### Connect to your EC2 instance 
Before connect you need to change permissions of the private key 
```bash
$ chmod 400 ~/.ssh/my-key.pem
```
After 
```bash
$ ssh -i ~/.ssh/my-key.pem ec2-user@public-ip-address
```

#### Filters and Query
```bash
 $ aws ec2 describe-instances \
> --filters "Name=instance-type,Values=t2.micro" \
> --query "Reservations[].Instances[].InstanceId" \
> --profile admin
```

```bash
 $ aws ec2 describe-instances \
> --filters "Name=image-id,Values=ami-001089eb624938d9f,ami-066157edddaec5e49" \
> --query "Reservations[].Instances[].InstanceId" \
> --profile admin
```

## IAM

#### Create group

```bash
$ aws iam create-group \
> --group-name my-group-cli \
> --profile admin 
```

#### Describe group
```bash
$ aws iam get-group --group-name  my-group-cli --profile admin
```

#### Create user 
```bash
$ aws iam create-user --user-name bedrilCli --profile admin
```

#### Add user to group

```bash
$ aws iam add-user-to-group --user-name bedrilCli --group-name my-group-cli --profile admin 
```

#### Attach group policy
```bash
$ aws iam attach-group-policy \
> --group-name my-group-cli \
> --policy-arn arn:aws:iam::aws:policy/AmazonEC2FullAccess \
> --profile admin
```
#### List attached group policies 
```bash
$ aws iam list-attached-group-policies --group-name my-group-cli --profile admin
```
####  Create credentials for new user
To login on your aws account you two aws access type
Markup : 
<details>
<summary>Programmatic access</summary>
<p>
Enables an access key ID and secret access key for the AWS API,CLI, SDK and other development tools
</p>
</details>

```bash
$ aws iam create-access-key --user-name bedrilCli --profile admin

```

<details>
<summary>AWS Management Console access</summary>
<p>
Enables a password that allows users to sign-in to AWS Management Console
</p>
</details>

```bash
$ aws iam create-login-profile \
> --user-name bedrilCli \
> --password mypwd@2020 \
> --password-reset-required \
> --profile admin 
```
Login to AWS Management with this user. You should be change your password. If it isn't possible 
because you don't change password policy create and attach it to a group users.

```bash
$ aws iam create-policy \
> --policy-name changePwd \ 
> --policy-document file://changePwdPolicy.json \
> --profile admin
```
Where changePwdPolicy.json content is :
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "iam:ChangePassword"
            ],
            "Resource": [
                "arn:aws:iam::*:user/${aws:username}"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "iam:GetAccountPasswordPolicy"
            ],
            "Resource": "*"
        }
    ]
}
```
We find those policies rules on the EC2 dashboard section policies in the aws management console. Attach now 

```bash
$ aws iam attach-group-policy \
> --group-name my-group-cli \
> --policy-arn arn:aws:iam::637771966635:policy/changePwd \
> --profile admin
```
Where arn:aws:iam::637771966635:policy/changePwd is the result of previous command which create policy

### DELETE RESOURCES

```bash
$ aws ec2 delete-key-pair --key-name my-kp-cli --profile admin # delete key pair
$ aws ec2 delete-security-group --group-name my-sg --profile admin # delete security group
$ aws ec2 terminate-instances --instance-ids i-0115ab4b62e28a5d4  # terminate instance

````

#### How to see operation of a command 
```bash
$ aws ec2 aa
$ aws iam aa
```

It is difficult to do it manually. We can automate it by Terraform. We have example on this repository
https://github.com/moussbed/terraform

In production or staging, it's necessary to commit terraform.tfvars because ops define it. It's why we add default 
value in the Terraform config file.

If we want to destroy our environment we must do it from CI/CD tool. Because in the local machine, 
we don't have the current  state of our infrastructure.
In jenkins for example pick the build number, replay the pipeline and modify 
```groovy
  sh "terraform apply --auto-approve"
      EC2_PUBLIC_IP = sh(
              script: "terraform output ec2_public_ip",
              returnStdout: true
      ).trim()
```

by 
```groovy
   sh "terraform destroy --auto-approve"
```
and remove stage deploy