package test;

import managers.Managers;
import managers.task.TaskManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserTestSprint7 { // Дополнительное задание Спринта № 7. Реализуем пользовательский сценарий

    public static void main(String[] args) {

        Managers managers = new Managers();
        String filePath = "SaveTasks.txt"; // Путь для создания, записи и удаления файла
        TaskManager taskManagerOne = managers.getFileBackedTaskManager(filePath); // Создаём первый Менеджер задач

        // Создаём Задачу № 1 и выводим её значение
        taskManagerOne.creatingTask("Задача 1", "Задача 1...", "2021-12-21T21:00:00",
                10);

        // Создаём задачу типа Эпик № 1 и выводим её значение
        int idEpic = taskManagerOne.creatingEpic("Эпик 1", "Эпик 1...");

        // Создаём Подзадачу № 1 для задачи типа Эпик № 1 и выводим её значение
        taskManagerOne.creatingSubTask(idEpic, "Подзадача 1.1", "Подзадача 1.1...",
                "2021-12-21T22:00:00", 10);

        TaskManager taskManagerTwo = managers.getFileBackedTaskManager(filePath); // Создаём второй Менеджер задач

        System.out.println(taskManagerTwo.printAllTasks());

        // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

}