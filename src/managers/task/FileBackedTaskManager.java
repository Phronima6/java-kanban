package managers.task;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.status.Status;
import tasks.type.Type;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import exceptions.ManagerSaveException;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String filePath; // Путь для создания, записи и удаления файла

    public FileBackedTaskManager(String filePath) throws NullPointerException {
        if (filePath != null) {
            this.filePath = filePath;
        }
    }

    // Метод для создания и восстанавления задач из файла
    public void loadFromFile(String filePath) {
        Path pathTasksFile = Paths.get(filePath);
        HashMap<Integer, Integer> idEpicOldNew = new HashMap<>();
        try {
            if (!Files.exists(pathTasksFile)) { // Если файл отсутствует
                Files.createFile(pathTasksFile);
            }
        } catch (IOException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка при создании файла для хранения задач." + "!\033[0m");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String safeString = ""; // Вспомогательная строка для считывания задач из файла
            while ((safeString = bufferedReader.readLine()) != null) {
                String[] split = safeString.split(", ");
                if (split[1].equals(Type.TASK.toString())) { // Если считання строка относится к обычной Задаче
                    int idTask = super.creatingTask(getTaskList(), split[2], split[4]);
                }
                if (split[1].equals(Type.EPIC.toString())) { // Если считання строка относится к задаче типа Эпик
                    int idEpic = super.creatingEpic(getSubTaskListOfEpic(), getEpicList(), split[2], split[4]);
                    idEpicOldNew.put(Integer.parseInt(split[0]), idEpic);
                }
                if (split[1].equals(Type.SUBTASK.toString())) { // Если считання строка относится к Подзадаче
                    int idSubTask = super.creatingSubTask(getSubTaskListOfEpic(), getEpicList(),
                            idEpicOldNew.get(Integer.parseInt(split[5])), split[2], split[4]);
                }
            }
        } catch (NoSuchFileException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка, файл не найден." + "!\033[0m");
        } catch (IOException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка при чтении файла." + "!\033[0m");
        }
    }

    // Метод для сохранения задач в файл
    public void save() {
        try (BufferedWriter bufferedWriter
                     = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id, type, name, staus, description, epic");
            for (Task task : getTaskList().values()) {
                bufferedWriter.newLine();
                bufferedWriter.write(String.format("%d, %s, %s, %s, %s", task.hashCode(), task.getType(),
                        task.getName(), task.getStatus(), task.getDescription()));
            }
            for (Epic epic : getEpicList().values()) {
                bufferedWriter.newLine();
                bufferedWriter.write(String.format("%d, %s, %s, %s, %s", epic.hashCode(), epic.getType(),
                        epic.getName(), epic.getStatus(), epic.getDescription()));
            }
            for (Integer idEpic : getSubTaskListOfEpic().keySet()) {
                for (SubTask subTask : getSubTaskListOfEpic().get(idEpic).values()) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(String.format("%d, %s, %s, %s, %s, %d", subTask.hashCode(), subTask.getType(),
                            subTask.getName(), subTask.getStatus(), subTask.getDescription(), idEpic));
                }
            }
        } catch (NoSuchFileException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка, файл не найден." + "!\033[0m");
        } catch (IOException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка при записи задач в файл." + "!\033[0m");
            throw new ManagerSaveException(exception);
        }
    }

    @Override
    public int creatingTask(HashMap<Integer, Task> taskList, String name, String description) {
        int idTask = super.creatingTask(taskList, name, description);
        save();
        return idTask;
    }

    @Override
    public int creatingEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                            HashMap<Integer, Epic> epicList, String name, String description) {
        int idEpic = super.creatingEpic(subTaskListOfEpic, epicList, name, description);
        save();
        return idEpic;
    }

    @Override
    public int creatingSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                               HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        int idSubTask = super.creatingSubTask(subTaskListOfEpic, epicList, idEpic, name, description);
        save();
        return idSubTask;
    }

    @Override
    public void updateTask(HashMap<Integer, Task> taskList, int idTask, String name, String description,
                           Status status) {
        super.updateTask(taskList, idTask, name, description, status);
        save();
    }

    @Override
    public void updateEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                           HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        super.updateEpic(subTaskListOfEpic, epicList, idEpic, name, description);
        save();
    }

    @Override
    public void updateSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                              HashMap<Integer, Epic> epicList, int idEpic, int idSubTask, String name,
                              String description, Status status) {
        super.updateSubTask(subTaskListOfEpic, epicList, idEpic, idSubTask, name, description, status);
        save();
    }

    @Override
    public void delAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                            HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic) {
        super.delAllTasks(taskList, epicList, subTaskListOfEpic);
        save();
    }

    @Override
    public void delTask(HashMap<Integer, Task> taskList, int idTask) {
        super.delTask(taskList, idTask);
        save();
    }

    @Override
    public void delEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                        HashMap<Integer, Epic> epicList, int idEpic) {
        super.delEpic(subTaskListOfEpic,epicList, idEpic);
        save();
    }

    @Override
    public void delSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                           HashMap<Integer, Epic> epicList, int idEpic, int idSubTask) {
        super.delSubTask(subTaskListOfEpic,epicList, idEpic, idSubTask);
        save();
    }

}