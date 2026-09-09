import thread.NumberTask;
import thread.LetterTask;
import thread.MessageTask;

public class Main {

    public static void main(String[] args) {

        Thread numberThread =
                new Thread(new NumberTask(), "Number-Thread");

        Thread letterThread =
                new Thread(new LetterTask(), "Letter-Thread");

        Thread messageThread =
                new Thread(new MessageTask(), "Message-Thread");

        numberThread.start();
        letterThread.start();
        messageThread.start();
    }
}