import java.util.concurrent.*;

public class ExecutorFrameworkLearning {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> System.out.println(Thread.currentThread().getName() + " Task 1"));

        executor.submit(() -> System.out.println(Thread.currentThread().getName() + " Task 2"));

        executor.submit(() -> System.out.println(Thread.currentThread().getName() + " Task 3"));

        Runnable task1 = () -> {
            System.out.println(Thread.currentThread().getName() + " is processing the task.");
        };

        executor.submit(task1);

        Callable<Integer> task2 = () -> {
            return 10 + 20;
        };

        Future<Integer> future = executor.submit(task2);
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println(e.getCause());
        }

        executor.shutdown();
    }
}

/*
Instead of manually creating and managing many threads, let Java's Executor Framework manage them for us.

imagine an application receives 10,000 tasks.
Would we do:
Thread t1 = new Thread(...);
Thread t2 = new Thread(...);
Thread t3 = new Thread(...);
...
Thread t10000 = new Thread(...);
Definitely not.
Creating thousands of threads can consume significant resources and make thread management difficult.
Instead:
10,000 Tasks
     ↓
Thread Pool
     ↓
Fixed number of worker threads
     ↓
Tasks processed efficiently
That's the basic idea behind the Executor Framework.


Executor Framework is a set of classes and interfaces in Java for managing and executing tasks asynchronously.
The main package is:
java.util.concurrent
Some important components are:
Executor
   ↓
ExecutorService
   ↓
ThreadPoolExecutor
And for returning results:
Callable
   ↓
Future

Thread vs Task
Previously, we did:
Thread thread = new Thread(task);
thread.start();
Here, we directly manage the thread.
With Executor Framework:
Task
 ↓
Executor
 ↓
Worker Thread
 ↓
Task executes
You primarily tell the executor:
"Here is a task. Execute it."
The executor decides which worker thread should execute it.
This gives us separation between task submission and thread management.

Executor
It provides:
void execute(Runnable command)
Example:
Executor executor = command -> {
    new Thread(command).start();
};

executor.execute(() -> {
    System.out.println("Task running");
});
But you normally won't create executors this way for real applications.
Instead, you'll commonly use:

ExecutorService
ExecutorService extends Executor and provides much more functionality.
For example:
ExecutorService executor =
        Executors.newFixedThreadPool(3);
Now we have a thread pool containing up to 3 worker threads.
Conceptually:
                 ExecutorService
                       |
            ┌──────────┼──────────┐
            ↓          ↓          ↓
         Worker 1   Worker 2   Worker 3
We submit tasks:
executor.submit(task);
The executor assigns them to available worker threads.


What Is a Thread Pool?
A thread pool is a collection of reusable worker threads.
Suppose:
ExecutorService executor =
        Executors.newFixedThreadPool(3);
We have:
Thread Pool

┌────────────┐
│ Worker 1   │
├────────────┤
│ Worker 2   │
├────────────┤
│ Worker 3   │
└────────────┘
Now submit 6 tasks:
Task 1
Task 2
Task 3
Task 4
Task 5
Task 6
Initially:
Worker 1 → Task 1
Worker 2 → Task 2
Worker 3 → Task 3
Tasks 4–6 wait in the executor's work queue.
When a worker finishes:
Worker 1 → finishes Task 1
             ↓
          Task 4
The worker is reused.
That's the major advantage.

Why Reuse Threads?
Creating and destroying threads has overhead.
A thread pool allows:
Create workers
      ↓
Reuse workers
      ↓
Execute many tasks
      ↓
Avoid creating a new thread for every task
This can improve resource utilization and make application behavior easier to control.

execute() vs submit()
This is important.
Executor provides:
execute()
ExecutorService provides:
submit()
execute()
Used for Runnable tasks when you don't need a returned result.
executor.execute(() -> {
    System.out.println("Hello");
});
submit()
Can submit:
Runnable
Callable
and returns a Future.
Future<?> future = executor.submit(() -> {
    System.out.println("Hello");
});
So:
execute()
    ↓
Runnable
    ↓
No Future result

submit()
    ↓
Runnable / Callable
    ↓
Future

execute() = "Run this task. I don't need anything back."
submit() = "Run this task, and give me a Future so I can track/get the result."


Traditional approach:
Task
 ↓
Create Thread
 ↓
start()
 ↓
Execute
Executor approach:
Task
 ↓
ExecutorService
 ↓
Worker Thread
 ↓
Execute


| Runnable                                              | Callable                              |
| ----------------------------------------------------- | ------------------------------------- |
| `run()`                                               | `call()`                              |
| Returns nothing                                       | Returns a result                      |
| Cannot throw checked exceptions directly from `run()` | `call()` can throw checked exceptions |
| Used for tasks without a result                       | Used for tasks that produce a result  |
| Can be submitted using `submit()`                     | Can be submitted using `submit()`     |


Runnable
   ↓
"Do this task."

Callable
   ↓
"Do this task and give me the result."

A Future represents the result of an asynchronous computation.
Think of it like a receipt or placeholder for a result that may not be ready yet.
Conceptually:
Callable
   ↓
submit()
   ↓
Future
   ↓
Task running in background
   ↓
Result becomes available
We can later ask:
future.get();
for the result.

Is future.get() Blocking?
Yes.
If the task isn't finished yet:
future.get();
causes the calling thread to wait until the result is available, unless the wait is interrupted or the computation fails.
Conceptually:
Main Thread
     |
     | future.get()
     ↓
   waits
     |
     ↓
Worker Thread
     |
   task running
     |
     ↓
  result ready
     |
     ↓
Main continues
So Future doesn't mean the result is immediately available.
It represents a result that will become available.

Future.cancel()
A future can also be cancelled:
future.cancel(true);
The boolean indicates whether the executor should attempt to interrupt the running task if it is currently executing.
Cancellation is cooperative in Java; interruption doesn't forcibly kill a thread.

When you call:
future.get();
the failure is reported through:
ExecutionException
So you commonly write:
try {
    Integer result = future.get();
} catch (ExecutionException e) {
    System.out.println(
            "Task failed: " + e.getCause()
    );
}
This becomes useful in real task-processing systems.

*/


