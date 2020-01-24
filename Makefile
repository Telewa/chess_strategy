# create a deployment
release:
	helm package helm_releases && mv helm_releases*.tgz charts/
