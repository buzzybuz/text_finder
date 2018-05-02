import org.apache.log4j.Logger;
import java.io.*;

public class ResultWriter {
    private BufferedWriter writer;
    private static final Logger errLogger = Logger.getLogger("errors");

    ResultWriter(String resultFile) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile), "Cp1251"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            errLogger.error(e);
        }
    }

    public void closeOutputStream() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                errLogger.error(e);
            }
        }
    }

    public synchronized void writeSynchro(String source, String target) {
        try {
            writer.newLine();
            writer.write(source);
            writer.newLine();
            writer.write(target);
        } catch (IOException e) {
            errLogger.error(e);
        }
    }
}
