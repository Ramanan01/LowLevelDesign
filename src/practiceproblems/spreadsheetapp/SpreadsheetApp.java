package src.practiceproblems.spreadsheetapp;


import java.util.*;
import java.util.regex.*;

class Cell {
    String input;
    public int value;

    public Set<String> dependants;
    public Set<String> dependencies;

    public Cell() {
        this.dependants = new HashSet<>();
        this.dependencies = new HashSet<>();
    }
}

class FormulaEvaluator {
    public static Pattern pattern = Pattern.compile("[A-Z]+\\d+");

    public Set<String> extractReferences(String input) {
        Set<String> refs = new HashSet<>();
        Matcher matcher = pattern.matcher(input);

        while(matcher.find()) {
            refs.add(matcher.group());
        }

        return refs;
    }

    public int evaluate(String input, Map<String, Cell> cells) {
        if(!input.startsWith("=")) {
            return Integer.parseInt(input);
        }

        String expression = input.substring(1).trim();
        String[] parts = expression.split("\\s+");
        int operand1 = getValue(parts[0].trim(), cells);
        int operand2 = getValue(parts[2].trim(), cells);
        String operator = parts[1].trim();

        switch(operator) {
            case "+": return operand1 + operand2;
            case "-": return operand1 - operand2;
            case "*": return operand1 * operand2;
            case "/":
                if(operand2==0) {
                    throw new IllegalArgumentException("Division by 0");
                }
                return operand1/operand2;
            default: throw new IllegalArgumentException("Enter valid expression");
        }
    }

    private int getValue(String operand, Map<String, Cell> cells) {
        if(Character.isLetter(operand.charAt(0))) {
            return cells.getOrDefault(operand, new Cell()).value;
        }

        return Integer.parseInt(operand);
    }

}

class SpreadSheet {
    Map<String, Cell> cells = new HashMap<>();
    private final FormulaEvaluator formulaEvaluator = new FormulaEvaluator();

    public void setCell(String cellId, String input) {
        Cell cell = cells.computeIfAbsent(cellId, x-> new Cell());
        for(String oldDependencyId: cell.dependencies) {
            cells.get(oldDependencyId).dependants.remove(cellId);
        }
        cell.dependencies.clear();
        cell.input = input;

        Set<String> dependencies = formulaEvaluator.extractReferences(input);

        for(String newDependencyId: dependencies) {
            Cell newDependency = cells.computeIfAbsent(newDependencyId, x-> new Cell());
            newDependency.dependants.add(cellId);
        }

        if(hasCycle()) {
            for(String dependencyId : dependencies) {
                cells.get(dependencyId).dependants.remove(cellId);
            }

            throw new IllegalArgumentException("Circular dependency");
        }

        propagate(cellId);
    }

    private boolean hasCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for(String cell: cells.keySet()) {
            if(dfs(cell, visiting, visited)){
                return true;
            }
        }
        return false;
    }

    private boolean dfs(String cell, Set<String> visiting, Set<String> visited) {
        if(visiting.contains(cell)) {
            return true;
        }
        if(visited.contains(cell)) {
            return false;
        }

        visiting.add(cell);

        for(String dependant: cells.get(cell).dependants) {
            if(dfs(dependant, visiting, visited)) {
                return true;
            }
        }

        visiting.remove(cell);

        visited.add(cell);

        return false;
    }

    private void evaluateCell(String cellId) {
        Cell cell = cells.get(cellId);
        cell.value = formulaEvaluator.evaluate(cell.input, cells);
    }

    private void propagate(String cellId) {
        Queue<String> q = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        q.offer(cellId);

        while(!q.isEmpty()) {
            String id = q.poll();
            Cell cell = cells.get(id);
            evaluateCell(id);
            for(String dependant: cell.dependants) {
                if(!visited.contains(dependant)) {
                    visited.add(dependant);
                    q.offer(dependant);
                }
            }
        }
    }
}

public class SpreadsheetApp {

    public static void main(String[] args) {

        SpreadSheet sheet = new SpreadSheet();

        // A1 = 10
        sheet.setCell("A1", "10");

        // B1 = A1 + 5
        sheet.setCell("B1", "=A1 + 5");

        // C1 = B1 * 2
        sheet.setCell("C1", "=B1 * 2");

        System.out.println("Initial values:");
        System.out.println("A1 = " + sheet.cells.get("A1").value);
        System.out.println("B1 = " + sheet.cells.get("B1").value);
        System.out.println("C1 = " + sheet.cells.get("C1").value);

        /*
            Expected:

            A1 = 10
            B1 = 15
            C1 = 30
        */


        // Update A1
        sheet.setCell("A1", "20");

        System.out.println("\nAfter changing A1:");

        System.out.println("A1 = " + sheet.cells.get("A1").value);
        System.out.println("B1 = " + sheet.cells.get("B1").value);
        System.out.println("C1 = " + sheet.cells.get("C1").value);

        /*
            Expected:

            A1 = 20
            B1 = 25
            C1 = 50
        */


        // Circular dependency test
        try {

            sheet.setCell(
                    "A1",
                    "=C1 + 1"
            );

        } catch(Exception e) {

            System.out.println(
                    "\nException: "
                            + e.getMessage()
            );
        }

        /*
            Expected:

            Exception:
            Circular dependency
        */
    }
}