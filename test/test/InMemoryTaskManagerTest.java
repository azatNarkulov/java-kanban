package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.*;
import ru.yandex.practicum.models.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void impossibleAddEpicInEpic() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask = new Subtask("Заголовк подзадачи", "Описание подзадачи", epicId);
        subtask.setId(epicId);
        taskManager.addSubtask(subtask);

        assertFalse(taskManager.getEpic(epicId).getSubtasksId().contains(subtask.getId()));
    }

    @Test
    void impossibleAddSubtaskInSubtask() {
        Subtask subtask = new Subtask("Заголовк подзадачи", "Описание подзадачи", 1);
        int epicId = subtask.getEpicId();
        taskManager.addSubtask(subtask);

        assertNotEquals(subtask.getId(), epicId);
    }

    @Test
    void canAddAndGetDifferentTasks() {
        Task task = new Task("Заголовок задачи", "Описание задачи");
        taskManager.addTask(task);
        Epic epic = new Epic("Заголовок эпика", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", epic.getId());
        taskManager.addSubtask(subtask);


        assertEquals(task, taskManager.getTask(task.getId()));
        assertEquals(epic, taskManager.getEpic(epic.getId()));
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void givenIdTaskAndGeneratedIdTaskShouldNotConflict() {
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
    void checkTaskPersistanceAfterAdding(){
        Task task = new Task("Заголовок задачи", "Описание задачи");
        taskManager.addTask(task);

        Task receivedTask = taskManager.getTask(task.getId());

        assertEquals(task.getId(), receivedTask.getId());
        assertEquals(task.getTitle(), receivedTask.getTitle());
        assertEquals(task.getDescription(), receivedTask.getDescription());
        assertEquals(task.getStatus(), receivedTask.getStatus());
    }
}