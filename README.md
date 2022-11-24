# alpha

Answer-key of the Developer Training. This is the complete application built during the training and can be used as a reference for the training.

# Building, Running and Testing
From the command line, cd into the root directory of the project and then follow these steps.

## Build
Run:
1. ./gradlew assemble

## Run
Make sure Docker is running on your machine and run:
1. docker-compose up -d

Now log into the `gsf` container:
2. docker exec -it gsf bash

and load the reference data:
3. su - alpha
4. cd /home/alpha/run/site-specific/data
5. SendIt -a
(make sure answer Yes to the command prompt)
6. SetPrimary
(make sure answer Yes to the command prompt)

check if all services are running:
7. mon
Feel free to keep running `mon` until all services are RUNNING.

## Test
Allow up to 5 mins for all the services to be up and running, then open your browser and navigate to http://localhost:6060

# License

This is free and unencumbered software released into the public domain.

For full terms, see [LICENSE](./LICENSE)

**NOTE** This project uses licensed components listed in the next section, thus licenses for those components are required during development.

## Licensed components
Genesis low-code platform
