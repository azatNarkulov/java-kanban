package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.http.HttpTaskServer;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHandlerTest {
    private static final String URL = "http://localhost:8080/prioritized";

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
    public void hangleGet_getPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание 1", LocalDateTime.now(), Duration.ofMinutes(15));
        manager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", LocalDateTime.now().plusMinutes(100), Duration.ofMinutes(15));
        manager.addTask(task2);
        Task task3 = new Task("Задача 3", "Описание 3", LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(15));
        manager.addTask(task3);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);

        assertEquals(200, response.statusCode());
        assertEquals(3, prioritizedTasks.length);
        // Задачи должны вывестись в порядке task1, task3, task2
        assertEquals(task1.getId(), prioritizedTasks[0].getId());
        assertEquals(task3.getId(), prioritizedTasks[1].getId());
        assertEquals(task2.getId(), prioritizedTasks[2].getId());
    }
}
