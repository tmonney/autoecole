#!/bin/bash

set -e

# Build deployable artifacts for all projects
sbt stage

# -v wants an absolute path
WORKING_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

docker rm -f $(docker ps -a -q)

echo -n "Database container:"
docker run -d --name=db \
			  --hostname=db.autoecole.ch \
			  autoecole/postgresql

echo -n "API container:"
docker run -d --name=api \
			  --hostname=api.autoecole.ch \
			  --link db:db \
			  --publish=8080:8080 \
			  --volume=$WORKING_DIR/api/target/universal/stage/:/usr/share/autoecole-api \
			  autoecole/api

echo -n "Web container:"
docker run -d --name=web \
			  --hostname=www.autoecole.ch \
			  --link api:api \
			  --publish=80:80 \
			  --volume=$WORKING_DIR/www/target/universal/stage/:/var/www/autoecole.ch \
			  autoecole/nginx