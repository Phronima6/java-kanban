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

    // Метод для создания и восстановления задач из файла
    public void loadFromFile(String filePath) {
        Path pathTasksFile = Paths.get(filePath);
        HashMap<Integer, Integer> idEpicOldNew = new HashMap<>();
        try {
            if (!Files.exists(pathTasksFile)) { // Если файл отсутствует
                Files.createFile(pathTasksFile);
            }
        } catch (IOException exception) {
            System.out.println("Ошибка при создании файла для хранения задач.");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String safeString = ""; // Вспомогательная строка для считывания задач из файла
            while ((safeString = bufferedReader.readLine()) != null) {
                String[] split = safeString.split(", ");
                if (split[1].equals(Type.TASK.toString())) { // Если считанная строка относится к обычной Задаче
                    int idTask = super.creatingTask(split[2], split[4], split[5], Integer.parseInt(split[6]));
                }
                if (split[1].equals(Type.EPIC.toString())) { // Если считанная строка относится к задаче типа Эпик
                    int idEpic = super.creatingEpic(split[2], split[4]);
                    idEpicOldNew.put(Integer.parseInt(split[0]), idEpic);
                }
                if (split[1].equals(Type.SUBTASK.toString())) { // Если считанная строка относится к Подзадаче
                    int idSubTask = super.creatingSubTask(idEpicOldNew.get(Integer.parseInt(split[7])),
                            split[2], split[4], split[5], Integer.parseInt(split[6]));
                }
            }
        } catch (NoSuchFileException exception) {
            System.out.println("Ошибка, файл не найден.");
        } catch (IOException exception) {
            System.out.println("Ошибка при чтении файла.");
        }
    }

    // Метод для сохранения задач в файл
    public void save() {
        try (BufferedWriter bufferedWriter
                     = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id, type, name, staus, description, startTime, duration, epic");
            for (Task task : getTaskList().values()) {
                bufferedWriter.newLine();
                bufferedWriter.write(String.format("%d, %s, %s, %s, %s, %s, %d", task.hashCode(), task.getType(),
                        task.getName(), task.getStatus(), task.getDescription(), task.getStartTime(),
                        task.getDuration().toMinutes()));
            }
            for (Epic epic : getEpicList().values()) {
                bufferedWriter.newLine();
                bufferedWriter.write(String.format("%d, %s, %s, %s, %s", epic.hashCode(), epic.getType(),
                        epic.getName(), epic.getStatus(), epic.getDescription()));
            }
            for (Integer idEpic : getSubTaskListOfEpic().keySet()) {
                for (SubTask subTask : getSubTaskListOfEpic().get(idEpic).values()) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(String.format("%d, %s, %s, %s, %s, %s, %d, %d", subTask.hashCode(),
                            subTask.getType(), subTask.getName(), subTask.getStatus(), subTask.getDescription(),
                            subTask.getStartTime(), subTask.getDuration().toMinutes(), idEpic));
                }
            }
        } catch (NoSuchFileException exception) {
            System.out.println("Ошибка, файл не найден.");
        } catch (IOException exception) {
            System.out.println("Ошибка при записи задач в файл.");
            throw new ManagerSaveException(exception);
        }
    }

    @Override
    public int creatingTask(String name, String description, String startTime, long duration) {
        int idTask = super.creatingTask(name, description, startTime, duration);
        save();
        return idTask;
    }

    @Override
    public int creatingEpic(String name, String description) {
        int idEpic = super.creatingEpic(name, description);
        save();
        return idEpic;
    }

    @Override
    public int creatingSubTask(int idEpic, String name, String description, String startTime, long duration) {
        int idSubTask = super.creatingSubTask(idEpic, name, description, startTime, duration);
        save();
        return idSubTask;
    }

    @Override
    public void updateTask(int idTask, String name, String description, Status status, String startTime,
                           long duration) {
        super.updateTask(idTask, name, description, status, startTime, duration);
        save();
    }

    @Override
    public void updateEpic(int idEpic, String name, String description) {
        super.updateEpic(idEpic, name, description);
        save();
    }

    @Override
    public void updateSubTask(int idEpic, int idSubTask, String name, String description, Status status,
                              String startTime, long duration) {
        super.updateSubTask(idEpic, idSubTask, name, description, status, startTime, duration);
        save();
    }

    @Override
    public void delAllTasks() {
        super.delAllTasks();
        save();
    }

    @Override
    public void delTask(int idTask) {
        super.delTask(idTask);
        save();
    }

    @Override
    public void delEpic(int idEpic) {
        super.delEpic(idEpic);
        save();
    }

    @Override
    public void delSubTask(int idEpic, int idSubTask) {
        super.delSubTask(idEpic, idSubTask);
        save();
    }

}