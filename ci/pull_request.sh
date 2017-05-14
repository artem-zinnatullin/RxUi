#!/bin/bash
# TODO don't merge, enable back
#set -e

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/..

"$PROJECT_DIR"/gradlew -s clean build --no-daemon -p "$PROJECT_DIR"
cat "$PROJECT_DIR/rxui/build/reports/lint-results.xml"
