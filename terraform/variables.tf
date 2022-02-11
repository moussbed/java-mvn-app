# Variables
variable "region" {
   default = "us-east-2"
}
variable "vpc_cidr_block" {
  default = "10.0.0.0/16"
}
variable "subnet_cidr_block" {
  default = "10.0.10.0/24"
}
variable "availability_zone" {
  default = "us-east-2a"
}
variable "env_prefix" {
  default = "test"
}
variable "my_ip" {
  default = "102.162.70.105/32"
}
variable jenkins_ip {
  default = "206.189.58.254/32"
}
variable "instance_type" {
  default = "t2.micro"
}
