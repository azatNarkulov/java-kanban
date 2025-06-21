package ru.yandex.practicum.models;

import java.time.Duration;
import java.time.LocalDateTime;

import static ru.yandex.practicum.models.TaskType.SUBTASK;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String title, String description, Status status, Integer epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String title, String description, Integer epicId) {
        super(id, title, description);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String title, String description, Status status) {
        super(id, title, description, status);
    }

    public Subtask(Integer id, String title, String description) {
        super(id, title, description);
    }

    public Subtask(Integer id, String title, String description,
                   Status status, LocalDateTime startTime, Duration duration) {
        super(id, title, description, status, startTime, duration);
    }

    public Subtask(Integer id, String title, String description,
                   LocalDateTime startTime, Duration duration, Integer epicId) {
        super(id, title, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Subtask anotherSubtask) {
        super(anotherSubtask);
        this.epicId = anotherSubtask.getEpicId();
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public Subtask copy() {
        return new Subtask(this);
    }

    @Override
    public TaskType getTaskType() {
        return SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + getEpicId() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }

    @Override
    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,%s",
                getId(), getTaskType(), getTitle(), getStatus(), getDescription(), getEpicId());
    }
}
