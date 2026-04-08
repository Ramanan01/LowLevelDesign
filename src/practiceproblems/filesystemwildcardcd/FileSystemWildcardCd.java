package src.practiceproblems.filesystemwildcardcd;

import java.util.*;

abstract class FileSystemEntry {
    private String name;
    private Folder parent;

    public FileSystemEntry(String name) {
        this.name = name;
        this.parent = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public String getPath(){
        if(parent == null){
            return name;
        }


        String parentPath = parent.getPath();

        if(parentPath.equals("/")){
            return parentPath + name;
        }

        return parentPath + "/" + name;
    }

    public abstract boolean isDirectory();
}


class Folder extends FileSystemEntry {
    private Map<String, FileSystemEntry> children;

    public Folder(String name) {
        super(name);
        children = new TreeMap<>();
    }

    public boolean addChild(FileSystemEntry entry){
        if(entry == null){
            return false;
        }

        if(children.containsKey(entry.getName())){
            return false;
        }

        children.put(entry.getName(), entry);
        entry.setParent(this);
        return true;
    }

    public FileSystemEntry removeChild(String name){
        FileSystemEntry entry = children.remove(name);
        if(entry != null){
            entry.setParent(null);
        }

        return entry;
    }

    public FileSystemEntry getChild(String name){
        return children.get(name);
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    public List<FileSystemEntry> getChildren(){
        return new ArrayList<>(children.values());
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}


class File extends FileSystemEntry {

    private String content;

    public File(String name) {
        super(name);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


public class FileSystemWildcardCd {
    private final Folder root;
    private Folder current;
    
    public FileSystemWildcardCd() {
        root = new Folder("/");
        current = root;
    }

    public void touch(String filePath) {
        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("Invalid file name");
        }

        Folder parent = resolveParent(filePath);

        if(parent == null) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String filename;
        int lastIndex = filePath.lastIndexOf("/");
        if(lastIndex == -1){
            filename = filePath;
        }
        else{
            filename = filePath.substring(lastIndex + 1);
        }

        File file = new File(filename);
        parent.addChild(file);
    }

    public void cd(String path) {
        if(path == null || path.isEmpty()){
            throw new IllegalArgumentException("Invalid path");
        }

        if(path.equals("/")){
            current = root;
            return;
        }

        Folder ref = path.startsWith("/") ? root : current;

        Queue<Folder> queue = new LinkedList<>();
        queue.add(ref);

        String[] segments = path.split("/");

        int i=0;
        int n= segments.length;

        while(!queue.isEmpty() && i<n) {
            int k=queue.size();
            String segment = segments[i];

            if(segment==null || segment.isEmpty() || segment.equals(".")){
                i++;
                continue;
            }

            Set<Folder> nextLevel = new HashSet<>();

            for(int j=0;j<k;j++) {
                Folder curr = queue.peek();
                queue.poll();

                Folder next;

                if(segment.equals("..")){
                    if(curr.getParent() != null){
                        nextLevel.add(curr.getParent());
                    }
                }
                else if(segment.equals("*")) {
                    for(FileSystemEntry child : curr.getChildren()) {
                        if(child.isDirectory()){
                            nextLevel.add((Folder) child);
                        }
                    }
                }
                else{
                    FileSystemEntry child = curr.getChild(segment);
                    if(child != null && child.isDirectory()){
                        nextLevel.add((Folder) curr.getChild(segment));
                    }
                }
            }

            queue.addAll(nextLevel);

            i++;
        }

        if(!queue.isEmpty()) {
            current = queue.peek();
        }
        else{
            throw new IllegalArgumentException("Cannot cd into specified path");
        }
    }

    private Folder resolveParent(String filePath) {
        if(filePath.equals("/")){
            return root;
        }

        if(filePath.lastIndexOf("/") == -1){
            return current;
        }

        String parentPath = filePath.substring(0, filePath.lastIndexOf("/"));
        if(parentPath.isEmpty()) {
            return root;
        }

        return navigateToFolder(parentPath);
    }

    private Folder navigateToFolder(String folderPath) {
        String[] segments = folderPath.split("/");
        Folder temp;
        if(folderPath.startsWith("/")){
            temp = root;
        }
        else{
            temp = current;
        }

        for(String segment: segments) {
            if(segment.isEmpty() || segment.equals(".")){
                continue;
            }

            if(segment.equals("..")){
                if(temp.getParent() != null){
                    temp = temp.getParent();
                }
                continue;
            }

            if(temp.getChild(segment) != null && temp.getChild(segment).isDirectory()) {
                temp = (Folder) temp.getChild(segment);
            }
            else{
                return null;
            }
        }
        return temp;
    }
    
}
