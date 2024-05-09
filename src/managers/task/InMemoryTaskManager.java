package managers.task;

import managers.history.HistoryManager;
import managers.Managers;
import tasks.*;
import tasks.status.Status;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import exceptions.IntersectionException;

public class InMemoryTaskManager implements TaskManager {

    Managers managers = new Managers();
    HistoryManager historyManager = managers.getDefaultHistory();

    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic = new HashMap<>();
    private TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    @Override
    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    @Override
    public HashMap<Integer, HashMap<Integer, SubTask>> getSubTaskListOfEpic() {
        return subTaskListOfEpic;
    }

    public TreeSet<Task> getSortedTasks() {
        return sortedTasks;
    }

    @Override
    public int creatingTask(String name, String description, String startTime, long duration) {
        LocalDateTime localDateTime = LocalDateTime.parse(startTime);
        Duration durationTask = Duration.ofMinutes(duration);
        intersectionDataTime(localDateTime, durationTask);
        Task task = new Task(name, description, localDateTime, durationTask);
        getTaskList().put(task.hashCode(), task);
        getSortedTasks().add(task);
        return task.hashCode();
    }

    @Override
    public int creatingEpic(String name, String description) {
        Epic epic = new Epic(name, description, null, null);
        getEpicList().put(epic.hashCode(), epic);
        HashMap<Integer, SubTask> subTaskList = new HashMap<>(); // Создаём хеш-таблицу для хранения Подзадач
        getSubTaskListOfEpic().put(epic.hashCode(), subTaskList); // для конкретной задачи типа Эпик
        return epic.hashCode();
    }

    @Override
    public int creatingSubTask(int idEpic, String name, String description, String startTime, long duration) {
        LocalDateTime localDateTime = LocalDateTime.parse(startTime);
        Duration durationTask = Duration.ofMinutes(duration);
        intersectionDataTime(localDateTime, durationTask);
        if (getSubTaskListOfEpic().containsKey(idEpic)) { // Если создана задача типа Эпик
            SubTask subTask = new SubTask(name, description, localDateTime, durationTask);
            getSubTaskListOfEpic().get(idEpic).put(subTask.hashCode(), subTask);
            getSortedTasks().add(subTask);
            updateStatusOfEpic(idEpic);
            updateDateTimeOfEpic(idEpic);
            return subTask.hashCode();
        } else {
            return 0;
        }
    }

    @Override
    public void updateTask(int idTask, String name, String description, Status status, String startTime,
                           long duration) {
        LocalDateTime localDateTime = LocalDateTime.parse(startTime);
        Duration durationTask = Duration.ofMinutes(duration);
        intersectionDataTime(localDateTime, durationTask);
        if (getTaskList().containsKey(idTask)) { // Если создана обычная Задача
            Task task = new Task(name, description, localDateTime, durationTask);
            task.setStatus(status);
            getTaskList().put(idTask, task);
            getSortedTasks().add(task);
        }
    }

    @Override
    public void updateEpic(int idEpic, String name, String description) {
        if (getEpicList().containsKey(idEpic)) { // Если создана задача типа Эпик
            Epic epic = new Epic(name, description, getEpicList().get(idEpic).getStartTime(),
                    getEpicList().get(idEpic).getDuration());
            getEpicList().put(idEpic, epic);
            if (!getSubTaskListOfEpic().get(idEpic).isEmpty()) { // Если Подзадачи есть
                updateStatusOfEpic(idEpic);
            }
        }
    }

    @Override
    public void updateSubTask(int idEpic, int idSubTask, String name, String description, Status status,
                              String startTime, long duration) {
        LocalDateTime localDateTime = LocalDateTime.parse(startTime);
        Duration durationTask = Duration.ofMinutes(duration); // Если создана задача типа Эпик и Подзадача
        intersectionDataTime(localDateTime, durationTask);
        if (getSubTaskListOfEpic().containsKey(idEpic) && getSubTaskListOfEpic().get(idEpic).containsKey(idSubTask)) {
            SubTask subTask = new SubTask(name, description, localDateTime, durationTask);
            subTask.setStatus(status);
            getSubTaskListOfEpic().get(idEpic).put(idSubTask, subTask);
            getSortedTasks().add(subTask);
            updateStatusOfEpic(idEpic);
            updateDateTimeOfEpic(idEpic);
        }
    }

