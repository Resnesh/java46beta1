package kz.edu.java46.library.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) { super(message, cause); }
    public DataAccessException(String message) { super(message); }
}