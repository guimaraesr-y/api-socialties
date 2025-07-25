JAR_FILE := $(shell ls target/*.jar 2>/dev/null | head -n1)
IMAGE_NAME := socialties
IMAGE_TAG  := latest

.PHONY: all build package docker clean help

all: help

help:
	@echo "Use: make [target]"
	@echo ""
	@echo "Available targets:"
	@echo "  build    - Checks if JAR exists and shows its name"
	@echo "  package  - Executes 'mvn clean package' to generate the JAR"
	@echo "  docker   - Builds the Docker image (depends on build)"
	@echo "  help     - Shows this help (default)"
	@echo "  clean    - Cleans Maven artifacts and removes Docker image"

build:
ifeq ($(JAR_FILE),)
	@echo "No .jar file found in target/. Run 'make package' first."
	@exit 1
endif
	@echo "JAR found: $(notdir $(JAR_FILE))"

package:
	./mvnw clean package

docker: build
	docker build \
	  --build-arg JAR_FILE=$(notdir $(JAR_FILE)) \
	  -t $(IMAGE_NAME):$(IMAGE_TAG) .

clean:
	mvn clean
	docker rmi $(IMAGE_NAME):$(IMAGE_TAG) || true
