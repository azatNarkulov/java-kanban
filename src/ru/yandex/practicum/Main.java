package ru.yandex.practicum;

import ru.yandex.practicum.manager.task.InMemoryTaskManager;

public class Main {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static void main(String[] args) {
        System.out.println("Поехали!");

    }
}
