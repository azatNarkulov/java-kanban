package ru.yandex.practicum.manager.history;

import java.util.ArrayList;
import java.util.HashMap;

import ru.yandex.practicum.models.*;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        int id = task.getId();
        if (nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
        }
        linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(Task task) {
        int id = task.getId();
        Node node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    private void linkLast(Task task) {
        Node oldTail = tail;
        Node node = new Node(oldTail, task, null);
        tail = node;
        if (oldTail == null) {
            head = node;
        } else {
            oldTail.next = node;
        }
        nodeMap.put(task.getId(), node);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasksArray = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasksArray.add(current.task);
            current = current.next;
        }
        return tasksArray;
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;
        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
        nodeMap.remove(node.task.getId());

    }
}
