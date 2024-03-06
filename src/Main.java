import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        HashMap<Integer, Task> taskList = new HashMap<>(); // Список обычных Задач
        HashMap<Integer, Epic> epicList = new HashMap<>(); // Список задач типа Эпик
        HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic = new HashMap<>(); // Список Подзадач с id Эпика

        /* String name = ""; // Ввод названия задачи
        String description = ""; // Ввод описания задачи
        Status status = Status.DONE; // Ввод статуса задачи
        // получаем id задачи
        int idTask = taskManager.creatingTask(taskList, name, description);
        int idEpic = taskManager.creatingEpic(subTaskListOfEpic, epicList, description);
        int idSubTask = taskManager.creatingSubTask(subTaskListOfEpic, idEpic, name, description); */

        String name = "Задача 1";
        String description = "Задача 1...";
        int idTask = taskManager.creatingTask(taskList, name, description);
        System.out.println(idTask);
        System.out.println(taskList.get(idTask));

        name = "Эпик 1";
        description = "Эпик 1...";
        int idEpic = taskManager.creatingEpic(subTaskListOfEpic, epicList, name, description);
        System.out.println(idEpic);
        System.out.println(epicList.get(idEpic));

        name = "Подзадача 1";
        description = "Подзадача 1...";
        int idSubTask = taskManager.creatingSubTask(subTaskListOfEpic, idEpic, name, description);
        System.out.println(idSubTask);
        System.out.println(subTaskListOfEpic.get(idEpic).get(idSubTask));

        name = "Подзадача 2";
        description = "Подзадача 2...";
        idSubTask = taskManager.creatingSubTask(subTaskListOfEpic, idEpic, name, description);
        System.out.println(idSubTask);
        System.out.println(subTaskListOfEpic.get(idEpic).get(idSubTask));

        name = "Подзадача 3";
        description = "Подзадача 3...";
        idSubTask = taskManager.creatingSubTask(subTaskListOfEpic, idEpic, name, description);
        System.out.println(idSubTask);
        System.out.println(subTaskListOfEpic.get(idEpic).get(idSubTask));

        name = "Подзадача 3";
        description = "Подзадача 3...";
        Status status = Status.DONE;
        taskManager.updateSubTask(subTaskListOfEpic, epicList, idEpic, idSubTask, name, description, status);
        System.out.println(idSubTask);
        System.out.println(subTaskListOfEpic.get(idEpic).get(idSubTask));
        System.out.println(epicList.get(idEpic));

        name = "Эпик 2";
        description = "Эпик 2...";
        taskManager.updateEpic(subTaskListOfEpic, epicList, idEpic, name, description);
        System.out.println(idEpic);
        System.out.println(epicList.get(idEpic));

        System.out.println(taskManager.printTask(taskList, idTask));

        System.out.println(taskManager.printEpic(epicList, idEpic));

        System.out.println(taskManager.printSubTask(subTaskListOfEpic, idEpic, idSubTask));

        System.out.println(taskManager.printAllEpicSubTasks(subTaskListOfEpic, idEpic));

        System.out.println(taskManager.printAllTasks(taskList, epicList, subTaskListOfEpic));

        taskManager.delTask(taskList, idTask);
        System.out.println(taskManager.printAllTasks(taskList, epicList, subTaskListOfEpic));

        taskManager.delSubTask (subTaskListOfEpic, epicList, idEpic, idSubTask);
        System.out.println(taskManager.printAllTasks(taskList, epicList, subTaskListOfEpic));

        taskManager.delAllTasks(taskList, epicList, subTaskListOfEpic);
        System.out.println(taskManager.printAllTasks(taskList, epicList, subTaskListOfEpic) + "Пусто");

    }
}