package runnable;

import com.sun.tools.javac.Main;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CharacterRun implements Runnable {

    final ConcurrentLinkedQueue<String> messenger;
    final String name;
    public static String controlMessage = MainRun.controlMessage;

    public CharacterRun(String name, ConcurrentLinkedQueue<String> messenger) {
        this.messenger = messenger;
        this.name = name;
    }

    public boolean sayPhrase() throws InterruptedException {
        synchronized (messenger) {
            while (messenger.isEmpty()) messenger.wait();
            String phrase = messenger.peek();
            messenger.remove();
            if (phrase.compareTo(controlMessage) == 0) return false;
            System.out.println(name + ":" + phrase);
            messenger.notify();
            return true;
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!sayPhrase()) break;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
