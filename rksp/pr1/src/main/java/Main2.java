import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int i;

        while (true) {
            System.out.println("Enter the number: ");
            Scanner scanner = new Scanner(System.in);
            i = scanner.nextInt();

            int res = i;
            Callable task = () -> {
                var timeToSleep = (int) ((Math.random() * (5000 - 1000)) + 1000);
                Thread.sleep(timeToSleep);
//                System.out.println(res*res);
//                return null;
                return res * res;
            };
            FutureTask<Integer> future = new FutureTask<>(task);
            new Thread(future).start();
            System.out.println(future.get());
        }
    }
}
