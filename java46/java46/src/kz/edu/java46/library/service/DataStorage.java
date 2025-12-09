package kz.edu.java46.library.service;

// Этот класс будет создан позже, сейчас он только для компиляции
// В реальной реализации он будет использовать JsonUtil
public class DataStorage {

    // Метод, который MainFrame требует через LibraryService
    public void saveAllData() {
        System.out.println("LOG: Saving all data to file...");
        // Здесь будет код для вызова JsonUtil.saveAllData(все DAOs);
    }

    // Метод для загрузки данных при старте
    public void loadAllData() {
        System.out.println("LOG: Loading all data from file...");
        // Здесь будет код для вызова JsonUtil.loadAllData(все DAOs);
    }
}