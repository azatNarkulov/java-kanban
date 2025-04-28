package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.*;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    Task addTask(Task task);

    Task updateTask(Task newTask);

    void deleteTask(Integer id);

    ArrayList<Epic> getAllEpics();

    void deleteAllEpics();

    Epic addEpic(Epic epic);

    Epic updateEpic(Epic newEpic);

    void deleteEpic(Integer id);

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask addSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask newSubtask);

    void deleteSubtask(Integer id);

    Task getTask(Integer id);

    Subtask getSubtask(Integer id);

    Epic getEpic(Integer id);

    List<Task> getHistory();
}
