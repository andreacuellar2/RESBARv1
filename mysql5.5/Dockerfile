FROM mysql:5.5

#Variables de entorno
ENV MYSQL_ROOT_PASSWORD 1234
ENV MYSQL_USER resbar
ENV MYSQL_PASSWORD Restaurante2018
ENV MYSQL_DATABASE resbar

#Copiando el sql
COPY ResBar.sql /docker-entrypoint-initdb.d/