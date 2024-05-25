package managers.server.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import managers.task.TaskManager;
import java.io.IOException;

public class SubTaskHandler extends BaseHttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        switch (httpExchange.getRequestMethod()) {
            case "GET": {
                if (pathParts.length == 3) { // Получаем все Подзадачи по id задачи типа Эпик
                    try {
                        writeResponse(httpExchange, gson.toJson(taskManager.getSubTaskListOfEpic()
                                        .get(Integer.parseInt(pathParts[2]))), 200);
                    } catch (IOException exception) {
                        writeResponse(httpExchange, "Ошибка, такой Подзадачи нет или передан"
                                + " некорректный id.", 404);
                    }
                }
                if (pathParts.length == 4) { // Получаем Подзадачу по id
                    try {
                        writeResponse(httpExchange,
                                gson.toJson(taskManager.getSubTaskListOfEpic().get(Integer.parseInt(pathParts[2]))
                                        .get(Integer.parseInt(pathParts[3]))), 200);
                    } catch (IOException exception) {
                        writeResponse(httpExchange, "Ошибка, такой Подзадачи нет или передан"
                                + " некорректный id.", 404);
                    }
                }
                break;
            }
            case "POST": {
                JsonObject jsonObject = JsonParser.parseString(readRequest(httpExchange)).getAsJsonObject();
                if (pathParts.length == 3) { // Создаём Подзадачу
                    try {
                        taskManager.creatingSubTask(Integer.parseInt(pathParts[2]), getName(jsonObject),
                                getDescription(jsonObject), getStartTime(jsonObject), getDuration(jsonObject));
                        writeResponse(httpExchange, "Подзадача создана.", 201);
                    } catch (Exception exception) {
                        writeResponse(httpExchange, "Ошибка, Подзадачи пересекаются по времени.",
                                406);
                    }
                }
                if (pathParts.length == 4) { // Обновляем Подзадачу по id
                    try {
                        taskManager.updateSubTask(Integer.parseInt(pathParts[2]), Integer.parseInt(pathParts[3]),
                                getName(jsonObject), getDescription(jsonObject), getStatus(jsonObject),
                                getStartTime(jsonObject), getDuration(jsonObject));
                        writeResponse(httpExchange, "Подзадача обновлена.", 201);
                    } catch (Exception exception) {
                        writeResponse(httpExchange, "Ошибка, Подзадачи пересекаются по времени.",
                                406);
                    }
                }
                break;
            }
            case "DELETE": {
                if (pathParts.length == 4) { // удаляем Подзадачу по id
                    try {
                        taskManager.delSubTask(Integer.parseInt(pathParts[2]), Integer.parseInt(pathParts[3]));
                        writeResponse(httpExchange, "Подзадача удалена.", 200);
                    } catch (Exception exception) {
                        writeResponse(httpExchange, "Ошибка при удалении Подзадачи", 404);
                    }
                }
                break;
            }
            default:
                writeResponse(httpExchange, "Такого эндпоинта не существует.", 404);
        }
    }

    /*------------------------------------------------------------------------------------------------------------------
    GET     http://localhost:8080/subtasks/{idEpic}              // Получаем все Подзадачи по id задачи типа Эпик
    GET     http://localhost:8080/subtasks/{idEpic}/{idSubTask}  // Получаем Подзадачу по id
    POST    http://localhost:8080/subtasks/{idEpic}              // Создаём Подзадачу
    POST    http://localhost:8080/subtasks/{idEpic}/{idSubTask}  // Обновляем Подзадачу по id
    DELETE  http://localhost:8080/subtasks/{idEpic}/{idSubTask}  // Удаляем Подзадачу по id
    ------------------------------------------------------------------------------------------------------------------*/

}