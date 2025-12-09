package kz.edu.java46.library.dao;

import kz.edu.java46.library.db.DatabaseManager;
import kz.edu.java46.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao {

    public Book save(Book b) throws Exception {
        if (b.getId() == null) {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement("INSERT INTO book(title,author,isbn) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, b.getTitle());
                ps.setString(2, b.getAuthor());
                ps.setString(3, b.getIsbn());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) b.setId(rs.getLong(1));
                }
            }
        } else {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement("UPDATE book SET title=?,author=?,isbn=? WHERE id=?")) {
                ps.setString(1, b.getTitle());
                ps.setString(2, b.getAuthor());
                ps.setString(3, b.getIsbn());
                ps.setLong(4, b.getId());
                ps.executeUpdate();
            }
        }
        return b;
    }

    public Optional<Book> findById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id,title,author,isbn FROM book WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Book> findAll() throws Exception {
        List<Book> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id,title,author,isbn FROM book");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void deleteById(Long id) throws Exception {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM book WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setIsbn(rs.getString("isbn"));
        return b;
    }
}