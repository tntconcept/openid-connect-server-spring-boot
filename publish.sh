#!/bin/bash 

./gradlew uploadArchives -i -s
RETVAL=$?

if [ $RETVAL -eq 0 ]; then
    echo 'Artifacts published to Sonatype'
else
    echo 'Sonatype publish failed.'
    return 1
fi