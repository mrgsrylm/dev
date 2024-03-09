app.build:
	go build

app.run:
	go run main.go

docker.image:
	docker build . -t task-manager:0.1

docker.container:
	docker run --rm -p 8080:8080 --env REDIS_HOST=host.docker.internal:6379 task-manager:0.1

task.get_all:
	curl -X GET "localhost:8080/task"

task.get:
	curl -X GET "localhost:8080/task/$(id)"

task.post:
	curl -X POST "localhost:8080/task/" -H "Content-Type: application/json" -d '{
		"id": "$(id)",
		"name": "A task",
		"description": "A task that need to be executed at the timestamp specified",
		"timestamp": 1645275972000
	}'

task.delete:
	curl -X DELETE "localhost:8080/task/$(id)"

