package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.models.*;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void remove(int id) {
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
            oldTail.setNext(node);
        }
        nodeMap.put(task.getId(), node);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasksArray = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasksArray.add(current.getTask());
            current = current.getNext();
        }
        return tasksArray;
    }

    private void removeNode(Node node) {
        Node prev = node.getPrev();
        Node next = node.getNext();
        if (prev != null) {
            prev.setNext(next);
        } else {
            head = next;
        }
        if (next != null) {
            next.setPrev(prev);
        } else {
            tail = prev;
        }
        nodeMap.remove(node.getTask().getId());

    }
}
