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

import static org.junit.jupiter.api.Assertions.*;

public class EpicHandlerTest {
    private static final String URL = "http://localhost:8080/epics";

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
    public void handlePost_addEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String json = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getAllEpics().size());
    }

    @Test
    public void handleGet_getAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        manager.addEpic(epic2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Epic[] epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(2, epics.length);
    }

    @Test
    public void handleGet_getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        int id = epic.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Epic returnedEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals(1, returnedEpic.getId());
    }

    @Test
    public void handleGet_notGetEpicById_epicDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/100500"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void handleGet_getSubtasksOfEpicByEpicId() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        int epicId = epic.getId();
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epicId);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epicId);
        manager.addSubtask(subtask2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + epicId + "/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(manager.getAllSubtasks().size(), subtasks.length);
    }

    @Test
    public void handleGet_notGetSubtasksOfEpicByEpicId_epicDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/100500"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void handleDelete_deleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic);
        int id = epic.getId();

        assertFalse(manager.getAllEpics().isEmpty());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void handleDelete_deleteAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        manager.addEpic(epic2);

        assertEquals(2, manager.getAllEpics().size());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(manager.getAllEpics().isEmpty());
    }
}
