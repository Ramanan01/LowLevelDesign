package src.com.designpatterns.creational.builder;

public class LoanApplication {

    // Required fields
    private final String applicantName;
    private final double amount;
    private final String loanType;

    // Optional fields
    private final Double interestRate;
    private final Integer durationMonths;
    private final String collateral;
    private final String coApplicant;
    private final String notes;

    private LoanApplication(LoanApplicationBuilder builder){
        this.applicantName = builder.applicantName;
        this.amount = builder.amount;
        this.loanType = builder.loanType;
        this.interestRate = builder.interestRate;
        this.durationMonths = builder.durationMonths;
        this.collateral = builder.collateral;
        this.coApplicant = builder.coApplicant;
        this.notes = builder.notes;
    }

    public static class LoanApplicationBuilder{
        private final String applicantName;
        private final double amount;
        private final String loanType;

        private Double interestRate;
        private Integer durationMonths;
        private String collateral;
        private String coApplicant;
        private String notes;

        public LoanApplicationBuilder(String applicantName, double amount, String loanType) {
            this.applicantName = applicantName;
            this.amount = amount;
            this.loanType = loanType;
        }

        public LoanApplicationBuilder interestRate(double rate) {
            this.interestRate = rate;
            return this;
        }

        public LoanApplicationBuilder durationMonths(int months) {
            this.durationMonths = months;
            return this;
        }

        public LoanApplicationBuilder collateral(String collateral) {
            this.collateral = collateral;
            return this;
        }

        public LoanApplicationBuilder coApplicant(String coApplicant) {
            this.coApplicant = coApplicant;
            return this;
        }

        public LoanApplicationBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public LoanApplication build(){
            return new LoanApplication(this);
        }
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "applicantName='" + applicantName + '\'' +
                ", amount=" + amount +
                ", loanType='" + loanType + '\'' +
                ", interestRate=" + interestRate +
                ", durationMonths=" + durationMonths +
                ", collateral='" + collateral + '\'' +
                ", coApplicant='" + coApplicant + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
