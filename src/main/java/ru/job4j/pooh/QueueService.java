package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", "500");
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).add(req.getParam());
            result = new Resp("Entry is added", "200");
            System.out.println(result.text());
        }
        if ("GET".equals(req.httpRequestType())) {
            String param = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
            result = param != null ? new Resp(param, "200") : new Resp("Entry is null", "404");
            System.out.println(result.text());
        }
        return result;
    }
}