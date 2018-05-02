import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileIO {
    private static final Logger errLogger = Logger.getLogger("errors");

    public static ArrayList<String> toArrayList(String searchFile) {
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(searchFile), "Cp1251"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line.trim());
            }
        } catch (IOException e) {
            errLogger.error(e);
        }
        return result;
    }
}
