package managers.server.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.task.TaskManager;
import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    // Получаем историю вызова 10 последних задач
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        writeResponse(httpExchange, gson.toJson(taskManager.getHistory()), 200);
    }

    /*------------------------------------------------------------------------------------------------------------------
    GET     http://localhost:8080/history  // Получаем историю вызова 10 последних задач
    ------------------------------------------------------------------------------------------------------------------*/

}