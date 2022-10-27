Simulation of Markovian Queuing System

Description:  This program simulates a Markovian queueing system.  
The simulation generates arrival and departure events.
After 100000 departures of customers from system, the simulation stops.
The simulation outputs various peformance metrics for different values of rho.
The program also calculates theoretical values and outputs the result. 

Language - Java
Platform - UNIX
Compiler - OpenJDK version 11, OpenJDK runtime environment, OpenJDK 64-bit server VM

Files:
Event.java - Data Structure for representing event.
EventList.java - Data Structure for storing generated events.
ExponentialRV.java - Function which returns exponential random value.
TheoreticalCalculation.java - Program which calculates theoretical values for user provided input.
QueuingSimulation.java - Program which performs the simulation and outputs both simulated and theoretical values.
MainProgram.java - Program which calls functions and starts the simulation.
Graph-Plots.xlsx - File containing 4 graph plots.

Compiling Instructions - 
1. TYpe and run command "javac MainProgram.java".
2. This command compiles all java files and creates class files.
3. Now type and run command "java MainProgram".
4. This will start the program.