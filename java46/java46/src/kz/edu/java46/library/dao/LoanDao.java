package kz.edu.java46.library.dao;

import kz.edu.java46.library.db.DatabaseManager;
import kz.edu.java46.library.model.Loan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanDao {

    public Loan save(Loan l) throws Exception {
        if (l.getId() == null) {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                         "INSERT INTO loan(reader_id,book_id,date_issued,date_due,date_returned) VALUES (?,?,?,?,?)",
                         Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, l.getReaderId());
                ps.setObject(2, l.getBookId());
                ps.setDate(3, Date.valueOf(l.getDateIssued()));
                ps.setDate(4, Date.valueOf(l.getDateDue()));
                if (l.getDateReturned() != null) ps.setDate(5, Date.valueOf(l.getDateReturned()));
                else ps.setNull(5, Types.DATE);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) l.setId(rs.getLong(1));
                }
            }
        } else {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                         "UPDATE loan SET reader_id=?,book_id=?,date_issued=?,date_due=?,date_returned=? WHERE id=?")) {
                ps.setObject(1, l.getReaderId());
                ps.setObject(2, l.getBookId());
                ps.setDate(3, Date.valueOf(l.getDateIssued()));
                ps.setDate(4, Date.valueOf(l.getDateDue()));
                if (l.getDateReturned() != null) ps.setDate(5, Date.valueOf(l.getDateReturned()));
                else ps.setNull(5, Types.DATE);
                ps.setLong(6, l.getId());
                ps.executeUpdate();
            }
        }
        return l;
    }

    public Optional<Loan> findById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM loan WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<Loan> findAll() throws Exception {
        List<Loan> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM loan");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Loan> findAllOverdueLoans() throws Exception {
        List<Loan> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM loan WHERE date_returned IS NULL AND date_due < CURRENT_DATE()");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void deleteById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM loan WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.setId(rs.getLong("id"));
        l.setReaderId((Long) rs.getObject("reader_id"));
        l.setBookId((Long) rs.getObject("book_id"));
        Date di = rs.getDate("date_issued");
        Date dd = rs.getDate("date_due");
        Date dr = rs.getDate("date_returned");
        l.setDateIssued(di != null ? di.toLocalDate() : null);
        l.setDateDue(dd != null ? dd.toLocalDate() : null);
        l.setDateReturned(dr != null ? dr.toLocalDate() : null);
        return l;
    }
}