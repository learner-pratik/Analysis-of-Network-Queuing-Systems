// Program which performs the simulation and outputs both simulated and theoretical values.

public class QueuingNetworkSimulation {
    
    // constants for the queuing network
    double lambda;
    double pH;
    double pL;
    double r2d;
    double r21;
    double r22;
    double mu1;
    double mu2H;
    double mu2L;

    int totalCustomers = 500000;
    int departedCustomers;                          // customers departing from system
    double clock;                                   // clock value
    ExponentialRV eRv = new ExponentialRV();        // initialise object which will gemerate exponential random numbers
    EventList eventList = new EventList();          // initialise event list to store events

    int customersQ1High;                            // current high priority customers present in queue 1
    int customersQ1Low;                             // current low priority customers present in queue 1
    int customersQ2High;                            // current high priority customers present in queue 2
    int customersQ2Low;                             // current low priority customers present in queue 1

    int custEnterQ1High;                            // high priority customers that entered queue 1
    int custEnterQ1Low;                             // low priority customers that entered queue 1
    int custEnterQ2High;                            // high priority customers that entered queue 2
    int custEnterQ2Low;                             // low priority customers that entered queue 2

    double throughputQ1High;                        //throughput of high priority customers from queue 1
    double throughputQ1Low;                         //throughput of low priority customers from queue 1
    double throughputQ2High;                        //throughput of high priority customers from queue 2
    double throughputQ2Low;                         //throughput of low priority customers from queue 2

    double expectedCustQ1High;                      // expected high priority customers in queue 1
    double expectedCustQ1Low;                       // expected low priority customers in queue 1
    double expectedCustQ2High;                      // expected high priority customers in queue 2
    double expectedCustQ2Low;                       // expected low priority customers in queue 2

    double avgTimeQ1High;                           // average time of high priority customer in queue 1
    double avgTimeQ1Low;                            // average time of low priority customer in queue 1
    double avgTimeQ2High;                           // average time of high priority customer in queue 2
    double avgTimeQ2Low;                            // average time of low priority customer in queue 2

    double ECustQ1High;                             // for finding expected high priority customers in queue 1
    double ECustQ1Low;                              // for finding expected low priority customers in queue 1
    double ECustQ2High;                             // for finding expected high priority customers in queue 2
    double ECustQ2Low;                              // for finding expected low priority customers in queue 2

