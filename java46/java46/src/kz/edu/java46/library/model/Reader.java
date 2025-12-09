package kz.edu.java46.library.model;

public class Reader {
    private Long id;
    private String fullName;
    private String contact;

    public Reader() {}
    public Reader(Long id, String fullName, String contact) {
        this.id = id; this.fullName = fullName; this.contact = contact;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return "Reader{" + id + "," + fullName + "}";
    }
}