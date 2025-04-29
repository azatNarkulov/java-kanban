package ru.yandex.practicum.manager;

import ru.yandex.practicum.manager.history.*;
import ru.yandex.practicum.manager.task.*;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
