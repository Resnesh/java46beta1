package kz.edu.java46.library.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:./data/librarydb;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    static {
        try {
            initSchema();
        } catch (Exception e) {
            System.err.println("DB init error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void initSchema() throws Exception {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS book (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(1024), author VARCHAR(512), isbn VARCHAR(100)" +
                    ")");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS reader (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "full_name VARCHAR(512), contact VARCHAR(512)" +
                    ")");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS loan (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "reader_id BIGINT, book_id BIGINT, " +
                    "date_issued DATE, date_due DATE, date_returned DATE, " +
                    "CONSTRAINT fk_loan_reader FOREIGN KEY (reader_id) REFERENCES reader(id) ON DELETE SET NULL, " +
                    "CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE SET NULL" +
                    ")");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS fine (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "loan_id BIGINT, amount DOUBLE, paid BOOLEAN, date_calculated DATE, " +
                    "CONSTRAINT fk_fine_loan FOREIGN KEY (loan_id) REFERENCES loan(id) ON DELETE CASCADE" +
                    ")");
        }
    }
}