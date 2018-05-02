import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestFileIO {
    private static final Logger logger = Logger.getRootLogger();
    private static final Logger errLogger = Logger.getLogger("test_errors");
    private static final String filePath = "src/test/resources/testSearchList.txt";
    private static final List<String> testList = Arrays.asList("line1", "line2", "line3");

    @BeforeClass
    public static void createTestResource() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "Cp1251"))) {
            for (String s : testList) {
                writer.write(s + System.lineSeparator());
            }
        } catch (IOException e) {
            errLogger.error(e);
        }
    }

    @Test
    public void testGetList() {
        List<String> result = FileIO.toArrayList(filePath);
        assertEquals("getSearchList() - fail", testList, result);
    }

}
