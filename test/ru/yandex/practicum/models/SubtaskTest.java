package ru.yandex.practicum.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
    @Test
    void setId_returnSameSubtasks_ifIdAreEquals() {
        Subtask subtask1 = new Subtask("Заголовок1", "Описание1", 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Заголовок2", "Описание2", 1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Задачи должны быть одинаковыми");
    }
}