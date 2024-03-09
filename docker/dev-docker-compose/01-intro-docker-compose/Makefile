# Docker Engine will run a Docker NGINX image.
# The default port on the image is 80, so we shall map it locally to 8080 to avoid using a privileged port.
# The name we assign will be constant in order to make interactions with the container easier.
# By using the —rm argument, we ensure that once we are done with our task and stop the container, the container will be deleted.
run.nginx:
	docker run --rm -p 8080:80 --name nginx-compose nginx

run.compose:
	docker compose up

build.custom-nginx:
	docker build -t custom-nginx:0.1 .