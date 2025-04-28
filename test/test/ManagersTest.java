package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.*;

class ManagersTest {
    @Test
    void returnTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void returnHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}