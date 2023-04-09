package runnable;

import com.sun.tools.javac.Main;
import service.FileWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainRun implements Runnable {

    static String[] actors = new String[]{"Chandler", "Joey", "Monica", "Phoebe", "Rachel", "Ross"};
    static final String fileDataName = "script.txt";

    public static String controlMessage = "Done";

    static FileWork fileWork = new FileWork();
    static Map<String, ConcurrentLinkedQueue<String>> messengers = new HashMap<>();

    @Override
    public void run() {
        for (String actor:actors) {
            ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
            messengers.put(actor, messageQueue);
            new Thread(new CharacterRun(actor, messageQueue)).start();
        }

        try {
            ArrayList<String> script = fileWork.getFile(fileDataName);
            for (String line:script) {
                String[] replica = line.split(":");
                ConcurrentLinkedQueue<String> messenger = messengers.get(replica[0]);
                synchronized (messenger) {
                    messenger.add(replica[1]);
                    messenger.notify();
                    messenger.wait();
                }
            }
            for (String name:messengers.keySet()) {
                var m = messengers.get(name);
                synchronized (m) {
                    m.add(controlMessage);
                    m.notify();
                }
            }
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
