package ru.yandex.practicum.manager;

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
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public int getSize() {
        return history.size();
    }
}
