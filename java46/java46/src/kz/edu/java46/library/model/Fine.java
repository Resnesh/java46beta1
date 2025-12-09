package kz.edu.java46.library.model;

import java.time.LocalDate;

public class Fine {
    private Long id;
    private Long loanId;
    private double amount;
    private boolean paid;
    private LocalDate dateCalculated;

    public Fine() {}
    public Fine(Long id, Long loanId, double amount, boolean paid, LocalDate dateCalculated) {
        this.id = id; this.loanId = loanId; this.amount = amount; this.paid = paid; this.dateCalculated = dateCalculated;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public LocalDate getDateCalculated() { return dateCalculated; }
    public void setDateCalculated(LocalDate dateCalculated) { this.dateCalculated = dateCalculated; }

    @Override
    public String toString() {
        return "Fine{" + id + ", loan=" + loanId + ", amt=" + amount + ", paid=" + paid + "}";
    }
}