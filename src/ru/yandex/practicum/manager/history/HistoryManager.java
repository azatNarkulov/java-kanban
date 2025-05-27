package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.models.*;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Task> getHistory(); // изменить возвращаемое значение

    void remove(Task task);
}
