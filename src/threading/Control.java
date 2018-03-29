package threading;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Control {
    private final static String DIR = "txt";
    private final static String EXT = ".txt";
    //private static final ArrayList<File> alFiles = new ArrayList<>();
    private static File[] files;
    private static int threadsNum;
    private static int parts, rest, start=0, end=0;
    private long[][] durations; //thread index, duration

    private static void getTXTFiles(String path) {
        ArrayList<File> alFiles = new ArrayList<>();
        File f1 = new File(path);
        if (f1.isDirectory()) {
            for (String s : f1.list()) {
                File f = new File(path + "\\" + s);
                if (f.isDirectory()) {
                    getTXTFiles(f.getAbsolutePath());
                } else {
                    if (f.getName().endsWith(EXT)) {
                        alFiles.add(f);
                    }
                }
            }
        } else {
            System.out.println(path + " is not a directory");
        }
        if (alFiles.size() == 0) {
            System.out.println("No txt-files");
        }
        File[] arrFiles = new File[alFiles.size()];
        for (int i=0; i<alFiles.size(); i++) {
            arrFiles[i] = alFiles.get(i);
        }
        files = arrFiles;
    }

    private static void setThreadsNum() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type the number of threads: ");
        threadsNum = scanner.nextInt(); //verify value?
        if (threadsNum > files.length || threadsNum <= 0) {
            threadsNum = files.length;
        }
    }

    private File[] getPart() {
        File[] part = null;
        if (rest == 0) {
            part = new File[parts];
        } else {
            part = new File[parts+1];
            rest--;
        }
        for (int i=0; i<part.length; i++) { // not finished
            part[i] = files[start++];
        }
        return part;
    }

    public static void searchTXT(String path) {
        getTXTFiles(path);
        setThreadsNum();
        parts = files.length / threadsNum;
        rest = files.length % threadsNum;
        // TODO: Divide files between threads - getPart()

        SearchThread[] arrThreads = new SearchThread[threadsNum];
        for (SearchThread st: arrThreads) {
            st = new SearchThread();
        }
        new SearchThread(files);
    }
}
