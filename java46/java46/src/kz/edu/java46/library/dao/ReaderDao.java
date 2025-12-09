package kz.edu.java46.library.dao;

import kz.edu.java46.library.db.DatabaseManager;
import kz.edu.java46.library.model.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDao {

    public Reader save(Reader r) throws Exception {
        if (r.getId() == null) {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement("INSERT INTO reader(full_name,contact) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getContact());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) r.setId(rs.getLong(1));
                }
            }
        } else {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement("UPDATE reader SET full_name=?,contact=? WHERE id=?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getContact());
                ps.setLong(3, r.getId());
                ps.executeUpdate();
            }
        }
        return r;
    }

    public Optional<Reader> findById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id,full_name,contact FROM reader WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reader r = new Reader();
                    r.setId(rs.getLong("id"));
                    r.setFullName(rs.getString("full_name"));
                    r.setContact(rs.getString("contact"));
                    return Optional.of(r);
                }
            }
        }
        return Optional.empty();
    }

    public List<Reader> findAll() throws Exception {
        List<Reader> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id,full_name,contact FROM reader");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reader r = new Reader();
                r.setId(rs.getLong("id"));
                r.setFullName(rs.getString("full_name"));
                r.setContact(rs.getString("contact"));
                list.add(r);
            }
        }
        return list;
    }

    public void deleteById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM reader WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}