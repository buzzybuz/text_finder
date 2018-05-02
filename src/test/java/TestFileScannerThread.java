import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;

public class TestFileScannerThread {
    private static final Logger logger = Logger.getRootLogger();
    private static final Logger errLogger = Logger.getLogger("test_errors");
    private static final String textPath = "src/test/resources/testFileScannerThread.txt";
    private static final String resultPath = "src/test/resources/testFileScannerThreadResult.txt";
    private static final String targetText =
            "Block with word1. Block with word2? Block with word3! Block with word4. Block with word5 and word6?";
    private static final List<String> searchWords = new ArrayList<>(Arrays.asList("word1", "word3", "word5 and word6"));
    private final List<String> expectedList = Arrays.asList(
            "", "testFileScannerThread.txt", "Block with word1.", "testFileScannerThread.txt",
            "Block with word3!", "testFileScannerThread.txt", "Block with word5 and word6?");
    private ResultWriter resultWriter;
    private FileScannerThread fileScannerThread;

    @BeforeClass
    public static void createTestResource() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textPath), "Cp1251"))) {
            writer.write(targetText);
        } catch (IOException e) {
            errLogger.error(e);
        }
    }

    @BeforeClass
    public static void setSearchList() {
        FileScannerThread.searchList = (ArrayList<String>) searchWords;
    }

    @Before
    public void initClasses() {
        resultWriter = new ResultWriter(resultPath);
        fileScannerThread = new FileScannerThread(new File(textPath), resultWriter);
    }

    @Test
    public void testRun() {
        try {
            fileScannerThread.run();
        } finally {
            resultWriter.closeOutputStream();
        }
        List<String> result = FileIO.toArrayList(resultPath);
        assertEquals("FileScannerThread.run() - fail", expectedList, result);
    }

}
