INSERT INTO `customer`(id, name, active) VALUES (1,'Big News Media Corp',true);
INSERT INTO `customer`(id, name, active) VALUES (2,'Online Mega Store',true);
INSERT INTO `customer`(id, name, active) VALUES (3,'Nachoroo Delivery',false);
INSERT INTO `customer`(id, name, active) VALUES (4,'Euro Telecom Group',true);
INSERT INTO `ip_blacklist` VALUES (1, '0.0.0.0'),(2, '2130706433'),(3, '4294967295');
INSERT INTO `ua_blacklist` VALUES (1, 'A6-Indexer'),(2, 'Googlebot-News'),(3, 'Googlebot');


INSERT INTO `hourly_stats`(id, customer_id, time, date, request_count, invalid_count) VALUES (1, 1, '10:12', '2020-05-08', 10, 11);
INSERT INTO `hourly_stats`(id, customer_id, time, date, request_count, invalid_count) VALUES (2, 2, '10:12', '2020-05-08', 10, 11);