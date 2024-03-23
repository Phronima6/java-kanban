package managers.history;

import tasks.Task;
import java.util.ArrayList;

public interface HistoryManager {

    // Добавляем просмотренную пользоветелем задачу в историю
    void addHistory(Task task);

    // Возвращаем историю просмотренных задач в виде списка
    ArrayList<Task> getHistory();

}