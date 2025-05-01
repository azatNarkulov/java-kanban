package ru.yandex.practicum.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.models.Subtask;

class SubtaskTest {
    @Test
    void subtasksShouldBeEqualIfTheirIdAreEquals() {
        Subtask subtask1 = new Subtask("Заголовок1", "Описание1", 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Заголовок2", "Описание2", 1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Задачи должны быть одинаковыми");
    }
}