package ru.yandex.practicum.manager.history;

import java.util.List;
import java.util.ArrayList;
import ru.yandex.practicum.models.*;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task.copy());
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
