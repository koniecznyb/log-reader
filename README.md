## SQL queries

SELECT * FROM logs WHERE ip_address = '192.168.0.182';

SELECT ip_address, count(*) AS request_count FROM logs
WHERE date > '2017-01-01.13:00:00' AND date < '2017-01-01.14:00:00'
GROUP BY ip_address
HAVING request_count > 100;

## Database init

Run `./make` inside database directory. `Schema.sql` gets automatically initialized. Requires Docker. 