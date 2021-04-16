#!/bin/bash

PROJECT_ARRAY=("parent-pom" "ee-environment" "deployment" "base-platform" "account-platform" "base-executor" "e-commerce" )
GREEN='\033[0;32m'
NC='\033[0m'


function pushAll() {
    for i in "${PROJECT_ARRAY[@]}"
    do
        cd "$i"
        git add .
        git commit -m "$1"
        git push
        cd ../
    done
}

function execAll() {
    for i in "${PROJECT_ARRAY[@]}"
    do
        cd "$i"
        $1
        cd ../
    done
}

function pullAll() {
    for i in "${PROJECT_ARRAY[@]}"
    do
        pullOne "$i"
    done
}

function pullOne() {
    REPOSITORY=$1
    echo -e "Pull repository $GREEN$REPOSITORY$NC"
    cd "$REPOSITORY"
    git pull
    cd ../
}

function statusAll() {
    for i in "${PROJECT_ARRAY[@]}"
    do
        statusOne "$i"
    done
}

function statusOne() {
    REPOSITORY=$1
    echo -e "Checking status repository $GREEN$REPOSITORY$NC"
    cd "$REPOSITORY"
    git status
    cd ../
}

function cloneAll() {
    for i in "${PROJECT_ARRAY[@]}"
    do
        cloneOne "$i"
    done
}

function cloneOne() {
    REPOSITORY=$1
    echo -e "Clone repository $GREEN$REPOSITORY$NC"
    git clone git@github.com:hvantran/"$REPOSITORY".git
}

function deploy() {
    cd deployment/target/classes
    docker compose -f "${1}".yml up -d
}

COMMAND=$1
OPTION=$2
REPO=$3

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

if [ "$OPTION" == "--all" ] && [ "$COMMAND" == "status" ]
then
    statusAll
    exit 0
fi

if [ "$OPTION" == "--one" ] && [ "$COMMAND" == "status" ]
then
    statusOne "$REPO"
    exit 0
fi

if [ "$OPTION" == "--all" ] && [ "$COMMAND" == "push" ]
then
    pushAll "$3"
    exit 0
fi

if [ "$COMMAND" == "exec" ]
then
    execAll "$2"
    exit 0
fi

if [ "$COMMAND" == "deploy" ]
then
    deploy "$2"
    exit 0
fi

echo "Only available commands:
      clone --all
      clone --one <repository>
      pull --all
      pull --one <repository>
      status --all
      status --one <repository>
      push --all <message>
      exec <command>
      deploy <product-name>"
exit 1