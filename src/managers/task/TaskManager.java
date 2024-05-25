package managers.task;

import tasks.status.Status;
import tasks.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public interface TaskManager {

    // Возвращаем список обычных Задач
    HashMap<Integer, Task> getTaskList();

    // Возвращаем список задач типа Эпик
    HashMap<Integer, Epic> getEpicList();

    // Возвращаем список Подзадач с id Эпика
    HashMap<Integer, HashMap<Integer, SubTask>> getSubTaskListOfEpic();

    // Возвращаем историю просмотренных задач в виде списка
    public ArrayList<Task> getHistory();

    // Возвращаем список задач, отсортированных по приоритету
    public TreeSet<Task> getSortedTasks();

    // Создание обычной Задачи
    int creatingTask(String name, String description, String startTime, long duration);

    // Создание задачи типа Эпик
    int creatingEpic(String name, String description);

    // Создание Подзадачи
    int creatingSubTask(int idEpic, String name, String description, String startTime, long duration);

    // Обновление обычной Задачи
    void updateTask(int idTask, String name, String description, Status status, String startTime, long duration);

    // Обновление задачи типа Эпик
    void updateEpic(int idEpic, String name, String description);

    // Обновление Подзадачи и обновление статуса задачи типа Эпик
    void updateSubTask(int idEpic, int idSubTask, String name, String description, Status status, String startTime,
                       long duration);

    // Получение списка всех задач
    String printAllTasks();

    // Получение списка всех Подзадач определённой задачи типа Эпик
    String printAllEpicSubTasks(int idEpic);

    // Получение обычной Задачи по идентификатору
    String printTask(int idTask);

    // Получение задачи типа Эпик по идентификатору
    String printEpic(int idEpic);

    // Получение Подзадачи по идентификатору
    String printSubTask(int idEpic, int idSubTask);

    // Печать истории просмотренных задач
    String printHistory();

    // Печать задач, отсортированных по приоритету
    String getPrioritizedTasks();

    // Удаление всех задач
    void delAllTasks();

    // Удаление обычной Задачи по идентификатору
    void delTask(int idTask);

    // Удаление задачи типа Эпик по идентификатору и, как следствие, удаление всех Подзадач данного Эпика
    void delEpic(int idEpic);

    // Удаление Подзадачи по идентификатору и обновление статуса задачи типа Эпик
    void delSubTask(int idEpic, int idSubTask);

    // Обновление статуса задачи типа Эпик
    void updateStatusOfEpic(int idEpic);

    // Обновляет дату и время задачи типа Эпик
    void updateDateTimeOfEpic(int idEpic);

    // Проверяет пересечение временных отрезков задач
    void intersectionDataTime(LocalDateTime startTime, Duration duration);

}