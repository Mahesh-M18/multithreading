import java.util.concurrent.atomic.AtomicInteger;

class CounterSys {

    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}

public class AtomicOperationsLearning {
    public static void main(String[] args) throws InterruptedException {
        CounterSys count = new CounterSys();

        Thread thread1 = new Thread(() -> {
            count.increment();
        }, "First-Thread");

        Thread thread2 = new Thread(() -> {
            count.increment();
        }, "Second-Thread");

        thread1.start();
        thread1.join();

        thread2.start();
        thread2.join();

        System.out.println(count.getCount());
    }
}

/*
An atomic operation is performed as one indivisible operation from the perspective of other threads.

An atomic operation is an operation that happens as one indivisible step.
That means another thread cannot see it halfway through or interfere with it.
Think of it like a single transaction:
Atomic operation
      ↓
Start → Complete
      ↓
No interruption in between

Java provides classes such as:
AtomicInteger
AtomicLong
AtomicBoolean
from:

java.util.concurrent.atomic
For example:
AtomicInteger counter = new AtomicInteger(0);
Increment:
counter.incrementAndGet();
Get value:
counter.get();


What about volatile?

volatile int count;
Mainly provides visibility.
It means when one thread changes the value, other threads can see the latest value.
But:
count++;
is still not atomic.
So:
volatile
   ↓
Visibility

counter++ vs Atomic Increment
This:
counter++;
is a compound read-modify-write operation.
But:
atomicCounter.incrementAndGet();
provides an atomic increment operation.
Conceptually:
Normal int:

Read → Modify → Write
     ↑
     Can interleave


AtomicInteger:

incrementAndGet()
       ↓
Atomic operation

volatile
   → "Everyone can see the latest value"

synchronized
   → "Only one thread enters at a time"

AtomicInteger
   → "This operation happens atomically"

 */

/*
Three Ways to Make Our Counter Safe
Our counter can be implemented in different ways.
Option 1 — synchronized
public synchronized void increment() {
    count++;
}
Option 2 — Lock
lock.lock();

try {
    count++;
} finally {
    lock.unlock();
}
Option 3 — AtomicInteger
count.incrementAndGet();
All can provide safe approaches, but they work differently and have different tradeoffs.
 */

/*
join() is used when you want one thread to wait until another thread finishes.
Simple example:
Thread thread = new Thread(() -> {
    System.out.println("Task is running");
});

thread.start();

thread.join();

System.out.println("Task completed");
The execution is:
Main Thread
    ↓
thread.start()
    ↓
Worker Thread starts
    ↓
Main Thread waits at join()
    ↓
Worker Thread finishes
    ↓
Main Thread continues
    ↓
"Task completed"

join() means: "Wait for this thread to finish before continuing."


thread1.start();
thread1.join();

thread2.start();
thread2.join();

System.out.println("Both completed");
Here, thread2 won't start until thread1 finishes.
Important: join() can throw InterruptedException, so you'll commonly write:
try {
    thread.join();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
In short:
start() → Start a thread
join()  → Wait for a thread to finish
 */

/*
Which One Should You Use?
Don't think:
"AtomicInteger is newer, so synchronized is bad."
They solve related but different problems.
Use synchronized when:
You need to protect multiple related operations.
You need a critical section.
You want straightforward mutual exclusion.
Example:
synchronized (this) {
    if (balance >= amount) {
        balance -= amount;
    }
}
Multiple operations need to stay together.
Use AtomicInteger when:
You have a simple atomic state update.
You need operations like increment/decrement/compare-and-set.
Example:
counter.incrementAndGet();


Atomicity
An operation happens indivisibly.
Visibility
One thread's changes become visible to another thread.
Ordering
Operations happen in an order constrained by the Java Memory Model.
Synchronization mechanisms such as synchronized help establish the required visibility and ordering
while also providing mutual exclusion.

 */

/*
A deadlock occurs when threads wait for locks held by each other, so neither can proceed.
For example:
Thread A
   ↓
holds Lock 1
   ↓
waits for Lock 2


Thread B
   ↓
holds Lock 2
   ↓
waits for Lock 1
Result:
Thread A → waiting
Thread B → waiting

Neither can continue.
Conceptually:
        Lock 1
       ↗      ↘
 Thread A     Thread B
       ↘      ↙
        Lock 2



Starvation happens when a thread repeatedly fails to get the resources/CPU time it needs to make progress
because other threads keep getting them.
Conceptually:
Thread A → gets resource
Thread B → waits
Thread A → gets resource again
Thread B → waits
Thread A → gets resource again
Thread B → waits...
Thread B may be unable to make meaningful progress.
This is different from deadlock because the system as a whole may still be making progress.


Livelock
In a livelock, threads are active and responding to each other, but they still don't make useful progress.
Think of two people trying to pass each other in a hallway:
Person A → moves left
Person B → moves left

Person A → moves right
Person B → moves right

Person A → left
Person B → left
They're moving, but neither gets through.
In concurrent programs, threads can similarly keep changing state without completing their work.


| Problem        | Meaning                                             |
| -------------- | --------------------------------------------------- |
| Race condition | Result depends on timing/interleaving               |
| Deadlock       | Threads wait for each other's locks indefinitely    |
| Starvation     | A thread doesn't get sufficient access to resources |
| Livelock       | Threads keep responding but make no progress        |

 */

/*
1. Shared data
Multiple threads can access the same object/data.
Thread A ──┐
           ├──→ Shared Data
Thread B ──┘
2. Race condition
Multiple threads modify shared data without proper coordination, causing unpredictable results.
3. Thread safety
Code behaves correctly when accessed concurrently.
4. Critical section
Code that accesses/modifies shared mutable data and needs protection.
5. synchronized
Provides mutual exclusion using an object's monitor and establishes the necessary memory visibility guarantees.
6. Synchronized method
public synchronized void increment() {
    count++;
}
7. Synchronized block
synchronized (this) {
    count++;
}
8. Explicit Lock
lock.lock();

try {
    // critical section
} finally {
    lock.unlock();
}
9. Atomic operation
AtomicInteger count = new AtomicInteger(0);

count.incrementAndGet();
10. Common concurrency problems
Race Condition
Deadlock
Starvation
Livelock

 */