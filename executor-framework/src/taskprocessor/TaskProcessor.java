package taskprocessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskProcessor {

    private ExecutorService executor;

    public TaskProcessor(int numberOfThreads) {
        executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void processTask(Task task) {

        executor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " started task: " + task.getName());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(Thread.currentThread().getName() + " completed task: " + task.getName());
        });
    }

    public Future<String> processTaskWithResult(Task task) {

        return executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " processing: " + task.getName());

            Thread.sleep(1000);

            return "Task " + task.getId() + " " + task.getName() + " completed successfully";
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
