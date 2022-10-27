// program for running simulation and finding performance metrics
public class QueuingSimulation {
    
    double gamma;                                   // arrival rate of components from first machine
    double lambda;                                  // arrival rate of components from second machine
    double mu;                                      // service rate of worker to package the component

    int K;                                          // max components in the system
    int m;                                          // number of workers in the processing center
    int totalDepartures = 100000;                   // total number of departures of customers from system 

    double clock;                                   // System clock
    double prev;                                    // to store old clock value
    int N;                                          // number of customers in system
    int Ndep;                                       // Number of departures from system
    int servers;                                    // used to store number of servers used
    boolean done;                                   // used to check whether number of departures have crossed total departures
                                                    // after which the simulation will stop
    
    Event currentEvent, arrEvent, depEvent;         // store events during simulation
    ExponentialRV eRv = new ExponentialRV();        // initialise object which will gemerate exponential random numbers
    EventList eventList = new EventList();          // data structure to store all events

    double EN;                                      // for calculating E[N] i.e average number of customers in system
    int nEnter;                                     // for calculating total customers which entered the system
    double util;                                    // for calculating utilization of servers in system
    double arr;                                     // for calculating total arrivals to the system
    double block;                                   // for calculating arrival which were blocked
    int q;                                          // for storing current number of customers in queue

    // performance metrics
    double avgCustomers;                            // for storing average number of customers in system
    double avgDelay;                                // for storing average delay of customer in system
    double enterThroughput;                         // for storing throughput of system (Yenter)
    double departThroughput;                        // for storing throughput of system (Ydepart)
    double utilization;                             // for storing utilization of system
    double blockingProb;                            // for storing blocking probability of system

    // this function initilaizes the values provided from input
    public QueuingSimulation(double gamma, double mu, int K, int m) {
        this.gamma = gamma;
        this.mu = mu;
        this.K = K;
        this.m = m;
    }

    // this function performs the simulation
    private void processEvents() {

        // initialize values for all variables
        done = false;
        N = 0;
        q = 0;
        servers = 0;
        Ndep = 0;
        EN = 0.0;
        nEnter = 0;
        util = 0.0;
        arr = 0.0;
        block = 0.0;

        while (!done) {
            currentEvent = eventList.list.poll();   // get next event from event list
            prev = clock;                           // store old clock value
            clock = currentEvent.clock;             // update system clock

            util += (servers/m)*(clock - prev);     // update system utilization
            // if arrival from machine 1
            if (currentEvent.eventType == "ARR1") {
                EN += N*(clock-prev);               // update avg customer statistics

                // if customers are less than 2, only then event generated will be processed
                if (N < 2) {
                    N++;            // increase no of customer in system
                    q++;            // increase no of customer in queue
                    nEnter++;       // increment value of customer entering system
                    
                    // check if queue has space and server is available
                    if (q > 0 && servers < m) {
                        // generate departure event
                        depEvent = new Event("DEP", clock + eRv.generateRandomVariable(mu));
                        eventList.list.add(depEvent);       // add generated event to event list 
                        q--;                                // remove customer from queue
                        servers++;                          // assign server to process event
                    }

                } // if system is full, arriving customer is blocked
                else if (N >= K) {
                    block += 1.0;   // increment value of no of blocks
                }

                // if no of customers is less than 2, then generate arrival event from first machine
                if (N < 2) {
                    arrEvent = new Event("ARR1", clock + eRv.generateRandomVariable(gamma)); // generate arrival event
                    eventList.list.add(arrEvent);       // add generated event to event list
                    arr += 1.0;                         // increase value of number of arrivals
                }

            } // if arrival from machine 2
            else if (currentEvent.eventType == "ARR2") {
                EN += N*(clock-prev);       // update avg customer statistics

                // if system is not full, then we can process event
                if (N < K) {
                    N++;                // increase no of customer in system
                    q++;                // increase no of customer in queue
                    nEnter++;           // increment value of customer entering system

                    // check if queue has space and server is available
                    if (q > 0 && servers < m) {
                        // generate departure event
                        depEvent = new Event("DEP", clock + eRv.generateRandomVariable(mu));
                        eventList.list.add(depEvent);   // add generated event to event list 
                        q--;                            // remove customer from queue
                        servers++;                      // assign server to process event
                    }

                } // if system is full, arriving customer is blocked
                else if (N >= K) {
                    block += 1.0;   // increment value of no of blocks
                }

                // genenate arrival event from machine 2, this is always generated regardless of system state
                arrEvent = new Event("ARR2", clock + eRv.generateRandomVariable(lambda)); // generate arrival event
                eventList.list.add(arrEvent);           // add generated event to event list
                arr += 1.0;                             // increase value of number of arrivals

            } // departure event
            else if (currentEvent.eventType == "DEP") {
                
                EN += N*(clock-prev);                   // update avg customer statistics
                N--;                                    // decrease no of customer in system
                servers--;                              // decrease value as server becomes available

                // if no of customers in system becomes less than 2, then we have to again start arrival event from machine 1
                if (N < 2) {
                    // generate arrival event
                    arrEvent = new Event("ARR1", clock + eRv.generateRandomVariable(gamma));
                    eventList.list.add(arrEvent);       // add generated event to event list
                    arr += 1.0;                         // increase value of number of arrivals
                }
                Ndep++;                                 // increment value of number of departures

                // check if queue has space and server is available
                if (q > 0 && servers < m) {
                    // generate departure event
                    depEvent = new Event("DEP", clock + eRv.generateRandomVariable(mu));
                    eventList.list.add(depEvent);       // add generated event to event list 
                    q--;                                // remove customer from queue
                    servers++;                          // assign server to process event
                } 
            }

            // Loop end condition
            if (Ndep >= totalDepartures) {
                done = true;
            }

        }

        // calculating peformance metrics
        avgCustomers = EN/clock;                        // calculating average customers in system
        avgDelay = EN/nEnter;                           // calculating average delay for a customer
        enterThroughput = nEnter/clock;                 // calculating throughput of system
        departThroughput = Ndep/clock;                  // calculating throughput of system
        utilization = util/clock;                       // calculating total system utilization
        blockingProb = block/arr;                       // calculating blocking probability of customer
    }
 
