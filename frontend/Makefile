build:
	eval $(minikube docker-env) && docker build -t chesser-frontend:1.0.0 .

run:
	docker run -p 8080:8080 chesser-frontend:1.0.0