/*
ThreadPoolExecutor is a configurable implementation of ExecutorService.
Instead of:
Executors.newFixedThreadPool(3);
we can directly create:
ThreadPoolExecutor
Example:
ThreadPoolExecutor executor =
        new ThreadPoolExecutor(
                2,
                4,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );

ThreadPoolExecutor Parameters
The constructor above contains:
new ThreadPoolExecutor(
    2,
    4,
    60,
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>()
);
These correspond to:
2
↓
corePoolSize

4
↓
maximumPoolSize

60
↓
keepAliveTime

TimeUnit.SECONDS
↓
time unit

LinkedBlockingQueue
↓
task queue

corePoolSize
corePoolSize = 2
The executor tries to maintain two core worker threads.
Conceptually:
Core workers:

Worker 1
Worker 2

maximumPoolSize
maximumPoolSize = 4
The pool can grow up to four worker threads under the executor's thread-creation rules when the queue cannot accept additional tasks and the executor is still running.
So:
Normal:
2 workers

Higher demand:
up to 4 workers

keepAliveTime
Suppose:
60, TimeUnit.SECONDS
Non-core threads that remain idle for the configured keep-alive time can be terminated, subject to executor configuration.
This helps avoid keeping unnecessary extra worker threads alive.

Work Queue
The executor needs somewhere to keep submitted tasks that aren't immediately executed.
That's the:
BlockingQueue<Runnable>
For example:
new LinkedBlockingQueue<>()
Conceptually:
Tasks
 ↓
┌────────────────────┐
│ Task 3             │
│ Task 4             │
│ Task 5             │
│ Task 6             │
└────────────────────┘
       Queue
Workers take tasks from this queue.

corePoolSize = 2
maximumPoolSize = 4
Tasks arrive.
Conceptually, the executor behaves roughly like:
Task 1
   ↓
Worker 1

Task 2
   ↓
Worker 2

Task 3
   ↓
Queue

Task 4
   ↓
Queue
If the queue reaches the point where another worker needs to be created and the maximum hasn't been reached:
Worker 3
   ↓
Task
Potentially later:
Worker 4
   ↓
Task
The exact behavior depends on the queue and executor configuration.

Executors.newFixedThreadPool() is convenient.
But ThreadPoolExecutor gives you more control over:
Core thread count
Maximum thread count
Queue
Keep-alive time
Thread factory
Rejected execution policy
So:
Executors
   ↓
Easy configuration
while:
ThreadPoolExecutor
   ↓
Fine-grained control
 */


/*
Shutdown
This is extremely important.
When you're finished submitting tasks:
executor.shutdown();
This means:
Stop accepting new tasks, but allow already-submitted tasks to finish.
Conceptually:
shutdown()
    ↓
No new tasks
    ↓
Existing tasks continue
    ↓
Workers finish
    ↓
Executor terminates

shutdownNow()
There is also:
executor.shutdownNow();
It attempts to stop currently executing tasks by interrupting worker threads and returns tasks that never started.
But remember:
Interrupting a thread does not forcibly kill it.
The task should respond properly to interruption.
For normal graceful application shutdown, shutdown() is usually the safer default.

If you don't shut down an executor properly, its worker threads can keep the application alive.
So don't forget:
executor.shutdown();
when the executor is no longer needed.

awaitTermination()
Sometimes we want to wait for the executor to actually terminate.
executor.shutdown();

if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
    System.out.println("All tasks completed.");
}
Conceptually:
shutdown()
     ↓
Stop accepting tasks
     ↓
Wait for existing tasks
     ↓
awaitTermination()
     ↓
Executor terminates


                  Executor Framework
                         |
                ┌────────┴────────┐
                ↓                 ↓
           Runnable           Callable
                |                 |
                └────────┬────────┘
                         ↓
                  ExecutorService
                         ↓
                     Thread Pool
                         ↓
                  Worker Threads
                         ↓
                     Execute
                         ↓
                    ┌────┴────┐
                    ↓         ↓
                  Result    No Result
                    ↓
                  Future
                    ↓
                future.get()
 */