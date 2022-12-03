import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Random random = new Random();
        ArrayList<Integer> arr = new ArrayList<>();

        for (int i = 0; i < 9000; i++) {
            arr.add(random.nextInt());
        }

        FindBiggestThread findBiggestThread = new FindBiggestThread(arr);
        findBiggestThread.start();

        firstTask(arr);


        thirdTask(arr);
        thirdTaskAdditional(arr);

    }

    public static Integer firstTask(ArrayList<Integer> arr) {
        Integer max = arr.get(0);

        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) > max) {
                max = arr.get(i);

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Thread has been interrupted");
                }
            }
        }
        System.out.println("[" + Thread.currentThread().getName() + "] Максимальное значение, найденное последовательно: " + max);
        return max;
    }

    static class FindBiggestThread extends Thread {
        ArrayList<Integer> arr;

        public FindBiggestThread(ArrayList<Integer> arr) {
            this.arr = arr;
        }

        public void run() {
            System.out.printf("Thread: '%s' is started\n", Thread.currentThread().getName());

            Integer max = arr.get(0);

            for (int i = 1; i < arr.size(); i++) {
                if (arr.get(i) > max) {
                    max = arr.get(i);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Thread has been interrupted");
                    }
                }
            }
            System.out.println("Максимальное значение, найденное последовательно (в отдельном потоке): " + max);
            System.out.printf("Thread: '%s' is finished\n", Thread.currentThread().getName());
        }
    }

    public static void thirdTask(ArrayList<Integer> arr) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<String> task = () -> {

            Integer max = arr.get(0);
            System.out.println("[ExecutorService] Thread: " + Thread.currentThread().getName());
            for (int i = 1; i < arr.size(); i++) {
                if (arr.get(i) > max) {
                    max = arr.get(i);
                    Thread.sleep(1);
                }
            }

            System.out.println("Максимальное значение, найденное последовательно при помощи ExecutorService: " + max);
            return String.valueOf(max);
        };
        executorService.submit(task);
        executorService.shutdown();
    }

    public static class Task implements Callable<Integer> {
        List<Integer> subArray = new ArrayList<>();

        public Task(List<Integer> subArray) {
            this.subArray = subArray;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println("[Task] Task started in: " + Thread.currentThread().getName());
            int max = this.subArray.get(0);
            for (int i = 1; i < this.subArray.size(); i++) {
                if (this.subArray.get(i) > max) {
                    max = this.subArray.get(i);
                    Thread.sleep(1);
                }
            }
            System.out.println("[Task] Task in: " + Thread.currentThread()
                    .getName() + " find max value: " + max + " of itself partition. Finishing ...");
            return max;
        }
    }

    public static Integer thirdTaskAdditional(ArrayList<Integer> arr) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        int chunkSize = 3000;
        List<List<Integer>> partitionedList = Lists.partition(arr, chunkSize);
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < partitionedList.size(); i++) {
            Task task = new Task(partitionedList.get(i));
            tasks.add(task);
        }

        List<Future<Integer>> futures = executorService.invokeAll(tasks);

        var maxOfPartitions = new ArrayList<Integer>();
        for (Future<Integer> f: futures){
            maxOfPartitions.add(f.get());
        }

        Integer res = firstTask(maxOfPartitions);

        executorService.shutdown();

        return res;
    }
}
