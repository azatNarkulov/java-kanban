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

    public Integer addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Integer updateTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        subtasks.clear(); // если удалить все эпики, не будет ни одного сабтаска
        epics.clear();
    }

    public Task findEpic(Integer id) {
        return epics.get(id);
    }

    public Integer addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public Integer updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasksId()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
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

    public Integer addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getSubtasksId().add(subtask.getId());
            updateEpicStatus(epic);
            return subtask.getId();
        } else {
            return null;
        }
    }

    public Integer updateSubtask(Subtask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        updateEpicStatus(epics.get(newSubtask.getEpicId()));
        return newSubtask.getId();
    }

    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().remove(id);

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

    public void clearSubtasksFromEpic(Epic epic) {
        epic.getSubtasksId().clear();
        updateEpicStatus(epic);
    }
}
