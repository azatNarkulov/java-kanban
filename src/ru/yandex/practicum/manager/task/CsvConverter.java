package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.models.*;

import java.time.Duration;
import java.time.LocalDateTime;

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
        LocalDateTime startTime = LocalDateTime.parse(parts[5]);
        Duration duration = Duration.ofMinutes(Integer.parseInt(parts[6]));

        switch (taskType) {
            case TASK:
                return new Task(id, name, description, status, startTime, duration);
            case EPIC:
                return new Epic(id, name, description, status, startTime, duration);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[7]);
                Subtask subtask = new Subtask(id, name, description, status, startTime, duration);
                subtask.setEpicId(epicId);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + taskType);
        }
    }

    public static String taskToLine(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%s",
                task.getId(),
                TASK,
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                task.getStartTime() != null ? task.getStartTime() : "",
                task.getDuration() != null ? task.getDuration().toMinutes() : "");
    }

    public static String taskToLine(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s,%s",
                epic.getId(),
                EPIC,
                epic.getTitle(),
                epic.getStatus(),
                epic.getDescription(),
                epic.getStartTime() != null ? epic.getStartTime() : "",
                epic.getDuration() != null ? epic.getDuration().toMinutes() : "");
    }

    public static String taskToLine(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                subtask.getId(),
                SUBTASK,
                subtask.getTitle(),
                subtask.getStatus(),
                subtask.getDescription(),
                subtask.getStartTime() != null ? subtask.getStartTime() : "",
                subtask.getDuration() != null ? subtask.getDuration().toMinutes() : "",
                subtask.getEpicId());
    }
}
