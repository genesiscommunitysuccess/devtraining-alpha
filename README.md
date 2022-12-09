# Answer-key of the Developer Training

This is the complete application built during the [developer training](https://docs.genesis.global/secure/getting-started/developer-training/training-intro/) and can be used as a reference for it.

# Building, Running and Testing
From the command line, cd into the root directory of the project and then follow these steps.

## Build
```shell
./gradlew assemble
```

## Run
Make sure Docker is running on your machine and run:
```shell
docker-compose up -d
```

Now log into the `gsf` container:
```shell
docker exec -it gsf bash
```

And load the reference data (make sure answer Yes to the command prompt):
```shell
su - alpha
cd /home/alpha/run/site-specific/data
SendIt -a
SetPrimary
```

Check if all services are running:
```shell
mon
```

Feel free to keep running `mon` until all services are RUNNING.

## Test
Allow up to 5 mins for all the services to be up and running, then open your browser and navigate to http://localhost:6060

# License

This is free and unencumbered software released into the public domain.

For full terms, see [LICENSE](./LICENSE)

**NOTE** This project uses licensed components listed in the next section, thus licenses for those components are required during development.

## Licensed components
Genesis low-code platform
