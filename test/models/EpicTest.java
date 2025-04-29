package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.models.Epic;

class EpicTest {
    @Test
    /* у нас в теории и в ТЗ использовался обычный lowerCamelCase для наименования методов-тестов,
    поэтому тут решили пока так же оставить, но за рекомендацию спасибо!
    Я поизучаю ещё как лучше – у наставника уже спросил.
    А пока я так понял, что на настоящей работе решается внутри команды, как лучше методы называть*/
    void epicsAreEqualIfIdIsSame() {
        Epic epic1 = new Epic("Заголовок1", "Описание1");
        epic1.setId(1);
        Epic epic2 = new Epic("Заголовок2", "Описание2");
        epic2.setId(1);

        assertEquals(epic1, epic2, "Задачи должны быть одинаковыми");
    }
}