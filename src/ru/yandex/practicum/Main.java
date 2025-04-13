package ru.yandex.practicum;

import ru.yandex.practicum.models.*;
import ru.yandex.practicum.manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Тестовая задача", "Описание тестовой задачи", Status.NEW);
        Integer result = taskManager.addTask(task1);
        if (result != null) {
            System.out.println("Задача с id-номером " + result + " успешно добавлена");
        } else {
            System.out.println("Произошла ошибка");
        }
        Task task2 = new Task("Тестовая задача #2", "Описание тестовой задачи #2", Status.NEW);
        result = taskManager.addTask(task2);
        if (result != null) {
            System.out.println("Задача с id-номером " + result + " успешно добавлена");
        } else {
            System.out.println("Произошла ошибка");
        }

        Epic epic1 = new Epic("Эпик с двумя подзадачами", "Описание эпика с двумя подзадачами");
        result = taskManager.addEpic(epic1);
        if (result != null) {
            System.out.println("Эпик с id-номером " + result + " успешно добавлен");
        } else {
            System.out.println("Произошла ошибка");
        }
        Subtask subtask1 = new Subtask("Подзадача №1", "Описание подзадачи №1", epic1.getId());
        result = taskManager.addSubtask(subtask1);
        if (result != null) {
            System.out.println("Подзадача с id-номером " + result + " успешно добавлена");
        } else {
            System.out.println("Произошла ошибка");
        }
        Subtask subtask2 = new Subtask("Подзадача №2", "Описание подзадачи №2", epic1.getId());
        result = taskManager.addSubtask(subtask2);
        if (result != null) {
            System.out.println("Подзадача с id-номером " + result + " успешно добавлена");
        } else {
            System.out.println("Произошла ошибка");
        }

        Epic epic2 = new Epic("Эпик с одной подзадачей", "Описание эпика с одной подзадачей");
        result = taskManager.addEpic(epic2);
        if (result != null) {
            System.out.println("Эпик с id-номером " + result + " успешно добавлен");
        } else {
            System.out.println("Произошла ошибка");
        }
        Subtask subtask3 = new Subtask("Подзадача №3", "Описание подзадачи №1", epic2.getId());
        result = taskManager.addSubtask(subtask3);
        if (result != null) {
            System.out.println("Подзадача с id-номером " + result + " успешно добавлена");
        } else {
            System.out.println("Произошла ошибка");
        }

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        task1.setTitle("Тестовая задача с др стат-м");
        task1.setStatus(Status.IN_PROGRESS);
        result = taskManager.updateTask(task1);
        if (result != null) {
            System.out.println("Задача с id-номером " + result + " успешно обновлена");
        } else {
            System.out.println("Произошла ошибка");
        }

        subtask1.setTitle("Подзадача №1 c др стат-м");
        subtask1.setStatus(Status.IN_PROGRESS);
        result = taskManager.updateSubtask(subtask1);
        if (result != null) {
            System.out.println("Подзадача с id-номером " + result + " успешно обновлена");
        } else {
            System.out.println("Произошла ошибка");
        }

        System.out.println();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic1.getId());
        taskManager.deleteSubtask(subtask3.getId());

        System.out.println();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
