#!/usr/bin/env bash

git clean -xfd
mvn -B -DskipTests clean package
