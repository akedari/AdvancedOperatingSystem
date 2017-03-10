

1. Create server instance - 
2. On Local machine go to directory where Abhijeet.pem is saved
 
    ssh-add Abhijeet.pem
    chmod 400 Abhijeet.pem
    ssh -i Abhijeet.pem ubuntu@52.23.159.73

3. copy the MongoInstall.sh script (/home/abhijeet/workspace/Prog2_Kedari_Abhijeet/Redis) to newly created server instance (via FileZilla OR pscp 
   [pscp -h newHost.txt  -O StrictHostKeyChecking=no -r /home/abhijeet/workspace/Prog2_Kedari_Abhijeet/Redis/MongoInstall.sh /home/ubuntu/])
   (newHost.txt this file contains one ip of server instace we created)

   exceute the script on Server instance ("sh ./MongoInstall.sh") 

   This script will install all required software for this assignment, so you have to do this step only once.

4. copy the code from local machine (/home/abhijeet/workspace/Prog2_Kedari_Abhijeet/Redis) to newly created server instance
5. Create Image for this instance and create total 16 instances.
6. get the private ip addressess of all the instances in list.
6. For each instance update the config file with private IP Address from list created in 6th step
7. On you local machine create the config file with public IP Addressess of all the instances. This config file will be associated with Evaluation class (/home/abhijeet/workspace/
   Prog2_Kedari_Abhijeet/Redis/src/RedisTest.java)
   AFter creating config file copy it to all servers instances
   pscp -h host.txt -O StrictHostKeyChecking=no -r /home/abhijeet/workspace/Prog2_Kedari_Abhijeet/Redis/config.properties /home/ubuntu/Redis

   (host.txt this file contains ips of server instances we created)

8. type below command on local machine where (/home/abhijeet/workspace/Prog2_Kedari_Abhijeet/Redis/src/RedisTest.java)
   pssh -h host.txt -O StrictHostKeyChecking=no -o . 'sh ./runAnt.sh'

   if you want 2,4,8,16 concurrent nodes update host.txt before fire pssh command
   

