package managers.server.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.server.adapter.DurationAdapter;
import managers.server.adapter.LocalDateTimeAdapter;
import managers.task.TaskManager;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import tasks.status.Status;

public class BaseHttpHandler implements HttpHandler {

    TaskManager taskManager;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    }

    // Считываем тело запроса Клиента
    protected String readRequest(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    // Записывает и отправляем ответ Клиенту
    protected void writeResponse(HttpExchange httpExchange, String responseString,
                                 int responseCode) throws IOException {
        byte[] responseBytes = responseString.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(responseCode, responseBytes.length);
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(responseBytes);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Получаем имя задачи
    protected String getName(JsonObject jsonObject) {
        return jsonObject.get("name").getAsString();
    }

    // Получаем описание задачи
    protected String getDescription(JsonObject jsonObject) {
        return jsonObject.get("description").getAsString();
    }

    // Получаем статус задачи
    protected Status getStatus(JsonObject jsonObject) {
        Status statusOut = null;
        String statusIn = jsonObject.get("status").getAsString();
        if (statusIn.equals(Status.NEW.name())) {
            statusOut = Status.NEW;
        }
        if (statusIn.equals(Status.IN_PROGRESS.name())) {
            statusOut = Status.IN_PROGRESS;
        }
        if (statusIn.equals(Status.DONE.name())) {
            statusOut = Status.DONE;
        }
        return statusOut;
    }

    // Получаем время начала выполнения задачи
    protected String getStartTime(JsonObject jsonObject) {
        return jsonObject.get("startTime").getAsString();
    }

    // Получаем длительность выполнения задачи
    protected Long getDuration(JsonObject jsonObject) {
        return jsonObject.get("duration").getAsLong();
    }

}