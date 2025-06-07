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

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
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
                Task task = manager.fromString(lines.get(i));
                switch (task.getType()) {
                    case TASK:
                        manager.addTask(task);
                        break;
                    case EPIC:
                        manager.addEpic((Epic) task);
                        break;
                    case SUBTASK:
                        manager.addSubtask((Subtask) task);
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестный тип задачи: " + task.getType());
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Возникла ошибка при загрузке");
        }
        return manager;
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");

        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(id, name, description, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    private String toString(Task task) {
        String result = String.format("%d,%s,%s,%s,%s,",
                task.getId(), task.getType(), task.getTitle(), task.getStatus(), task.getDescription());
        if (task instanceof Subtask) {
            return result + ((Subtask) task).getEpicId();
        } else {
            return result;
        }
    }

    private void printAll() {
        for (Task task : getAllTasks()) {
            System.out.println(task.toString());
        }
        for (Epic epic : getAllEpics()) {
            System.out.println(epic.toString());
        }
        for (Subtask subtask : getAllSubtasks()) {
            System.out.println(subtask.toString());
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
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
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
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
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }

    public static void main(String[] args) {
        File file = new File("saveManager.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task = new Task("Тестовая задача", "Описание тестовой задачи");
        manager.addTask(task);

        Epic epic = new Epic("Эпик с подзадачей", "Описание эпика с подзадачей");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        manager.addSubtask(subtask);

        FileBackedTaskManager loadManager = FileBackedTaskManager.loadFromFile(file);
        loadManager.printAll();
    }
}
