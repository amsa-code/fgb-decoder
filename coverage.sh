#!/bin/bash
set -e
set -x
count=0
for f in target/files/*
do 
  echo $f 
  echo -------------------
  filename=${f:13}
  cp $f "src/test/resources/compliance-kit/$filename" 
  previous=$count
  count=`(mvn jacoco:prepare-agent test jacoco:report >/dev/null && cat  target/site/jacoco/jacoco.xml|sed -e 's/\/>/\/>\n/g'|grep counter|tail -5|sed -e 's/.*missed="\([0-9]*\)".*/\1/g'|awk '{sum+=$1}END{print sum}')`
  echo count = $count
  if (( previous >= count )); then
    echo removing $filename because coverage did not increase
    count=$previous
    rm "src/test/resources/compliance-kit/$filename" 
  fi
done
