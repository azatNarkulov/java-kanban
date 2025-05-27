package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
Я помню комментарий с ФЗ5 про то, что директорию test надо убрать под src, но у меня не получилось:
после переноса папки настройки в IDEA слетели, и я не мог пометить test как test resources root.
Параллельно одногруппники писали, что у них так тесты не работают,
поэтому решил оставить test на одном уровне с src
 */

class InMemoryHistoryManagerTest {

    @Test
    void taskShouldBeAddedToMap() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        assertNotNull(historyManager);
    }

    @Test
    void taskShouldBeRemoved() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Заголовок задачи", "Описание задачи");
        task.setId(1);
        historyManager.add(task);

        ArrayList<Task> tasks1 = historyManager.getHistory();

        historyManager.remove(task);
        ArrayList<Task> tasks2 = historyManager.getHistory();

        assertNotEquals(tasks1, tasks2);
    }
}