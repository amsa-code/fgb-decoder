#!/bin/bash
set -e
count=0
for f in /home/dxm/files/*
do 
  echo $f 
  echo -------------------
  filename=${f:16}
  dest="src/test/resources/compliance-kit/$filename"
  cp -p $f $dest 
  echo added $dest
  previous=$count
  count=`(mvn jacoco:prepare-agent test jacoco:report >/dev/null && cat  target/site/jacoco/jacoco.xml|sed -e 's/\/>/\/>\n/g'|grep counter|tail -5|sed -e 's/.*missed="\([0-9]*\)".*/\1/g'|awk '{sum+=$1}END{print sum}')`
  echo count = $count
  if (( previous >= count )); then
    echo removing $filename because coverage did not increase
    count=$previous
    rm $dest 
  fi
done