    private void startSimulation() {

        double checkPriority; // to find customer is high or low priority
        double lowPriorityCustomerDepartProb; // to calculate probability of departing low priority customer
        String typeOfEvent; 
        double prev; // to store previous clock value

        clock = 0.0;
        departedCustomers = 0;

        // generating the first arrival event
        Event newEvent = new Event("Arr", clock+eRv.generateRandomVariable(lambda));
        eventList.list.add(newEvent);

        Event currentEvent; // to store event

        // initializing variables
        customersQ1High = 0;
        customersQ1Low = 0;
        customersQ2High = 0;
        customersQ2Low = 0;

        ECustQ1High = 0;
        ECustQ1Low = 0;
        ECustQ2High = 0;
        ECustQ2Low = 0;

        custEnterQ1High = 0;
        custEnterQ1Low = 0;
        custEnterQ2High = 0;
        custEnterQ2Low = 0;

        // starting the simulation
        while (departedCustomers != totalCustomers) {

            // initializing the event constants
            currentEvent = eventList.list.poll();
            prev = clock;
            clock = currentEvent.clock;
            typeOfEvent = currentEvent.eventType;

            // metrics for calculating expected customers
            ECustQ1High += customersQ1High*(clock-prev);
            ECustQ1Low += customersQ1Low*(clock-prev);
            ECustQ2High += customersQ2High*(clock-prev);
            ECustQ2Low += customersQ2Low*(clock - prev);

            // if arrival event
            if (typeOfEvent == "Arr") {

                // finding priority of customer
                checkPriority = eRv.uniformRandomVariable();
                if (checkPriority <= pH) {
                    customersQ1High++;
                    custEnterQ1High++;
                } else {
                    customersQ1Low++;
                    custEnterQ1Low++;
                }

                // generating new arrival event
                newEvent = new Event("Arr", clock+eRv.generateRandomVariable(lambda));
                eventList.list.add(newEvent);

                // if first customer in queue 1, then create departure event
                if (customersQ1High + customersQ1Low == 1) {

                    if (customersQ1High == 1) {
                        newEvent = new Event("DepQ1High", clock+eRv.generateRandomVariable(mu1));
                        eventList.list.add(newEvent);
                    } else if (customersQ1Low == 1) {
                        newEvent = new Event("DepQ1Low", clock+eRv.generateRandomVariable(mu1));
                        eventList.list.add(newEvent);
                    }

                }

            // if departure event of high priority customer from queue 1
            } else if (typeOfEvent == "DepQ1High") {

                // increment customers of queue 2
                customersQ2High++;
                custEnterQ2High++;

                // if first customer in queue 2, then create departure event
                if (customersQ2High == 1 && customersQ2Low == 0) {
                    newEvent = new Event("DepQ2High", clock+eRv.generateRandomVariable(mu2H));
                    eventList.list.add(newEvent);
                }

                // generate departure event for queue 1
                customersQ1High--;
                if (customersQ1High > 0) {
                    newEvent = new Event("DepQ1High", clock+eRv.generateRandomVariable(mu1));
                    eventList.list.add(newEvent);
                } else if (customersQ1Low > 0) {
                    newEvent = new Event("DepQ1Low", clock+eRv.generateRandomVariable(mu1));
                    eventList.list.add(newEvent);
                }

            // if departure event of low priority customer from queue 1
            } else if (typeOfEvent == "DepQ1Low") {

                // increment customers of queue 2
                customersQ2Low++;
                custEnterQ2Low++;

                // if first customer in queue 2, then create departure event
                if (customersQ2High == 0 && customersQ2Low == 1) {
                    newEvent = new Event("DepQ2Low", clock+eRv.generateRandomVariable(mu2L));
                    eventList.list.add(newEvent);
                }

                // generate departure event for queue 1
                customersQ1Low--;
                if (customersQ1High > 0) {
                    newEvent = new Event("DepQ1High", clock+eRv.generateRandomVariable(mu1));
                    eventList.list.add(newEvent);
                } else if (customersQ1Low > 0) {
                    newEvent = new Event("DepQ1Low", clock+eRv.generateRandomVariable(mu1));
                    eventList.list.add(newEvent);
                }

                // if departure event of high priority customer from queue 2
            } else if (typeOfEvent == "DepQ2High") {

                // generate departure event for queue 2
                customersQ2High--;
                if (customersQ2High > 0) {
                    newEvent = new Event("DepQ2High", clock+eRv.generateRandomVariable(mu2H));
                    eventList.list.add(newEvent);
                } else if (customersQ2Low > 0) {
                    newEvent = new Event("DepQ2Low", clock+eRv.generateRandomVariable(mu2L));
                    eventList.list.add(newEvent);
                }

                // customer departed
                departedCustomers++;

            // if departure event of low priority customer from queue 2
            } else if (typeOfEvent == "DepQ2Low") {

                // generate departure event for queue 2
                customersQ2Low--;
                if (customersQ2High > 0) {
                    newEvent = new Event("DepQ2High", clock+eRv.generateRandomVariable(mu2H));
                    eventList.list.add(newEvent);
                } else if (customersQ2Low > 0) {
                    newEvent = new Event("DepQ2Low", clock+eRv.generateRandomVariable(mu2L));
                    eventList.list.add(newEvent);
                }

                // to find where the low priority customer will go
                lowPriorityCustomerDepartProb = eRv.uniformRandomVariable();

                // low priority customer departs
                if (lowPriorityCustomerDepartProb <= r2d) {
                    departedCustomers++;

                // low priority customer goes to queue 1
                } else if (lowPriorityCustomerDepartProb <= r2d+r21) {
                    
                    // increment customers of queue 1
                    customersQ1Low++;
                    custEnterQ1Low++;

                    // if first customer in queue 1, then create departure event
                    if (customersQ1High == 0 && customersQ1Low == 1) {
                        newEvent = new Event("DepQ1Low", clock+eRv.generateRandomVariable(mu1));
                        eventList.list.add(newEvent);
                    }

                // low priority customer goes to queue 2
                } else if (lowPriorityCustomerDepartProb <= 1) {

                    // increment customers of queue 2
                    customersQ2Low++;
                    custEnterQ2Low++;

                    // if first customer in queue 2, then create departure event
                    if (customersQ2High == 0 && customersQ2Low == 1) {
                        newEvent = new Event("DepQ2Low", clock+eRv.generateRandomVariable(mu2L));
                        eventList.list.add(newEvent);
                    }
                }
            }
        }

        // calculating expected customers in each queue for each priority
        expectedCustQ1High = ECustQ1High/clock;
        expectedCustQ1Low = ECustQ1Low/clock;
        expectedCustQ2High = ECustQ2High/clock;
        expectedCustQ2Low = ECustQ2Low/clock;

        // calculating avg time customer spent in each queue for each priority
        avgTimeQ1High = ECustQ1High/custEnterQ1High;
        avgTimeQ1Low = ECustQ1Low/custEnterQ1Low;
        avgTimeQ2High = ECustQ2High/custEnterQ2High;
        avgTimeQ2Low = ECustQ2Low/custEnterQ2Low;

        // calculating throughput of customers in each queue for each priority
        throughputQ1High = custEnterQ1High/clock;
        throughputQ1Low = custEnterQ1Low/clock;
        throughputQ2High = custEnterQ2High/clock;
        throughputQ2Low = custEnterQ2Low/clock;

        // removing events from list
        eventList.list.clear();
    }

