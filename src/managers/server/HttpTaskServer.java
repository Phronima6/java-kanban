package managers.server;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.server.handler.*;
import managers.task.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Managers managers = new Managers();
    public static final TaskManager taskManager = managers.getDefault();
    private static HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
    }

    // Запускаем сервер
    public void startServer() {
        httpServer.start();
    }

    // Останавливаем сервер
    public void stopServer() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

}