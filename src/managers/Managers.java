package managers;

import managers.history.*;
import managers.task.*;

public class Managers {

    // Создание обычного менеджера для управления задачами
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // Создание менеджера для управления задачами (с сохранением в файле)
    public TaskManager getFileBackedTaskManager(String filePath) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(filePath);
        fileBackedTaskManager.loadFromFile(filePath); // Восстанавливаем задачи из файла
        return fileBackedTaskManager;
    }

    // Создание менеджера историй задач
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}