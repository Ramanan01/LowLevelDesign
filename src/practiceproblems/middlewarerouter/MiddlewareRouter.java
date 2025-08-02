package src.practiceproblems.middlewarerouter;

import java.util.HashMap;
import java.util.Map;

class RouteNotFoundException extends Exception {
    public RouteNotFoundException(String message){
        super(message);
    }
}

class TrieNode{
    Map<String, TrieNode> children;
    String value;

    TrieNode(){
        children = new HashMap<>();
        value = null;
    }
}

class Router {
    TrieNode root;

    Router(){
        root = new TrieNode();
    }

    public void addRoute(String path, String value){
        if(path == null || path.isEmpty()){
            throw new IllegalArgumentException("Invalid path");
        }

        String[] parts = path.split("/");

        TrieNode node = root;
        for(String part : parts){
            if(part.isEmpty()){
                continue;
            }

            node.children.putIfAbsent(part, new TrieNode());
            node = node.children.get(part);
        }

        node.value = value;
    }

    public String callRoute(String path) throws RouteNotFoundException {
        if(path == null || path.isEmpty()){
            throw new IllegalArgumentException("Invalid path");
        }

        String[] parts = path.split("/");

        String value = dfs(root, parts, 0);
        if(value == null){
            throw new RouteNotFoundException("Route does not match");
        }

        return value;
    }

    private String dfs(TrieNode node, String[] parts, int ind){
        if(node == null) return null;

        if(ind == parts.length){
            // Base case: Fully matched path
            return node.value;
        }

        String part = parts[ind];
        if(part.isEmpty()){
            return dfs(node, parts, ind+1);
        }

        if(part.equals("*")){
            // Wildcard in input path: Try all children
            for(TrieNode child : node.children.values()){
                String pathValue = dfs(child, parts, ind + 1);
                if(pathValue != null){
                    return pathValue;
                }
            }
        } else {
            // Exact match
            if(node.children.containsKey(part)){
                return dfs(node.children.get(part), parts, ind + 1);
            }
        }

        // No match found
        return null;
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
