class CounterSystem {

    private int counter = 0;

    private final Object lock = new Object();

    public void increment() {

        synchronized (lock) {
            counter++;
        }
    }

    public int getCounter() {
        return counter;
    }
}

public class LockLearning {
    public static void main(String[] args) {
        CounterSystem count = new CounterSystem();

        Thread thread1 = new Thread(() -> {
            count.increment();
        }, "First-Thread");

        Thread thread2 = new Thread(() -> {
            count.increment();
        }, "Second-Thread");

        thread1.start();
        thread2.start();

        System.out.println(count.getCounter());
    }
}

/*
What is this lock object?

private final Object lock = new Object();
Here, lock is simply a normal Java object.
For example:
Object lock = new Object();
You aren't creating some special "Lock" type here.
You're creating an ordinary Object that Java can use as a monitor.
Think of it as:
lock object
    ↓
🔑 acts as the key
    ↓
controls access to synchronized(lock)

Why do we use Object?
You don't need the object to contain any data.
You just need a unique object that threads can synchronize on.
So this is common:
private final Object lock = new Object();
The object exists primarily to serve as the monitor.

Now imagine:
Thread 1 → increment()
Thread 2 → increment()
Thread 3 → increment()
All three threads use:
synchronized (lock)
Therefore they compete for the same monitor.
Only one can enter at a time:
        lock 🔒
          │
     ┌────┴────┐
     ↓         ↓
 Thread 1    Thread 2
    ↓
 enters
    ↓
counter++
    ↓
 leaves
          ↓
       Thread 2
        enters
          ↓
       counter++
This prevents the race condition.

private
  ↓
Only this class controls the lock

final
  ↓
The lock reference cannot be changed

Same lock = synchronization
Thread 1:
synchronized (lock) {
    counter++;
}
Thread 2:
synchronized (lock) {
    counter++;
}
Both use the same lock object.
             SAME LOCK
                🔒
             /      \
        Thread 1   Thread 2
           ↓          ↓
         wait?      wait?
Only one enters at a time.

Different locks = no protection between them
Object lock1 = new Object();
Object lock2 = new Object();
Thread 1:
synchronized (lock1) {
    counter++;
}
Thread 2:
synchronized (lock2) {
    counter++;
}
They are using different objects.
Therefore, they don't block each other.
Thread 1 → 🔒 lock1 → counter++

Thread 2 → 🔒 lock2 → counter++
Both can enter simultaneously.
So remember:
synchronized only provides mutual exclusion between threads that synchronize on the SAME monitor object.


synchronized primarily gives you two important properties:
1. Mutual exclusion
Only one thread at a time can execute the protected section under that monitor.
2. Memory visibility
Changes made by one thread before releasing the monitor become visible to another thread after it successfully acquires the same monitor.
This is an important part of Java's memory model.

synchronized
     ↓
one thread at a time
     +
proper memory visibility

A lock is a mechanism used to control access to shared resources.
There are two related concepts you'll encounter:
Intrinsic monitor
Used automatically by:
synchronized
Explicit lock
Java also provides:
java.util.concurrent.locks.Lock
For example:
Lock lock = new ReentrantLock();
Then:
lock.lock();

try {
    counter++;
} finally {
    lock.unlock();
}
Both are used for controlling access to shared resources, but they are different Java mechanisms.

Lock provides additional capabilities that synchronized doesn't directly provide, such as:
tryLock()
interruptible lock acquisition
more flexible locking patterns
Condition objects

Why finally?
If you had:
lock.lock();

counter++;

lock.unlock();
if counter++ somehow threw an exception, the unlock() might never execute.
The lock could remain held.
Using:
finally
ensures:
Something goes wrong
       ↓
finally executes
       ↓
unlock()
So the lock gets released.

| `synchronized`                           | `Lock`                                      |
| ---------------------------------------- | ------------------------------------------- |
| Built into Java language                 | Interface from `java.util.concurrent.locks` |
| Simple                                   | More flexible                               |
| Lock automatically released              | Must explicitly unlock                      |
| Less code                                | More control                                |
| Good for straightforward synchronization | Useful for advanced locking requirements    |

if your Java work requires advanced concurrency, you'll encounter ReentrantLock, tryLock(), conditions, etc.
 */

/*
Think of this:
private final Object lock = new Object();
as:
"I have a private key."
Then:
synchronized (lock) {
    counter++;
}
means:
"Only the thread holding this key can execute this section at a time."
And:
final
means:
"This key will always remain the same key."
And:
private
means:
"Only my class controls this key."
 */
