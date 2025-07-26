JAR_FILE := $(shell ls target/*.jar 2>/dev/null | head -n1 | xargs basename)
IMAGE_NAME := socialties
IMAGE_TAG  := latest

export IMAGE_NAME
export IMAGE_TAG
export JAR_FILE

.PHONY: all build package docker clean help

all: help

help:
	@echo "Use: make [target]"
	@echo ""
	@echo "Available targets:"
	@echo "  build    - Checks if JAR exists and shows its name"
	@echo "  package  - Executes 'mvn clean package' to generate the JAR"
	@echo "  docker   - Builds the Docker image (depends on build)"
	@echo "  compose-up       - Starts services defined in docker-compose.yml"
	@echo "  compose-down     - Stops and removes services and networks"
	@echo "  compose-logs     - Streams logs from all services"
	@echo "  compose-restart  - Recreates containers for all services"
	@echo "  help     - Shows this help (default)"
	@echo "  clean    - Cleans Maven artifacts and removes Docker image"

build:
ifeq ($(JAR_FILE),)
	@echo "No .jar file found in target/. Run 'make package' first."
	@exit 1
endif
	@echo "JAR found: $(notdir $(JAR_FILE))"

package:
	@mvnw clean package

docker: build
	docker build \
	  --build-arg JAR_FILE=$(notdir $(JAR_FILE)) \
	  -t $(IMAGE_NAME):$(IMAGE_TAG) .

compose-up:
	docker-compose up -d --build

compose-down:
	docker-compose down

compose-logs:
	docker-compose logs -f

compose-restart: compose-down compose-up

clean:
	@mvnw clean
	docker rmi $(IMAGE_NAME):$(IMAGE_TAG) || true
