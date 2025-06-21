package ru.yandex.practicum.manager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.models.Status.*;
import static java.time.Month.JUNE;

import ru.yandex.practicum.models.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

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

    @Test
    void getStatus_returnEpicStatusNew_ifAllSubtasksAreNew() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", NEW, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", NEW, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(NEW, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void getStatus_returnEpicStatusDone_ifAllSubtasksAreDone() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", DONE, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", DONE, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(DONE, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void getStatus_returnEpicStatusInProgress_ifSubtasksAreNewAndDone() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", NEW, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", DONE, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(IN_PROGRESS, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void getStatus_returnEpicStatusInProgress_ifAllSubtasksAreInProgress() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", IN_PROGRESS, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", IN_PROGRESS, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(IN_PROGRESS, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void addSubtask_returnNull_ifEpicDoesNotExist() {
        Subtask subtask = new Subtask(1, "Заголовок подзадачи", "Описание подзадачи");
        Subtask result = taskManager.addSubtask(subtask);

        assertNull(result);
    }

    @Test
    void tasksOverlap_returnTrue_ifTasksOverlap() {
        Task task1 = new Task(1, "Заголовок задачи №1", "Описание задачи №1",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15));
        Task task2 = new Task(2, "Заголовок задачи №2", "Описание задачи №2",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 50), Duration.ofMinutes(15));

        assertTrue(taskManager.tasksOverlap(task1, task2));
    }

    @Test
    void tasksOverlap_returnFalse_ifTasksDoNotOverlap() {
        Task task1 = new Task(1, "Заголовок задачи №1", "Описание задачи №1",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15));
        Task task2 = new Task(2, "Заголовок задачи №2", "Описание задачи №2",
                NEW, LocalDateTime.of(2025, JUNE, 21, 21, 15), Duration.ofMinutes(15));

        assertFalse(taskManager.tasksOverlap(task1, task2));
    }

    @Test
    void addTask_throwException_ifTasksOverlap() {
        Task task1 = new Task(1, "Заголовок задачи №1", "Описание задачи №1",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15));
        taskManager.addTask(task1);
        Task task2 = new Task(2, "Заголовок задачи №2", "Описание задачи №2",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 50), Duration.ofMinutes(15));

        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addTask(task2);
        }, "Задача пересекается по времени с другой задачей");
    }
}