package ru.yandex.practicum;

import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.models.*;

public class Main {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task task1 = new Task("Тестовая задача #1", "Описание тестовой задачи #1"); // 1
        checkResult(taskManager.addTask(task1));
        Task task2 = new Task("Тестовая задача #2", "Описание тестовой задачи #2"); // 2
        checkResult(taskManager.addTask(task2));

        Epic epic1 = new Epic("Эпик с тремя подзадачами", "Описание эпика с тремя подзадачами"); // 3
        checkResult(taskManager.addEpic(epic1));
        Subtask subtask1 = new Subtask("Подзадача №1", "Описание подзадачи №1", epic1.getId()); // 4
        checkResult(taskManager.addSubtask(subtask1));
        Subtask subtask2 = new Subtask("Подзадача №2", "Описание подзадачи №2", epic1.getId()); // 5
        checkResult(taskManager.addSubtask(subtask2));
        Subtask subtask3 = new Subtask("Подзадача №3", "Описание подзадачи №3", epic1.getId()); // 6
        checkResult(taskManager.addSubtask(subtask3));

        Epic epic2 = new Epic("Эпик без подзадач", "Описание эпика без подзадач"); // 7
        checkResult(taskManager.addEpic(epic2));

        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getTask(task1.getId());

        System.out.println(taskManager.getHistory());

        taskManager.deleteSubtask(subtask2.getId());

        System.out.println(taskManager.getHistory());

        taskManager.deleteEpic(epic1.getId());

        System.out.println(taskManager.getHistory());
    }

    private static void checkResult(Task task) {
        if (task != null) {
            System.out.println("Успешное действие с id " + task.getId());
        } else {
            System.out.println("Произошла ошибка");
        }
    }
}
