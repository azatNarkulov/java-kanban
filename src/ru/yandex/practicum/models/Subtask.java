package ru.yandex.practicum.models;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(Subtask anotherSubtask) {
        super(anotherSubtask);
        this.epicId = anotherSubtask.getEpicId();
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public Subtask copy() {
        return new Subtask(this);
    }
}
