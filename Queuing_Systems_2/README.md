# Simulation of Queuing Network

Description:  This program simulates a Queuing Network.  
The simulation generates arrival and departure events.
After 500000 departures of customers from system, the simulation stops.
The simulation outputs various peformance metrics for different values of lambda.
The program also calculates theoretical values and outputs the result. 

Language - Java
Platform - UNIX
Compiler - OpenJDK version 11, OpenJDK runtime environment, OpenJDK 64-bit server VM

Files:
Event.java - Data Structure for representing event.
EventList.java - Data Structure for storing generated events.
ExponentialRV.java - Function which returns exponential random value.
TheoreticalCalculation.java - Program which calculates theoretical values for user provided input.
QueuingNetworkSimulation.java - Program which performs the simulation and outputs both simulated and theoretical values.
MainProgram.java - Program which calls functions and starts the simulation.
Graph_plots.xlsx - File containing 10 graph plots.

Compiling Instructions - 
1. Extract the folder 'pxn210006' from zip file.
2. Open the directory in the terminal/command line.
3. TYpe and run command "javac MainProgram.java".
4. This command compiles all java files and creates class files.
4. Now type and run command "java MainProgram".
5. This will start the program.