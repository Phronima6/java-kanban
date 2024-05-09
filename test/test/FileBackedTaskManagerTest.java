package test;

import managers.Managers;
import managers.task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class FileBackedTaskManagerTest {

    Managers managers = new Managers();
    String filePath = "SaveTasks.txt"; // Путь для создания, записи и удаления файла
    TaskManager taskManager = managers.getFileBackedTaskManager(filePath);
    int idTask; // id обычной Задачи
    int idEpic; // id задачи типа Эпик
    int idSubTask; // id Подзадачи

    // Проверяем создание пустого файла
    @Test
    public void createEmptyFileTasks() {
        int quantityString = 0; // Счётчик строк записанных в файл
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            while (bufferedReader.readLine() != null) {
                quantityString++;
            }
        } catch (NoSuchFileException exception) {
            System.out.println("Ошибка, файл не найден.");
        } catch (IOException exception) {
            System.out.println("Ошибка при чтении файла.");
        }
        Assertions.assertEquals(0, quantityString, "Ошибка, файл не пустой.");
    }

    // Проверяем сохранение задач в файл
    @Test
    public void createTasksSaveFile() {
        int quantityString = 0; // Счётчик строк записанных в файл
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 1.1", "Подзадача 1.1...",
                "2021-12-21T21:00:00", 10);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            while (bufferedReader.readLine() != null) {
                quantityString++;
            }
        } catch (NoSuchFileException exception) {
            System.out.println("Ошибка, файл не найден.");
        } catch (IOException exception) {
            System.out.println("Ошибка при чтении файла.");
        }
        Assertions.assertEquals(3, quantityString, "Ошибка, количество строк в файле не соответствует "
                + "ожидаемому значению.");

        // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // Проверяем восстановление задач из файла
    @Test
    public void createTasksFromFile() {
        int quantityTasks = 0; // Счётчик задач записаных в HashMap
        idTask = taskManager.creatingTask("Задача 1", "Задача 1...", "2021-12-21T21:00:00",
                10);
        idTask = taskManager.creatingTask("Задача 2", "Задача 2...","2021-12-21T22:00:00",
                10);
        TaskManager taskManagerOne = managers.getFileBackedTaskManager(filePath);
        for (Task task : taskManagerOne.getTaskList().values()) {
            quantityTasks++;
        }
        Assertions.assertEquals(2, quantityTasks, "Ошибка, количество восстановленных из файла "
                + "задач не соответствует ожидаемому згачению.");

        // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}