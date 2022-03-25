package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", "204");
        switch (req.httpRequestType()) {
            case "GET" -> {
                map.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
                map.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
                String param = map.getOrDefault(req.getSourceName(), new ConcurrentHashMap<>())
                        .getOrDefault(req.getParam(), new ConcurrentLinkedQueue<>()).poll();
                if (param != null) {
                    result = new Resp(param, "204");
                }
            }
            case "POST" -> {
                ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> weather = map.get(req.getSourceName());
                if (weather != null) {
                    weather.values().forEach(value -> value.add(req.getParam()));
                }
            }
            default -> {
                result = new Resp("", "501");
            }
        }
        return result;
    }
}