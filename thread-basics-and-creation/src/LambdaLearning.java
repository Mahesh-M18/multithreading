public class LambdaLearning {
    public static void main() {
        Thread thread = new Thread(()->{
            System.out.println("Hello from thread");
            System.out.println(Thread.currentThread().getName());
        });

        thread.start();

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 is running");
            System.out.println(Thread.currentThread().getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 is running");
            System.out.println(Thread.currentThread().getName());
        });

        thread1.start();
        thread2.start();

        Runnable downloadTask = () -> {
            System.out.println("Downloading file...");
        };

        Runnable processTask = () -> {
            System.out.println("Processing file...");
        };

        Thread downloadThread = new Thread(downloadTask);
        Thread processThread = new Thread(processTask);

        downloadThread.start();
        processThread.start();
    }
}

/*
The traditional way is:
Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("Task is running");
    }
};

Thread thread = new Thread(task);
thread.start();
This works, but there is a lot of unnecessary code.
Because Runnable is a functional interface, we can use a lambda expression to write the same thing much more simply.

Here:
() -> {
    System.out.println("Task is running");
}
is a lambda expression that provides the implementation of:
run()
So you can think of it as:
Runnable
   ↓
run()
   ↓
Lambda provides the code
   ↓
Thread executes it
 */

/*
Traditional Runnable vs Lambda

Traditional approach
Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("Task running");
    }
};

Thread thread = new Thread(task);
thread.start();

Lambda approach
Runnable task = () -> {
    System.out.println("Task running");
};

Thread thread = new Thread(task);
thread.start();

The lambda version is shorter and easier to read.

You don't necessarily need to create a separate Runnable variable.
You can directly pass the lambda to Thread:
Thread thread = new Thread(() -> {
    System.out.println("Task running");
});

thread.start();
This is very common in modern Java.

 */

/*
Lambda syntax

(parameters) -> {
    // code
}

For Runnable, the run() method has no parameters:
void run()
Therefore:
() -> {
    System.out.println("Task running");
}

Lambda with a single statement
If the lambda contains only one statement, you can remove {}.
Instead of:
Runnable task = () -> {
    System.out.println("Task running");
};
you can write:
Runnable task = () -> System.out.println("Task running");

Lambda with a loop
You can put more complex logic inside the lambda:
Thread thread = new Thread(() -> {

    for (int i = 1; i <= 5; i++) {
        System.out.println("Count: " + i);
    }

});

thread.start();
The lambda is essentially providing the implementation of:
public void run() {
    for (int i = 1; i <= 5; i++) {
        System.out.println("Count: " + i);
    }
}


Lambda and Runnable relationship

When you write:
Thread thread = new Thread(() -> {
    System.out.println("Hello");
});
Java understands that the lambda is being used where a Runnable is expected.

Conceptually:
Runnable task = () -> {
    System.out.println("Hello");
};

Thread thread = new Thread(task);
So:
Lambda
   ↓
implements Runnable's run()
   ↓
Runnable task
   ↓
Thread
   ↓
start()
   ↓
run() executes

Runnable  → performs a task → no return value
Callable  → performs a task → can return a value

A lambda expression can provide the implementation of Runnable's run() method,
allowing you to create and execute a thread with much less code.
 */