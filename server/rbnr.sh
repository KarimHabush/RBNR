echo "Building..." &&
cd ~/Desktop/Web\ Services/rbnr/server &&
mvn clean install &&
echo "Deploying..." &&
cd ~/glassfish-4.1.1/glassfish/bin &&
./asadmin deploy --force ~/Desktop/Web\ Services/rbnr/server/target/RBNR-1.0-SNAPSHOT.war