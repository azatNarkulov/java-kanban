package ru.yandex.practicum.models;

import static ru.yandex.practicum.models.TaskType.EPIC;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(Integer id, String title, String description, Status status) {
        super(id, title, description, status);
    }

    public Epic(Epic anotherEpic) {
        super(anotherEpic);
        this.subtasksId = anotherEpic.getSubtasksId();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
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
                '}';
    }
}
