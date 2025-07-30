package src.practiceproblems.subscriptionbill;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

enum PricingPlan{
    BASIC(9.99),
    STANDARD(49.99),
    PREMIUM(249.99);

    private final double cost;

    PricingPlan(double cost){
        this.cost = cost;
    }

    public double getCost(){
        return cost;
    }
}

class InvalidSubscriptionException extends Exception{
    InvalidSubscriptionException(String message){
        super(message);
    }
}

class PricingStrategyNotSetException extends Exception{
    PricingStrategyNotSetException(String message){
        super(message);
    }
}

class SubscriptionPlan{

    private final PricingPlan pricingPlan;
    private final LocalDate startDate;
    private final int trialDays;

    public SubscriptionPlan(PricingPlan pricingPlan, LocalDate startDate, int trialDays) throws InvalidSubscriptionException{
        if(pricingPlan == null || startDate==null){
            throw new InvalidSubscriptionException("Need valid start date and pricing plan");
        }
        if(trialDays <= 0){
            throw new InvalidSubscriptionException("No valid trial days provided");
        }

        this.pricingPlan = pricingPlan;
        this.startDate = startDate;
        this.trialDays = trialDays;
    }

    public double getMonthlyCost(){
        return pricingPlan.getCost();
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public int getTrialDays(){
        return trialDays;
    }

    public LocalDate getTrialEndDate(){
        return startDate.plusDays(trialDays);
    }
}


interface PricingStrategy{
    Map<LocalDate, Double> getMonthlyBill(SubscriptionPlan subscriptionPlan, int year);
    Double getYearlyBill(SubscriptionPlan subscriptionPlan, int year);
}

class DefaultPricingStrategy implements PricingStrategy{

    @Override
    public Map<LocalDate, Double> getMonthlyBill(SubscriptionPlan subscriptionPlan, int year){
        LocalDate cursor = LocalDate.of(year, 1, 1);
        LocalDate startDate = subscriptionPlan.getStartDate();
        LocalDate trialEndDate = subscriptionPlan.getTrialEndDate();
        Map<LocalDate, Double> monthlyCost = new LinkedHashMap<>();

        while(cursor.getYear() == year){
            LocalDate monthEnd = LocalDate.of(cursor.getYear(), cursor.getMonth(), cursor.lengthOfMonth());
            LocalDate monthStart = LocalDate.of(cursor.getYear(), cursor.getMonth(), 1);
            if(monthEnd.isBefore(startDate)) {
                monthlyCost.put(monthStart, 0.0);
            }
            else if (monthEnd.isBefore(trialEndDate)) {
                monthlyCost.put(monthStart, 0.0);
            }
            else if (trialEndDate.isAfter(monthStart) && trialEndDate.isBefore(monthEnd)) {
                double cost = 0.0;
                int noOfDaysInMonth = cursor.lengthOfMonth();
                int noOfBillableDays = cursor.lengthOfMonth() - trialEndDate.getDayOfMonth();
                cost = ((double) noOfBillableDays / (double) noOfDaysInMonth) * subscriptionPlan.getMonthlyCost();
                monthlyCost.put(monthStart, cost);
            }
            else{
                monthlyCost.put(monthStart, subscriptionPlan.getMonthlyCost());
            }
            cursor = cursor.plusMonths(1);
        }
        return monthlyCost;
    }

    @Override
    public Double getYearlyBill(SubscriptionPlan subscriptionPlan, int year){
        Map<LocalDate, Double> monthlyBill = getMonthlyBill(subscriptionPlan, year);
        return monthlyBill.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}

class CostExplorer{
    private PricingStrategy pricingStrategy;

    public void setPricingStrategy(PricingStrategy pricingStrategy){
        this.pricingStrategy = pricingStrategy;
    }

    public void printMonthlyBill(SubscriptionPlan subscriptionPlan, int year) throws PricingStrategyNotSetException{
        if(pricingStrategy == null){
            throw new PricingStrategyNotSetException("Pricing strategy is not set");
        }
        Map<LocalDate, Double> monthlyBill = pricingStrategy.getMonthlyBill(subscriptionPlan, year);
        for(Map.Entry<LocalDate, Double> entry : monthlyBill.entrySet()){
            System.out.printf("Month: %s , Bill: %f%n", entry.getKey().getMonth().toString(), entry.getValue());
        }
    }

    public void printYearlyBill(SubscriptionPlan subscriptionPlan, int year){
        double total = pricingStrategy.getYearlyBill(subscriptionPlan, year);
        System.out.printf("\nEstimated total cost for %d: $%.2f\n", year, total);
    }
}

public class SubscriptionBill {
    public static void main(String[] args){
        CostExplorer costExplorer = new CostExplorer();
        costExplorer.setPricingStrategy(new DefaultPricingStrategy());

        try{
            SubscriptionPlan subscriptionPlan = new SubscriptionPlan(PricingPlan.BASIC, LocalDate.of(2023, 12, 12), 14);
            costExplorer.printMonthlyBill(subscriptionPlan, 2024);
            costExplorer.printMonthlyBill(subscriptionPlan, 2023);
        }catch (PricingStrategyNotSetException | InvalidSubscriptionException e){
            System.err.println(e.getMessage());
        }
    }
}
