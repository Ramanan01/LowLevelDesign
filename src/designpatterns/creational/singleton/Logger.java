package src.designpatterns.creational.singleton;

public class Logger {
    private static volatile Logger logger;

    private Logger() {
        System.out.println("Logger initialised");
    }

    static Logger getInstance(){
        if(logger == null) {
            synchronized (Logger.class) {
                logger = new Logger();
            }
        }
        return logger;
    }

    public void log(String message){
        System.out.println("[LOG] " + message);
    }
}
