#!/bin/bash
set -e
count=`(mvn jacoco:prepare-agent test jacoco:report >/dev/null && cat  target/site/jacoco/jacoco.xml|sed -e 's/\/>/\/>\n/g'|grep counter|tail -5|sed -e 's/.*missed="\([0-9]*\)".*/\1/g'|awk '{sum+=$1}END{print sum}')`
counter=0
for f in $HOME/files/*;
do 
  ((counter=counter+1))
  if (( counter > 0)); then
  echo ------------------------------------------
  filename=${f:16}
  dest="src/test/resources/compliance-kit/$filename"
  if [ ! -f "$dest" ]; then
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
  fi
  fi
done
