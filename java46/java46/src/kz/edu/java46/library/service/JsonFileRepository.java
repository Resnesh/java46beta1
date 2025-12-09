package kz.edu.java46.library.service;

import kz.edu.java46.library.exception.DataAccessException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 Simple generic repository using Java serialization to file.
 Filename is passed in constructor.
 */
public class JsonFileRepository<T extends Serializable> implements DataRepository<T> {

    private final List<T> storage = new ArrayList<>();
    private final File file;

    public JsonFileRepository(String filename) {
        this.file = new File(filename);
        if (file.exists()) {
            loadFromStorage();
        }
    }

    @Override
    public synchronized T save(T entity) {
        storage.removeIf(e -> e.equals(entity)); // naive
        storage.add(entity);
        saveToStorage();
        return entity;
    }

    @Override
    public synchronized Optional<T> findById(Long id) {
        // Not generic—caller should implement specific find logic. Provide fallback
        return storage.stream().findFirst();
    }

    @Override
    public synchronized List<T> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public synchronized void delete(Long id) {
        // generic delete not implemented by id; caller should handle via repository wrapper
        saveToStorage();
    }

    @Override
    public synchronized void saveToStorage() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new DataAccessException("Failed to save storage to file " + file.getAbsolutePath(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void loadFromStorage() {
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                storage.clear();
                storage.addAll((List<T>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new DataAccessException("Failed to load storage from file " + file.getAbsolutePath(), e);
        }
    }
}