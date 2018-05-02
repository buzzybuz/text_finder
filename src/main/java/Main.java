import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger("app_start");

    public static void main(String[] args) {
        final int THREADS_AMOUNT = 8;
        final int MAX_EXECTIME_MINUTES = 1;
        final String TEXT_TO_SEARCH_PATH = "C://Users//Marat//Documents//forMyScanner//textToSearch.txt";
        //files
        final String FILE_SOURCE_DIR = "100x1000";
        final String FILE_SOURCE_PATH = "C://Users//Marat//Documents//forMyScanner//TextResources//FilesArrays//" + FILE_SOURCE_DIR;
        final String FILE_RESULT_PATH = FILE_SOURCE_PATH + "_synchro.txt";
        //websites
        final String WEB_SOURCE_PATH = "C://Users//Marat//Documents//forMyScanner//Web//weblist.txt";
        final String WEB_RESULT_PATH = "C://Users//Marat//Documents//forMyScanner//Web//Web_result.txt";

        long startTime = System.currentTimeMillis();
        logger.info("started");
//         new FileScanner(THREADS_AMOUNT, MAX_EXECTIME_MINUTES, TEXT_TO_SEARCH_PATH, FILE_SOURCE_PATH, FILE_RESULT_PATH);
        new WebScanner(THREADS_AMOUNT, MAX_EXECTIME_MINUTES, TEXT_TO_SEARCH_PATH, WEB_SOURCE_PATH, WEB_RESULT_PATH);
        logger.info("finished, total run time: " + (System.currentTimeMillis() - startTime + " ms."));
    }


}
