#!/bin/bash
#
#  Bash script to launch a war file in tomcat
#

# Find tomcat folder in ~/bin
TOMCAT_LOOKUP=$HOME/bin

# find tomcat path
TOMCAT=$(find $TOMCAT_LOOKUP/apache-tomcat* | head -n 1)

# kill last process
ps -ef | grep tomcat | sed "s/  / /g" | cut -f 2 -d " " | head -n 1 | xargs kill

# find war path
WAR=$(ls -1 target/*.war | head -n 1)
URL=$(echo $WAR | sed "s/^target\///")
URL=$(echo $URL | sed "s/-.*//")

# remove old war
rm $TOMCAT"/webapps/"$URL.war
rm -r $TOMCAT"/webapps/"$URL

# copy war file to webapps folder
cp $WAR $TOMCAT"/webapps/"$URL.war

# start tomcat
export JRE_HOME=$HOME/bin/$(ls $HOME/bin/ | grep "jdk1.8")/
cd $TOMCAT"/bin/"
bash startup.sh start

# show information
echo "running "$URL
echo "listening at: http://localhost:8080/"$URL

