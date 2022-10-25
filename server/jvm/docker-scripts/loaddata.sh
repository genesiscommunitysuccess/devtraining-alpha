#!/bin/bash
source /home/alpha/.bashrc
systemctl start postgresql-14
cd /home/alpha/run/site-specific/data
su -c "source /home/alpha/.bashrc ; cd /home/alpha/run/site-specific/data ; yes | JvmRun global.genesis.environment.scripts.SendTable -a" - "alpha"
echo "data loaded"