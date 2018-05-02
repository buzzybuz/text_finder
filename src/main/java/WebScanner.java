import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class WebScanner {
    private ArrayList<String> sourceList;
    private ResultWriter resultWriter;
    private static final Logger errLogger = Logger.getLogger("errors");

    WebScanner(int threadsAmount, int maxTimeoutMinutes, String searchFile, String sourceFile, String resultFile) {
        sourceList = new ArrayList<>();
        resultWriter = new ResultWriter(resultFile);

        try {
            WebScannerThread.searchList = FileIO.toArrayList(searchFile);
            if (WebScannerThread.searchList.size()==0)
                return;
            setSourceList(sourceFile);
            executeThreads(threadsAmount, maxTimeoutMinutes);
        } catch (Exception e) {
            errLogger.error(e);
        } finally {
            resultWriter.closeOutputStream();
        }
    }

    private void setSourceList(String sourceFile) {
        sourceList.clear();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "Cp1251"))) {
            String line;
            while ((line = reader.readLine()) != null)
                sourceList.add(line);
        } catch (IOException e) {
            errLogger.error(e);
        }
    }

    private void executeThreads(int threadsAmount, int maxTimeoutMinutes) {
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        for (String webAddress : sourceList) {
            executor.execute(new WebScannerThread(webAddress, resultWriter));
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
