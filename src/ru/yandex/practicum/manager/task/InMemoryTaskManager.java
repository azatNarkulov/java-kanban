package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.manager.history.*;
import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Integer id = 1;
    private HistoryManager historyManager;
    private TreeSet<Task> prioritizedTasks;
    private Comparator<Task> startTimeComparator = Comparator.comparing(Task::getStartTime);

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(startTimeComparator);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            if (task.getStartTime() != null) {
                prioritizedTasks.remove(task);
            }
        }
        tasks.clear();
    }

    @Override
    public Task addTask(Task task) {
        checkOverlap(task);
        if (task.getId() == null) {
            task.setId(generateId());
        }
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        if (task.getStatus() == null) {
            task.setStatus(Status.NEW);
        }
        return task;
    }

    @Override
    public Task updateTask(Task newTask) {
        checkOverlap(newTask);
        tasks.put(newTask.getId(), newTask);
        if (newTask.getStartTime() != null) {
            prioritizedTasks.add(newTask);
        }
        return newTask;
    }

    @Override
    public void deleteTask(Integer id) {
        historyManager.remove(id);
        tasks.remove(id);
        Task task = tasks.get(id);
        if (task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
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
        if (epic.getStatus() == null) {
            epic.setStatus(Status.NEW);
        }
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
            for (Integer subtaskId : epic.getSubtasksId()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask.getStartTime() != null) {
                    prioritizedTasks.remove(subtask);
                }
            }
        }
        subtasks.clear();
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        checkOverlap(subtask);
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
        if (subtask.getStatus() == null) {
            subtask.setStatus(Status.NEW);
        }
        epic.getSubtasksId().add(subtaskId);
        updateEpicFields(epic);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        checkOverlap(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        updateEpicFields(epics.get(newSubtask.getEpicId()));
        if (newSubtask.getStartTime() != null) {
            prioritizedTasks.add(newSubtask);
        }
        return newSubtask;
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().remove((Object) id);

        subtasks.remove(id);
        updateEpicFields(epic);
        historyManager.remove(id);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.remove(subtask);
        }
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
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
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

    private void updateEpicTime(Epic epic) {
        ArrayList<Integer> epicSubtasks = epic.getSubtasksId();
        Subtask firstSubtask = subtasks.get(epicSubtasks.getFirst());
        Duration epicDuration = firstSubtask.getDuration();
        LocalDateTime minEpicStartTime = firstSubtask.getStartTime();
        LocalDateTime maxEpicEndTime = firstSubtask.getEndTime();
        for (int i = 1; i < epicSubtasks.size(); i++) {
            Subtask subtask = subtasks.get(epicSubtasks.get(i));
            if (subtask.getStartTime() != null && subtask.getStartTime().isBefore(minEpicStartTime)) {
                minEpicStartTime = subtask.getStartTime();
            }
            if (subtask.getEndTime() != null && subtask.getEndTime().isAfter(maxEpicEndTime)) {
                maxEpicEndTime = subtask.getEndTime();
            }
            if (epicDuration != null) {
                epicDuration = epicDuration.plus(subtask.getDuration());
            } else {
                epic.setDuration(subtask.getDuration());
            }
        }
        epic.setStartTime(minEpicStartTime);
        epic.setEndTime(maxEpicEndTime); // здесь проставляем endTime
        epic.setDuration(epicDuration);
    }

    private void updateEpicFields(Epic epic) {
        updateEpicStatus(epic);
        updateEpicTime(epic);
    }

    private Integer generateId() {
        return id++;
    }

    private void clearSubtasksFromEpic(Epic epic) {
        epic.getSubtasksId().clear();
        updateEpicFields(epic);
    }

    private void deleteSubtasksFromHistory(Epic epic) {
        for (Integer subtaskId : epic.getSubtasksId()) {
            historyManager.remove(subtaskId);
        }
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private boolean isTaskOverlap(Task task1, Task task2) {
        if (task1.getId().equals(task2.getId())) {
            return false;
        }
        return !(task1.getEndTime().isBefore(task2.getStartTime()) ||
                task2.getEndTime().isBefore(task1.getStartTime()));
    }

    private boolean isTaskOverlap(Task newTask) {
        for (Task task : prioritizedTasks) {
            if (isTaskOverlap(newTask, task)) {
                return true;
            }
        }
        return false;
    }

    private void checkOverlap(Task task) {
        if (isTaskOverlap(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей");
        }
    }
}
