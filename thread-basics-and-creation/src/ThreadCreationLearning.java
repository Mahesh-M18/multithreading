class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Task is Running");
        System.out.println(Thread.currentThread().getName());
    }

}

public class ThreadCreationLearning {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task);
        thread.start();
    }
}

/*
Creating a Thread by Extending Thread in ThreadLearning.java file

Creating a Thread Using Runnable
MyTask
  ↓
Runnable
  ↓
contains the task

Thread
  ↓
executes the task

Why Use Runnable?
Remember that Java supports single inheritance.
Suppose you already have:
class Employee extends Person {
}

You can't also do:
class Employee extends Thread {
}
because Java doesn't allow extending two classes.

But you can implement interfaces:
class Employee extends Person implements Runnable {
}

Therefore, Runnable allows you to separate:
What should be done?
from:
Which thread should execute it?
That's a useful design principle.

| `Thread`                         | `Runnable`                    |
| -------------------------------- | ----------------------------- |
| Extend `Thread`                  | Implement `Runnable`          |
| Your class becomes a Thread      | Your class represents a task  |
| Cannot extend another class      | Can extend another class      |
| Simpler for basic demonstrations | Generally more flexible       |
| Task and thread are coupled      | Task and thread are separated |

Thread
  ↓
Understand how threads work

Runnable
  ↓
Understand task/thread separation

| Runnable                                 | Callable                         |
| ---------------------------------------- | -------------------------------- |
| Doesn't return a result                  | Can return a result              |
| `run()`                                  | `call()`                         |
| Cannot directly throw checked exceptions | Can throw checked exceptions     |
| Used for tasks without result            | Used when task produces a result |

 */