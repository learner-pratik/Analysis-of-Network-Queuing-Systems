import java.util.Scanner;

public class MainProgram {
    
    public static void main(String[] args) {

        // constants for the queuing network
        double pH;
        double pL;
        double r2d;
        double r21;
        double r22;
        double mu1;
        double mu2H;
        double mu2L;

        // initializing simulation program
        QueuingNetworkSimulation qNetworkSimulation = new QueuingNetworkSimulation();

        Scanner input = new Scanner(System.in);

        // taking input
        System.out.println("Please input the following values - ");
        System.out.println("Enter pL - ");
        pL = input.nextDouble();
        System.out.println("Enter pH - ");
        pH = input.nextDouble();
        System.out.println("Enter r2d - ");
        r2d = input.nextDouble();
        System.out.println("Enter r21 - ");
        r21 = input.nextDouble();
        System.out.println("Enter r22 - ");
        r22 = input.nextDouble();
        System.out.println("Enter mu1 - ");
        mu1 = input.nextDouble();
        System.out.println("Enter mu2H - ");
        mu2H = input.nextDouble();
        System.out.println("Enter mu2L - ");
        mu2L = input.nextDouble();

        // starting the simulation
        qNetworkSimulation.startProgram(pL, pH, r2d, r21, r22, mu1, mu2H, mu2L);

        input.close();
    }
}
