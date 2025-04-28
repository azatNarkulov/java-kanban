package ru.yandex.practicum;

import ru.yandex.practicum.manager.*;
import ru.yandex.practicum.models.*;

public class Main {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task task1 = new Task("Тестовая задача", "Описание тестовой задачи");
        // почему в строке ниже IDEA подчёркивает taskManager и пишет, что нет доступа к TaskManager
        // хотя на самом деле доступ есть, ведь я импортировал пакет manager, и код компилируется?
        checkResult(taskManager.addTask(task1));

        Task task2 = new Task("Тестовая задача #2", "Описание тестовой задачи #2");
        checkResult(taskManager.addTask(task2));

        Epic epic1 = new Epic("Эпик с двумя подзадачами", "Описание эпика с двумя подзадачами");
        checkResult(taskManager.addEpic(epic1));

        Subtask subtask1 = new Subtask("Подзадача №1", "Описание подзадачи №1", epic1.getId());
        checkResult(taskManager.addSubtask(subtask1));

        Subtask subtask2 = new Subtask("Подзадача №2", "Описание подзадачи №2", epic1.getId());
        checkResult(taskManager.addSubtask(subtask2));

        Epic epic2 = new Epic("Эпик с одной подзадачей", "Описание эпика с одной подзадачей");
        checkResult(taskManager.addEpic(epic2));

        Subtask subtask3 = new Subtask("Подзадача №3", "Описание подзадачи №1", epic2.getId());
        checkResult(taskManager.addSubtask(subtask3));

        taskManager.getTask(task2.getId());
        printAllTasks();
        taskManager.getSubtask(subtask2.getId());
        printAllTasks();
        taskManager.getEpic(epic1.getId());
        printAllTasks();
    }

    private static void checkResult(Task task) {
        if (task != null) {
            System.out.println("Успешное действие с id " + task.getId());
        } else {
            System.out.println("Произошла ошибка");
        }
    }

    private static void printAllTasks() {
        System.out.println("Задачи");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Эпики");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("Подзадачи");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
