# create a deployment
create_deployment:
	kubectl create -f deployments/chess_strategy.yml

create_service:
	kubectl create -f services/chess_strategy.yml

service_status:
	kubectl get services

deployment_status:
	kubectl get deployments

get_url:
	minikube service chessstrategy --url