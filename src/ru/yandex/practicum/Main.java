package ru.yandex.practicum;

import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.models.*;

public class Main {
    private static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task task1 = new Task("Тестовая задача", "Описание тестовой задачи", Status.NEW);
        checkResult(taskManager.addTask(task1));

        Task task2 = new Task("Тестовая задача #2", "Описание тестовой задачи #2", Status.NEW);
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

        printAllTasks();

        task1.setTitle("Тестовая задача с др стат-м");
        task1.setStatus(Status.IN_PROGRESS);
        checkResult(taskManager.updateTask(task1));

        subtask1.setTitle("Подзадача №1 c др стат-м");
        subtask1.setStatus(Status.IN_PROGRESS);
        checkResult(taskManager.updateSubtask(subtask1));

        printAllTasks();

        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic1.getId());
        taskManager.deleteSubtask(subtask3.getId());

        printAllTasks();
    }

    // эти два метода я только для тестов создал (как и все проверки в main)
    private static void checkResult(Task task) {
        if (task != null) {
            System.out.println("Успешное действие с id " + task.getId());
        } else {
            System.out.println("Произошла ошибка");
        }
    }

    private static void printAllTasks() {
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
