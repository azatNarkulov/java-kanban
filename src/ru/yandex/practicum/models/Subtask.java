package ru.yandex.practicum.models;

import ru.yandex.practicum.manager.task.Type;
import static ru.yandex.practicum.manager.task.Type.SUBTASK;

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

    @Override
    public Type getType() {
        return SUBTASK;
    }
}
