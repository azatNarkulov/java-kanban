package ru.yandex.practicum.manager.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

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
        Task task = new Task("Задача", "Описание задачи");
        manager.addTask(task);

        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        manager.addSubtask(subtask);

        FileBackedTaskManager loadManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(manager.getAllTasks().size(), loadManager.getAllTasks().size());
    }
}
