package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.manager.history.*;
import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<Integer, Subtask>();
    private Integer id = 1;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public Task addTask(Task task) {
        if (task.getId() == null) {
            task.setId(generateId());
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public void deleteTask(Integer id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            deleteSubtasksFromHistory(epic);
            historyManager.remove(epic.getId());
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            deleteSubtasksFromHistory(epic);
            clearSubtasksFromEpic(epic);
        }
        subtasks.clear();
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Integer epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epicId == null) {
            return null;
        }

        Integer subtaskId = subtask.getId();
        if (subtaskId == null) {
            subtaskId = generateId();
        }
        if (subtaskId.equals(epicId)) {
            return null;
        }
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);

        epic.getSubtasksId().add(subtaskId);
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        updateEpicStatus(epics.get(newSubtask.getEpicId()));
        return newSubtask;
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().remove((Object) id);

        subtasks.remove(id);
        updateEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public Task getTask(Integer id) {
        Task task = tasks.get(id);
        if (task == null) {
            return task;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return subtask;
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return epic;
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public ArrayList<Subtask> getAllSubtasksFromEpic(Integer id) {
        ArrayList<Subtask> allSubtasks = new ArrayList<Subtask>();
        for (Integer subtaskId : epics.get(id).getSubtasksId()) {
            allSubtasks.add(subtasks.get(subtaskId));
        }
        return allSubtasks;
    }

    private Status getEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasksFromEpic = getAllSubtasksFromEpic(epic.getId());
        if (subtasksFromEpic.isEmpty()) {
            return Status.NEW;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasksFromEpic) {
            Status status = subtask.getStatus();
            if (status != Status.NEW) {
                allNew = false;
            }
            if (status != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            return Status.NEW;
        } else if (allDone) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    private void updateEpicStatus(Epic epic) {
        Status status = getEpicStatus(epic);
        epic.setStatus(status);
    }

    public Integer generateId() {
        return id++;
    }

    private void clearSubtasksFromEpic(Epic epic) {
        epic.getSubtasksId().clear();
        updateEpicStatus(epic);
    }

    private void deleteSubtasksFromHistory(Epic epic) {
        for (int subtaskId : epic.getSubtasksId()) {
            historyManager.remove(subtaskId);
        }
    }
}
