import producerconsumerproblem.Producer;
import producerconsumerproblem.Consumer;
import producerconsumerproblem.SharedBuffer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SharedBuffer buffer = new SharedBuffer();

        Thread producer = new Thread(new Producer(buffer), "Producer-Thread");
        Thread consumer = new Thread(new Consumer(buffer), "Consumer-Thread");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Producer and Consumer Completed");
    }
}
