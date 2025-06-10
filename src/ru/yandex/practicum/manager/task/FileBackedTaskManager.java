package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.models.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(CsvConverter.taskToLine(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(CsvConverter.taskToLine(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(CsvConverter.taskToLine(subtask) + "\n");
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Возникла ошибка при сохранении");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                Task task = CsvConverter.fromString(lines.get(i));
                if (task.getId() > manager.id) {
                    manager.id = task.getId() + 1;
                }
                switch (task.getTaskType()) {
                    case TASK:
                        manager.tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        manager.epics.put(task.getId(), (Epic) task);
                        break;
                    case SUBTASK:
                        Subtask subtask = (Subtask) task;
                        manager.subtasks.put(subtask.getId(), subtask);
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестный тип задачи: " + task.getTaskType());
                }
            }
            for (Subtask subtask : manager.subtasks.values()) {
                // добавляем все подзадачи в эпики
                manager.epics.get(subtask.getEpicId()).getSubtasksId().add(subtask.getId());
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Возникла ошибка при загрузке");
        }
        return manager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task addTask(Task newTask) {
        Task task = super.addTask(newTask);
        save();
        return task;
    }

    @Override
    public Task updateTask(Task newTask) {
        Task task = super.updateTask(newTask);
        save();
        return task;
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        Epic epic = super.addEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        Epic epic = super.updateEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Subtask addSubtask(Subtask newSubtask) {
        Subtask subtask = super.addSubtask(newSubtask);
        save();
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        Subtask subtask = super.updateSubtask(newSubtask);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }

    public static void main(String[] args) {
        File file = new File("saveManager.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task testTask = new Task("Тестовая задача", "Описание тестовой задачи");
        manager.addTask(testTask);

        Epic testEpic = new Epic("Эпик с подзадачей", "Описание эпика с подзадачей");
        manager.addEpic(testEpic);

        Subtask testSubtask = new Subtask("Подзадача", "Описание подзадачи", testEpic.getId());
        manager.addSubtask(testSubtask);

        FileBackedTaskManager loadManager = FileBackedTaskManager.loadFromFile(file);

        for (Task task : loadManager.tasks.values()) {
            System.out.println(task.toString());
        }
        for (Epic epic : loadManager.epics.values()) {
            System.out.println(epic.toString());
        }
        for (Subtask subtask : loadManager.subtasks.values()) {
            System.out.println(subtask.toString());
        }
    }
}
