package test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.*;
import ru.yandex.practicum.models.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    @Test
    void historyManagerSavesTasks() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);

        historyManager.add(task);

        task.setTitle("Изменённый заголовок");
        Task changedTask = historyManager.getHistory().get(0);

        assertEquals("Изменённый заголовок", changedTask.getTitle());
    }
}