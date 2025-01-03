# Getting Started

## How to Run the app 

### install wscat
```
yarn add wscat
```

and then

```shell
  wscat -c ws://localhost:8080/echo  
```

## Running Kafka Publisher
```shell
  docker exec -it kafka-server kafka-console-producer.sh --bootstrap-server localhost:9092 --topic stock-quotes
```

## Running Kafka Consumer
```shell
  docker exec -it kafka-server kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic stock-quotes --from-beginning
```

## Message payload
``` text
{"symbol":"AAPL","price":175.50,"volume":15000,"timestamp":"2023-10-28T10:30:00Z"}
```