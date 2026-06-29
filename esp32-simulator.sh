#!/bin/bash

# esp32 simulator. v1.0
DEFAULT_HOST="localhost"
DEFAULT_PORT=1883
DEFAULT_TOPIC="esp32/dht/data"
DEFAULT_ITERATIONS=20
DEFAULT_INTERVAL=5
CORRUPT_EVERY=10

HOST="${1:-$DEFAULT_HOST}"
PORT="${2:-$DEFAULT_PORT}"
TOPIC="${3:-$DEFAULT_TOPIC}"
ITERATIONS="${4:-$DEFAULT_ITERATIONS}"
INTERVAL="${5:-$DEFAULT_INTERVAL}"

generateRandomNumberBetween() {
  awk -v min="$1" -v max="$2" 'BEGIN{srand(); printf "%d", min + rand() * (max-min)}'
}

generateData() {
    TEMP=$(generateRandomNumberBetween 18 35)
    HUM=$(generateRandomNumberBetween 30 90)
    echo "{\"temperature\":$TEMP,\"humidity\":$HUM}"
}

generateCorruptData() {
    CORRUPT_TYPES=(
        "{\"temperature\":null,\"humidity\":-1000}"
        "{\"temperature\":-1000,\"humidity\":null}"
    )
    INDEX=$((RANDOM % ${#CORRUPT_TYPES[@]}))
    echo "${CORRUPT_TYPES[$INDEX]}"
}

echo -e "Broker:      $HOST:$PORT"
echo -e "Topic:       $TOPIC"
echo -e "Iterations:  $ITERATIONS"
echo -e "Interval:    $INTERVAL"

for ((i = 1; i <= ITERATIONS; i++)); do
    if (( i % CORRUPT_EVERY == 0 )); then
        PAYLOAD=$(generateCorruptData)
        TYPE="[CORRUPT]"
    else
        PAYLOAD=$(generateData)
        TYPE="[OK]"
    fi

    mosquitto_pub -h "$HOST" -p "$PORT" -t "$TOPIC" -m "$PAYLOAD"
    STATUS=$?

    TIMESTAMP=$(date '+%H:%M')
    if [[ $STATUS -eq 0 ]]; then
        echo -e "[$TIMESTAMP] $TYPE $PAYLOAD"
    else
        echo -e "[$TIMESTAMP] [ERROR] Unable to connect to broker"
        exit 1
    fi

    if (( i < ITERATIONS )); then
        sleep $INTERVAL
    fi
done
