#!/bin/sh

DIR=sample-image/
COUNT=10
domain=https://daily-life.herokuapp.com/

cat sample-content.txt | while read line
do
	if [ ${#line} -eq 0 ]; then
		continue
	fi

	COUNT=$(( COUNT + 1 ))

	fileName=${DIR}${COUNT}.jpg
 	fileId=`curl ${domain}'user/api/v1/entry/image' -X POST --data-binary '@'${fileName} -H 'Content-Type: image/jpeg' |  jq '.["fileId"]'`;
 	echo ${fileId}
	message="{\"content\":\"${line}\",\"postDate\":\"201510${COUNT}\",\"fileId\":${fileId}}"
	curl ${domain}'user/api/v1/entry' -X POST -H "Content-Type: application/json" -d ${message}
done
