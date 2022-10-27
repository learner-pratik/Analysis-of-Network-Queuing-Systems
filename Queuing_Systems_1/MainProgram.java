import java.util.Scanner;

// This is the main program which is compiled and run to start the simulation
public class MainProgram {

    public static void main(String[] args) {

        double gamma, mu; // arrival rate, service rate
        int K, m; // total system capacity, no of servers

        // object for taking input
        Scanner input = new Scanner(System.in);

        System.out.println("Please input the following values - ");

        System.out.print("Enter value of gamma - ");
        gamma = input.nextDouble();

        System.out.print("Enter value of mu - ");
        mu = input.nextDouble();

        System.out.print("Enter value of K, total capacity of system - ");
        K = input.nextInt();

        System.out.print("Enter value of m, no of workers packaging components - ");
        m = input.nextInt();

        input.close();
        System.out.println();

        // calling the program to start the simulation
        QueuingSimulation qs = new QueuingSimulation(gamma, mu, K, m);
        qs.runSimulation();

    }
}