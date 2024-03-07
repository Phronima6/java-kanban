package TaskManager;

import Tasks.*;
import Status.Status;
import java.util.HashMap;

public class TaskManager {
    // Создание обычной Задачи
    public static int creatingTask(HashMap<Integer, Task> taskList, String name, String description) {
        Task task = new Task(name, description);
        taskList.put(task.hashCode(), task);
        return task.hashCode();
    }
    // Создание задачи типа Эпик
    public static int creatingEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                                   HashMap<Integer, Epic> epicList, String name, String description) {
        Epic epic = new Epic(name, description);
        epicList.put(epic.hashCode(), epic);
        HashMap<Integer, SubTask> subTaskList = new HashMap<>(); // Создаём хеш-таблицу для хранения Подзадач
        subTaskListOfEpic.put(epic.hashCode(), subTaskList); // для конкретной задачи типа Эпик
        return epic.hashCode();
    }
    // Создание Подзадачи
    public static int creatingSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                                      HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        if (subTaskListOfEpic.containsKey(idEpic)) { // Если создана задача типа Эпик
            SubTask subTask = new SubTask(name, description);
            subTaskListOfEpic.get(idEpic).put(subTask.hashCode(), subTask);
            int statusNew = 0;
            int statusDone = 0;
            for (SubTask subTasks : subTaskListOfEpic.get(idEpic).values()) {
                if (subTasks.getStatus() == Status.NEW) {
                    statusNew++;
                } else if (subTasks.getStatus() == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == subTaskListOfEpic.get(idEpic).size()) {
                epicList.get(idEpic).setStatus(Status.NEW);
            } else if (statusDone == subTaskListOfEpic.get(idEpic).size()) {
                epicList.get(idEpic).setStatus(Status.DONE);
            } else {
                epicList.get(idEpic).setStatus(Status.IN_PROGRESS);
            }
            return subTask.hashCode();
        } else {
            return 0;
        }
    }
    // Обновление обычной Задачи
    public static void updateTask(HashMap<Integer, Task> taskList, int idTask, String name, String description,
                                  Status status) {
        if (taskList.containsKey(idTask)) { // Если создана обычная Задача
            Task task = new Task(name, description);
            task.setStatus(status);
            taskList.put(idTask, task);
        }
    }
    // Обновление задачи типа Эпик
    public static void updateEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                                  HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        if (epicList.containsKey(idEpic)) { // Если создана задача типа Эпик
            if (subTaskListOfEpic.get(idEpic).isEmpty()) { // Если Подзадач нет
                Epic epic = new Epic(name, description);
                epicList.put(idEpic, epic);
            } else { // Если Подзадачи есть, то обновляем статус задачи типа Эпик
                Epic epic = new Epic(name, description);
                int statusNew = 0;
                int statusDone = 0;
                for (SubTask subTask : subTaskListOfEpic.get(idEpic).values()) {
                    if (subTask.getStatus() == Status.NEW) {
                        statusNew++;
                    } else if (subTask.getStatus() == Status.DONE) {
                        statusDone++;
                    }
                }
                if (statusNew == subTaskListOfEpic.get(idEpic).size()) {
                    epic.setStatus(Status.NEW);
                } else if (statusDone == subTaskListOfEpic.get(idEpic).size()) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
                epicList.put(idEpic, epic);
            }
        }
    }
    // Обновление Подзадачи и обновление статуса задачи типа Эпик
    public static void updateSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                                     HashMap<Integer, Epic> epicList, int idEpic, int idSubTask, String name,
                                     String description, Status status) {
        if (subTaskListOfEpic.containsKey(idEpic) && subTaskListOfEpic.get(idEpic).containsKey(idSubTask)) {
            SubTask subTask = new SubTask(name, description); // Если создана задача типа Эпик и Подзадача
            subTask.setStatus(status);
            subTaskListOfEpic.get(idEpic).put(idSubTask, subTask);
            int statusNew = 0;
            int statusDone = 0;
            for (SubTask subTasks : subTaskListOfEpic.get(idEpic).values()) {
                if (subTasks.getStatus() == Status.NEW) {
                    statusNew++;
                } else if (subTasks.getStatus() == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == subTaskListOfEpic.get(idEpic).size()) {
                epicList.get(idEpic).setStatus(Status.NEW);
            } else if (statusDone == subTaskListOfEpic.get(idEpic).size()) {
                epicList.get(idEpic).setStatus(Status.DONE);
            } else {
                epicList.get(idEpic).setStatus(Status.IN_PROGRESS);
            }
        }
    }
    // Получение списка всех задач
    public String printAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                                HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic) {
        String infoAllTasks = "";
        if (!taskList.isEmpty()) { // Если список обычных Задач не пустой
            for (Task task : taskList.values()) {
                infoAllTasks += task + "\n";
            }
        }
        if (!epicList.isEmpty()) { // Если список задач типа Эпик не пустой
            for (Epic epic : epicList.values()) {
                infoAllTasks += epic + "\n";
            }
        }
        if (!subTaskListOfEpic.isEmpty()) { // Если список Подзадач не пустой
            for (Integer id : subTaskListOfEpic.keySet()) {
                for (SubTask subTask : subTaskListOfEpic.get(id).values()) {
                    if (subTask != null) {
                        infoAllTasks += subTask + "\n";
                    }
                }
            }
        }
        return infoAllTasks;
    }
    // Получение списка всех Подзадач определённой задачи типа Эпик
    public String printAllEpicSubTasks(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic, int idEpic) {
        String infoAllSubTaskOfEpic = ""; // Если задача типа Эпик создана и список Подзадач не пустой
        if (subTaskListOfEpic.containsKey(idEpic) && !subTaskListOfEpic.get(idEpic).isEmpty()) {
            for (SubTask subTask : subTaskListOfEpic.get(idEpic).values()) {
                infoAllSubTaskOfEpic += subTask + "\n";
            }
        }
        return infoAllSubTaskOfEpic;
    }
    // Получение обычной Задачи по идентификатору
    public String printTask(HashMap<Integer, Task> taskList, int idTask) {
        if (taskList.containsKey(idTask)) { // Если обычная Задача создана
            return  String.valueOf(taskList.get(idTask));
        }
        return null;
    }
    // Получение задачи типа Эпик по идентификатору
    public String printEpic(HashMap<Integer, Epic> epicList, int idEpic) {
        if (epicList.containsKey(idEpic)) { // Если задача типа Эпик создана
            return  String.valueOf(epicList.get(idEpic));
        }
        return null;
    }
    // Получение Подзадачи по идентификатору
    public String printSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                               int idEpic, int idSubTask) {
        if (subTaskListOfEpic.get(idEpic).containsKey(idSubTask)) { // Если Подзадача создана
            return  String.valueOf(subTaskListOfEpic.get(idEpic).get(idSubTask));
        }
        return null;
    }
    // Удаление всех задач
    public static void delAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                                   HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic) {
        if (!taskList.isEmpty()) { // Если список обычных Задач не пустой
            taskList.clear();
        }
        if (!epicList.isEmpty()) { // Если список задач типа Эпик не пустой
            epicList.clear();
        }
        if (!subTaskListOfEpic.isEmpty()) { // Если список Подзадач не пустой
            subTaskListOfEpic.clear();
        }
    }
    // Удаление обычной Задачи по идентификатору
    public static void delTask(HashMap<Integer, Task> taskList, int idTask) {
        if (taskList.containsKey(idTask)) { // Если обычная Задача создана
            taskList.remove(idTask);
        }
    }
    // Удаление задачи типа Эпик по идентификатору и как следствие удаление всех Подзадач данного Эпика
    public static void delEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                               HashMap<Integer, Epic> epicList, int idEpic) {
        if (epicList.containsKey(idEpic)) { // Если задач типа Эпик создана
            epicList.remove(idEpic);
            if (subTaskListOfEpic.containsKey(idEpic)) { // Если список Подзадач конктерной задачи типа Эпик создан
                subTaskListOfEpic.remove(idEpic);
            }
        }
    }
    // Удаление Подзадачи по идентификатору и обновление статуса задачи типа Эпик
    public static void delSubTask (HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                                   HashMap<Integer, Epic> epicList, int idEpic, int idSubTask) {
        if (subTaskListOfEpic.get(idEpic).containsKey(idSubTask)) { // Если Подзадача создана
            subTaskListOfEpic.get(idEpic).remove(idSubTask);
            int statusNew = 0;
            int statusDone = 0;
            for (SubTask subTask : subTaskListOfEpic.get(idEpic).values()) {
                if (subTask.getStatus() == Status.NEW) {
                    statusNew++;
                } else if (subTask.getStatus() == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == subTaskListOfEpic.get(idEpic).size()) {
                epicList.get(idEpic).setStatus(Status.NEW);
            } else if (statusDone == subTaskListOfEpic.get(idEpic).size()) {
                epicList.get(idEpic).setStatus(Status.DONE);
            } else {
                epicList.get(idEpic).setStatus(Status.IN_PROGRESS);
            }
        }
    }

}