import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class FileScannerThread extends Thread {
    public static ArrayList<String> searchList;
    private File sourceFile;
    private ResultWriter outputFile;
    private static final Logger logger = Logger.getRootLogger();
    private static final Logger errLogger = Logger.getLogger("errors");

    FileScannerThread(File sourceFile, ResultWriter outputFile) {
        this.sourceFile = sourceFile;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        logger.info("new file thread: " + this.getName());
        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "Cp1251"))) {

            StringBuilder stringBuilder = new StringBuilder();
            int chInt;
            char ch;
            String sentence;
            String fileName = sourceFile.getName();

            while ((chInt = reader.read()) != -1) {
                ch = (char) chInt;
                stringBuilder.append(ch);
                if ((ch == '.') || (ch == '!') || (ch == '?')) {
                    sentence = stringBuilder.toString();
                    stringBuilder.setLength(0);
                    for (String searchText : searchList) {
                        if (sentence.contains(searchText)) {
                            outputFile.writeSynchro(fileName, sentence.trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            errLogger.error(e);
        }
    }
}