import org.apache.log4j.Logger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WebScannerThread extends Thread {
    public static ArrayList<String> searchList;
    private String webAddress;
    private ResultWriter outputFile;
    private static final Logger logger = Logger.getRootLogger();
    private static final Logger errLogger = Logger.getLogger("errors");

    WebScannerThread(String webAddress, ResultWriter outputFile) {
        this.webAddress = webAddress;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        logger.info("new web thread: " + this.getName());
        HttpURLConnection con = null;
        try {
            URL url = new URL(webAddress);
            con = (HttpURLConnection) url.openConnection();
//            try (Reader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "Cp1251"))) {
            try (Reader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder stringBuilder = new StringBuilder();
                int chInt;
                char ch;
                String sentence;

                while ((chInt = reader.read()) != -1) {
                    ch = (char) chInt;
                    stringBuilder.append(ch);
                    if ((ch == '.') || (ch == '!') || (ch == '?')) {
                        sentence = stringBuilder.toString();
                        stringBuilder.setLength(0);
                        for (String searchText : searchList) {
                            if (sentence.contains(searchText)) {
                                outputFile.writeSynchro(webAddress, sentence.trim());
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            errLogger.error(e);
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

}


