Run docker compose:
docker-compose up docker-compose-with-volume

Enter container:
docker exec -it mariadb_garden sh

Login:
mysql -u root -p

Add user:
CREATE USER 'test'@'%' IDENTIFIED BY 'test';

select * from test.planters left join test.planter_measurement on test.planter_measurement.planter_id = test.planters.id;