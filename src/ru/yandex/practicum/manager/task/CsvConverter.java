package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.models.*;

import static ru.yandex.practicum.models.TaskType.TASK;
import static ru.yandex.practicum.models.TaskType.EPIC;
import static ru.yandex.practicum.models.TaskType.SUBTASK;

public class CsvConverter {
    public static Task fromString(String value) {
        String[] parts = value.split(",");

        int id = Integer.parseInt(parts[0]);
        TaskType taskType = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (taskType) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(id, name, description, status);
                subtask.setEpicId(epicId);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + taskType);
        }
    }

    public static String taskToLine(Task task) {
        return String.format("%d,%s,%s,%s,%s,",
                task.getId(), TASK, task.getTitle(), task.getStatus(), task.getDescription());
    }

    public static String taskToLine(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,",
                epic.getId(), EPIC, epic.getTitle(), epic.getStatus(), epic.getDescription());
    }

    public static String taskToLine(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%s",
                subtask.getId(),
                SUBTASK,
                subtask.getTitle(),
                subtask.getStatus(),
                subtask.getDescription(),
                subtask.getEpicId());
    }
}
