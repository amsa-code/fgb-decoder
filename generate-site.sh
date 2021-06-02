#!/bin/bash
set -e
PROJECT=fgb-decoder
mvn site
cd ../amsa-code.github.io
git pull
mkdir -p $PROJECT
cp -r ../$PROJECT/target/site/* $PROJECT/
git add .
git commit -am "update site reports"
git push
