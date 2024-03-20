package Managers;

import HistoryManager.*;
import TaskManager.*;

public class Managers {

    // Создание менеджера для управления задачами
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // Создание менеджера историй задач
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}