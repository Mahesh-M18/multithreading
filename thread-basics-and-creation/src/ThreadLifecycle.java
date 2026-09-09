public class ThreadLifecycle {
    public static void main(String[] args) {
        MyTasks thread1 = new MyTasks();

        System.out.println(thread1.getState());
        System.out.println(thread1.isAlive());

        thread1.start();

        System.out.println(thread1.getState());
        System.out.println(thread1.isAlive());

    }
}

class MyTasks extends Thread {

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {

            System.out.println(i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }
        System.out.println(currentThread().getState());
    }
}

/*
sleep()
Sometimes we want a thread to pause temporarily.

Thread.sleep(2000);
The currently executing thread enters a sleeping/timed-waiting state for approximately 2 seconds.
Conceptually:
RUNNING
   ↓
sleep(2000)
   ↓
TIMED WAITING
   ↓
approximately 2 seconds
   ↓
eligible to run again
Important:
sleep() pauses the current thread.
It does not pause all threads.
 */

/*
Thread Lifecycle
A thread doesn't simply go:
created → finished

It passes through different states.
Java's Thread.State includes:
NEW
RUNNABLE
BLOCKED
WAITING
TIMED_WAITING
TERMINATED

NEW
Thread object has been created but hasn't started.
Thread t = new Thread(...);

RUNNABLE
After:
t.start();
the thread becomes eligible to run.
Java's RUNNABLE state covers a thread that may be running or ready to run.
Don't think of it as strictly "currently executing."

BLOCKED
A thread is waiting to acquire a monitor lock.

WAITING
A thread waits indefinitely for another thread/action.
For example, certain uses of:
wait()
join()
can result in WAITING.

TIMED_WAITING
The thread waits for a specified amount of time.
Examples:
Thread.sleep(1000);
and timed versions of join() / wait().

TERMINATED
The thread has finished execution.
run() completed
      ↓
TERMINATED
A thread cannot be restarted after termination.

For example:
Thread t = new Thread(...);
t.start();

After it finishes:
t.start(); // ❌ IllegalThreadStateException
You need to create a new Thread object if you want another execution.

Thread Lifecycle -
https://media.geeksforgeeks.org/wp-content/uploads/20240318155846/Lifecycle-and-States-of-a-Thread-in-Java-1.png

Checking Thread State
You can use:
thread.getState()

System.out.println(thread.getState());
The exact second output can vary depending on timing because thread scheduling is nondeterministic.
You should therefore not assume a particular state at an arbitrary instant.

isAlive()
Another useful method:
thread.isAlive()
It tells you whether the thread has been started and has not yet terminated.

System.out.println(thread.isAlive());

join()
Suppose:
Main Thread
     |
     ├── Worker Thread
     |
     ↓
continues immediately
If we want the main thread to wait until the worker finishes:
thread.join();
Conceptually:
Main Thread
     |
     |---- start Worker
     |
     |---- join()
              |
              ↓
        waits for Worker
              |
              ↓
        Worker finishes
              |
              ↓
        Main continues

 */

/*
Thread
A path of execution within a process.
Multithreading
Running multiple threads concurrently within a program.
Main thread
The thread that begins execution of main().
Thread
Java's class for representing/managing a thread.
Runnable
Represents a task that can be executed by a thread.
start()
Starts a new thread of execution.
run()
Contains the code executed by the thread; calling it directly does not create a new thread.
sleep()
Temporarily pauses the currently executing thread.
Thread lifecycle
NEW
 ↓
RUNNABLE
 ↓
WAITING / TIMED_WAITING / BLOCKED
 ↓
RUNNABLE
 ↓
TERMINATED
 */