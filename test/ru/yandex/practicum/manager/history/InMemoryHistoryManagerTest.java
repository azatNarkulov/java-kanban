package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.models.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add_removeOldFromHistory_addedTaskExistingInHistory() {
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
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        assertNotNull(historyManager);
    }

    @Test
    void remove_removeFromHistory() {
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        ArrayList<Task> tasks1 = historyManager.getHistory();

        historyManager.remove(task.getId());
        ArrayList<Task> tasks2 = historyManager.getHistory();

        assertNotEquals(tasks1, tasks2);
    }

    @Test
    void size_equals0_ifHistoryIsEmpty() {
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void remove_removeFromBeginningOfHistory() {
        Task taskInTheBeginning = new Task(1,"Заголовок задачи №1", "Описание задачи №1");
        historyManager.add(taskInTheBeginning);
        Task taskInTheMiddle = new Task(2,"Заголовок задачи №2", "Описание задачи №2");
        historyManager.add(taskInTheMiddle);
        Task taskInTheEnd = new Task(3,"Заголовок задачи №3", "Описание задачи №3");
        historyManager.add(taskInTheEnd);

        ArrayList<Task> tasksBeforeRemove = historyManager.getHistory();

        historyManager.remove(taskInTheBeginning.getId());
        ArrayList<Task> tasksAfterRemove = historyManager.getHistory();

        assertNotEquals(tasksBeforeRemove, tasksAfterRemove);
    }

    @Test
    void remove_removeFromMiddleOfHistory() {
        Task taskInTheBeginning = new Task(1,"Заголовок задачи №1", "Описание задачи №1");
        historyManager.add(taskInTheBeginning);
        Task taskInTheMiddle = new Task(2,"Заголовок задачи №2", "Описание задачи №2");
        historyManager.add(taskInTheMiddle);
        Task taskInTheEnd = new Task(3,"Заголовок задачи №3", "Описание задачи №3");
        historyManager.add(taskInTheEnd);

        ArrayList<Task> tasksBeforeRemove = historyManager.getHistory();

        historyManager.remove(taskInTheMiddle.getId());
        ArrayList<Task> tasksAfterRemove = historyManager.getHistory();

        assertNotEquals(tasksBeforeRemove, tasksAfterRemove);
    }

    @Test
    void remove_removeFromEndOfHistory() {
        Task taskInTheBeginning = new Task(1,"Заголовок задачи №1", "Описание задачи №1");
        historyManager.add(taskInTheBeginning);
        Task taskInTheMiddle = new Task(2,"Заголовок задачи №2", "Описание задачи №2");
        historyManager.add(taskInTheMiddle);
        Task taskInTheEnd = new Task(3,"Заголовок задачи №3", "Описание задачи №3");
        historyManager.add(taskInTheEnd);

        ArrayList<Task> tasksBeforeRemove = historyManager.getHistory();

        historyManager.remove(taskInTheEnd.getId());
        ArrayList<Task> tasksAfterRemove = historyManager.getHistory();

        assertNotEquals(tasksBeforeRemove, tasksAfterRemove);
    }
}