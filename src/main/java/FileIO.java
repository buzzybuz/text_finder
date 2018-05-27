import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileIO {

    public static ArrayList<String> toArrayList(String searchFile) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(searchFile), "Cp1251"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line.trim());
            }
        }
        return result;
    }
}
