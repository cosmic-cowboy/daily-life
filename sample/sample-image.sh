#!/bin/sh

DIR=sample-image/

for FILENAME in `ls ${DIR}`
do
	domain='https://daily-life.herokuapp.com/'
	fileName=${DIR}${FILENAME}
 	curl ${domain}'/user/api/v1/entry/image' -H "Content-Type: application/json" -d '@'${fileName}

done