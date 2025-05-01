package ru.yandex.practicum.manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.task.*;
import ru.yandex.practicum.manager.history.*;
import ru.yandex.practicum.manager.Managers;

class ManagersTest {
    @Test
    void shouldReturnTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void shouldReturnHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}