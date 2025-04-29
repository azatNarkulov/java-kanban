package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.models.Task;

class TaskTest {
    @Test
    void tasksAreEqualIfIdIsSame() {
        Task task1 = new Task("Заголовок1", "Описание1");
        task1.setId(1);
        Task task2 = new Task("Заголовок2", "Описание2");
        task2.setId(1);

        assertEquals(task1, task2, "Задачи должны быть одинаковыми");
    }
}