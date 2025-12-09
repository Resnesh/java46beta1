package kz.edu.java46.library.model;

import java.time.LocalDate;

public class Loan {
    private Long id;
    private Long readerId;
    private Long bookId;
    private LocalDate dateIssued;
    private LocalDate dateDue;
    private LocalDate dateReturned; // nullable

    public Loan() {}

    public Loan(Long id, Long readerId, Long bookId, LocalDate dateIssued, LocalDate dateDue, LocalDate dateReturned) {
        this.id = id;
        this.readerId = readerId;
        this.bookId = bookId;
        this.dateIssued = dateIssued;
        this.dateDue = dateDue;
        this.dateReturned = dateReturned;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public LocalDate getDateIssued() { return dateIssued; }
    public void setDateIssued(LocalDate dateIssued) { this.dateIssued = dateIssued; }
    public LocalDate getDateDue() { return dateDue; }
    public void setDateDue(LocalDate dateDue) { this.dateDue = dateDue; }
    public LocalDate getDateReturned() { return dateReturned; }
    public void setDateReturned(LocalDate dateReturned) { this.dateReturned = dateReturned; }

    @Override
    public String toString() {
        return "Loan{" + id + ", r=" + readerId + ", b=" + bookId + ", due=" + dateDue + ", ret=" + dateReturned + "}";
    }
}