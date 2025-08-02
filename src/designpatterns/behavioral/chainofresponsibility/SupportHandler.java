package src.designpatterns.behavioral.chainofresponsibility;

abstract public class SupportHandler {
    protected SupportHandler nextHandler;

    public void setNextHandler(SupportHandler nextHandler){
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(String issueType);
}
