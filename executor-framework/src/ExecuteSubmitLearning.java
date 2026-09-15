import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecuteSubmitLearning {
    public static void main(String[] args) {

        //execute
        ExecutorService executor1 = Executors.newFixedThreadPool(2);

        executor1.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " is executing");
        });

        executor1.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " is executing");
        });

        executor1.shutdown();

        //submit
        ExecutorService executor2 = Executors.newFixedThreadPool(2);

        Future<Integer> future = executor2.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " is calculating");

            return 10 + 20;

        });

        try {
            Integer result = future.get();
            System.out.println("Result: " + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor2.shutdown();
        }

    }
}

/*
Why is it called execute()?
Think of it like telling the executor:
"Here is a task. Please execute it."
execute()
    ↓
Runnable
    ↓
Run the task
    ↓
Done
You don't get a Future.

submit()
submit() is more flexible.
It can accept:
Runnable
Callable
And it returns a:
Future
Example:
ExecutorService executor =
        Executors.newFixedThreadPool(2);

Future<?> future = executor.submit(() -> {
    System.out.println("Task is running");
});

executor.shutdown();
Here:
Future<?> future
represents the future completion/result of the submitted task.

You can later ask:
"Has this task finished?"
using:
future.isDone();


submit() with Runnable
You can submit a Runnable:
Future<?> future = executor.submit(() -> {
    System.out.println("Downloading file...");
});
Notice something important:
The Runnable itself doesn't return anything.
So:
Future<?>
doesn't contain a useful value.
But the Future still allows you to track the task.
For example:
future.get();
will wait until the task finishes.

submit() with Callable
This is where submit() becomes especially useful.
Callable is similar to Runnable, but it can return a value.
Example:
Callable<Integer> task = () -> {
    return 10 + 20;
};
Then:
Future<Integer> future = executor.submit(task);
Now we can get the result:
Integer result = future.get();

System.out.println(result);
Output:
30
The flow is:
Callable
   ↓
submit()
   ↓
Executor executes task
   ↓
Future<Integer>
   ↓
future.get()
   ↓
30


Why can't execute() return a result?
Because execute() accepts only:
Runnable
And Runnable.run() returns:
void
For example:
Runnable task = () -> {
    System.out.println("Hello");
};
There is no result.
Therefore:
executor.execute(task);
just runs it.

| Feature                     | `execute()` | `submit()`        |
| --------------------------- | ----------- | ----------------- |
| Interface                   | `Executor`  | `ExecutorService` |
| Accepts `Runnable`          | ✅           | ✅                 |
| Accepts `Callable`          | ❌           | ✅                 |
| Returns `Future`            | ❌           | ✅                 |
| Get task result             | ❌           | ✅ with `Future`   |
| Track completion            | ❌ directly  | ✅ using `Future`  |
| Simple fire-and-forget task | ✅           | ✅                 |


One important difference: Exceptions
There is another useful difference.
With execute():
executor.execute(() -> {
    throw new RuntimeException("Something went wrong");
});
The exception is handled by the executor/thread's uncaught-exception mechanism.
With submit():
Future<?> future = executor.submit(() -> {
    throw new RuntimeException("Something went wrong");
});
The exception is captured by the Future.
When you call:
future.get();
you'll get an ExecutionException whose cause is the exception thrown by the task.
For example:
try {
    future.get();
} catch (ExecutionException e) {
    System.out.println("Task failed: " + e.getCause());
}


Future<Integer> future = executor.submit(() -> {
    return 10 + 20;
});
does not mean that submit() immediately gives you 30.
It gives you a Future, which represents the eventual result.
Think:
submit()
   ↓
Future<Integer>
   ↓
(wait for task)
   ↓
future.get()
   ↓
30


execute()
I have a task.
Just run it.
I don't need a result.
executor.execute(() -> {
    System.out.println("Hello");
});
submit()
I have a task.
Run it.
Give me a Future so I can track it
and/or get its result.
Future<Integer> future =
        executor.submit(() -> {
            return 100;
        });

int result = future.get();


execute()
    ↓
Runnable
    ↓
No Future
    ↓
"Just run this."


submit()
    ↓
Runnable OR Callable
    ↓
Future
    ↓
"Run this and let me track/get the result."


 */