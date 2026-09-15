class Counter {
    private int count = 0;

    public synchronized void increment() {
        System.out.println(Thread.currentThread().getName());
        count++;
        System.out.println(count);
    }

    /*
    public void increment() {

        System.out.println("Some unrelated work");

        synchronized (this) {
            counter++;
        }

        System.out.println("More unrelated work");
    }
    */
    public int getCount() {
        return count;
    }
}

public class SynchronizationLearning {
    public static void main(String[] args) {
        Counter count = new Counter();

        Thread thread1 = new Thread(() -> {
            count.increment();
        }, "First-Thread");

        Thread thread2 = new Thread(() -> {
            count.increment();
        }, "Second-Thread");

        thread1.start();
        thread2.start();
    }
}

/*
we face the problem:
What happens when multiple threads access and modify the same data at the same time?
This is where race conditions, synchronization, locks, and atomic operations come in.

Shared Data
Suppose we have:
int counter = 0;
And we have two threads:
counter = 0

Thread A → counter++
Thread B → counter++
Both threads are accessing the same counter.
This is called shared data.
                  counter
                     ↑
              ┌──────┴──────┐
              │             │
              ↓             ↓
          Thread A       Thread B
The threads share the same variable because they're running within the same Java process
and can access shared objects/state.

Race Condition
A race condition occurs when the result of a program depends on the timing/order in which
multiple threads access shared data.
Consider:
counter = 0
Two threads want to increment it.
Possible execution:
Thread A                    Thread B
   |                           |
Read counter = 0              |
   |                           |
                              Read counter = 0
   |                           |
Add 1                         Add 1
   |                           |
Write 1                       Write 1
Final result:
counter = 1
But we expected:
counter = 2
One increment was lost.

Race condition = Multiple threads competing to modify shared data, causing an unpredictable or incorrect result.

A race condition occurs when multiple threads access and modify the same shared data at the same time,
and the final result depends on which thread executes first.

Why Does This Happen?
Because:
counter++;
is not an indivisible operation.
Conceptually:
counter++

     ↓

Read
  ↓
Modify
  ↓
Write
Two threads can interleave those steps.
That's the important point.
The problem isn't simply that multiple threads exist.
The problem is that multiple threads are accessing shared mutable data without proper coordination.

Real-Life Example
Imagine a bank account:
Balance = ₹10,000
Two operations happen at nearly the same time.
Thread A → Withdraw ₹5,000
Thread B → Withdraw ₹7,000
If both read the old balance:
Thread A → reads ₹10,000
Thread B → reads ₹10,000
both may think the transaction is valid.
They could then produce an incorrect final balance.
This is why thread safety matters in:
Banking systems
Payment systems
Inventory systems
Ticket booking
Order processing
Counters
Shared caches
Logging systems

Thread Safety
Thread safety means that shared code/data behaves correctly when accessed by multiple threads concurrently.
For example, suppose multiple threads increment a counter.
A thread-safe counter should guarantee that:
1000 increments
+
1000 increments
+
1000 increments
+
1000 increments
produces:
4000
rather than an unpredictable smaller number.

How Do We Make Code Thread-Safe?
One important mechanism Java provides is:
synchronized
The idea is:
Allow only one thread at a time to execute a particular critical section.
This is called mutual exclusion.

Mutual exclusion is a concurrency control requirement ensuring that only one thread
can access a shared resource or critical section at any given time.

What Is a Critical Section?
A critical section is a section of code that accesses shared mutable data and therefore must be protected from unsafe concurrent access.
For example:
counter++;
If counter is shared between multiple threads, this operation is a critical section.
Conceptually:
             Shared counter
                   ↑
                   |
          ┌────────┴────────┐
          ↓                 ↓
      Thread A           Thread B
          ↓                 ↓
       counter++         counter++
          ↑
          |
    Critical Section
We need to control access to that section.

synchronized
Java's synchronized keyword provides mutual exclusion around a monitor.
For example:
public synchronized void increment() {
    counter++;
}
Now, if multiple threads call increment(), only one thread at a time can execute that synchronized method on the same object instance.
Conceptually:
Thread A
   ↓
gets lock
   ↓
increment()
   ↓
releases lock

Thread B
   ↓
gets lock
   ↓
increment()
   ↓
releases lock
This prevents the increment operations from interfering with each other.

The Lock Concept
When you use:
synchronized
Java uses an object's monitor lock.
Think of it like a room with one key.
             Critical Section
                   🚪
                 🔑
                   |
          Only one thread
          can enter at once
Suppose Thread A gets the lock:
Thread A → 🔒 → enters
Thread B → waits
Thread C → waits
After Thread A finishes:
Thread A → releases 🔓
Another waiting thread can acquire the lock.
So:
synchronized
      ↓
acquire monitor
      ↓
execute critical section
      ↓
release monitor
You don't manually acquire/release the intrinsic monitor when using the synchronized keyword; Java handles that for you.

 */

/*
Counter counter = new Counter();

Thread t1 = new Thread(() -> counter.increment());
Thread t2 = new Thread(() -> counter.increment());
Both threads operate on:
same Counter object
Therefore they compete for the same monitor.
But if you create:
Counter counter1 = new Counter();
Counter counter2 = new Counter();
then:
counter1 → its own lock
counter2 → its own lock
A synchronized instance method on counter1 does not use the same monitor as one on counter2.
So synchronized doesn't magically lock all instances of a class.

Synchronized Block
Sometimes we don't want to synchronize an entire method.
Suppose:
public void increment() {

    System.out.println("Some unrelated work");

    counter++;

    System.out.println("More unrelated work");
}
We only need to protect:
counter++;
We can use a synchronized block:
public void increment() {

    System.out.println("Some unrelated work");

    synchronized (this) {
        counter++;
    }

    System.out.println("More unrelated work");
}
Now only the critical section is synchronized.

Why Use Synchronized Blocks?
Suppose we have:
public synchronized void process() {

    doSomething();
    doSomethingElse();
    counter++;
    doAnotherThing();
}
The entire method is protected.
That may unnecessarily prevent other threads from entering the method while the unrelated operations execute.
Instead:
public void process() {

    doSomething();
    doSomethingElse();

    synchronized (this) {
        counter++;
    }

    doAnotherThing();
}
Only the shared-data operation is protected.
This can allow more concurrency.

Thread concurrency is a program's ability to execute multiple smaller execution paths (threads) within a single process,
sharing resources while making progress on multiple tasks.

| Synchronized Method              | Synchronized Block                       |
| -------------------------------- | ---------------------------------------- |
| Synchronizes the whole method    | Synchronizes only selected code          |
| Simpler                          | More precise                             |
| Easy to understand               | Gives more control                       |
| Uses the method's monitor target | You explicitly choose the monitor object |

 */
