#!/bin/bash
set -e
echo Coverage totals for src/main/java classes using jacoco
echo ----------------------------------------------------------
mvn jacoco:prepare-agent test jacoco:report >/dev/null && cat target/site/jacoco/jacoco.xml|sed -e 's/\/>/\/>\n/g'|grep counter|tail -5
