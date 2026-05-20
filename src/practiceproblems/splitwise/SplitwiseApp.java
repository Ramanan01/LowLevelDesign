package src.practiceproblems.splitwise;

import java.util.*;

class User {
    private final String id;
    private final String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

class Split {
    private final User user;
    private double amount;

    public Split(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

class Expense {
    private final String expenseId;
    private final User paidBy;
    private final List<Split> splits;

    public Expense(String expenseId, User paidBy, List<Split> splits) {
        this.expenseId = expenseId;
        this.paidBy = paidBy;
        this.splits = splits;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }
}

interface SplitStrategy {
    public void calculateSplits(List<Split> splits, double amount, User paidBy);
}

class EqualSplitStrategy implements SplitStrategy {

    @Override
    public void calculateSplits(List<Split> splits, double amount, User paidBy) {
        int n=splits.size();
        double share = Math.round((amount/n)*100.0)/100.0;
        double remaining = amount - share*(n);
        int i=0;
        for(Split split: splits) {
            if(split.getUser().equals(paidBy)) {
                continue;
            }
            if(i==n-1){
                split.setAmount(share + remaining);
            }
            else{
                split.setAmount(share);
            }
            i++;
        }
    }
}

class ExactSplitStrategy implements SplitStrategy{

    @Override
    public void calculateSplits(List<Split> splits, double amount, User paidBy) {
        double total = 0;
        for(Split split: splits) {
            total += split.getAmount();
        }

        if(Math.abs(total - amount) > 0.001) {
            throw new IllegalArgumentException("Invalid split");
        }
    }
}


public class SplitwiseApp {
    Map<String, User> users;
    Map<String, Expense> expenses;
    Map<String, Map<String, Double>> balances;

    public SplitwiseApp() {
        this.users = new HashMap<>();
        this.expenses = new HashMap<>();
        this.balances = new HashMap<>();
    }

    public void addUser(String userId, String name) {

        if(userId == null || userId.isEmpty()){
            throw new IllegalArgumentException("Invalid user Id");
        }

        if(users.containsKey(userId)) {
            throw new IllegalArgumentException("UserId already present");
        }

        User user = new User(userId, name);

        users.put(userId, user);
    }

    public void addExpense(String expenseId, String paidId, double amount, List<Split> splits, SplitStrategy splitStrategy) {
        if(expenseId == null || expenseId.isEmpty()){
            throw new IllegalArgumentException("Invalid user Id");
        }

        if(paidId == null || paidId.isEmpty()){
            throw new IllegalArgumentException("Invalid user Id");
        }

        if(expenses.containsKey(expenseId)) {
            throw new IllegalArgumentException("ExpenseId already exists");
        }

        if(!users.containsKey(paidId)) {
            throw new IllegalArgumentException("User does not exist");
        }

        for(Split split: splits) {
            if(!users.containsKey(split.getUser().getId())) {
                throw new IllegalArgumentException("User does not exist");
            }
        }

        User paidBy = users.get(paidId);

        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be empty");
        }

        splitStrategy.calculateSplits(splits, amount, paidBy);

        Expense expense = new Expense(expenseId, paidBy, splits);

        expenses.put(expenseId, expense);

        updateBalances(expense);
    }

    private void updateBalances(Expense expense) {
        String creditorId = expense.getPaidBy().getId();
        balances.putIfAbsent(creditorId,new HashMap<>());
        for(Split split: expense.getSplits()) {
            String debtorId = split.getUser().getId();
            if(creditorId.equals(debtorId)) continue;
            double reverseBalance = balances.getOrDefault(creditorId, new HashMap<>()).getOrDefault(debtorId, 0.0);
            balances.putIfAbsent(debtorId,new HashMap<>());
            if(reverseBalance >= split.getAmount()) {
                balances.get(creditorId).put(debtorId, reverseBalance - split.getAmount());
                balances.get(debtorId).put(creditorId, 0.0);
            }
            else{
                balances.get(debtorId).put(creditorId, split.getAmount() - reverseBalance);
                balances.get(creditorId).put(debtorId, 0.0);
            }
        }
    }

    public void showBalances() {
        for(Map.Entry<String, Map<String, Double>> entry: balances.entrySet()) {
            for(Map.Entry<String, Double> balance: entry.getValue().entrySet()) {
                if(Math.abs(balance.getValue()) > 0.001) {
                    System.out.println(entry.getKey() + " owes " + balance.getKey() + " " + balance.getValue());
                }
            }
        }
    }

    public void showBalance(String userId){
        if(!balances.containsKey(userId))
            return;
        for(Map.Entry<String,Double> entry: balances.get(userId).entrySet()){
            if(Math.abs(entry.getValue())>0.001){
                System.out.println(userId + " owes " + entry.getKey() + " " + entry.getValue());
            }
        }
    }
}
