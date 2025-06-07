package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.models.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    void addTask(Task task);

    void updateTask(Task newTask);

    void deleteTask(Integer id);

    ArrayList<Epic> getAllEpics();

    void deleteAllEpics();

    void addEpic(Epic epic);

    void updateEpic(Epic newEpic);

    void deleteEpic(Integer id);

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask newSubtask);

    void deleteSubtask(Integer id);

    Task getTask(Integer id);

    Subtask getSubtask(Integer id);

    Epic getEpic(Integer id);

    List<Task> getHistory();
}
