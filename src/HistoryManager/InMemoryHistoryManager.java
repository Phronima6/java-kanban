package HistoryManager;

import Tasks.Task;
import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> historyTasksList = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        if (historyTasksList.size() == 10) {
            historyTasksList.remove(0);
        }
        historyTasksList.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyTasksList;
    }

}