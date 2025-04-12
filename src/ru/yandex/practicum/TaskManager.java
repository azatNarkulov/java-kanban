package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<Integer, Subtask>();
    private int id = 0;

    public TaskManager() {
    }

    public ArrayList<Task> printAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<Task>();
        allTasks.addAll(tasks.values());
        return allTasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task findTask(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> printAllEpiks() {
        ArrayList<Epic> allEpics = new ArrayList<Epic>();
        allEpics.addAll(epics.values());
        return allEpics;
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Task findEpic(int id) {
        return epics.get(id);
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    public ArrayList<Subtask> printAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<Subtask>();
        allSubtasks.addAll(subtasks.values());
        return allSubtasks;
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public Subtask findSubtask(int id) {
        return subtasks.get(id);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().add(subtask.getId());

        updateEpicStatus(epic);
    }

    public void updateSubtask(Subtask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        updateEpicStatus(epics.get(newSubtask.getEpicId()));
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().remove((Integer) id);

        subtasks.remove(id);
        updateEpicStatus(epic);
    }

    public ArrayList<Subtask> printAllEpicSubtasks(int id) {
        ArrayList<Subtask> allSubtasks = new ArrayList<Subtask>();
        ArrayList<Integer> EpicsSubtasks = epics.get(id).getSubtasksId();
        for (Integer intId : EpicsSubtasks) {
            allSubtasks.add(subtasks.get(intId));
        }
        return allSubtasks;
    }

    public void updateEpicStatus(Epic epic) {
        // вот тут мудрёно получилось, и я не до конца уверен, что это лучший способ реализации метода
        ArrayList<Integer> subtasksId = epic.getSubtasksId();

        if (subtasksId.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public int generateId() {
        id++;
        return id;
    }
}





