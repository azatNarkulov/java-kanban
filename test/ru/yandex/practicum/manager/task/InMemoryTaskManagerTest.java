package ru.yandex.practicum.manager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ru.yandex.practicum.models.*;

import java.io.IOException;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void add_shouldNotBeAdded_withSameId() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", epicId);
        subtask.setId(epicId);
        taskManager.addSubtask(subtask);

        assertFalse(taskManager.getEpic(epicId).getSubtasksId().contains(subtask.getId()));
    }

    @Test
    void addSubtask_shouldNotBeAdded_withSameId() {
        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", 1);
        int epicId = subtask.getEpicId();
        taskManager.addSubtask(subtask);

        assertNotEquals(subtask.getId(), epicId);
    }

    @Test
    void addTask_returnSameTask() {
        Task task = new Task("Заголовок задачи", "Описание задачи");
        taskManager.addTask(task);

        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    void addEpic_returnSameEpic() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);

        assertEquals(epic, taskManager.getEpic(epic.getId()));
    }

    @Test
    void addSubtask_returnSameSubtask() {
        Epic epic = new Epic("Заголовок эпика для подзадачи", "Описание эпика для подзадачи");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", 1);
        taskManager.addSubtask(subtask);

        assertEquals(subtask, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void addTask_generatedIdTaskShouldNotConflict() {
        Task givenIdTask = new Task("Заголовок задачи с заданным ID", "Описание задачи");
        givenIdTask.setId(10);
        taskManager.addTask(givenIdTask);

        Task generatedIdTask = new Task("Заголовок задачи со сгенерированным ID", "Описание задачи");
        taskManager.addTask(generatedIdTask);

        assertNotEquals(givenIdTask.getId(), generatedIdTask.getId());
        assertEquals(givenIdTask, taskManager.getTask(10));
        assertEquals(generatedIdTask, taskManager.getTask(generatedIdTask.getId()));
    }

    @Test
    void getTask_checkTaskPersistence_afterAdding(){
        Task task = new Task("Заголовок задачи", "Описание задачи");
        taskManager.addTask(task);

        Task receivedTask = taskManager.getTask(task.getId());

        assertEquals(task.getId(), receivedTask.getId());
        assertEquals(task.getTitle(), receivedTask.getTitle());
        assertEquals(task.getDescription(), receivedTask.getDescription());
        assertEquals(task.getStatus(), receivedTask.getStatus());
    }
}