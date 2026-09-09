package thread;

public class LetterTask implements Runnable {

    @Override
    public void run() {

        for (char ch = 'A'; ch <= 'J'; ch++) {

            System.out.println(
                    Thread.currentThread().getName() + " : " + ch
            );

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Letter thread interrupted.");
            }
        }
    }
}
