package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.*;

class EpicTest {
    @Test
    void epicsAreEqualIfIdIsSame() {
        Epic epic1 = new Epic("Заголовок1", "Описание1");
        epic1.setId(1);
        Epic epic2 = new Epic("Заголовок2", "Описание2");
        epic2.setId(1);

        Assertions.assertEquals(epic1, epic2, "Задачи должны быть одинаковыми");
    }
}