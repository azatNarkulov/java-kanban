package ru.yandex.practicum.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Задача не найдена");
    }
}
