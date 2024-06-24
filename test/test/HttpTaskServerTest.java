package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.server.HttpTaskServer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import managers.server.adapter.DurationAdapter;
import managers.server.adapter.LocalDateTimeAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServerTest {

    private static HttpTaskServer httpTaskServer;

    // Запускаем сервер перед началом выполнения тестов
    @BeforeAll
    public static void startServer() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

    // Останавливаем сервер после завершения тестов
    @AfterAll
    public static void stopServer() {
        httpTaskServer.stopServer();
    }

    // Проверяем: создание, обновление, вывод, удаление обычной Задачи
    @Test
    public void taskHandlerTest() {
        int idTask = httpTaskServer.taskManager.creatingTask("Задача 1", "Задача 1...",
                "2021-12-21T22:00:00", 10);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + idTask);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
        Assertions.assertEquals("{\"name\":\"Задача 1\",\"description\":\"Задача 1...\",\"status\":\"NEW\","
                        + "\"startTime\":\"2021-12-21T22:00\",\"duration\":10,\"endTime\":\"2021-12-21T22:10\"}",
                response.body(), "Ошибка, вывод Задач не совпадает.");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Задача 2\",\"description\":\"Задача 2...\","
                        + "\"status\":\"NEW\",\"startTime\":\"2021-12-22T22:00\",\"duration\":10,\"endTime\""
                        + ":\"2021-12-21T22:10\"}"))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .header("Content-Type", "application/json")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(201, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .header("Content-Type", "application/json")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
    }

    // Проверяем: создание, обновление, вывод, удаление задачи типа Эпик
    @Test
    public void epicHandlerTest() {
        int idEpic = httpTaskServer.taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + idEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
        Assertions.assertEquals("{\"name\":\"Эпик 1\",\"description\":\"Эпик 1...\",\"status\":\"NEW\"}",
                response.body(), "Ошибка, вывод Задач не совпадает.");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Задача 2\",\"description\""
                        + ":\"Задача 2...\"}"))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .header("Content-Type", "application/json")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(201, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .header("Content-Type", "application/json")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
    }

    // Проверяем: создание, обновление, вывод, удаление Подзадачи
    @Test
    public void subTaskHandlerTest() {
        int idEpic = httpTaskServer.taskManager.creatingEpic("Эпик 1", "Эпик 1...");
        int idSubTask = httpTaskServer.taskManager.creatingSubTask(idEpic, "Подзадача 1",
                "Подзадача 1...", "2021-12-21T22:00:00", 10);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + idEpic + "/" + idSubTask);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
        Assertions.assertEquals("{\"name\":\"Подзадача 1\",\"description\":\"Подзадача 1...\",\"status\":\""
                        + "NEW\",\"startTime\":\"2021-12-21T22:00\",\"duration\":10,\"endTime\":\"2021-12-21T22:10\"}",
                response.body(), "Ошибка, вывод Задач не совпадает.");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Подзадача 2\",\"description\":"
                        + "\"Подзадача 2...\",\"status\":\"NEW\",\"startTime\":\"2021-12-22T22:00\",\"duration\":10,"
                        + "\"endTime\":\"2021-12-21T22:10\"}"))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .header("Content-Type", "application/json")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(201, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .header("Content-Type", "application/json")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
    }

    // Проверяем вывод истории просмотренных задач
    @Test
    public void historyHandlerTest() {
        httpTaskServer.taskManager.creatingTask("Задача 1", "Задача 1...",
                "2021-12-21T22:20:00", 10);
        httpTaskServer.taskManager.creatingTask("Задача 2", "Задача 2...",
                "2021-12-21T22:40:00", 10);
        httpTaskServer.taskManager.creatingTask("Задача 3", "Задача 3...",
                "2021-12-21T23:00:00", 10);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String json = gson.toJson(httpTaskServer.taskManager.getHistory());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
        Assertions.assertEquals(json, response.body(), "Ошибка, вывод Задач не совпадает.");
    }

    // Проверяем вывод задач, отсортированных по приоритету
    @Test
    public void prioritizedHandlerTest() {
        httpTaskServer.taskManager.creatingTask("Задача 1", "Задача 1...",
                "2022-12-21T22:20:00", 10);
        httpTaskServer.taskManager.creatingTask("Задача 2", "Задача 2...",
                "2022-12-21T22:40:00", 10);
        httpTaskServer.taskManager.creatingTask("Задача 3", "Задача 3...",
                "2022-12-21T23:00:00", 10);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String json = gson.toJson(httpTaskServer.taskManager.getSortedTasks());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "json/application")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        Assertions.assertEquals(200, response.statusCode(), "Ошибка, код выполнения "
                + "не совпадает с ожидаемым");
        Assertions.assertEquals(json, response.body(), "Ошибка, вывод Задач не совпадает.");
    }

}