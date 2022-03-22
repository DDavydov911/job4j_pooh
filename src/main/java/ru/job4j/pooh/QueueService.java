package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {

        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).add(req.getParam());
            return new Resp("", "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            String param = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
            return param != null ? new Resp(param, "200") : new Resp(null, "404");
        }
        return null;
    }
}