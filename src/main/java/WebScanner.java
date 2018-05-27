import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class WebScanner implements TextScanner {
    private ArrayList<String> sourceList;
    private ResultWriter resultWriter;
    private static final Logger logger = Logger.getRootLogger();
    private int threadsAmount;
    private int maxTimeoutMinutes;
    private String searchFile;
    private String sourceFile;

    public WebScanner(int threadsAmount, int maxTimeoutMinutes, String searchFile, String sourceFile, String resultFile) {
        this.sourceList = new ArrayList<>();
        this.resultWriter = new ResultWriter(resultFile);
        this.threadsAmount = threadsAmount;
        this.maxTimeoutMinutes = maxTimeoutMinutes;
        this.searchFile = searchFile;
        this.sourceFile = sourceFile;
    }

    @Override
    public void scan() throws Exception {
        try {
            WebScannerThread.searchList = FileIO.toArrayList(searchFile);
            if (WebScannerThread.searchList.size()==0)
                return;
            setSourceList(sourceFile);
            resultWriter.init();
            executeThreads(threadsAmount, maxTimeoutMinutes);
        } finally {
            resultWriter.destroy();
        }
    }

    private void setSourceList(String sourceFile) throws Exception {
        sourceList.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "Cp1251"))) {
            String line;
            while ((line = reader.readLine()) != null)
                sourceList.add(line);
        }
    }

    private void executeThreads(int threadsAmount, int maxTimeoutMinutes) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        for (String webAddress : sourceList) {
            executor.execute(new WebScannerThread(webAddress, resultWriter));
        }
        executor.shutdown();
            if (!executor.awaitTermination(maxTimeoutMinutes, TimeUnit.MINUTES))
                logger.info("interrupted by awaitTermination");
    }

}
