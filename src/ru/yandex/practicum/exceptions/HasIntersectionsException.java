package ru.yandex.practicum.exceptions;

public class HasIntersectionsException extends RuntimeException {
    public HasIntersectionsException() {
        super("Задача пересекается по времени с другой задачей");
    }
}
