package thread;

public class MessageTask implements Runnable {

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {

            System.out.println(
                    Thread.currentThread().getName()
                            + " : Hello from Message Thread"
            );

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Message thread interrupted.");
            }
        }
    }
}
