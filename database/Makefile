.PHONY: build run
all: build run

build:
	docker build . -t logreaderdb

run:
	docker run -p=3306:3306 logreaderdb

