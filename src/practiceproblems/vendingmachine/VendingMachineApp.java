package src.practiceproblems.vendingmachine;

//states -> idle -> choosing -> pending payment ->

import java.util.HashMap;
import java.util.Map;

class Item {
    int quantity;
    String name;
    double price;

    public Item(int qunatity, String name, double price) {
        this.quantity = qunatity;
        this.name = name;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void reduceQuantity() {
        quantity--;
    }
}

abstract class VendingMachineState {
    void chooseItem(VendingMachine vendingMachine, int row, int rowPos) {
        invalid();
    }

    double makePayment(VendingMachine vendingMachine, int amount){
        invalid();
        return 0;
    }

    void productPicked(VendingMachine vendingMachine) {
        invalid();
    }

    void changePicked(VendingMachine vendingMachine){
        invalid();
    }

    void cancel(VendingMachine vendingMachine){
        invalid();
    }

    protected void invalid() {
        System.out.println("Invalid state transition");
    }
}

class IdleState extends VendingMachineState {

    @Override
    void chooseItem(VendingMachine vendingMachine, int row, int rowPos) {
        if(vendingMachine.isPositionValid(row, rowPos)) {
            vendingMachine.setCurrentItem(vendingMachine.getItem(row, rowPos));
            vendingMachine.setCurrentState(vendingMachine.getItemSelectedState());
        }
    }
}

class ItemSelectedState extends  VendingMachineState {
    @Override
    double makePayment(VendingMachine vendingMachine, int amount){
        if(vendingMachine.getCurrentItem().getPrice() <= amount) {
            vendingMachine.setCurrentState(vendingMachine.getDispensingProductState());
            return amount - vendingMachine.getCurrentItem().getPrice();
        }
        return -1;
    }

    @Override
    void cancel(VendingMachine vendingMachine) {
        vendingMachine.setCurrentState(vendingMachine.getIdleState());
        vendingMachine.setCurrentItem(vendingMachine.emptyItem);
        vendingMachine.resetChangeToDispense();
    }
}

class DispensingProductState extends VendingMachineState {
    @Override
    void productPicked(VendingMachine vendingMachine) {
        vendingMachine.setCurrentState(vendingMachine.getDispensingChangeState());
        vendingMachine.reduceQuantity(vendingMachine.getCurrentItem());
    }
}

class DispensingChangeState extends  VendingMachineState{
    @Override
    void changePicked(VendingMachine vendingMachine){
        vendingMachine.setCurrentState(vendingMachine.getIdleState());
        vendingMachine.setCurrentItem(vendingMachine.emptyItem);
        vendingMachine.resetChangeToDispense();
    }
}

class VendingMachine {
    Map<Integer, Map<Integer, Item>> items;
    public Item currentItem;
    int rows;
    int itemsPerRow;
    public Item emptyItem;
    private VendingMachineState currentState;
    private double changeToDispense;

    private final IdleState idleState = new IdleState();
    private final ItemSelectedState itemSelectedState = new ItemSelectedState();
    private final DispensingProductState dispensingProductState = new DispensingProductState();
    private final DispensingChangeState dispensingChangeState = new DispensingChangeState();

    public VendingMachine(int rows, int itemsPerRow) {
        this.rows = rows;
        this.itemsPerRow = itemsPerRow;
        items = new HashMap<>();
        emptyItem = new Item(-1, "empty item", 0.0);
        for(int i=0;i<rows;i++) {
            items.put(i, new HashMap<>());
            for(int j=0;j<itemsPerRow;j++) {
                items.get(i).put(j, emptyItem);
            }
        }
        currentState = idleState;
    }

    public void addItem(int row, int rowPos, String name, double price, int quantity) {
        if(currentState != idleState) {
            throw new IllegalArgumentException(" Cannot add item when not idle");
        }

        if(!isPositionValid(row, rowPos)) {
            throw new IllegalArgumentException("Invalid selection");
        }

        if(items.get(row).get(rowPos) != emptyItem) {
            throw new IllegalArgumentException("Slot not empty");
        }

        Item newItem = new Item(quantity, name, price);

        items.get(row).put(rowPos, newItem);
    }

    public void removeItem(int row, int rowPos) {
        if(currentState != idleState) {
            throw new IllegalArgumentException(" Cannot remove item when not idle");
        }
        if(!isPositionValid(row, rowPos)) {
            throw new IllegalArgumentException("Invalid position");
        }

        if(items.get(row).get(rowPos) == emptyItem) {
            throw new IllegalArgumentException("Slot already empty");
        }

        items.get(row).put(rowPos, emptyItem);
    }
    public boolean isPositionValid(int row, int rowPos) {
        if(row<0 || row>=rows || rowPos<0 || rowPos>=itemsPerRow) {
            return false;
        }
        return true;
    }

    public Item getItem(int row, int rowPos) {
        return items.get(row).get(rowPos);
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentState(VendingMachineState currentState) {
        this.currentState = currentState;
    }

    public Map<Integer, Map<Integer, Item>> getItems() {
        return items;
    }

    public IdleState getIdleState() {
        return idleState;
    }

    public ItemSelectedState getItemSelectedState() {
        return itemSelectedState;
    }

    public DispensingProductState getDispensingProductState() {
        return dispensingProductState;
    }

    public DispensingChangeState getDispensingChangeState() {
        return dispensingChangeState;
    }

    public void resetChangeToDispense() {
        changeToDispense = 0;
    }

    void reduceQuantity(Item item) {
        item.reduceQuantity();
    }

    void chooseItem(int row, int rowPos) {
        this.currentState.chooseItem(this, row, rowPos);
    }

    void makePayment(int amount){
        changeToDispense = this.currentState.makePayment(this, amount);
    }

    void productPicked() {
        this.currentState.productPicked(this);
    }

    void changePicked(){
        this.currentState.changePicked(this);
    }

    void cancel(){
        this.currentState.cancel(this);
    }
}


public class VendingMachineApp {
}
