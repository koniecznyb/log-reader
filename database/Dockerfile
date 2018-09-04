FROM mysql:5.7.15

MAINTAINER me

ENV MYSQL_ROOT_PASSWORD=password \
    MYSQL_DATABASE=logreader \
    MYSQL_USER=user \
    MYSQL_PASSWORD=password

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306