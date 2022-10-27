// program for calculating performace metrics using Continuous Time Markov Chain
public class TheoreticalCalculation {

    private double gamma;                       // arrival rate of components from first machine
    private double lambda;                      // arrival rate of components from second machine
    private double mu;                          // service rate of worker to package the component

    private int K;                              // max components in the system
    private int m;                              // number of workers in the processing center

    // performance values
    public double avgCustomers;                        // for storing average number of customers in system
    public double avgDelay;                            // for storing average delay of customer in system
    public double enterThroughput;                     // for storing throughput of system (Yenter)
    public double departThroughput;                    // for storing throughput of system (Ydepart)
    public double utilization;                         // for storing utilization of system
    public double blockingProb;                        // for storing blocking probability of system

    private double lambdaArray[];               // stores all arrival rate values (lambda0 to lambdaK)
    private double muArray[];                   // stores all service rate values (mu1 to muK)
    private double steadyStateProbArray[];      // stores all steady state probability values (p0 to pK)
    private double arrivalRateArray[];          // stores all arrival rate values
    
    // this function initializes all variables with user input
    public TheoreticalCalculation(double gamma, double lambda, double mu, int K, int m) {
        this.gamma = gamma;
        this.lambda = lambda;
        this.mu = mu;
        this.K = K;
        this.m = m; 

        lambdaArray = new double[K+1];
        muArray = new double[K+1];
        steadyStateProbArray = new double[K+1];
        arrivalRateArray = new double[K+1];
    }

    // calculating factorial
    private int factorial(int n){    
        if (n == 0)    
          return 1;    
        else    
          return(n * factorial(n-1));    
    }    

    // this function finds all transtion rates, arrival rates and steady state probabilities
    private void initializeValues() {

        // this loop calculates rate at which customers enter the system for different system states
        for (int i = 0; i < K; i++) {
            // for state < 2, both machines generate components
            if (i < 2) {
                lambdaArray[i] = gamma+lambda;
            } // here only machine 2 generates components
            else {
                lambdaArray[i] = lambda;
            }
        }
        lambdaArray[K] = 0.0; // for state K, components do not enter system, so value is )

        muArray[0] = 0.0; // service rate is 0 when there is no customer
        // this loop calculates service rate of the the system for different system states
        for (int i = 1; i <= K; i++) {
            // for customers less than m
            if (i < m) {
                muArray[i] = i*mu;
            } // for customers >= m
            else {
                muArray[i] = m*mu;
            }
        }

        // this loop calculates rate at which customers arrive to the system for different system states
        for (int i = 0; i <= K; i++) {
            if (i < 2) {
                arrivalRateArray[i] = gamma+lambda;
            } else {
                arrivalRateArray[i] = lambda;
            }
        }

        // term1, term2 and term3 are used to calculate the steady state prob at state 0
        double term1 = 1.0 + (gamma+lambda)/mu; // derived from calculation
        
        double term2 = 0.0;
        double t1, t2, t3, t4;
        // loop representing summation, derived from calculation
        for (int n = 2; n <= m-1; n++) {
            t1 = 1.0/factorial(n);
            t2 = Math.pow(gamma+lambda, 2);
            t3 = Math.pow(lambda, n-2);
            t4 = Math.pow(mu, n);
            term2 += (t1)*((t2*t3)/t4);     // dervived from calculation
        }

        double term3 = 0.0; // derived from calculation
        double r1, r2, r3, r4, r5;
        // loop representing summation, derived from calculation
        for (int n = m; n <= K; n++) {
            r1 = 1.0/factorial(m);
            r2 = 1.0/Math.pow(m, n-m);
            r3 = Math.pow(gamma+lambda, 2);
            r4 = Math.pow(lambda, n-2);
            r5 = Math.pow(mu, n);
            term3 += r1*r2*((r3*r4)/r5);    // derived from calculation
        }

        double denom = term1+term2+term3;   // denominator term of equation
        double p0 = 1.0/denom;              // calculating p0
        steadyStateProbArray[0] = p0;       // assigning value to array

        // these variables are used for calculation
        double v1, v2, v3, v4, v5;
        for (int n = 1; n <= K; n++) {
            // condition when there is one customer
            if (n == 1) {
                steadyStateProbArray[n] = ((gamma+lambda)/mu)*p0;   // derived from calculation
            } // condition when customers are less than m
            else if (n < m) {
                v1 = 1.0/factorial(n);
                v2 = Math.pow(gamma+lambda, 2);
                v3 = Math.pow(lambda, n-2);
                v4 = Math.pow(mu, n);
                steadyStateProbArray[n] = (v1)*((v2*v3)/v4)*p0;     // derived from calculation
            } // condition when customers are greater than m
            else {
                v1 = 1.0/factorial(m);
                v2 = 1.0/Math.pow(m, n-m);
                v3 = Math.pow(gamma+lambda, 2);
                v4 = Math.pow(lambda, n-2);
                v5 = Math.pow(mu, n);
                steadyStateProbArray[n] = v1*v2*((v3*v4)/v5)*p0;    // derived from calculation
            }
        }

    }

    // this function calculates all performance values
    public void performanceValues() {
        // function finds all transtion rates, arrival rates and steady state probabilities 
        initializeValues();

        // finding average no of customers in system
        avgCustomers = 0.0;
        for (int n = 0; n <= K; n++) {
            avgCustomers += n*steadyStateProbArray[n]; // formula
        }

        // finding throughput of system (Yenter)
        enterThroughput = 0.0;
        for (int i = 0; i <= K; i++) {
            enterThroughput += lambdaArray[i]*steadyStateProbArray[i]; // formula
        }

        // finding average delay for a customer in system
        avgDelay = (1/enterThroughput)*avgCustomers; // Little's law formula

        // finding throughput of system (Ydepart)
        departThroughput = 0.0;
        for (int i = 1; i <= K; i++) {
            departThroughput += muArray[i]*steadyStateProbArray[i]; // formula
        }

        // calculating total system utilization
        utilization = 0.0;
        double frac, f;
        for (int n = 0; n <= K; n++) {
            if (n < m) {
                f = n;
            } else {
                f = m;
            }
            frac = f/m;
            utilization += frac*steadyStateProbArray[n]; // derived from calculation
        }

        // calculating blocking probability
        double blockProbDenom = 0.0;
        for (int i = 0; i <= K; i++) {
            blockProbDenom += arrivalRateArray[i]*steadyStateProbArray[i]; // derived from calculation
        }
        blockingProb = (arrivalRateArray[K]*steadyStateProbArray[K])/blockProbDenom; // formula
    }
}
