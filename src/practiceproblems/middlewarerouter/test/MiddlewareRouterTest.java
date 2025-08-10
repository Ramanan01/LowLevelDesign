package src.practiceproblems.middlewarerouter.test;

import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.practiceproblems.middlewarerouter.MiddlewareRouter;
import src.practiceproblems.middlewarerouter.RouteNotFoundException;
import src.practiceproblems.middlewarerouter.Router;
import static org.junit.jupiter.api.Assertions.*;

public class MiddlewareRouterTest {
    Router router;

    @BeforeEach
    void setup(){
        router = new Router();
    }

    @Test
    void testMiddlewareMatching() throws RouteNotFoundException {
        router.addRoute("/foo", "fooResult");
        router.addRoute("/bar/a/baz", "barResult");
        router.addRoute("/bar/static/baz", "staticResult");

        assertEquals("fooResult", router.callRoute("/foo"));
        assertEquals("staticResult", router.callRoute("/bar/static/*"));

        assertThrows(RouteNotFoundException.class, () -> router.callRoute("/bar/dynamic/baz"));
    }
}
