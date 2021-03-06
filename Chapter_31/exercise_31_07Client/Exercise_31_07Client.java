package exercise_31_07Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Exercise_31_07Client {

    public static void main(String[] args) {
        System.out.println("Enter a count to get the get the last x pcs of prime numbers from PrimeNumbers.dat: ");
        int count = new Scanner(System.in).nextInt();
        long[] primes;
        
        try {
            Socket socket = new Socket("localhost", 8000);
            ObjectInputStream inputFromServer = new ObjectInputStream(socket.getInputStream());
            DataOutputStream outputToServer = new DataOutputStream(socket.getOutputStream());
            outputToServer.writeInt(count);
            System.out.println("Count was sent to server.");
            
            primes = (long[])(inputFromServer.readObject());
            
            int i = 1;
            System.out.println("The last " + count + " primes from PrimeNumbers.dat are: ");
            for(long number : primes) {
                if(i % 10 == 0) {
                    System.out.println(number);
                }
                else {
                    System.out.print(number + " ");
                }
                i++;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
