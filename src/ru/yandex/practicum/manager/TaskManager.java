package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<Integer, Subtask>();
    private Integer id = 1;

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task findTask(Integer id) {
        return tasks.get(id);
    }

    public Task addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public Task findEpic(Integer id) {
        return epics.get(id);
    }

    public Epic addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public void deleteEpic(Integer id) {
        if (!isEpicExist(id)) {
            return;
        }
        Epic epic = epics.get(id);
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            clearSubtasksFromEpic(epic);
        }
    }

    public Subtask findSubtask(Integer id) {
        return subtasks.get(id);
    }

    public Subtask addSubtask(Subtask subtask) {
        Integer epicId = subtask.getEpicId();
        if (!isEpicExist(epicId)) {
            return null;
        }

        Integer subtaskId = generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);

        Epic epic = epics.get(epicId);
        epic.getSubtasksId().add(subtaskId);
        updateEpicStatus(epic);
        return subtask;
    }

    public Subtask updateSubtask(Subtask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        updateEpicStatus(epics.get(newSubtask.getEpicId()));
        return newSubtask;
    }

    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().remove((Object) id);

        subtasks.remove(id);
        updateEpicStatus(epic);
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

    private Integer generateId() {
        return id++;
    }

    private void clearSubtasksFromEpic(Epic epic) {
        epic.getSubtasksId().clear();
        updateEpicStatus(epic);
    }

    private boolean isEpicExist(Integer epicId) {
        return epics.containsKey(epicId);
    }
}
