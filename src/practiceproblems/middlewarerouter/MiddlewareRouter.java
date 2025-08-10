package src.practiceproblems.middlewarerouter;

import java.util.HashMap;
import java.util.Map;

class TrieNode{
    Map<String, TrieNode> children;
    String value;

    TrieNode(){
        children = new HashMap<>();
        value = null;
    }
}

public class MiddlewareRouter {
    public static void main(String[] args) {
        Router router = new Router();

        router.addRoute("/foo", "fooResult");
        router.addRoute("/bar/a/baz", "barResult");
        router.addRoute("/bar/static/baz", "staticResult");
        try {
            System.out.println(router.callRoute("/foo")); // fooResult
            System.out.println(router.callRoute("/bar/a/baz")); // barResult
            System.out.println(router.callRoute("/bar/*/baz")); // barResult (wildcard in lookup)
            System.out.println(router.callRoute("/bar/static/baz")); // staticResult
            System.out.println(router.callRoute("/bar/*/baz")); // staticResult (second wildcard match)
            System.out.println(router.callRoute("/bar/a/b")); // Should throw exception
        } catch (RouteNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
