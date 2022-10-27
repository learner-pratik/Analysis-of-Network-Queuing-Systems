// Progtam for generating random number with exponential distribution 
public class ExponentialRV {

    private double seed = 1111.0;

    // return a uniform (0, 1) random variable
    public double uniformRandomVariable() {
        
        double k = 16807.0;
        double m = 2147483647e9;
        double rv;

        seed = (k*seed)%m;
        rv = seed/m;
        return rv;
    }
    
    // given an arrival rate lambda
    // returns an exponential random variable
    public double generateRandomVariable(double lambda) {
        
        double exp;
        exp = ((-1.0)/lambda) * Math.log(uniformRandomVariable()); // formula
        return exp;
    }
}
