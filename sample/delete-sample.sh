#!/bin/sh

domain=https://daily-life.herokuapp.com/



entryIds=`curl ${domain}'user/api/v1/entry' -X GET -H "Content-Type: application/json" | jq  -r '.entryList[].entryId'`

for entryId in ${entryIds}; do
	echo ${entryId}
	curl ${domain}'user/api/v1/entry?entryId='${entryId} -X DELETE -H 'Content-Type: application/json'
done
