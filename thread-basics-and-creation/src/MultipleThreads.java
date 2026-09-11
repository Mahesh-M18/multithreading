class Mythreads extends Thread {
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(Thread.currentThread().getName() + " - " + i);
        }
    }
}

public class MultipleThreads {
    public static void main(String[] args) {
        Mythreads mythread1 = new Mythreads();
        Mythreads mythread2 = new Mythreads();
        mythread1.start();
        mythread2.start();

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread Name: " + Thread.currentThread().getName());
            System.out.println("Downloading");
        }, "Download-Thread");

        thread1.start();

        Thread thread2 = new Thread(
                () -> System.out.println(Thread.currentThread().getName() + " - Processing"),
                "Processing-Thread"
        );

        thread2.start();

    }
}

/*
The order isn't guaranteed.
This is a fundamental characteristic of multithreading.

The JVM and operating system decide when threads get CPU time.
Conceptually:
CPU
 |
 ├── Thread 1 → executes
 |
 ├── Thread 2 → executes
 |
 ├── Thread 1 → executes
 |
 ├── Thread 2 → executes
 |
 └── ...
The scheduler determines which runnable thread gets execution time.
Therefore, don't write multithreaded programs assuming:
Thread 1 always finishes first
unless you explicitly establish that ordering.
 */