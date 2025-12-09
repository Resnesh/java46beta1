package kz.edu.java46.library.dao;

import kz.edu.java46.library.db.DatabaseManager;
import kz.edu.java46.library.model.Fine;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FineDao {

    public Fine save(Fine f) throws Exception {
        if (f.getId() == null) {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                         "INSERT INTO fine(loan_id,amount,paid,date_calculated) VALUES (?,?,?,?)",
                         Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, f.getLoanId());
                ps.setDouble(2, f.getAmount());
                ps.setBoolean(3, f.isPaid());
                ps.setDate(4, Date.valueOf(f.getDateCalculated()));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) f.setId(rs.getLong(1));
                }
            }
        } else {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                         "UPDATE fine SET loan_id=?,amount=?,paid=?,date_calculated=? WHERE id=?")) {
                ps.setObject(1, f.getLoanId());
                ps.setDouble(2, f.getAmount());
                ps.setBoolean(3, f.isPaid());
                ps.setDate(4, Date.valueOf(f.getDateCalculated()));
                ps.setLong(5, f.getId());
                ps.executeUpdate();
            }
        }
        return f;
    }

    public Optional<Fine> findById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM fine WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<Fine> findAll() throws Exception {
        List<Fine> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM fine");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void deleteById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM fine WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Fine mapRow(ResultSet rs) throws SQLException {
        Fine f = new Fine();
        f.setId(rs.getLong("id"));
        f.setLoanId((Long) rs.getObject("loan_id"));
        f.setAmount(rs.getDouble("amount"));
        f.setPaid(rs.getBoolean("paid"));
        Date dc = rs.getDate("date_calculated");
        f.setDateCalculated(dc != null ? dc.toLocalDate() : null);
        return f;
    }
}