package src.practiceproblems.filesystemwildcard2;

import java.util.*;

abstract class FileSystemEntry {
    String name;
    Folder parent;

    FileSystemEntry(String name, Folder parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Folder getParent() {
        return parent;
    }

    public String getPath() {
        if(parent == null) {
            return "/";
        }
        String parentPath = parent.getPath();
        return parentPath.equals("/")
                ? "/" + name
                : parentPath + "/" + name;
    }
}

class Folder extends FileSystemEntry{
    Map<String, FileSystemEntry> children;

    Folder(String name, Folder parent) {
        super(name, parent);
        children = new HashMap<>();
    }

    public void addChild(FileSystemEntry entry) {
        if(entry != null) {
            children.put(entry.getName(), entry);
        }
    }

    public FileSystemEntry getChild(String name) {
        return children.getOrDefault(name, null);
    }

    public Map<String, FileSystemEntry> getChildren() {
        return children;
    }
}

class File extends FileSystemEntry {
    String content;
    File(String name, Folder parent, String content) {
        super(name, parent);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

class FileSystem {
    Folder root;
    Folder current;

    FileSystem() {
        root = new Folder("", null);
        current = root;
    }

    public void mkdir(String path) {
        if(path==null || path.isEmpty()) {
            throw new IllegalArgumentException("Invalid path for get");
        }

        String[] segments = parseAndSplitPath(path);

        int n = segments.length;
        String folderName = segments[n-1];

        if(folderName == null || folderName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        Folder parentNode = getParent(path);

        if(parentNode.getChild(folderName) != null) {
            throw new IllegalArgumentException(folderName + " already exists");
        }

        Folder newFolder = new Folder(folderName, parentNode);
        parentNode.addChild(newFolder);
    }

    public void touch(String path, String content) {
        if(path==null || path.isEmpty()) {
            throw new IllegalArgumentException("Invalid path for get");
        }

        String[] segments = parseAndSplitPath(path);
        int n = segments.length;
        String fileName = segments[n-1];

        if(fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        Folder parentNode = getParent(path);

        if(parentNode.getChild(fileName) != null) {
            throw new IllegalArgumentException(fileName + " already exists");
        }

        File newFile = new File(fileName, parentNode, content);
        parentNode.addChild(newFile);
    }

    public String pwd() {
        return current.getPath();
    }

    public void cd(String path) {
        if(path==null || path.isEmpty()) {
            throw new IllegalArgumentException("Path is empty");
        }

        Folder node;
        int i=0;
        if(path.startsWith("/")){
            node = root;
            path = path.substring(1);
        }
        else{
            node = current;
        }

        String[] segments = parseAndSplitPath(path);
        Queue<Folder> q = new LinkedList<>();
        q.offer(node);


        for(;i<segments.length;i++) {
            int n = q.size();
            String segment = segments[i];

            if(n==0) {
                break;
            }

            for(int j=0;j<n;j++) {
                Folder curr = q.peek();
                q.poll();

                switch (segment) {
                    case ".." :
                        if(curr.getParent() != null){
                            q.offer(curr.getParent());
                        }
                        break;
                    case "." :
                        q.offer(curr);
                        break;
                    case "*" :
                        for(FileSystemEntry entry: curr.getChildren().values()) {
                            if(entry instanceof Folder) {
                                q.offer((Folder) entry);
                            }
                        }
                        break;
                    default:
                        FileSystemEntry next = curr.getChild(segment);
                        if(next != null && next instanceof Folder) {
                            q.offer((Folder) next);
                        }
                        break;
                }
            }
        }

        if(q.size()==1){
            current = q.peek();
        }
        else{
            throw new IllegalArgumentException("Invalid path");
        }
    }


    private Folder getParent(String path) {
        int i=0;
        Folder node;
        if(path.startsWith("/")){
            node = root;
            path = path.substring(1);
        }
        else{
            node = current;
        }
        String[] segments = parseAndSplitPath(path);

        for(;i<segments.length-1;i++) {
            String segment = segments[i];
            FileSystemEntry nextNode = node.getChild(segment);

            if(nextNode==null) {
                throw new IllegalArgumentException("Invalid path " + segment + " does not exist");
            }
            if(!(nextNode instanceof Folder)) {
                throw new IllegalArgumentException(nextNode.getName() + "is not a folder");
            }

            node = (Folder) nextNode;
        }
        return node;
    }

    private String[] parseAndSplitPath(String path) {
        return Arrays.stream(path.split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }
}


public class FileSystemWildcard {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        FileSystem fs = new FileSystem();

        System.out.println("Mini File System Started");
        System.out.println("Commands:");
        System.out.println("mkdir <path>");
        System.out.println("touch <path> <content>");
        System.out.println("cd <path>");
        System.out.println("pwd");
        System.out.println("exit");

        while(true) {
            System.out.print(fs.pwd() + " $ ");

            String input = sc.nextLine();
            String[] params = input.split(" ", 3);

            String command = params[0];

            switch(command) {
                case "pwd":
                    System.out.println(fs.pwd());
                    break;
                case "mkdir":
                    fs.mkdir(params[1]);
                    break;
                case "touch":
                    fs.touch(params[1], params[2]);
                    break;
                case "cd":
                    fs.cd(params[1]);
                    break;
                case "exit":
                    sc.close();
                    return;
                default:
                    throw new IllegalArgumentException("Invalid operatin");
            }
        }
    }

}
