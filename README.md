## SQL queries

MySQL query to find requests made by a given IP
```SQL
SELECT * FROM logs WHERE ip_address = '192.168.0.182';
```

MySQL query to find IPs that mode more than a certain number of requests for a given time period
```SQL
SELECT ip_address, count(*) AS request_count FROM logs
WHERE date > '2017-01-01.13:00:00' AND date < '2017-01-01.14:00:00'
GROUP BY ip_address
HAVING request_count > 100;
```

## Database init

Run `./make` inside database directory. `Schema.sql` gets automatically initialized. Requires Docker. 


## Usage

Execute `mvn clean package` to build fat jar.
Move and rename built package
```bash
mv target/parser-1.0-SNAPSHOT-jar-with-dependencies.jar parser.jar
```

To persist log file and find blocked IP addresses
```bash
java -cp "parser.jar" com.ef.Parser --accessfile=accesslog --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500
```

To only look for addresses in the database
```bash
java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500
```
