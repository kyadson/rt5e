@echo off
cd ..
"C:\Program Files\Java\jdk1.6.0_17\bin\java.exe" -cp out/production/core;out/production/lobby;deps/jna.jar;deps/jython.jar;deps/mysql-connector-java-5.1.10-bin.jar;deps/netty-3.1.5.GA.jar;deps/xpp3_min-1.1.4c.jar;deps/xstream-1.3.1.jar org.lazaro.rt5e.WorldApp
pause