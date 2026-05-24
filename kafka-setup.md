# Kafka Setup on Ubuntu/WSL

## Step 1: Install Java (if not already installed)
```bash
sudo apt update
sudo apt install default-jdk -y
java -version
```

## Step 2: Download and Extract Kafka
```bash
cd ~
wget https://downloads.apache.org/kafka/3.7.0/kafka_2.13-3.7.0.tgz
tar -xzf kafka_2.13-3.7.0.tgz
cd kafka_2.13-3.7.0
```

## Step 3: Start Zookeeper (Terminal 1)
```bash
cd ~/kafka_2.13-3.7.0
bin/zookeeper-server-start.sh config/zookeeper.properties
```
Keep this terminal running!

## Step 4: Start Kafka Broker (Terminal 2)
```bash
cd ~/kafka_2.13-3.7.0
bin/kafka-server-start.sh config/server.properties
```
Keep this terminal running!

## Step 5: Create the Topic (Terminal 3)
```bash
cd ~/kafka_2.13-3.7.0
bin/kafka-topics.sh --create --topic student-events --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

## Step 6: Verify Topic was Created
```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```
You should see: `student-events`

## Step 7 (Optional): Monitor Messages in Real-Time
```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic student-events --from-beginning
```

## Quick Start Commands (after first setup)
Every time you want to use Kafka, run these in order:
```bash
# Terminal 1: Start Zookeeper
cd ~/kafka_2.13-3.7.0 && bin/zookeeper-server-start.sh config/zookeeper.properties

# Terminal 2: Start Kafka
cd ~/kafka_2.13-3.7.0 && bin/kafka-server-start.sh config/server.properties

# Terminal 3 (optional): Monitor messages
cd ~/kafka_2.13-3.7.0 && bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic student-events --from-beginning
```

## Stopping Kafka
```bash
# Stop Kafka first, then Zookeeper
bin/kafka-server-stop.sh
bin/zookeeper-server-stop.sh
```
