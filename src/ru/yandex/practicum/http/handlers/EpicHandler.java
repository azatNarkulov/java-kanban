package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.http.HttpTaskServer;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public EpicHandler(TaskManager taskManager) {
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
        System.out.println("Обрабатываю GET /epics");
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            ArrayList<Epic> epics = taskManager.getAllEpics();
            sendText(exchange, gson.toJson(epics), 200);
            return;
        }

        int id = Integer.parseInt(pathParts[2]);
        if (pathParts.length == 3) {
            try {
                Epic epic = taskManager.getEpic(id);
                sendText(exchange, gson.toJson(epic), 200);
            } catch (NotFoundException exp) {
                sendNotFound(exchange);
            }
        } else if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
            try {
                List<Subtask> subtasks = taskManager.getEpicSubtasks(id).stream()
                        .map(taskManager::getSubtask)
                        .collect(Collectors.toList());
                sendText(exchange, gson.toJson(subtasks), 200);
            } catch (NotFoundException exp) {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(json, Epic.class);

        if (epic.getId() == null) {
            taskManager.addEpic(epic);
        } else {
            taskManager.updateEpic(epic);
        }

        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            taskManager.deleteAllEpics();
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteEpic(id);
        }
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }
}
