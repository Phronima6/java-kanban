package test;

import managers.Managers;
import managers.task.TaskManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserTestSprint7 { // Дополнительное задание Спринта № 7. Реализуем пользовательский сценарий

    public static void main(String[] args) {

        Managers managers = new Managers();
        String filePath = "C:/Users/schav/IdeaProjects/test.txt"; // Путь для создания, записи и удаления файла
        TaskManager taskManagerOne = managers.getFileBackedTaskManager(filePath); // Создаём первый Менеджер задач
        int idTask; // id обычной Задачи
        int idEpic; // id задачи типа Эпик
        int idSubTask; // id Подзадачи

        // Создаём Задачу № 1 и выводим её значение
        idTask = taskManagerOne.creatingTask(taskManagerOne.getTaskList(), "Задача 1", "Задача 1...");

        // Создаём задачу типа Эпик № 1 и выводим её значение
        idEpic = taskManagerOne.creatingEpic(taskManagerOne.getSubTaskListOfEpic(), taskManagerOne.getEpicList(),
                "Эпик 1", "Эпик 1...");

        // Создаём Подзадачу № 1 для задачи типа Эпик № 1 и выводим её значение
        idSubTask = taskManagerOne.creatingSubTask(taskManagerOne.getSubTaskListOfEpic(), taskManagerOne.getEpicList(),
                idEpic, "Подзадача 1.1", "Подзадача 1.1...");

        TaskManager taskManagerTwo = managers.getFileBackedTaskManager(filePath); // Создаём второй Менеджер задач

        System.out.println(taskManagerTwo.printAllTasks(taskManagerTwo.getTaskList(), taskManagerTwo.getEpicList(),
                taskManagerTwo.getSubTaskListOfEpic()));

        // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

}