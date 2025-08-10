package src.practiceproblems.middlewarerouter;

public class Router {
    TrieNode root;

    public Router(){
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
