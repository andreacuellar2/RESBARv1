FROM mysql:5.5

#Copiando el sql
COPY ResBar.sql docker-entrypoint-initdb.d/