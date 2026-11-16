#!/bin/bash

#DIR=/cfs/home/u000062/git/oop
#DIR=/home/david/git/oop
DIR=/home/david/git/oop/leic-a-po24/prj/000
DIR=`pwd`/..
PRJ=hva
UILIB=/usr/share/java
CLASSPATH=${DIR}/${PRJ}-core/${PRJ}-core.jar:${DIR}/${PRJ}-app/${PRJ}-app.jar:${UILIB}/po-uilib.jar

for x in tests/$1/*.in; do
    if [ -e ${x%.in}.import ]; then
        java -cp "${CLASSPATH}" -Dimport=${x%.in}.import -Din=$x -Dout=${x%.in}.outhyp ${PRJ}.app.App;
    else
        java -cp "${CLASSPATH}" -Din=$x -Dout=${x%.in}.outhyp ${PRJ}.app.App;
    fi

    diff -cB -w ${x%.in}.out ${x%.in}.outhyp > ${x%.in}.diff ;
    if [ -s ${x%.in}.diff ]; then
        echo "FAIL: $x. See file ${x%.in}.diff " ;
    else
        echo -n "."
        #rm -f ${x%.in}.diff ${x%.in}.outhyp ; 
    fi
done

#rm -f saved*

echo "Done."

