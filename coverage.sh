#!/bin/bash
set -e
count=`(mvn jacoco:prepare-agent test jacoco:report >/dev/null && cat  target/site/jacoco/jacoco.xml|sed -e 's/\/>/\/>\n/g'|grep counter|tail -5|sed -e 's/.*missed="\([0-9]*\)".*/\1/g'|awk '{sum+=$1}END{print sum}')`
for f in /home/dxm/files/*
do 
  echo ------------------------------------------
  filename=${f:16}
  dest="src/test/resources/compliance-kit/$filename"
  cp -p $f $dest 
  echo added $dest
  previous=$count
  count=`(mvn jacoco:prepare-agent test jacoco:report >/dev/null && cat  target/site/jacoco/jacoco.xml|sed -e 's/\/>/\/>\n/g'|grep counter|tail -5|sed -e 's/.*missed="\([0-9]*\)".*/\1/g'|awk '{sum+=$1}END{print sum}')`
  echo count = $count
  if (( previous <= count )); then
    echo removing $filename because missed coverage did not reduce 
    count=$previous
    rm $dest 
  fi
done
