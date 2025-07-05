package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.http.HttpTaskServer;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskHandlerTest {
    private static final String URL = "http://localhost:8080/subtasks";

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
    public void handlePost_addSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getId());
        String json = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getAllSubtasks().size());
    }

    @Test
    public void handlePost_notAddSubtask_subtaskOverlap() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getId(), LocalDateTime.now(), Duration.ofMinutes(30));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getId(), LocalDateTime.now().plusMinutes(15), Duration.ofMinutes(30));
        String json = gson.toJson(subtask2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals(1, manager.getAllSubtasks().size());
    }

    @Test
    public void handlePost_updateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getId());
        manager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("Обновлённая подзадача 1", "Описание 1", epic.getId());
        updatedSubtask.setId(subtask.getId());
        String json = gson.toJson(updatedSubtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Обновлённая подзадача 1", manager.getSubtask(updatedSubtask.getId()).getTitle());
    }

    @Test
    public void handleGet_getAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getId());
        manager.addSubtask(subtask2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(2, subtasks.length);
    }

    @Test
    public void handleGet_getSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getId());
        manager.addSubtask(subtask);
        int id = subtask.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Subtask returnedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(id, returnedSubtask.getId());
    }

    @Test
    public void handleGet_notGetSubtaskById_subtaskDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/100500"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void handleDelete_deleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getId());
        manager.addSubtask(subtask);
        int id = subtask.getId();
        
        assertFalse(manager.getAllSubtasks().isEmpty());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void handleDelete_deleteAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getId());
        manager.addSubtask(subtask2);

        assertEquals(2, manager.getAllSubtasks().size());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }
}
