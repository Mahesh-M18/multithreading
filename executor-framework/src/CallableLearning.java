import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CallableLearning {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> task = () -> {

            System.out.println(Thread.currentThread().getName() + " calculating...");

            return 10 + 20;
        };

        Future<Integer> future = executor.submit(task);

        Integer result = future.get();

        System.out.println("Result: " + result);

        executor.shutdown();
    }
}