    @Override
    public String printAllTasks() {
        String infoAllTasks = "";
        if (!getTaskList().isEmpty()) { // Если список обычных Задач не пустой
            for (Task task : getTaskList().values()) {
                infoAllTasks += task + "\n";
                historyManager.addHistory(task);
            }
        }
        if (!getEpicList().isEmpty()) { // Если список задач типа Эпик не пустой
            for (Epic epic : getEpicList().values()) {
                infoAllTasks += epic + "\n";
                historyManager.addHistory(epic);
            }
        }
        if (!getSubTaskListOfEpic().isEmpty()) { // Если список Подзадач не пустой
            for (Integer id : getSubTaskListOfEpic().keySet()) {
                for (SubTask subTask : getSubTaskListOfEpic().get(id).values()) {
                    if (subTask != null) {
                        infoAllTasks += subTask + "\n";
                        historyManager.addHistory(subTask);
                    }
                }
            }
        }
        return infoAllTasks;
    }

    @Override
    public String printAllEpicSubTasks(int idEpic) {
        String infoAllSubTaskOfEpic = ""; // Если задача типа Эпик создана и список Подзадач не пустой
        if (getSubTaskListOfEpic().containsKey(idEpic) && !getSubTaskListOfEpic().get(idEpic).isEmpty()) {
            for (SubTask subTask : getSubTaskListOfEpic().get(idEpic).values()) {
                infoAllSubTaskOfEpic += subTask + "\n";
                historyManager.addHistory(subTask);
            }
        }
        return infoAllSubTaskOfEpic;
    }

    @Override
    public String printTask(int idTask) {
        if (getTaskList().containsKey(idTask)) { // Если обычная Задача создана
            historyManager.addHistory(getTaskList().get(idTask));
            return String.valueOf(getTaskList().get(idTask));
        }
        return null;
    }

    @Override
    public String printEpic(int idEpic) {
        if (getEpicList().containsKey(idEpic)) { // Если задача типа Эпик создана
            historyManager.addHistory(getEpicList().get(idEpic));
            return String.valueOf(getEpicList().get(idEpic));
        }
        return null;
    }

    @Override
    public String printSubTask(int idEpic, int idSubTask) {
        if (getSubTaskListOfEpic().get(idEpic).containsKey(idSubTask)) { // Если Подзадача создана
            historyManager.addHistory(getSubTaskListOfEpic().get(idEpic).get(idSubTask));
            return String.valueOf(getSubTaskListOfEpic().get(idEpic).get(idSubTask));
        }
        return null;
    }

    @Override
    public String printHistory() {
        String infoHistory = "";
        if (!historyManager.getHistory().isEmpty()) {
            for (Task task : historyManager.getHistory()) {
                infoHistory += task + "\n";
            }
        }
        return infoHistory;
    }

    @Override
    public String getPrioritizedTasks() {
        String infoSortedTasks = getSortedTasks().stream()
                .map(task -> task.toString() + "\n")
                .collect(Collectors.joining());
        return infoSortedTasks;
    }

    @Override
    public void delAllTasks() {
        if (!getTaskList().isEmpty()) { // Если список обычных Задач не пустой
            getTaskList().clear();
        }
        if (!getEpicList().isEmpty()) { // Если список задач типа Эпик не пустой
            getEpicList().clear();
        }
        if (!getSubTaskListOfEpic().isEmpty()) { // Если список Подзадач не пустой
            getSubTaskListOfEpic().clear();
        }
        historyManager.remove(); // Удаляем все задачи из истории
        getSortedTasks().clear(); // Удаляем все задачи, отсортированные по приоритету
    }

    @Override
    public void delTask(int idTask) {
        if (getTaskList().containsKey(idTask)) { // Если обычная Задача создана
            getSortedTasks().remove(getTaskList().get(idTask)); // Удаляем Задачу, отсортированную по приоритету
            getTaskList().remove(idTask);
        }
        historyManager.remove(idTask); // Удаляем обычную Задачу из истории
    }

