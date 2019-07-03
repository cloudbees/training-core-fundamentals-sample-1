#!/usr/bin/env bash

echo "Deploying: $1"
# Cause approximately 1 out of 4 deployments to fail
if (( $RANDOM % 4 == 0 )); then
        echo "Unexpected failure to deploy"
        exit 1
fi
