package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.history.*;
import ru.yandex.practicum.models.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryHistoryManagerTest {
    @Test
    void taskShouldNotChange() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        Task newTask = task.copy();
        newTask.setTitle("Изменённый заголовок");
        historyManager.add(newTask);

        String firstTaskTitle = historyManager.getHistory().get(0).getTitle();
        String secondTaskTitle = historyManager.getHistory().get(1).getTitle();

        assertNotEquals(firstTaskTitle, secondTaskTitle);
    }
}