#!/bin/bash

# Load env variables
if [ -f .env ]; then
    export $(cat .env)
fi

./target/lemon-app
