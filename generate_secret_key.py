#!/usr/bin/env python
# Thanks to: https://github.com/kubernetes/minikube/issues/366#issuecomment-244617587

import re
import subprocess

def execute_cmd(cmd):
  proc = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
  comm = proc.communicate()

  if comm[1] != '':
    print(comm[1].rstrip('\n'))
    exit(-1)

  return comm[0]

def generate_secret_key():
  login_cmd = execute_cmd('aws --profile=emma-home  ecr get-login --no-include-email --region us-west-2').rstrip('\n')
  creds = re.sub(r"(-e none\ |docker login\ |-u\ |-p\ )", '', login_cmd).split(' ')
  generate_secret_cmd = "kubectl create secret docker-registry {0} --docker-username={1} --docker-password={2} --docker-server={3} --docker-email=emmanuelt2009@gmail.com"
  execute_cmd(generate_secret_cmd.format('aws', creds[0], creds[1], creds[2].replace('https://', '')))

if __name__ == "__main__":
  generate_secret_key()
