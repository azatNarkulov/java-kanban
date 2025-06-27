package ru.yandex.practicum.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    @Test
    void setId_epicsAreEqual_idsAreEqual() {
        Epic epic1 = new Epic("Заголовок1", "Описание1");
        epic1.setId(1);
        Epic epic2 = new Epic("Заголовок2", "Описание2");
        epic2.setId(1);

        assertEquals(epic1, epic2, "Задачи должны быть одинаковыми");
    }
}