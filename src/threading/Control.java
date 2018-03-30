package threading;

import time.Timer;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Control {
    private final static String EXT = ".txt";
    private static final int TESTS_NUM = 3;
    private static String expression = "Install";
    private static final ArrayList<File> alFiles = new ArrayList<>();
    private static File[] files;
    private static int threadsNum;
    private static int parts, rest, index = 0;
    private static long[][] durations;
    private static Timer timer;

    private static void getTXTFiles(String path) {
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

    private static void getSettings() {
        System.out.print("\nType the expression (for example, \"Install\"): ");
        Scanner scanner = new Scanner(System.in);
        expression = scanner.next();
        System.out.print("Type the number of threads: ");
        threadsNum = scanner.nextInt(); //verify value?
        if (threadsNum > files.length || threadsNum <= 0) {
            threadsNum = files.length;
        }
    }

    private static void search() {
        parts = files.length / threadsNum;
        rest = files.length % threadsNum;
        System.out.println("Number of txt-files: " + files.length);
        System.out.println("Number of threads: " + threadsNum);
        System.out.println();
        SearchThread[] arrThreads = new SearchThread[threadsNum];
        timer = new Timer();
        timer.start();
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
    public static void searchTXT(String path) {
        getTXTFiles(path);
        showTXTFiles();
        getSettings();
        search();
    }

    public static void getOptimalNumOfThreads() {
        System.out.println("Optimal number of threads calculation");
        durations = new long[files.length+1][1];
        timer.reset();
        for (threadsNum=1; threadsNum<=files.length; threadsNum++) {
            System.out.println("\n\t--- " + threadsNum + " thread(s) ---");
            long avr = 0;
            for (int i=0; i<TESTS_NUM; i++) {
                System.out.println("\n\t\tTest #" + (i+1));
                index = 0;
                timer.start();
                search();
                timer.stop();
                avr += timer.getTime();
            }
            avr /= TESTS_NUM;
            System.out.println("Average time: " + avr);
            durations[threadsNum][0] = avr;
        }
        long min = durations[1][0];
        int optimal = 1;
        for (int i=2; i<durations.length; i++) {
            if (durations[i][0] < min) {
                min = durations[i][0];
                optimal = i;
            }
        }
        System.out.println("\nOptimal number of threads: " + optimal + " (average time: " + min + ")");
    }
}
