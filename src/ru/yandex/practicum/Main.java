package ru.yandex.practicum;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Тестовая задача", "Описание тестовой задачи", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Тестовая задача #2", "Описание тестовой задачи #2", Status.NEW);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик с двумя подзадачами", "Описание эпика с двумя подзадачами");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", "Описание подзадачи №1", epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", "Описание подзадачи №2", epic1.getId());
        taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Эпик с одной подзадачей", "Описание эпика с одной подзадачей");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача №3", "Описание подзадачи №1", epic2.getId());
        taskManager.addSubtask(subtask3);

        System.out.println(taskManager.printAllTasks());
        System.out.println(taskManager.printAllEpiks());
        System.out.println(taskManager.printAllSubtasks());

        task1.setTitle("Тестовая задача с др стат-м");
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);

        subtask1.setTitle("Подзадача №1 c др стат-м");
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);

        System.out.println();
        System.out.println(taskManager.printAllTasks());
        System.out.println(taskManager.printAllEpiks());
        System.out.println(taskManager.printAllSubtasks());

        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic1.getId());
        taskManager.deleteSubtask(subtask3.getId());

        System.out.println();
        System.out.println(taskManager.printAllTasks());
        System.out.println(taskManager.printAllEpiks());
        System.out.println(taskManager.printAllSubtasks());
    }
}
