package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.http.HttpTaskServer;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.manager.task.TaskManager;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.models.Status.NEW;

import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandlerTest {
    private static final String URL = "http://localhost:8080/tasks";

    private TaskManager manager;
    private HttpTaskServer server;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();

        client = HttpClient.newHttpClient();
        gson = HttpTaskServer.getGson();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void handlePost_addTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", NEW, LocalDateTime.now(), Duration.ofMinutes(20));
        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    public void handlePost_notAddTask_taskOverlap() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание 1", NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        manager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", NEW, LocalDateTime.now().plusMinutes(15), Duration.ofMinutes(30));
        String json = gson.toJson(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    public void handlePost_updateTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", NEW);
        manager.addTask(task);
        Task updatedTask = new Task("Обновлённая задача 1", "Описание 1", NEW);
        updatedTask.setId(task.getId());
        String json = gson.toJson(updatedTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Обновлённая задача 1", manager.getTask(updatedTask.getId()).getTitle());
    }

    @Test
    public void handleGet_getAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание 1", NEW);
        manager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", NEW);
        manager.addTask(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, tasks.length);
    }

    @Test
    public void handleGet_getTaskById() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", NEW);
        manager.addTask(task);
        int id = task.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task returnedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(id, returnedTask.getId());
    }

    @Test
    public void handleGet_notGetTaskById_taskDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/100500"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void handleDelete_deleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", NEW);
        manager.addTask(task);
        int id = task.getId();

        // Вот тут у меня есть сомнение, что можно проверку добавлять в середину теста, но без этого
        // как будто нельзя полностью доверять финальной проверке: поскольку может ведь быть такое, что manager
        // изначально был пуст (допустим, manager.addTask(task) не сработал. А так мы убедились, что в нём была задача
        assertFalse(manager.getAllTasks().isEmpty());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void handleDelete_deleteAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание 1", NEW);
        manager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", NEW);
        manager.addTask(task2);

        assertEquals(2, manager.getAllTasks().size());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(manager.getAllTasks().isEmpty());
    }
}

