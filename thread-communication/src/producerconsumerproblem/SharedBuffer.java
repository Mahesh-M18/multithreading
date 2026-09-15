package producerconsumerproblem;

public class SharedBuffer {

    private int item;
    private boolean available = false;

    public synchronized void produce(int value) throws InterruptedException {

        while (available) {
            wait();
        }

        item = value;
        available = true;

        System.out.println(Thread.currentThread().getName() + " produced : " + value);

        notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        while (!available) {
            wait();
        }

        int value = item;
        available = false;

        System.out.println(Thread.currentThread().getName() + " consumed : " + value);

        notifyAll();
    }
}
