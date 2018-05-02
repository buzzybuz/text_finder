import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class FileScanner {
    private ArrayList<File> sourceList;
    private ResultWriter resultWriter;
    private static final Logger errLogger = Logger.getLogger("errors");


    FileScanner(int threadsAmount, int maxTimeoutMinutes, String searchFile, String sourceDir, String resultFile) {
        sourceList = new ArrayList<>();
        resultWriter = new ResultWriter(resultFile);

        try {
            FileScannerThread.searchList= FileIO.toArrayList(searchFile);
            if (FileScannerThread.searchList.size()==0)
                return;
            setSourceList(sourceDir);
            executeThreads(threadsAmount, maxTimeoutMinutes);
        } catch (Exception e) {
            errLogger.error(e);
        } finally {
            resultWriter.closeOutputStream();
        }
    }

    private void setSourceList(String sourceDir) {
        sourceList.clear();
        Collections.addAll(sourceList, Objects.requireNonNull(new File(sourceDir).listFiles()));
    }

    private void executeThreads(int threadsAmount, int maxTimeoutMinutes) {
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        for (File file : sourceList) {
            executor.execute(new FileScannerThread(file, resultWriter));
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(maxTimeoutMinutes, TimeUnit.MINUTES))
                System.out.println("interrupted by awaitTermination");
        } catch (InterruptedException e) {
            errLogger.error(e);
        }
    }
}

