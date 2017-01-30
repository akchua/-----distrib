# Shutdown running application
# Manual Mode - "jps" command to get the running PID, use the PID to run the command "kill -15 ${PID}"
# kill -15 option is gracefully shutdown, kill -9 option is instant kill 

# Export PRIMEPAD_HOME to be used for installation
export PRIMEPAD_HOME=/usr/local/primepad

# Package is in gunzip - .gz
# Hard code for now
gunzip distributions-1.3.12-deploy.tar.gz

# After gunzip, package is in tar - .tar
# Gunzip will auto delete the gz file
# Hard code for now
tar -xvf distributions-1.3.12-deploy.tar

# Delete the tar file
rm -rf distributions-1.3.12-deploy.tar

# Delete the currently used properties, sh and bin
rm -rf $PRIMEPAD_HOME/bin
rm -rf $PRIMEPAD_HOME/config
rm -rf $PRIMEPAD_HOME/sh

# Move the new properties, sh and bin
mv bin $PRIMEPAD_HOME
mv config $PRIMEPAD_HOME
mv sh $PRIMEPAD_HOME

# Start the process
cd $PRIMEPAD_HOME/sh
nohup ./start.sh