Run docker compose:
docker-compose up

Enter container:
docker exec -it mariadb_garden sh

Login:
mysql -u root -p

Add user:
CREATE USER 'test'@'%' IDENTIFIED BY 'test';