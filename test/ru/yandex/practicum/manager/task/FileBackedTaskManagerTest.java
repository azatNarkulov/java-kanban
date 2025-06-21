package ru.yandex.practicum.manager.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.Month.JUNE;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {
    FileBackedTaskManager manager;
    File file;

    @Override
    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = new InMemoryTaskManager();
        file = File.createTempFile("test", ".csv");
        manager = FileBackedTaskManager.loadFromFile(file);
    }

    @AfterEach
    void afterEach() {
        file.delete();
    }

    @Test
    void getAllTasks_shouldBeEmpty_ifFileIsEmpty() {
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void loadFromFile_shouldLoadSavedTasks() {
        Task task = new Task(1, "Задача", "Описание задачи", LocalDateTime.of(2025, JUNE, 21, 21,40), Duration.ofMinutes(15));
        manager.addTask(task);

        Epic epic = new Epic(2, "Эпик", "Описание эпика", LocalDateTime.of(2025, JUNE, 21, 22,40), Duration.ofMinutes(15));
        manager.addEpic(epic);

        Subtask subtask = new Subtask(3, "Подзадача", "Описание подзадачи", LocalDateTime.of(2025, JUNE, 21, 23,40), Duration.ofMinutes(15), epic.getId());
        manager.addSubtask(subtask);

        FileBackedTaskManager loadManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(manager.getAllTasks().size(), loadManager.getAllTasks().size());
    }
}
