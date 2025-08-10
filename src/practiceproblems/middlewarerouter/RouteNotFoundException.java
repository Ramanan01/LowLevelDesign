package src.practiceproblems.middlewarerouter;

public class RouteNotFoundException extends Exception {
    public RouteNotFoundException(String message){
        super(message);
    }
}
