package src.com.designpatterns.creational.builder;

public class Main {
    public static void main(String args[]){
        LoanApplication homeLoan = new LoanApplication.LoanApplicationBuilder("John Doe", 50_00_000, "Home Loan")
                .interestRate(7.5)
                .durationMonths(240)
                .collateral("House")
                .build();

        LoanApplication personalLoan = new LoanApplication.LoanApplicationBuilder("Jane Smith", 5_00_000, "Personal Loan")
                .durationMonths(60)
                .notes("For medical expenses")
                .build();

        System.out.println(homeLoan);
        System.out.println(personalLoan);
    }
}
