package managers.task;

import tasks.status.Status;
import tasks.*;
import java.util.HashMap;

public interface TaskManager {

    // Возвращаем список обычных Задач
    HashMap<Integer, Task> getTaskList();

    // Возвращаем список задач типа Эпик
    HashMap<Integer, Epic> getEpicList();

    // Возвращаем список Подзадач с id Эпика
    HashMap<Integer, HashMap<Integer, SubTask>> getSubTaskListOfEpic();

    // Создание обычной Задачи
    int creatingTask(HashMap<Integer, Task> taskList, String name, String description);

    // Создание задачи типа Эпик
    int creatingEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                     HashMap<Integer, Epic> epicList, String name, String description);

    // Создание Подзадачи
    int creatingSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                        HashMap<Integer, Epic> epicList, int idEpic, String name, String description);

    // Обновление обычной Задачи
    void updateTask(HashMap<Integer, Task> taskList, int idTask, String name, String description,
                    Status status);

    // Обновление задачи типа Эпик
    void updateEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                    HashMap<Integer, Epic> epicList, int idEpic, String name, String description);

    // Обновление Подзадачи и обновление статуса задачи типа Эпик
    void updateSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                       HashMap<Integer, Epic> epicList, int idEpic, int idSubTask, String name,
                       String description, Status status);

    // Получение списка всех задач
    String printAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                         HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic);

    // Получение списка всех Подзадач определённой задачи типа Эпик
    String printAllEpicSubTasks(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic, int idEpic);

    // Получение обычной Задачи по идентификатору
    String printTask(HashMap<Integer, Task> taskList, int idTask);

    // Получение задачи типа Эпик по идентификатору
    String printEpic(HashMap<Integer, Epic> epicList, int idEpic);

    // Получение Подзадачи по идентификатору
    String printSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                        int idEpic, int idSubTask);

    // Печать истории просмотренных задач
    String printHistory();

    // Удаление всех задач
    void delAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                     HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic);

    // Удаление обычной Задачи по идентификатору
    void delTask(HashMap<Integer, Task> taskList, int idTask);

    // Удаление задачи типа Эпик по идентификатору и, как следствие, удаление всех Подзадач данного Эпика
    void delEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                 HashMap<Integer, Epic> epicList, int idEpic);

    // Удаление Подзадачи по идентификатору и обновление статуса задачи типа Эпик
    void delSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                    HashMap<Integer, Epic> epicList, int idEpic, int idSubTask);

    // Обновление статуса задачи типа Эпик
    void updateStatusOfEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                       HashMap<Integer, Epic> epicList, int idEpic);

}