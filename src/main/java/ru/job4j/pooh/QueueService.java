package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", "204");
        switch (req.httpRequestType()) {
            case "GET" -> {
                String param = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
                if (param != null) {
                   result = new Resp("param=" + param, "204");
                }
            }
            case "POST" -> {
                queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
                queue.get(req.getSourceName()).add(req.getParam());
            }
            default -> {
                result = new Resp("", "501");
            }
        }
        return result;
    }
}