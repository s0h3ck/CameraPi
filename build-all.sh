alias mvn="JAVA_HOME=~/bin/jdk1.8.0_101/ ~/bin/apache-maven-3.3.9/bin/mvn"
cd libdetection
mvn -Dmaven.test.failure.ignore=true clean install
cd ..
cd libcom
mvn -Dmaven.test.failure.ignore=true clean install
cd ..
cd libdb
mvn -Dmaven.test.failure.ignore=true clean install
cd ..
cd libservo/libservo
mvn -Dmaven.test.failure.ignore=true clean install
cd ../..
cd client
mvn -Dmaven.test.failure.ignore=true clean package
cd ..
cd server
mvn -Dmaven.test.failure.ignore=true clean package
cd ..
