package managers.server.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.task.TaskManager;
import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    // Получаем все задачи, отсортированные по приоритету
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        writeResponse(httpExchange, gson.toJson(taskManager.getSortedTasks()), 200);
    }

}