package ru.yandex.practicum;

import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.models.*;

public class Main {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static void main(String[] args) {
        System.out.println("Поехали!");

    }
}
