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
    void addSubtask_doNotAdd_subtaskHasSameIdAsEpic() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", epicId);
        subtask.setId(epicId);
        taskManager.addSubtask(subtask);

        assertFalse(taskManager.getEpic(epicId).getSubtasks().contains(subtask));
    }

    @Test
    void addSubtask_doNotAddSubtask_subtaskHasSameIdAsAnotherSubtask() {
        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", 1);
        int epicId = subtask.getEpicId();
        taskManager.addSubtask(subtask);

        assertNotEquals(subtask.getId(), epicId);
    }

    @Test
    void addTask_addTaskToTaskManager() {
        Task task = new Task("Заголовок задачи", "Описание задачи");
        taskManager.addTask(task);

        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    void addEpic_addEpicToTaskManager() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);

        assertEquals(epic, taskManager.getEpic(epic.getId()));
    }

    @Test
    void addSubtask_addSubtaskToTaskManager() {
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
    void addTask_saveTaskFields(){
        Task task = new Task("Заголовок задачи", "Описание задачи");
        taskManager.addTask(task);

        Task receivedTask = taskManager.getTask(task.getId());

        assertEquals(task.getId(), receivedTask.getId());
        assertEquals(task.getTitle(), receivedTask.getTitle());
        assertEquals(task.getDescription(), receivedTask.getDescription());
        assertEquals(task.getStatus(), receivedTask.getStatus());
    }

    @Test
    void addSubtask_updateEpicStatusToNew_allSubtasksAreNew() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", NEW, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", NEW, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(NEW, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void addSubtask_updateEpicStatusToDone_allSubtasksAreDone() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", DONE, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", DONE, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(DONE, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void addSubtask_updateEpicStatusToInProgress_subtasksAreNewAndDone() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", NEW, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", DONE, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(IN_PROGRESS, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void addSubtask_updateEpicStatusToInProgress_allSubtasksAreInProgress() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №2", "Описание подзадачи №2", IN_PROGRESS, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №3", "Описание подзадачи №3", IN_PROGRESS, 1);
        taskManager.addSubtask(subtask2);

        assertEquals(IN_PROGRESS, taskManager.epics.get(epic.getId()).getStatus());
    }

    @Test
    void addSubtask_doNotAdd_epicDoesNotExist() {
        Subtask subtask = new Subtask(1, "Заголовок подзадачи", "Описание подзадачи");

        assertNull(taskManager.addSubtask(subtask));
    }

    @Test
    void addTask_addTaskToTaskManager_tasksDoNotOverlap() {
        Task task1 = new Task(1, "Заголовок задачи №1", "Описание задачи №1",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15));
        taskManager.addTask(task1);
        Task task2 = new Task(2, "Заголовок задачи №2", "Описание задачи №2",
                NEW, LocalDateTime.of(2025, JUNE, 21, 21, 15), Duration.ofMinutes(15));
        taskManager.addTask(task2);
        assertEquals(task2, taskManager.tasks.get(task2.getId()));
    }

    @Test
    void addTask_throwException_tasksOverlap() {
        Task task1 = new Task(1, "Заголовок задачи №1", "Описание задачи №1",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15));
        taskManager.addTask(task1);
        Task task2 = new Task(2, "Заголовок задачи №2", "Описание задачи №2",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 50), Duration.ofMinutes(15));

        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(task2), "Задача пересекается по времени с другой задачей");
    }

    @Test
    void updateTask_updateTask_updatedTaskIsOverlapped() {
        Task task1 = new Task(1, "Заголовок задачи №1", "Описание задачи №1",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15));
        taskManager.addTask(task1);
        Task task2 = new Task(1, "Обновлённый заголовок задачи №1", "Обновлённое описание задачи №1",
                NEW, LocalDateTime.of(2025, JUNE, 21, 20, 50), Duration.ofMinutes(15));
        taskManager.addTask(task2);

        assertEquals(task2, taskManager.tasks.get(task2.getId())); // тут должно быть успешное обновление
    }

    @Test
    void addSubtask_addSubtaskToTaskManager_subtasksDoNotOverlap() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №1", "Описание подзадачи №1",
                LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15), epic.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №2", "Описание подзадачи №2",
                LocalDateTime.of(2025, JUNE, 21, 21, 15), Duration.ofMinutes(15), epic.getId());
        taskManager.addSubtask(subtask2);
        assertEquals(subtask2, taskManager.subtasks.get(subtask2.getId()));
    }

    @Test
    void addSubtask_throwException_subtasksOverlap() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №1", "Описание подзадачи №1",
                LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15), epic.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(3, "Заголовок подзадачи №2", "Описание подзадачи №2",
                LocalDateTime.of(2025, JUNE, 21, 20, 50), Duration.ofMinutes(15), epic.getId());

        assertThrows(IllegalArgumentException.class, () -> taskManager.addSubtask(subtask2), "Задача пересекается по времени с другой задачей");
    }

    @Test
    void updateSubtask_updateSubtask_updatedSubtaskIsOverlapped() {
        Epic epic = new Epic(1, "Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(2, "Заголовок подзадачи №1", "Описание подзадачи №1",
                LocalDateTime.of(2025, JUNE, 21, 20, 45), Duration.ofMinutes(15), epic.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(2, "Обновлённый заголовок подзадачи №1", "Обновлённое описание подзадачи №1",
                LocalDateTime.of(2025, JUNE, 21, 20, 50), Duration.ofMinutes(15), epic.getId());
        taskManager.updateSubtask(subtask2);

        assertEquals(subtask2, taskManager.subtasks.get(subtask2.getId()));
    }
}