    @Override
    public void delEpic(int idEpic) {
        if (getEpicList().containsKey(idEpic)) { // Если задача типа Эпик создана
            getEpicList().remove(idEpic);
            for (Integer id : getSubTaskListOfEpic().get(idEpic).keySet()) {
                // Удаляем Подзадачи, отсортированные по приоритету
                getSortedTasks().remove(getSubTaskListOfEpic().get(idEpic).get(id));
                historyManager.remove(id); // Удаляем из истории все Подзадачи, которые относятся к задаче типа Эпик
            }
            if (getSubTaskListOfEpic().containsKey(idEpic)) { // Если список Подзадач конктерной задачи типа Эпик создан
                getSubTaskListOfEpic().remove(idEpic);
            }
            historyManager.remove(idEpic); // Удаляем задачу типа Эпик из истории
        }
    }

    @Override
    public void delSubTask(int idEpic, int idSubTask) {
        if (getSubTaskListOfEpic().get(idEpic).containsKey(idSubTask)) { // Если Подзадача создана
            // Удаляем Подзадачу, отсортированную по приоритету
            getSortedTasks().remove(getSubTaskListOfEpic().get(idEpic).get(idSubTask));
            getSubTaskListOfEpic().get(idEpic).remove(idSubTask);
            updateStatusOfEpic(idEpic);
            updateDateTimeOfEpic(idEpic);
        }
        historyManager.remove(idSubTask); // Удаляем Подзадачу из истории
    }

    @Override
    public void updateStatusOfEpic(int idEpic) {
        int statusNew = 0;
        int statusDone = 0;
        for (SubTask subTask : getSubTaskListOfEpic().get(idEpic).values()) {
            if (subTask.getStatus() == Status.NEW) {
                statusNew++;
            } else if (subTask.getStatus() == Status.DONE) {
                statusDone++;
            }
        }
        if (statusNew == getSubTaskListOfEpic().get(idEpic).size()) {
            getEpicList().get(idEpic).setStatus(Status.NEW);
        } else if (statusDone == getSubTaskListOfEpic().get(idEpic).size()) {
            getEpicList().get(idEpic).setStatus(Status.DONE);
        } else {
            getEpicList().get(idEpic).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateDateTimeOfEpic(int idEpic) {
        getEpicList().get(idEpic).setStartTime(getSubTaskListOfEpic().get(idEpic).values().stream()
                .map(subTask -> subTask.getStartTime())
                .min(Comparator.naturalOrder())
                .orElse(null)
        );
        getEpicList().get(idEpic).setDuration(getSubTaskListOfEpic().get(idEpic).values().stream()
                .map(subTask -> subTask.getDuration())
                .reduce(Duration::plus)
                .orElse(null)
        );
        getEpicList().get(idEpic).setEndTime(getSubTaskListOfEpic().get(idEpic).values().stream()
                .map(subTask -> subTask.getEndTime())
                .max(Comparator.naturalOrder())
                .orElse(null)
        );
    }

    @Override
    public void intersectionDataTime(LocalDateTime startTime, Duration duration) {
        try {
            LocalDateTime endTime = startTime.plus(duration);
            boolean isIntersectionTask = getTaskList().values().stream()
                    .anyMatch(task -> task.getStartTime().isBefore(endTime) && task.getEndTime().isAfter(startTime));
            boolean isIntersectionSubTask = getSubTaskListOfEpic().values().stream()
                    .flatMap(hashMap -> hashMap.values().stream())
                    .anyMatch(subTask -> subTask.getStartTime().isBefore(endTime)
                            && subTask.getEndTime().isAfter(startTime));
            if (isIntersectionTask || isIntersectionSubTask) {
                throw new IntersectionException("Временные интервалы задач наложены друг на друга");
            }
        } catch (IntersectionException exception) {
            try { // Удаляем созданный файл (необходимо для корректной работы созданных ранее тестов)
                Files.delete(Paths.get("SaveTasks.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(exception);
        }
    }

}