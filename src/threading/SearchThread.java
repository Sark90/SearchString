package threading;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SearchThread extends Thread {
    private File[] files;
    private int entries = 0;
    private StringBuffer sb;
    private String expression = "Install";

    private SearchThread() {}
    public SearchThread(String name, String expression, File...files) {
        super(name);
        this.files = files;
        this.expression = expression;
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
            // Result to another class field?
            sb.append("<thread ").append(getName()).append(">\n");
            sb.append(file.getAbsolutePath() + "\n");
            countEntries(text);
            sb.append("Entries: " + entries + "\n");
            entries = 0; // is there a better place for counter reset?
            sb.append("--------------\n");
        }
        System.out.println(sb);
    }

    private void countEntries(String s) {

        int index = s.indexOf(expression);
        if (index != -1) {
            sb.append("Index " + (++entries) + ": " + index + "\n");
            countEntries(s.substring(index + expression.length()));
        }
    }
}
