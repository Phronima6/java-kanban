package test;

import managers.Managers;
import managers.task.TaskManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserTestSprint6 { // Дополнительное задание Спринта № 6. Реализуем пользовательский сценарий

    public static void main(String[] args) {

        Managers managers = new Managers();
        String filePath = "SaveTasks.txt"; // Путь для создания, записи и удаления файла
        // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        TaskManager taskManager = managers.getFileBackedTaskManager(filePath);
        int idTask; // id обычной Задачи
        int idEpic; // id задачи типа Эпик
        int idSubTask; // id Подзадачи

        // Создаём Задачу № 1 и выводим её значение
        idTask = taskManager.creatingTask("Задача 1", "Задача 1...", "2021-12-20T11:30:00",
                10);
        System.out.println(taskManager.printTask(idTask));

        // Создаём Задачу № 2 и выводим её значение
        idTask = taskManager.creatingTask("Задача 2", "Задача 2...", "2021-12-21T12:22:21",
                10);
        System.out.println(taskManager.printTask(idTask));

        // Создаём задачу типа Эпик № 1 и выводим её значение
        idEpic = taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        System.out.println(taskManager.printEpic(idEpic));

        // Создаём Подзадачу № 1 для задачи типа Эпик № 1 и выводим её значение
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 1.1", "Подзадача 1.1...",
                "2021-12-22T21:00:00", 10);
        System.out.println(taskManager.printSubTask(idEpic, idSubTask));

        // Создаём Подзадачу № 2 для задачи типа Эпик № 1 и выводим её значение
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 2.1", "Подзадача 2.1...",
                "2021-12-23T21:20:00", 10);
        System.out.println(taskManager.printSubTask(idEpic, idSubTask));

        // Создаём Подзадачу № 3 для задачи типа Эпик № 1 и выводим её значение
        idSubTask = taskManager.creatingSubTask(idEpic, "Подзадача 3.1", "Подзадача 3.1...",
                "2021-12-24T21:30:00", 10);
        System.out.println(taskManager.printSubTask(idEpic, idSubTask));

        // Выводим значение задачи типа Эпик № 1
        System.out.println(taskManager.printEpic(idEpic));
        System.out.println(taskManager.printEpic(idEpic));

        // Удаляем задачу типа Эпик № 1 и как следствие все её подзадачи
        taskManager.delEpic(idEpic);

        // Создаём задачу типа Эпик № 2 и выводим её значение
        idEpic = taskManager.creatingEpic("Эпик 2", "Эпик 2...");
        System.out.println(taskManager.printEpic(idEpic));

        // Выводим значение Задачи № 2
        System.out.println(taskManager.printTask(idTask));
        System.out.println(taskManager.printTask(idTask));
        System.out.println(taskManager.printTask(idTask));

        System.out.print("\n");

        // Выводим историю задач
        System.out.println(taskManager.printHistory());

        // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

}