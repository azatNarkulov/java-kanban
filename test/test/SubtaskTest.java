package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.*;

class SubtaskTest {
    @Test
    void tasksAreEqualIfIdIsSame() {
        Subtask subtask1 = new Subtask("Заголовок1", "Описание1", 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Заголовок2", "Описание2", 1);
        subtask2.setId(1);

        Assertions.assertEquals(subtask1, subtask2, "Задачи должны быть одинаковыми");
    }
}