package ru.yandex.practicum.models;

import ru.yandex.practicum.manager.task.Type;
import static ru.yandex.practicum.manager.task.Type.EPIC;

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
    public Type getType() {
        return EPIC;
    }
}
