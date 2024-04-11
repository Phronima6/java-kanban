package test;

import managers.Managers;
import status.Status;
import managers.task.*;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class InMemoryTaskManagerTest {

    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefault();
    String name; // Название задачи
    String description; // Описание задачи
    int idTask; // id обычной Задачи
    int idEpic; // id задачи типа Эпик
    int idSubTask; // id Подзадачи
    Status status; // Стутус задачи

    // Проверяем: создание, обновление, вывод, удаление и совпадение id обычной Задачи
    @Test
    public void creatingAndUpdateAndPrintAndDelTask() {
        Task task = new Task("Задача 1", "Задача 1...");
        name = "Задача 1";
        description = "Задача 1...";
        idTask = taskManager.creatingTask(taskManager.getTaskList(), name, description);
        Assertions.assertEquals(String.valueOf(task), taskManager.printTask(taskManager.getTaskList(), idTask),
                "Ошибка, обычные Задачи не совпадают.");
        Assertions.assertEquals(task.hashCode(), idTask, "Ошибка, id обычных Задач не совпадает.");
        name = "Задача 2";
        description = "Задача 2...";
        status = Status.DONE;
        taskManager.updateTask(taskManager.getTaskList(), idTask, name, description, status);
        Assertions.assertNotEquals(String.valueOf(task), taskManager.printTask(taskManager.getTaskList(), idTask),
                "Ошибка, обычные Задачи совпадают.");
        Assertions.assertEquals(task.hashCode(), idTask, "Ошибка, id обычных Задач не совпадает.");
        taskManager.delTask(taskManager.getTaskList(), idTask);
        Assertions.assertNull(taskManager.printTask(taskManager.getTaskList(), idTask),
                "Ошибка, обычная Задача не удалена.");
    }

    // Проверяем: создание, обновление, вывод, удаление, совпадение id задач типа Эпик
    @Test
    public void creatingAndUpdateAndPrintAndDelEpic() {
        Epic epic = new Epic("Эпик 1", "Эпик 1...");
        name = "Эпик 1";
        description = "Эпик 1...";
        idEpic = taskManager.creatingEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(),
                name, description);
        Assertions.assertEquals(String.valueOf(epic), taskManager.printEpic(taskManager.getEpicList(), idEpic),
                "Ошибка, задачи типа Эпик не совпадают.");
        Assertions.assertEquals(epic.hashCode(), idEpic, "Ошибка, id задач типа Эпик не совпадает.");
        name = "Эпик 2";
        description = "Эпик 2...";
        taskManager.updateEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                name, description);
        Assertions.assertNotEquals(String.valueOf(epic), taskManager.printEpic(taskManager.getEpicList(), idEpic),
                "Ошибка, задачи типа Эпик совпадают.");
        Assertions.assertEquals(epic.hashCode(), idEpic, "Ошибка, id задач типа Эпик не совпадает.");
        taskManager.delEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic);
        Assertions.assertNull(taskManager.printEpic(taskManager.getEpicList(), idEpic),
                "Ошибка, задача типа Эпик не удалена.");
    }

    // Проверяем: создание, обновление, вывод, удаление, совпадение id Подзадачи, изменение статуса задачи типа Эпик
    @Test
    public void creatingAndUpdateAndPrintAndDelSubTask() {
        SubTask subTask = new SubTask("Подзадача 1", "Подзадача 1...");
        name = "Эпик 1";
        description = "Эпик 1...";
        idEpic = taskManager.creatingEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(),
                name, description);
        name = "Подзадача 1";
        description = "Подзадача 1...";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                name, description);
        Assertions.assertEquals(String.valueOf(subTask), taskManager.printSubTask(taskManager.getSubTaskListOfEpic(),
                idEpic, idSubTask), "Ошибка, Подзадачи не совпадают.");
        Assertions.assertEquals(subTask.hashCode(), idSubTask, "Ошибка, id Подзадач не совпадает.");
        name = "Подзадача 2";
        description = "Подзадача 2...";
        status = Status.DONE;
        taskManager.updateSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                idSubTask, name, description, status);
        Assertions.assertNotEquals(String.valueOf(subTask), taskManager.printSubTask(taskManager.getSubTaskListOfEpic(),
                idEpic, idSubTask), "Ошибка, Подзадачи совпадают.");
        Assertions.assertEquals(subTask.hashCode(), idSubTask, "Ошибка, id Подзадач не совпадает.");
        Assertions.assertEquals(Status.DONE, taskManager.getEpicList().get(idEpic).getStatus(),
                "Ошибка, статус задачи типа Эпик отличается от ожидаемого");
        name = "Подзадача 3";
        description = "Подзадача 3...";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                name, description);
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicList().get(idEpic).getStatus(),
                "Ошибка, статус задачи типа Эпик отличается от ожидаемого");
        taskManager.delSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic, idSubTask);
        Assertions.assertNull(taskManager.printSubTask(taskManager.getSubTaskListOfEpic(),
                idEpic, idSubTask), "Ошибка, Подзадача не удалена.");
    }

    // Проверяем совпадение id Задачи, id задачи типа Эпик и id Подзадачи
    @Test
    public void idTaskNotEqualsIdEpicNotEqualsIdSubTask() {
        name = "Задача 1";
        description = "Задача 1...";
        idTask = taskManager.creatingTask(taskManager.getTaskList(), name, description);
        name = "Эпик 1";
        description = "Эпик 1...";
        idEpic = taskManager.creatingEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(),
                name, description);
        name = "Подзадача 1";
        description = "Подзадача 1...";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                name, description);
        Assertions.assertNotEquals(idTask, idEpic, "Ошибка, id Задачи и задачи типа Эпик совпадает.");
        Assertions.assertNotEquals(idEpic, idSubTask, "Ошибка, id Задачи и задачи типа Эпик совпадает.");
    }

    // Проверяем печать всех задач, печать всех Подзадач, удаление всех задач
    @Test
    public void printAllTasksAndPrintAllEpicSubTasksAndDelAllTasks() {
        String infoAllTasks; // Ожидаемый результат вывода всех задач
        String infoAllEpicSubTasks; // Ожидаемый результат вывода всех Подзадач
        Task task = new Task("Задача 1", "Задача 1...");
        Epic epic = new Epic("Эпик 1", "Эпик 1...");
        SubTask subTaskOne = new SubTask("Подзадача 1", "Подзадача 1...");
        infoAllTasks = task + "\n" + epic + "\n" + subTaskOne + "\n";
        infoAllEpicSubTasks = subTaskOne + "\n";
        name = "Задача 1";
        description = "Задача 1...";
        idTask = taskManager.creatingTask(taskManager.getTaskList(), name, description);
        name = "Эпик 1";
        description = "Эпик 1...";
        idEpic = taskManager.creatingEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(),
                name, description);
        name = "Подзадача 1";
        description = "Подзадача 1...";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                name, description);
        Assertions.assertEquals(infoAllTasks, taskManager.printAllTasks(taskManager.getTaskList(),
                        taskManager.getEpicList(), taskManager.getSubTaskListOfEpic()),
                "Ошибка, выводимые задачи не совпадают с ожидаемыми.");
        Assertions.assertEquals(infoAllEpicSubTasks,
                taskManager.printAllEpicSubTasks(taskManager.getSubTaskListOfEpic(), idEpic),
                "Ошибка, выводимые Подзадачи не совпадают с ожидаемыми.");
        taskManager.delAllTasks(taskManager.getTaskList(), taskManager.getEpicList(),
                taskManager.getSubTaskListOfEpic());
        Assertions.assertEquals("", taskManager.printAllTasks(taskManager.getTaskList(),
                taskManager.getEpicList(), taskManager.getSubTaskListOfEpic()), "Ошибка, задачи не удалены.");
    }

    // Проверяем исключение повторов в истории задач и порядок сохранения задачи в историю
    @Test
    public void sizeHistoryAndSavingVersion() {
        String infoHistory; // Ожидаемый результат вывода истории задач
        Task taskOne = new Task("Задача 1", "Задача 1...");
        Task taskTwo = new Task("Задача 2", "Задача 2...");
        Epic epic = new Epic("Эпик 1", "Эпик 1...");
        infoHistory = taskOne + "\n" + epic + "\n" + taskTwo + "\n";
        name = "Задача 1";
        description = "Задача 1...";
        idTask = taskManager.creatingTask(taskManager.getTaskList(), name, description);
        taskManager.printTask(taskManager.getTaskList(), idTask);
        taskManager.printTask(taskManager.getTaskList(), idTask);
        name = "Задача 2";
        description = "Задача 2...";
        idTask = taskManager.creatingTask(taskManager.getTaskList(), name, description);
        taskManager.printTask(taskManager.getTaskList(), idTask);
        taskManager.printTask(taskManager.getTaskList(), idTask);
        name = "Эпик 1";
        description = "Эпик 1...";
        idEpic = taskManager.creatingEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(),
                name, description);
        taskManager.printEpic(taskManager.getEpicList(), idEpic);
        taskManager.printEpic(taskManager.getEpicList(), idEpic);
        taskManager.printTask(taskManager.getTaskList(), idTask);
        Assertions.assertEquals(infoHistory, taskManager.printHistory(),
                "Ошибка, выводимая история задач не совпадает с ожидаемым результатом.");
    }

}