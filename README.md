# URequest
Simple request processor service which is usable via REST API. 
* It's possible to send requests that need validation.
* The service validates, processes and generates statistics. 
* The statistics are available via HTTP GET.
* The new requests are sent to redis and the statistics incremented there;
* Asynchronously, a job reads data from redis and updates the statistics.

 ## How to Run Everything
 
 ### Requirements
 * Java 11;
 * Mysql;
 * Redis;
 
 ### Steps
 * On your terminal run: ```docker run --detach --name=mysql -p 52000:3306  --env="MYSQL_ROOT_PASSWORD=leomn138" mysql```;
 * Install Redis(https://redis.io/download) and run ```redis-server```;
 * If you'd like to initilise the db with some data, run ```./setup/setup.sh```;
 * Then, you can run the app.
 