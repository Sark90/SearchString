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
            sb.append("<thread ").append(getName()).append(">\n");
            sb.append(file.getAbsolutePath()).append("\n");
            countEntries(text);
            sb.append("Entries: ").append(entries).append("\n");
            entries = 0; // is there a better place for counter reset?
            sb.append("--------------\n");
        }
        System.out.print(sb);
    }

    private void countEntries(String s) {
        int index = s.indexOf(expression);
        if (index != -1) {
            sb.append("Index ").append(++entries).append(": ").append(index).append("\n");
            countEntries(s.substring(index + expression.length()));
        }
    }
}
