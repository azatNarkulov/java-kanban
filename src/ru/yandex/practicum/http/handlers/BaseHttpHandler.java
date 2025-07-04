package ru.yandex.practicum.http.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    public void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        System.out.println("Ответ отправлен, код: " + statusCode);
        exchange.sendResponseHeaders(statusCode, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public void sendNotFound(HttpExchange exchange) throws IOException {
        String response = " Задача не найдена";
        sendText(exchange, response, 404);
    }

    public void sendHasIntersection(HttpExchange exchange) throws IOException {
        String response = "Задача пересекается по времени с другой задачей";
        sendText(exchange, response, 406);
    }
}
