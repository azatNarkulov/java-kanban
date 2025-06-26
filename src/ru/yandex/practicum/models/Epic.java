package ru.yandex.practicum.models;

import static ru.yandex.practicum.models.TaskType.EPIC;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>(); // вместо id подзадач теперь хранятся сами подзадачи
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(Integer id, String title, String description) {
        super(id, title, description);
    }

    public Epic(Integer id, String title, String description,
                Status status, LocalDateTime startTime, Duration duration) {
        super(id, title, description, status, startTime, duration);
    }

    public Epic(Integer id, String title, String description,
                LocalDateTime startTime, Duration duration) {
        super(id, title, description, startTime, duration);
    }

    public Epic(Epic anotherEpic) {
        super(anotherEpic);
        this.subtasksId = anotherEpic.getSubtasksId();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Epic copy() {
        return new Epic(this);
    }

    @Override
    public TaskType getTaskType() {
        return EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() { // просто возвращаем endTime
        return endTime;
    }
}
