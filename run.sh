mvn clean package
cp target/HealthCare.war ~/Downloads/apache-tomcat-10.1.46/webapps/

shutdown.sh
sleep 2
startup.sh