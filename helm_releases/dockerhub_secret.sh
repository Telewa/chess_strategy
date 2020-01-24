#!/usr/bin/env bash
kubectl create secret docker-registry regcred --docker-server=index.docker.io --docker-username=emmanueltelewa --docker-password=u6BjHS273M48bazXMUfe --docker-email=emmanuelt2009@gmail.com

# inspect the secret
# kubectl get secret regcred --output=yaml
# kubectl get secret regcred --output="jsonpath={.data.\.dockerconfigjson}" | base64 --decode