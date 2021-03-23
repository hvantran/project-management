#!/bin/bash

PROJECT_ARRAY=("deployment" "parent-pom" "base-platform" "account-platform" "e-commerce" "base-executor")

function pullAll() {
    for i in "${PROJECT_ARRAY[@]}"
    do
        pullOne $i
    done
}

function pullOne() {
    REPOSITORY=$1
    cd "$REPOSITORY"
    git pull
    cd -
}

function cloneAll() {
    for i in "${PROJECT_ARRAY[@]}"
    do
        cloneOne $i
    done
}

function cloneOne() {
    REPOSITORY=$1
    git clone https://github.com/hvantran/"${REPOSITORY}".git
}

COMMAND=$1
OPTION=$2
REPO=$3
FULL_COMMAND=""

if [ "$COMMAND" == "clone" ] || [ "$COMMAND" == "pull" ]
then
    FULL_COMMAND=${FULL_COMMAND}${COMMAND}
else
    echo "Available commands: clone/pull --all/--one <repository> "
    exit 1
fi

if [ "$OPTION" == "--all" ] && [ "$COMMAND" == "clone" ]
then
    cloneAll
    exit 0
fi

if [ "$OPTION" == "--all" ] && [ "$COMMAND" == "pull" ]
then
    pullAll
    exit 0
fi

if [ "$OPTION" == "--one" ] && [ "$COMMAND" == "pull" ]
then
    pullOne "$REPO"
    exit 0
fi

if [ "$OPTION" == "--one" ] && [ "$COMMAND" == "clone" ]
then
    cloneOne "$REPO"
    exit 0
fi