    // this function is called from main program to run simulation
    public void startProgram(double pH, double pL, double r2d, double r21, double r22, double mu1, double mu2H, double mu2L) {

        this.pH = pH;
        this.pL = pL;
        this.r2d = r2d;
        this.r21 = r21;
        this.r22 = r22;
        this.mu1 = mu1;
        this.mu2H = mu2H;
        this.mu2L = mu2L;

        // initializing for performing theoretical calculation
        TheoreticalCalculation tc = new TheoreticalCalculation(pH, pL, r2d, r21, r22, mu1, mu2H, mu2L);

        // for loop for changing values of lambda
        for (int i = 1; i <= 10; i++) {

            lambda = i;
            startSimulation(); // function which starts simulation and procceses events
            tc.calculate(lambda); // function which performs theoretical calculation

            System.out.println("**********************************PERFORMANCE METRICS*****************************************");
            System.out.println("For Lambda - " + lambda);
            System.out.println("*******************Description*********************    /Simulation Value/    /Theoretical Value/");
            System.out.println("Expected number of High Priority Customers in Queue 1 - "+expectedCustQ1High + "    " + tc.eN1H);
            System.out.println("Expected number of Low Priority Customers in Queue 1 -  "+expectedCustQ1Low + "    " + tc.eN1L);
            System.out.println("Expected number of High Priority Customers in Queue 2 - "+expectedCustQ2High + "    " + tc.eN2H);
            System.out.println("Expected number of Low Priority Customers in Queue 2 -  "+expectedCustQ2Low + "    " + tc.eN2L);
            System.out.println("*********************");
            System.out.println("Average Time High priority customer spends in Queue 2 - "+avgTimeQ2High + "    " + tc.time2H);
            System.out.println("Average Time Low priority customer spends in Queue 2 -  "+avgTimeQ2Low + "    " + tc.time2L);
            System.out.println("*********************");
            System.out.println("Throughput of High priority customer in Queue 1 -       "+throughputQ1High + "    " + tc.theta1H);
            System.out.println("Throughput of Low priority customer in Queue 1 -        "+throughputQ1Low + "    " + tc.theta1L);
            System.out.println("Throughput of High priority customer in Queue 2 -       "+throughputQ2High + "    " + tc.theta2H);
            System.out.println("Throughput of Low priority customer in Queue 2 -        "+throughputQ2Low + "    " + tc.theta2L);
            System.out.println("***********************");
            System.out.println();
        }

    }
}
