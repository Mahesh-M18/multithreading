public class ThreadLearning {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName()); //main thread

        Mythread thread = new Mythread();
        thread.start();
    }
}

class Mythread extends Thread{
    @Override
    public void run(){
        System.out.println("My thread is running");
        System.out.println(Thread.currentThread().getName());
    }
}

/*
Process
A process is a running program.
Process - a program in execution

A process has its own memory and resources.
For example, when you run your Java application:
java Main
the operating system creates a process for that Java application.

Thread
A thread is a path of execution inside a process.

Java Process
     |
     └── Thread
That thread executes your Java code.

A process can contain multiple threads:
Java Process
     |
     ├── Thread 1
     ├── Thread 2
     ├── Thread 3
     └── Thread 4
These threads belong to the same process and can work concurrently.

Imagine a restaurant.
The restaurant is the process.
The workers are threads.
             Restaurant
                 |
       ┌─────────┼─────────┐
       ↓         ↓         ↓
    Worker 1  Worker 2  Worker 3
    Cooking    Billing   Cleaning

Similarly, a Java application can have multiple threads doing different work.

| Process                                  | Thread                                               |
| ---------------------------------------- | ---------------------------------------------------- |
| Running program                          | Execution path inside a process                      |
| Has its own memory space                 | Shares process memory                                |
| More expensive to create                 | Generally cheaper to create                          |
| Processes are relatively independent     | Threads within a process can interact/share data     |
| Communication can be more expensive      | Communication can be easier because memory is shared |
| One process can contain multiple threads | A thread belongs to a process                        |

Multithrading
It means executing multiple threads concurrently within a program.
For example:
Thread 1 → Download file
Thread 2 → Play music
Thread 3 → Update UI
All these activities can happen concurrently.
Without multithreading, you might have:
Task 1
  ↓
finish
  ↓
Task 2
  ↓
finish
  ↓
Task 3
With multiple threads:
Thread 1 → Task 1 ───────────────→
Thread 2 → Task 2 ───────→
Thread 3 → Task 3 ─────────→
The exact execution depends on the operating system and JVM scheduler.

Concurrency vs Parallelism

Concurrency
Multiple tasks are in progress during the same period.
Imagine one chef switching between multiple dishes:
Task A → work → switch
Task B → work → switch
Task A → work → switch
Task C → work

Parallelism
Multiple tasks actually execute at the same time, typically on different CPU cores.
CPU Core 1 → Task A
CPU Core 2 → Task B
CPU Core 3 → Task C

Concurrency = dealing with multiple tasks
Parallelism = executing multiple tasks simultaneously

Reasons for the need of Multithreading
1. Responsiveness
Suppose a GUI application needs to download a large file.
Without a separate thread:
Download
   ↓
Application waits
   ↓
UI becomes unresponsive
With another thread:
Main/UI Thread
      ↓
Remains responsive

Download Thread
      ↓
Downloads file

2. Better resource utilization
Modern CPUs have multiple cores.
Multithreading can allow applications to make better use of available CPU resources.

3. Perform multiple tasks concurrently
For example:
Thread 1 → Process orders
Thread 2 → Send notifications
Thread 3 → Generate reports

4. Background tasks
Applications frequently perform background operations such as:
Logging
File processing
Network operations
Database operations
Scheduled tasks
 */

/*
The Main Thread
Every Java application starts with a thread called the main thread.
Consider:
public class Main {

    public static void main(String[] args) {

        System.out.println("Hello Java");
    }
}
You didn't create a thread manually.
But Java still executes this code using a thread.
That thread is the:
main thread
Conceptually:
Java Program
     |
     └── main thread
             |
             └── main()

Thread.currentThread() gives you the thread that is currently executing the code.
 */

/*
Creating a Thread by Extending Thread
Java provides the Thread class.
We can extend it:
class MyThread extends Thread {

    @Override
    public void run() {

        System.out.println("My thread is running");
    }
}
Then:
public class Main {

    public static void main(String[] args) {

        MyThread thread = new MyThread();

        thread.start();
    }
}

MyThread thread = new MyThread();
creates a Thread object.
But the thread isn't running yet.
Thread object
      ↓
Created
      ↓
Not running yet
Then:
thread.start();
starts the new thread.
Thread object
      ↓
start()
      ↓
New thread starts
      ↓
run()
      ↓
Task executes

start() vs run()

class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("Running...");
    }
}

MyThread thread = new MyThread();
Correct way:
thread.start();

start() tells Java to create/start a separate thread of execution, which then invokes run().

Incorrect for starting a new thread:
thread.run();

Calling run() directly is simply a normal method call.
It does not start a new thread.

Think:
start()
   ↓
new thread execution
   ↓
run()

whereas:
run()
   ↓
normal method call

start() starts a new thread.
run() contains the task that the thread executes.

 */