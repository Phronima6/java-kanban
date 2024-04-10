package managers.history;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    // Добавляем просмотренную пользоветелем задачу в историю
    void addHistory(Task task);

    // Возвращаем историю просмотренных задач в виде списка
    ArrayList<Task> getHistory();

    // Удаляем все задачи из истории
    void remove();

    // Удаляем задачу из истории
    void remove(int idTask);

}