package managers.server.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import managers.task.TaskManager;
import java.io.IOException;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        switch (httpExchange.getRequestMethod()) {
            case "GET": {
                if (pathParts.length == 3) { // получаем задачу типа Эпик по id
                    try {
                        writeResponse(httpExchange, gson.toJson(taskManager.getEpicList()
                                .get(Integer.parseInt(pathParts[2]))), 200);
                    } catch (IOException exception) {
                        writeResponse(httpExchange, "Ошибка, такой задачи типа Эпик нет или передан"
                                + " некорректный id", 404);
                    }
                }
                break;
            }
            case "POST": {
                JsonObject jsonObject = JsonParser.parseString(readRequest(httpExchange)).getAsJsonObject();
                if (pathParts.length == 2) { // создаём задачу типа Эпик
                    taskManager.creatingEpic(getName(jsonObject), getDescription(jsonObject));
                    writeResponse(httpExchange, "Задача типа Эпик создана.", 201);
                }
                if (pathParts.length == 3) { // Обновляем задачу типа Эпик по id
                    taskManager.updateEpic(Integer.parseInt(pathParts[2]), getName(jsonObject),
                            getDescription(jsonObject));
                    writeResponse(httpExchange, "Задача типа Эпик обновлена.", 201);
                }
                break;
            }
            case "DELETE": {
                if (pathParts.length == 3) { // Удаляём задачу типа Эпик по id
                    try {
                        taskManager.delEpic(Integer.parseInt(pathParts[2]));
                        writeResponse(httpExchange, "Задача типа Эпик удалена.", 200);
                    } catch (Exception exception) {
                        writeResponse(httpExchange, "Ошибка при удалении задачи типа Эпик",
                                404);
                    }
                }
                break;
            }
            default:
                writeResponse(httpExchange, "Такого эндпоинта не существует.", 404);
        }
    }

    /*------------------------------------------------------------------------------------------------------------------
    GET     http://localhost:8080/epics/{idEpic}  // Получаем задачу типа Эпик по id
    POST    http://localhost:8080/epics           // Создаём Задачу типа Эпик
    POST    http://localhost:8080/epics/{idEpic}  // Обновляем задачу типа Эпик по id
    DELETE  http://localhost:8080/epics/{idEpic}  // Удаляем Задачу типа Эпик по id
    ------------------------------------------------------------------------------------------------------------------*/

}