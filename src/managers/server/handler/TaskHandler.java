package managers.server.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import managers.task.TaskManager;
import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        switch (httpExchange.getRequestMethod()) {
            case "GET": {
                if (pathParts.length == 3) { // Получаем Задачу по id
                    try {
                        writeResponse(httpExchange, gson.toJson(taskManager.getTaskList()
                                        .get(Integer.parseInt(pathParts[2]))), 200);
                    } catch (IOException exception) {
                        writeResponse(httpExchange, "Ошибка, такой Задачи нет или передан некорректный id.",
                                404);
                    }
                }
                break;
            }
            case "POST": {
                JsonObject jsonObject = JsonParser.parseString(readRequest(httpExchange)).getAsJsonObject();
                if (pathParts.length == 2) { // Создаём Задачу
                    try {
                        taskManager.creatingTask(getName(jsonObject), getDescription(jsonObject),
                                getStartTime(jsonObject), getDuration(jsonObject));
                        writeResponse(httpExchange, "Задача создана.", 201);
                    } catch (Exception exception) {
                        writeResponse(httpExchange, "Ошибка, Задачи пересекаются по времени.",
                                406);
                    }
                }
                if (pathParts.length == 3) { // Обновляем Задачу по id
                    try {
                        taskManager.updateTask(Integer.parseInt(pathParts[2]), getName(jsonObject),
                                getDescription(jsonObject), getStatus(jsonObject),
                                getStartTime(jsonObject), getDuration(jsonObject));
                        writeResponse(httpExchange, "Задача обновлена.", 201);
                    } catch (Exception exception) {
                        writeResponse(httpExchange, "Ошибка, Задачи пересекаются по времени.",
                                406);
                    }
                }
                break;
            }
            case "DELETE": {
                if (pathParts.length == 3) { // Удаляем Задачу по id
                    try {
                        taskManager.delTask(Integer.parseInt(pathParts[2]));
                        writeResponse(httpExchange, "Задача удалена.", 200);
                    } catch (Exception exception) {
                        writeResponse(httpExchange, "Ошибка при удалении Задачи.", 404);
                    }
                }
                break;
            }
            default:
                writeResponse(httpExchange, "Такого эндпоинта не существует.", 404);
        }
    }

}