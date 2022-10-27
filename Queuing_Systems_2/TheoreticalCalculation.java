// Program which calculates theoretical values for user provided input.

public class TheoreticalCalculation {
    
    // constants for the queuing network
    double pH;
    double pL;
    double r2d;
    double r21;
    double r22;
    double mu1;
    double mu2H;
    double mu2L;

    public double theta1H;  //throughput of high priority customers from queue 1
    public double theta2H;  //throughput of high priority customers from queue 2
    public double theta1L;  //throughput of low priority customers from queue 1
    public double theta2L;  //throughput of low priority customers from queue 2

    public double rho1H;    // rho for high priority cust in queue 1
    public double rho2H;    // rho for high priority cust in queue 2
    public double rho1L;    // rho for low priority cust in queue 1
    public double rho2L;    // rho for low priority cust in queue 2

    public double eN1H;     // expected high priority customers in queue 1
    public double eN2H;     // expected high priority customers in queue 2
    public double eN1L;     // expected low priority customers in queue 1
    public double eN2L;     // expected low priority customers in queue 2

    public double time1H;   // average time of high priority customer in queue 1
    public double time2H;   // average time of low priority customer in queue 1
    public double time1L;   // average time of high priority customer in queue 2
    public double time2L;   // average time of low priority customer in queue 2

    // constructor for initializing constants
    public TheoreticalCalculation(double pH, double pL, double r2d, double r21, double r22, double mu1, double mu2H, double mu2L) {
        this.pH = pH;
        this.pL = pL;
        this.r2d = r2d;
        this.r21 = r21;
        this.r22 = r22;
        this.mu1 = mu1;
        this.mu2H = mu2H;
        this.mu2L = mu2L;
    }

    // performing calculation
    public void calculate(double lambda) {
        
        // For high priority customers
        // throughput
        theta1H = lambda*pH;
        theta2H = theta1H;

        // rho
        rho1H = theta1H/mu1;
        rho2H = theta2H/mu2H;

        // expected customers
        eN1H = rho1H/(1-rho1H);
        eN2H = rho2H/(1-rho2H);

        // average time
        time2H = (1/theta2H)*eN2H;

        // For low priority customers
        // throughput
        theta1L = (lambda*pL*(1-r22))/(1-r21-r22);
        theta2L = (lambda*pL)/(1-r21-r22);

        // rho
        rho1L = theta1L/mu1;
        rho2L = theta2L/mu2L;

        // expected customers
        eN1L = rho1L/(1-rho1L);
        eN2L = rho2L/(1-rho2L);

        // average time
        time2L = (1/theta2L)*eN2L;
    }
}
