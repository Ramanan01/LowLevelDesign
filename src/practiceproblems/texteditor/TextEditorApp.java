package src.practiceproblems.texteditor;

import java.util.*;

class Document {
    List<StringBuilder> rows;

    public Document() {
        rows = new ArrayList<>();
    }

    private void checkAndInsertRows(int row) {
        while(rows.size() <= row) {
            rows.add(new StringBuilder());
        }
    }

    public void insert(int row, int col, String text) {
        checkAndInsertRows(row);
        StringBuilder currentRow = rows.get(row);

        if(currentRow.length() < col) {
            throw new IllegalArgumentException("Cannot insert at column. Pointer still towards the left of it");
        }

        currentRow.insert(col, text);
    }

    public String delete(int row, int col, int length) {
        checkAndInsertRows(row);
        StringBuilder currentRow = rows.get(row);

        if(currentRow.length() < col + length) {
            throw new IllegalArgumentException("Cannot delete at column. No characters at point");
        }

        String deletedString = currentRow.substring(col, col + length);
        currentRow.delete(col, col + length);

        return deletedString;
    }

    public void print() {
        for(int i=0;i<rows.size();i++) {
            System.out.println(i + " -> " + rows.get(i));
        }
        System.out.println();
    }
}

interface Command {
    void execute();
    void undo();
}

class InsertCommand implements Command {

    private final Document document;
    private final int row;
    private final int col;
    private final String text;

    public InsertCommand(Document document, int row, int col, String text) {
        this.document = document;
        this.row = row;
        this.col = col;
        this.text = text;
    }

    @Override
    public void execute() {
        document.insert(row, col, text);
    }

    @Override
    public void undo() {
        document.delete(row, col, text.length());
    }
}

class DeleteCommand implements Command {

    private Document document;
    private final int row;
    private final int col;
    private final int length;
    String deletedText;

    public DeleteCommand(Document document, int row, int col, int length) {
        this.document = document;
        this.row = row;
        this.col = col;
        this.length = length;
    }

    @Override
    public void execute() {
        deletedText = document.delete(row, col, length);
    }

    @Override
    public void undo() {
        document.insert(row, col, deletedText);
    }
}

class TextEditor {
    private final Deque<Command> undoStack;
    private final Deque<Command> redoStack;

    public TextEditor() {
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }

    public void execute(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if(undoStack.isEmpty()){
            return;
        }

        Command command = undoStack.pop();
        command.undo();

        redoStack.push(command);
    }

    public void redo() {
        if(redoStack.isEmpty()){
            return;
        }

        Command command = redoStack.pop();
        command.execute();

        undoStack.push(command);
    }
}

public class TextEditorApp {
}
