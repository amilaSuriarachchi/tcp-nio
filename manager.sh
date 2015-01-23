#! /bin/bash
#worker workerPort adminPort numberOfMsgs workers clientBuffer
java -cp target/tcp-nio-1.0-SNAPSHOT.jar cs.colostate.edu.tcp.Manager localhost 3000 4000 100000000 10 200 
