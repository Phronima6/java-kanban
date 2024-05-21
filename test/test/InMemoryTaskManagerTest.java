package test;

import managers.Managers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import tasks.status.Status;
import managers.task.*;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest {

    Managers managers = new Managers();
    static String filePath = "SaveTasks.txt"; // Путь для создания, записи и удаления файла
    TaskManager taskManager;
    int idTask; // id обычной Задачи
    int idEpic; // id задачи типа Эпик
    int idSubTask; // id Подзадачи

    // Перед каждым тестом удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
    @BeforeEach
    public void deleteFileBeforeEach() {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        taskManager = managers.getFileBackedTaskManager(filePath);
    }

    // После всех тестов удаляем созданный файл (необходимо для корректной работы созданныъ ранее тестов)
    @AfterAll
    public static void deleteFileAfterAll() {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // Проверяем: создание, обновление, вывод, удаление и совпадение id обычной Задачи
    @Test
    public void createUpdatePrintDeleteTask() {
        Task task = new Task("Задача 1", "Задача 1...", LocalDateTime.parse("2021-12-21T22:00:00"),
                Duration.ofMinutes(10));
        idTask = taskManager.creatingTask("Задача 1", "Задача 1...",
                "2021-12-21T22:00:00", 10);
        Assertions.assertEquals(String.valueOf(task), taskManager.printTask(idTask),
                "Ошибка, обычные Задачи не совпадают.");
        Assertions.assertEquals(task.hashCode(), idTask, "Ошибка, id обычных Задач не совпадает.");
        taskManager.updateTask(idTask, "Задача 2", "Задача 2...",
                Status.DONE, "2021-12-21T23:00:00", 20);
        Assertions.assertNotEquals(String.valueOf(task), taskManager.printTask(idTask),
                "Ошибка, обычные Задачи совпадают.");
        Assertions.assertEquals(task.hashCode(), idTask, "Ошибка, id обычных Задач не совпадает.");
        taskManager.delTask(idTask);
        Assertions.assertNull(taskManager.printTask(idTask),
                "Ошибка, обычная Задача не удалена.");
    }

    // Проверяем: создание, обновление, вывод, удаление, совпадение id задач типа Эпик
    @Test
    public void createUpdatePrintDeleteEpic() {
        Epic epic = new Epic("Эпик 1", "Эпик 1...", null, null);
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        Assertions.assertEquals(String.valueOf(epic), taskManager.printEpic(idEpic),
                "Ошибка, задачи типа Эпик не совпадают.");
        Assertions.assertEquals(epic.hashCode(), idEpic, "Ошибка, id задач типа Эпик не совпадает.");
        taskManager.updateEpic(idEpic, "Эпик 2", "Эпик 2...");
        Assertions.assertNotEquals(String.valueOf(epic), taskManager.printEpic(idEpic),
                "Ошибка, задачи типа Эпик совпадают.");
        Assertions.assertEquals(epic.hashCode(), idEpic, "Ошибка, id задач типа Эпик не совпадает.");
        taskManager.delEpic(idEpic);
        Assertions.assertNull(taskManager.printEpic(idEpic), "Ошибка, задача типа Эпик не удалена.");
    }

    // Проверяем: создание, обновление, вывод, удаление, совпадение id Подзадачи, изменение статуса задачи типа Эпик
    @Test
    public void createUpdatePrintDeleteSubTask() {
        SubTask subTask = new SubTask("Подзадача 1", "Подзадача 1...",
                LocalDateTime.parse("2021-12-21T22:00:00"), Duration.ofMinutes(10));
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 1", "Подзадача 1...",
                "2021-12-21T22:00:00", 10);
        Assertions.assertEquals(String.valueOf(subTask), taskManager.printSubTask(idEpic, idSubTask),
                "Ошибка, Подзадачи не совпадают.");
        Assertions.assertEquals(subTask.hashCode(), idSubTask, "Ошибка, id Подзадач не совпадает.");
        taskManager.updateSubTask(idEpic, idSubTask, "Подзадача 2", "Подзадача 2...",
                Status.DONE, "2021-12-21T21:00:00",20);
        Assertions.assertNotEquals(String.valueOf(subTask), taskManager.printSubTask(idEpic, idSubTask),
                "Ошибка, Подзадачи совпадают.");
        Assertions.assertEquals(subTask.hashCode(), idSubTask, "Ошибка, id Подзадач не совпадает.");
        Assertions.assertEquals(Status.DONE, taskManager.getEpicList().get(idEpic).getStatus(),
                "Ошибка, статус задачи типа Эпик отличается от ожидаемого");
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 3", "Подзадача 3...",
                "2021-12-21T23:00:00",20);
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicList().get(idEpic).getStatus(),
                "Ошибка, статус задачи типа Эпик отличается от ожидаемого");
        taskManager.delSubTask(idEpic, idSubTask);
        Assertions.assertNull(taskManager.printSubTask(idEpic, idSubTask), "Ошибка, Подзадача не удалена.");
    }

    // Проверяем: совпадение id Задачи, id задачи типа Эпик и id Подзадачи
    @Test
    public void idTasksNotEquals() {
        idTask = taskManager.creatingTask("Задача 1", "Задача 1...", "2021-12-21T21:00:00",
                10);
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 1", "Подзадача 1...",
                "2021-12-21T22:00:00",20);
        Assertions.assertNotEquals(idTask, idEpic, "Ошибка, id Задачи и задачи типа Эпик совпадает.");
        Assertions.assertNotEquals(idEpic, idSubTask, "Ошибка, id Задачи и задачи типа Эпик совпадает.");
    }

    // Проверяем: печать всех задач, печать всех Подзадач, удаление всех задач
    @Test
    public void printDeleteAllTasks() {
        String infoAllTasks; // Ожидаемый результат вывода всех задач
        String infoAllEpicSubTasks; // Ожидаемый результат вывода всех Подзадач
        Task task = new Task("Задача 1", "Задача 1...", LocalDateTime.parse("2021-12-21T21:00:00"),
                Duration.ofMinutes(10));
        Epic epic = new Epic("Эпик 1", "Эпик 1...",
                LocalDateTime.parse("2021-12-21T22:00:00"), Duration.ofMinutes(10));
        SubTask subTaskOne = new SubTask("Подзадача 1", "Подзадача 1...",
                LocalDateTime.parse("2021-12-21T22:00:00"), Duration.ofMinutes(10));
        infoAllTasks = task + "\n" + epic + "\n" + subTaskOne + "\n";
        infoAllEpicSubTasks = subTaskOne + "\n";
        idTask = taskManager.creatingTask("Задача 1", "Задача 1...", "2021-12-21T21:00:00",
                10);
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 1", "Подзадача 1...",
                "2021-12-21T22:00:00",10);
        Assertions.assertEquals(infoAllTasks, taskManager.printAllTasks(),
                "Ошибка, выводимые задачи не совпадают с ожидаемыми.");
        Assertions.assertEquals(infoAllEpicSubTasks,
                taskManager.printAllEpicSubTasks(idEpic),
                "Ошибка, выводимые Подзадачи не совпадают с ожидаемыми.");
        taskManager.delAllTasks();
        Assertions.assertEquals("", taskManager.printAllTasks(), "Ошибка, задачи не удалены.");
    }

    // Проверяем: исключение повторов в истории задач и порядок сохранения задачи в историю
    @Test
    public void sizeHistorySavingVersion() {
        String infoHistory; // Ожидаемый результат вывода истории задач
        Task taskOne = new Task("Задача 1", "Задача 1...",
                LocalDateTime.parse("2021-12-21T22:00:00"), Duration.ofMinutes(10));
        Task taskTwo = new Task("Задача 2", "Задача 2...",
                LocalDateTime.parse("2021-12-21T23:00:00"), Duration.ofMinutes(10));
        Epic epic = new Epic("Эпик 1", "Эпик 1...", null, null);
        infoHistory = taskOne + "\n" + epic + "\n" + taskTwo + "\n";
        idTask = taskManager.creatingTask("Задача 1", "Задача 1...", "2021-12-21T22:00:00",
                10);
        taskManager.printTask(idTask);
        taskManager.printTask(idTask);
        idTask = taskManager.creatingTask("Задача 2", "Задача 2...", "2021-12-21T23:00:00",
                10);
        taskManager.printTask(idTask);
        taskManager.printTask(idTask);
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        taskManager.printEpic(idEpic);
        taskManager.printEpic(idEpic);
        taskManager.printTask(idTask);
        Assertions.assertEquals(infoHistory, taskManager.printHistory(),
                "Ошибка, выводимая история задач не совпадает с ожидаемым результатом.");
    }

    // Проверяем: корректность обновления DataTime для задач типа Эпик
    @Test
    public void updateDateTimeOfEpic() {
        Epic epicOne = new Epic("Эпик 1", "Эпик 1...", LocalDateTime.parse("2000-01-01T00:00:00"),
                Duration.ofMinutes(180));
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 1", "Подзадача 1...",
                "2000-01-01T00:00:00", 60);
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 2", "Подзадача 2...",
                "2000-01-01T01:00:00", 60);
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 3", "Подзадача 3...",
                "2000-01-01T02:00:00", 60);
        Assertions.assertEquals(String.valueOf(epicOne), taskManager.printEpic(idEpic));
        Epic epicTwo = new Epic("Эпик 1", "Эпик 1...", LocalDateTime.parse("2000-01-01T00:00:00"),
                Duration.ofMinutes(120));
        taskManager.delSubTask(idEpic, idSubTask);
        Assertions.assertEquals(String.valueOf(epicTwo), taskManager.printEpic(idEpic));
    }

    // Проверяем: сохранение, вывод и корректность удаления задач, отсорттрованных по приоритету
    @Test
    public void intersectionDataTimeAndDel() {
        String infoAllSortedTasks; // Ожидаемый результат вывода всех задач, отсортированных по приоритету
        Task taskOne = new Task("Задача 1", "Задача 1...",
                LocalDateTime.parse("2021-12-29T21:00:00"), Duration.ofMinutes(10));
        Task taskTwo = new Task("Задача 2", "Задача 2...",
                LocalDateTime.parse("2021-12-21T22:00:00"), Duration.ofMinutes(10));
        SubTask subTask = new SubTask("Подзадача 1", "Подзадача 1...",
                LocalDateTime.parse("2021-12-23T22:00:00"), Duration.ofMinutes(10));
        infoAllSortedTasks = taskTwo + "\n" + subTask + "\n" + taskOne + "\n";
        idTask = taskManager.creatingTask("Задача 1", "Задача 1...", "2021-12-29T21:00:00",
                10);
        idTask = taskManager.creatingTask("Задача 2", "Задача 2...", "2021-12-21T22:00:00",
                10);
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 1", "Подзадача 1...",
                "2021-12-23T22:00:00",10);
        Assertions.assertEquals(infoAllSortedTasks, taskManager.getPrioritizedTasks(), "Ошибка, выводимые"
                + " задачи, отсортированные по приоритету не совпадает с ожидаемым результатом.");
        infoAllSortedTasks = taskTwo + "\n" + taskOne + "\n";
        taskManager.delEpic(idEpic);
        Assertions.assertEquals(infoAllSortedTasks, taskManager.getPrioritizedTasks(), "Ошибка, выводимые"
                + " задачи, отсортированные по приоритету не совпадает с ожидаемым результатом.");
    }

}