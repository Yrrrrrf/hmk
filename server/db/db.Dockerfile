FROM postgres:18-alpine

ENV LANG=en_US.utf8
ENV TZ=UTC

# 1. Install Bash (required for your init scripts)
RUN apk add --no-cache bash

# 2. Create directory for initialization files
RUN mkdir -p /init

# 3. Copy the folders (Setup, Tables, Seed)
COPY init/ /init/

# 4. Copy the orchestrator script
COPY init-db.sh /docker-entrypoint-initdb.d/init-db.sh
RUN chmod +x /docker-entrypoint-initdb.d/init-db.sh

EXPOSE 5432