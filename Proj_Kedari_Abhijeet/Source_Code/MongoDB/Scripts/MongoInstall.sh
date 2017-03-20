clear
echo "Installing Java"
sudo apt-get update
sudo apt-get install openjdk-7-jdk
sudo apt-get install ant
sudo apt-get install -y mongodb-org-server mongodb-org-shell mongodb-org-tools
sudo rm /var/lib/dpkg/lock
echo "Installing MongoDb"
sudo apt-get install mongodb
sudo service mongod start
sudo apt-get install mongodb-clients
echo "Installing CouchDB"
sudo apt-get install couchdb
echo "Installing Redis"
sudo apt-get install -y python-software-properties
sudo add-apt-repository -y ppa:rwky/redis
sudo apt-get update
sudo apt-get install -y redis-server
sudo service redis-server restart

exit 1
