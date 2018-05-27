import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class FileScanner implements TextScanner {
    private ArrayList<File> sourceList;
    private ResultWriter resultWriter;
    private static final Logger logger = Logger.getRootLogger();
    private int threadsAmount;
    private int maxTimeoutMinutes;
    private String searchFile;
    private String sourceDir;

    public FileScanner(int threadsAmount, int maxTimeoutMinutes, String searchFile, String sourceDir, String resultFile) {
        this.sourceList = new ArrayList<>();
        this.resultWriter = new ResultWriter(resultFile);
        this.threadsAmount = threadsAmount;
        this.maxTimeoutMinutes = maxTimeoutMinutes;
        this.searchFile = searchFile;
        this.sourceDir = sourceDir;
    }

    @Override
    public void scan() throws Exception {
        try {
            FileScannerThread.searchList = FileIO.toArrayList(searchFile);
            if (FileScannerThread.searchList.size() == 0)
                return;
            setSourceList(sourceDir);
            resultWriter.init();
            executeThreads(threadsAmount, maxTimeoutMinutes);
        } finally {
            resultWriter.destroy();
        }
    }

    private void setSourceList(String sourceDir) throws Exception {
        sourceList.clear();
        File dir = new File(sourceDir);
        if (dir.exists()) {
            Collections.addAll(sourceList, Objects.requireNonNull(dir.listFiles()));
        } else {
            logger.info("directory \"" + sourceDir + "\" not found");
        }

    }

    private void executeThreads(int threadsAmount, int maxTimeoutMinutes) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        for (File file : sourceList) {
            executor.execute(new FileScannerThread(file, resultWriter));
        }
        executor.shutdown();
        if (!executor.awaitTermination(maxTimeoutMinutes, TimeUnit.MINUTES))
            logger.info("interrupted by awaitTermination");
    }


}

