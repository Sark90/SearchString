package threading;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class SearchThread /*implements Runnable*/ extends Thread {
    //private Thread t;   //private?
    private final static String SEARCH_STR = "Install";
    private File[] files;
    private int entries = 0;
    private StringBuffer sb;

    private SearchThread() {}
    public SearchThread(File...files) {
        super();
        this.files = files;
        sb = new StringBuffer();
        start();
    }

    @Override
    public void run() {
        for(File file: files) {
            List<String> stringList = null;
            String text = "";
            try {
                stringList = Files.readAllLines(Paths.get(file.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String s : stringList) {
                text += (s + "\n");
            }
            //TODO: Result to another class field?
            sb.append(file.getAbsolutePath() + "\n");
            countEntries(text);
            sb.append("Entries: " + entries + "\n");
            entries = 0; // is there a better place for counter reset?
            sb.append("--------------\n");
        }
        System.out.println(sb);
    }

    private int countEntries(String s) {
        int index = s.indexOf(SEARCH_STR);
        if (index != -1) {
            sb.append("Index " + (++entries) + ": " + index + "\n");
            countEntries(s.substring(index + SEARCH_STR.length()));
        }
        return entries;
    }
}
