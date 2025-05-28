package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.models.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {

    @Test
    void add_removeOldFromHistory_addedTaskExistingInHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        Task newTask = task.copy();
        newTask.setTitle("Изменённый заголовок");
        historyManager.add(newTask);
        Task testTask = historyManager.getHistory().get(0);

        assertEquals(newTask, testTask);
    }

    @Test
    void add_addToMap() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        assertNotNull(historyManager);
    }

    @Test
    void remove_removeFromHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        ArrayList<Task> tasks1 = historyManager.getHistory();

        historyManager.remove(task.getId());
        ArrayList<Task> tasks2 = historyManager.getHistory();

        assertNotEquals(tasks1, tasks2);
    }
}