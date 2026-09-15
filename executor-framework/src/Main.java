import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import taskprocessor.Task;
import taskprocessor.TaskProcessor;


public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        TaskProcessor processor = new TaskProcessor(3);

        Task task1 = new Task(1, "Send Email");

        Task task2 = new Task(2, "Generate Report");

        Task task3 = new Task(3, "Process Payment");

        Task task4 = new Task(4, "Backup Files");

        // Using execute()
        processor.processTask(task1);
        processor.processTask(task2);

        // Using submit()
        Future<String> result1 = processor.processTaskWithResult(task3);
        Future<String> result2 = processor.processTaskWithResult(task4);

        System.out.println(result1.get());
        System.out.println(result2.get());

        processor.shutdown();

        System.out.println("All tasks submitted.");

    }
}
