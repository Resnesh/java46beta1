package kz.edu.java46.library.util;

import kz.edu.java46.library.exception.ValidationException;

public class ValidationUtil {
    public static void requireNonEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) throw new ValidationException(message);
    }
}