provider "aws" {
  region = var.region
}
# Useful when we want to use remote state to avoid that each environment having its current state
terraform {
  required_version = ">= 0.12"
  backend "s3" {
     bucket = "remote-state-terraform-bucket"
     key = "myapp/state.tfstate"
    region = "us-east-2"
  }
}
# VPC (Virtual Private Cloud)
resource "aws_vpc" "myapp-vpc" {
  cidr_block = var.vpc_cidr_block
  tags = {
    Name : "${var.env_prefix}-vpc"
  }
}

module "myapp-subnet" {
  source = "./modules/subnet"
  # Passing data to subnet module
  vpc_id= aws_vpc.myapp-vpc.id
  default_route_table_id= aws_vpc.myapp-vpc.default_route_table_id
  subnet_cidr_block= var.subnet_cidr_block
  availability_zone= var.availability_zone
  env_prefix= var.env_prefix
}

module "myapp-webserver" {
  source = "./modules/webserver"
  # Passing data to subnet module
  vpc_id= aws_vpc.myapp-vpc.id
  my_ip= var.my_ip
  env_prefix= var.env_prefix
  instance_type=var.instance_type
  subnet_id=module.myapp-subnet.subnet.id
  availability_zone= var.availability_zone
  jenkins_ip=var.jenkins_ip
}