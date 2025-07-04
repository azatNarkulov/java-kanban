package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.exceptions.HasIntersectionsException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.http.HttpTaskServer;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.models.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

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
        System.out.println("Обрабатываю GET /subtasks");
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            ArrayList<Subtask> subtasks = taskManager.getAllSubtasks();
            sendText(exchange, gson.toJson(subtasks), 200);
        } else if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                Subtask subtask = taskManager.getSubtask(id);
                sendText(exchange, gson.toJson(subtask), 200);
            } catch (NotFoundException exp) {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(json, Subtask.class);

        try {
            if (subtask.getId() == null) {
                taskManager.addSubtask(subtask);
            } else {
                taskManager.updateSubtask(subtask);
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
            taskManager.deleteAllSubtasks();
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteSubtask(id);
        }
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }
}

