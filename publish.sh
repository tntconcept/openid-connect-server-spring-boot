#!/bin/bash 

./gradlew uploadArchives -i -s
RETVAL=$?

if [ $RETVAL -eq 0 ]; then
    echo 'Artifacts published'
else
    echo 'Publish failed.'
    return 1
fi