package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.exceptions.HasIntersectionsException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.http.HttpTaskServer;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        System.out.println("Получен запрос " + method + " " + path);

        switch (method) {
            case "GET":
                handleGet(exchange, path);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange, path);
                break;
            default:
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        System.out.println("Обрабатываю GET /tasks");
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            ArrayList<Task> tasks = taskManager.getAllTasks();
            sendText(exchange, gson.toJson(tasks), 200);
        } else if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                Task task = taskManager.getTask(id);
                sendText(exchange, gson.toJson(task), 200);
            } catch (NotFoundException exp) {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(json, Task.class);

        try {
            if (task.getId() == null) {
                taskManager.addTask(task);
            } else {
                taskManager.updateTask(task);
            }
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        } catch (HasIntersectionsException exp) {
            sendHasIntersection(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            taskManager.deleteAllTasks();
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteTask(id);
        }
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }
}
