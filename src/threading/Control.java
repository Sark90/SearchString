package threading;

import time.Timer;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Control {
    private final static String EXT = ".txt";
    private static final ArrayList<File> alFiles = new ArrayList<>();
    private static File[] files;
    private static int threadsNum;
    private static int parts, rest, index = 0;

    private static void getTXTFiles(String path) {
        //ArrayList<File> alFiles = new ArrayList<>();
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

    private static void showTXTFiles() {
        System.out.println("txt-files:");
        for (File f: files) {
            System.out.println(f.getAbsolutePath());
        }
    }

    private static void setThreadsNum() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type the number of threads: ");
        threadsNum = scanner.nextInt(); //verify value?
        if (threadsNum > files.length || threadsNum <= 0) {
            threadsNum = files.length;
        }
    }

    private static File[] getPart() {
        File[] part;
        if (rest == 0) {
            part = new File[parts];
        } else {
            part = new File[parts+1];
            rest--;
        }
        for (int i=0; i<part.length; i++) {
            if (index >= files.length) {
                System.out.println("No more txt-files available");
                return null;
            }
            part[i] = files[index++];
        }
        return part;
    }

    public static void searchTXT(String path) {
        getTXTFiles(path);
        showTXTFiles();
        setThreadsNum();
        parts = files.length / threadsNum;
        rest = files.length % threadsNum;
        System.out.println("\nNumber of txt-files: " + files.length);
        System.out.println("Number of threads: " + threadsNum);
        SearchThread[] arrThreads = new SearchThread[threadsNum];
        System.out.print("\nType the expression (for example, \"Install\"): ");
        String expression = new Scanner(System.in).next();
        System.out.println();
        Timer timer = new Timer();
        Timer stdTimer = new Timer();
        timer.start();
        stdTimer.start();
        for (int i=0; i<arrThreads.length; i++) {
            arrThreads[i] = new SearchThread("t" + i, expression, getPart());
        }
        for (SearchThread st: arrThreads) {
            try {
                st.join();
            } catch (InterruptedException e) {
                System.out.println("Thread " + st.getName() + " interrupted.");
            }
        }
        timer.stop();
        System.out.println("Time passed: " + timer.getTime());
    }
}
