package src.designpatterns.structural.adapter;

public class ExternalLoggerAdapter implements AppLogger{
    private final ExternalLogger externalLogger;

    public ExternalLoggerAdapter(ExternalLogger externalLogger){
        this.externalLogger = externalLogger;
    }

    @Override
    public void logInfo(String message) {
        externalLogger.info(message);
    }

    @Override
    public void logError(String message) {
        externalLogger.error(message);
    }
}
