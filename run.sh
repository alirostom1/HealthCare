mvn clean package
cp target/heartline.war ~/Downloads/apache-tomcat-10.1.46/webapps/

shutdown.sh
sleep 2
startup.sh