    // this function is called from main program to run simulation
    public void runSimulation() {

        double rho; // rho value

        // for loop for changing values of rho
        for (rho = 0.1; rho <= 1.0; rho += 0.1) {
            rho = (double) Math.round(rho * 10) / 10; // rounding rho to one decimal place
            lambda = rho*m*mu; // calculating lambda value

            clock = 0.0; // initializing clock value
            Event event1 = new Event("ARR1", clock + eRv.generateRandomVariable(gamma));    // generate first arrival event from first machine
            Event event2 = new Event("ARR2", clock + eRv.generateRandomVariable(lambda));   // generate first arrival event from second machine

            eventList.list.add(event1);     // add event from machine 1 to event list
            eventList.list.add(event2);     // add event from machine 2 to event list

            processEvents(); // function whic starts simulation and procceses events

            // calling the prgram to calculate theoretical values for given parametres
            TheoreticalCalculation tCalculation = new TheoreticalCalculation(gamma, lambda, mu, K, m);
            tCalculation.performanceValues();

            System.out.println("*******************PERFORMANCE METRICS**********************");
            System.out.println("for rho  = "+rho+" Lambda - " + lambda);
            System.out.println("***************Description***************    /Simulation Value/    /Theoretical Value/");
            System.out.println("Average number of customers in system :      "+avgCustomers+"    "+tCalculation.avgCustomers);
            System.out.println("Average Delay for a customer :               "+avgDelay+"    "+tCalculation.avgDelay);
            System.out.println("Through of the system (Yenter) :             "+enterThroughput+"    "+tCalculation.enterThroughput);
            System.out.println("Through of the system (Ydepart) :            "+departThroughput+"    "+tCalculation.departThroughput);
            System.out.println("Total System Utilization :                   "+utilization+"    "+tCalculation.utilization);
            System.out.println("Blocking Probability :                       "+blockingProb+"    "+tCalculation.blockingProb);
            System.out.println();

        }

    }
}
