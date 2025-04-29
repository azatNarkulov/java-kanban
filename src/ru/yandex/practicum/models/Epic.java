package ru.yandex.practicum.models;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
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
}
