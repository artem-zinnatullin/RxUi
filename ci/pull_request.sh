#!/bin/bash
set -e

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/..

"$PROJECT_DIR"/gradlew -s clean build --daemon -p "$PROJECT_DIR"