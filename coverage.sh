#!/bin/bash
(mvn jacoco:prepare-agent test jacoco:report >/dev/null 2>&1) && cat  target/site/jacoco/jacoco.xml|sed -e 's/\/>/\/>\n/g'|grep counter|tail -5|sed -e 's/.*missed="\([0-9]*\)".*/\1/g'|awk '{sum+=$1}END{print sum}'

