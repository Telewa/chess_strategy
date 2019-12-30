# create a deployment
deploy:
	kubectl create -f deployments/chess_strategy.yml
	kubectl create -f services/chess_strategy.yml
	minikube service frontend --url
	minikube service backend --url


restart_frontend:
	kubectl delete  service frontend
	kubectl delete  deployment frontend
	kubectl create -f deployments/chess_strategy.yml
	kubectl create -f services/chess_strategy.yml

service_status:
	kubectl get services

deployment_status:
	kubectl get deployments

get_url:
	minikube service frontend --url

clean:
	kubectl delete  deployment backend frontend
	kubectl delete  service backend frontend
