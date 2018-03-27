import com.sun.javafx.collections.MappingChange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search {
    private final ArrayList<File> files;
    private final static String DIR = "txt";
    private final static String EXT = ".txt";
    private final static String SEARCH_STR = "Install";
    private final static int SEARCH_DELTA = SEARCH_STR.length();
    private final static int DEF_THREAD_NUM = 3;

    public Search() {
        /*this.DIR = DIR;
        this.SEARCH_STR = SEARCH_STR;*/
        files = new ArrayList<>();
    }

    public void run() {
        HashMap<File, HashMap<Integer, Integer>> hm = new HashMap<>();
        HashMap<Integer, Integer> hmPos = new HashMap<>(1);
        hmPos.put(0, 0);
        //ArrayList<File> al = getTXTFiles(DIR);
        for (File f : getTXTFiles(DIR)) {
            hm.put(f, hmPos);
        }
        findString(hm);
    }

    private ArrayList<File> getTXTFiles(String path) {
        File f1 = new File(path);
        if (f1.isDirectory()) {
            for (String s : f1.list()) {
                File f = new File(path + "\\" + s);
                if (f.isDirectory()) {
                    getTXTFiles(f.getAbsolutePath());
                } else {
                    if (f.getName().endsWith(EXT)) {
                        System.out.println("Add " + f.getAbsolutePath());
                        files.add(f);
                    }
                }
            }
        } else {
            System.out.println(path + " is not a directory");
        }
        if (files.size() == 0) {
            System.out.println("No txt-files");
        }
        return files;
    }

    private void findString(HashMap<File, HashMap<Integer, Integer>> map) { //File f, int start, int end
        //Set<Map.Entry<File, Integer[][]>> set = map.entrySet();
        for(Map.Entry<File, HashMap<Integer, Integer>> me: map.entrySet()) {
            File file = me.getKey();
            //Set<Map.Entry<Integer, Integer>> entries = me.getValue().entrySet();
            Map.Entry<Integer, Integer> positions = me.getValue().entrySet().iterator().next();
            int start = positions.getValue();
            int end = positions.getKey();
            List<String> stringList = null;
            String text = "";
            try {
                stringList = Files.readAllLines(Paths.get(file.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(String s: stringList) {
                text += (s + "\n");
            }
            System.out.println("\n-------------\n" + text);
        }
    }
}
