public class ThreadCommunicationLearning {
}

/*
Why Do Threads Need to Communicate?
Imagine two threads:
Producer Thread
      ↓
produces data
      ↓
Shared Buffer
      ↓
Consumer Thread
      ↓
consumes data
The producer and consumer need to coordinate.
For example:
Producer → "I have produced an item."
Consumer → "I need an item."
The problem is that the producer might produce faster than the consumer can consume,
or the consumer might try to consume when there is nothing available.
We need a mechanism for them to communicate.
That's where:
wait()
notify()
become important.

Without join():
Main → starts Worker
Main → continues immediately
Worker → still working
With join():
Main → starts Worker
Main → waits
Worker → finishes
Main → continues


| `join()`                               | `wait()`                                              |
| -------------------------------------- | ----------------------------------------------------- |
| Waits for another thread to terminate  | Waits for a condition/notification                    |
| Used for thread completion             | Used for thread communication                         |
| Called on a `Thread` object            | Called on an object's monitor                         |
| Example: `thread.join()`               | Example: `lock.wait()`                                |
| Commonly used by a coordinating thread | Commonly used by producer/consumer-style coordination |

join()
  ↓
"Wait until that thread finishes."

wait()
  ↓
"Wait until another thread signals that I may continue."


wait()
wait() is a method of the Object class.
That's important:
Object
  ↓
wait()
notify()
notifyAll()
It is not a method defined only by Thread.
Example:
synchronized (lock) {
    lock.wait();
}
When a thread calls wait() on an object's monitor, it:
Releases that object's monitor.
Enters the waiting state.
Waits until it is notified/interrupted (or otherwise awakened according to the waiting semantics).

Why Does wait() Release the Lock?

Thread A
   ↓
holds lock
   ↓
needs to wait
If Thread A kept the lock while waiting:
Thread A → holds lock
Thread A → waits
Thread B → needs same lock
Thread B → cannot enter
Now nobody can make progress.
Therefore, wait() releases the object's monitor while the thread waits.
Conceptually:
Thread A
   ↓
acquires lock
   ↓
wait()
   ↓
releases lock
   ↓
WAITING
Another thread can then acquire the lock.

notify()
notify() is used to signal one thread waiting on the same object's monitor.
Example:
synchronized (lock) {
    lock.notify();
}
Conceptually:
Thread A
   ↓
wait()
   ↓
WAITING
   ↑
   |
notify()
   |
Thread B
But there is an important detail:
notify() does not immediately hand the lock to the waiting thread.
The notified thread becomes eligible to compete for the monitor, and it can continue only after
it successfully reacquires that monitor.


notifyAll()
Java also provides:
notifyAll()
It wakes all threads waiting on that object's monitor so that they can compete to reacquire the monitor.
Difference:
notify()
   ↓
one waiting thread is notified
notifyAll()
   ↓
all waiting threads are notified

You cannot normally do this:
lock.wait();
from arbitrary code.
The current thread must own that object's monitor.
So this is correct:
synchronized (lock) {
    lock.wait();
}
Similarly:
synchronized (lock) {
    lock.notify();
}
Without owning the monitor, Java throws:
IllegalMonitorStateException


wait() / notify() Relationship
Think about this simple situation:
Consumer
   ↓
No item available
   ↓
wait()
   ↓
releases lock
   ↓
WAITING
Then:
Producer
   ↓
produces item
   ↓
notify()
   ↓
Consumer becomes eligible to continue
Conceptually:
             Shared Buffer
                  |
        ┌─────────┴─────────┐
        ↓                   ↓
    Producer            Consumer
        |                   |
    produces             waits
        |                   |
        └── notify() ───────┘
                            ↓
                         consumes

 */



/*
Producer-Consumer Problem
This is the classic example of thread communication.
We have:
Producer
Produces items.
Producer
   ↓
Item 1
Item 2
Item 3
...
Consumer
Consumes items.
Consumer
   ↓
Item 1
Item 2
Item 3
...
Between them is a shared buffer.
Producer
    ↓
┌─────────────┐
│ Shared      │
│ Buffer      │
└─────────────┘
    ↓
Consumer

The Buffer Has Limited Capacity
Suppose our buffer can hold only 3 items.
Capacity = 3
If it is full:
┌─────────────┐
│ A │ B │ C │
└─────────────┘
the producer cannot add another item.
So:
Producer
   ↓
Buffer full
   ↓
wait()
Similarly, if the buffer is empty:
┌─────────────┐
│             │
└─────────────┘
the consumer cannot consume anything.
So:
Consumer
   ↓
Buffer empty
   ↓
wait()
This is exactly where thread communication becomes useful.

Producer-Consumer Flow
Producer
Check buffer
     ↓
Is buffer full?
     ↓
Yes → wait()
     ↓
No
     ↓
Add item
     ↓
notify
Consumer
Check buffer
     ↓
Is buffer empty?
     ↓
Yes → wait()
     ↓
No
     ↓
Remove item
     ↓
notify
The shared buffer is protected with synchronization.

Why do we use while, not if?
This is a very important Java threading rule.
You have:
while (available) {
    wait();
}
and:
while (!available) {
    wait();
}
You could be tempted to write:
if (available) {
    wait();
}
But while is preferred and important because after a thread wakes up, it must check the condition again.
For example:
Thread waits
   ↓
notifyAll()
   ↓
Thread wakes up
   ↓
Check condition again
   ↓
Still not safe?
   ↓
wait again
So:
wait() should generally be used inside a while loop that checks the condition.
 */
