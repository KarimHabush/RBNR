echo "Building..." &&
cd ~/NetBeansProjects/RBNR &&
mvn clean install &&
echo "Deploying..." &&
cd ~/glassfish-4.1.1/glassfish/bin &&
./asadmin deploy --force ~/NetBeansProjects/RBNR/target/RBNR-1.0-SNAPSHOT.war