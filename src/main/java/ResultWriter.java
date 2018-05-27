import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ResultWriter {
    private String resultFile;
    private BufferedWriter writer;

    public ResultWriter(String resultFile) {
        this.resultFile = resultFile;
    }

    public void init() throws Exception {
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile), "Cp1251"));
    }

    public void destroy() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }

    public synchronized void writeSynchro(String source, String target) throws Exception {
        writer.newLine();
        writer.write(source);
        writer.newLine();
        writer.write(target);
    }
}
