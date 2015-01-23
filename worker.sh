#! /bin/bash

#workerPort, adminPort, ioThreads, workerPool, connections, bufferSize, taskBuffer
java -cp target/tcp-nio-1.0-SNAPSHOT.jar cs.colostate.edu.tcp.Worker $1 $2 1 20 8 8192 20 
