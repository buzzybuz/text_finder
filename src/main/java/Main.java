import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger logger = Logger.getLogger("app_start");
    private static final Logger errLogger = Logger.getLogger("errors");

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");
        TextScanner textScanner = (TextScanner) context.getBean("webScanner");

        long startTime = System.currentTimeMillis();
        logger.info("started");
        try {
            textScanner.scan();
        } catch (Exception e) {
            errLogger.error(e);
        }
        logger.info("finished, total run time: " + (System.currentTimeMillis() - startTime) + " ms.");
    }


}
