package test;

import managers.Managers;
import managers.task.TaskManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserTestSprint6 { // Дополнительное задание Спринта № 6. Реализуем пользовательский сценарий

    public static void main(String[] args) {

        Managers managers = new Managers();
        String filePath = "C:/Users/schav/IdeaProjects/test.txt"; // Путь для создания, записи и удаления файла
        TaskManager taskManager = managers.getFileBackedTaskManager(filePath);
        int idTask; // id обычной Задачи
        int idEpic; // id задачи типа Эпик
        int idSubTask; // id Подзадачи

        // Создаём Задачу № 1 и выводим её значение
        idTask = taskManager.creatingTask(taskManager.getTaskList(), "Задача 1", "Задача 1...");
        System.out.println(taskManager.printTask(taskManager.getTaskList(), idTask));

        // Создаём Задачу № 2 и выводим её значение
        idTask = taskManager.creatingTask(taskManager.getTaskList(), "Задача 2", "Задача 2...");
        System.out.println(taskManager.printTask(taskManager.getTaskList(), idTask));

        // Создаём задачу типа Эпик № 1 и выводим её значение
        idEpic = taskManager.creatingEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(),
                "Эпик 1", "Эпик 1...");
        System.out.println(taskManager.printEpic(taskManager.getEpicList(), idEpic));

        // Создаём Подзадачу № 1 для задачи типа Эпик № 1 и выводим её значение
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                "Подзадача 1.1", "Подзадача 1.1...");
        System.out.println(taskManager.printSubTask(taskManager.getSubTaskListOfEpic(), idEpic, idSubTask));

        // Создаём Подзадачу № 2 для задачи типа Эпик № 1 и выводим её значение
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                "Подзадача 2.1", "Подзадача 2.1...");
        System.out.println(taskManager.printSubTask(taskManager.getSubTaskListOfEpic(), idEpic, idSubTask));

        // Создаём Подзадачу № 3 для задачи типа Эпик № 1 и выводим её значение
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic,
                "Подзадача 3.1", "Подзадача 3.1...");
        System.out.println(taskManager.printSubTask(taskManager.getSubTaskListOfEpic(), idEpic, idSubTask));

        // Выводим значение задачи типа Эпик № 1
        System.out.println(taskManager.printEpic(taskManager.getEpicList(), idEpic));
        System.out.println(taskManager.printEpic(taskManager.getEpicList(), idEpic));

        // Удаляем задачу типа Эпик № 1 и как следствие все её подзадачи
        taskManager.delEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(), idEpic);

        // Создаём задачу типа Эпик № 2 и выводим её значение
        idEpic = taskManager.creatingEpic(taskManager.getSubTaskListOfEpic(), taskManager.getEpicList(),
                "Эпик 2", "Эпик 2...");
        System.out.println(taskManager.printEpic(taskManager.getEpicList(), idEpic));

        // Выводим значение Задачи № 2
        System.out.println(taskManager.printTask(taskManager.getTaskList(), idTask));
        System.out.println(taskManager.printTask(taskManager.getTaskList(), idTask));
        System.out.println(taskManager.printTask(taskManager.getTaskList(), idTask));

        System.out.print("\n");

        // Выводим истоитю задач
        System.out.println(taskManager.printHistory());

        // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

}