import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableLearning {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " is processing.");
        };

        executor.submit(task);
        executor.submit(task);
        executor.submit(task);
        executor.submit(task);
        executor.submit(task);

        executor.shutdown();
    }
}


/*
| Manual Threads                    | Executor Framework                 |
| --------------------------------- | ---------------------------------- |
| Create threads yourself           | Executor manages workers           |
| `new Thread()`                    | Thread pool                        |
| `start()`                         | `submit()` / `execute()`           |
| Difficult for many tasks          | Designed for many tasks            |
| More manual management            | Reuses worker threads              |
| No built-in result abstraction    | `Future` for results               |
| Suitable for learning/basic cases | Better for task-based applications |


Manual approach
1000 tasks
   ↓
Potentially many Thread objects
   ↓
Resource overhead
   ↓
Manual management
Executor approach
1000 tasks
   ↓
Thread Pool
   ↓
Controlled number of workers
   ↓
Tasks queued and processed
The executor doesn't magically make every program faster. It gives you a structured way to control
concurrency and reuse worker threads, which can improve efficiency and resource management when configured appropriately.
 */