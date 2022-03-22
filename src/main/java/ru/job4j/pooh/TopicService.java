package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    Resp result;

    @Override
    public Resp process(Req req) {
        if ("POST".equals(req.httpRequestType())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> weather = map.get(req.getSourceName());
            if (weather == null) {
                result = new Resp(null, "404");
            } else {
                for (String key : weather.keySet()) {
                    weather.get(key).add(req.getParam());
                }
                result = new Resp("", "200");
            }
        }
        if ("GET".equals(req.httpRequestType())) {
            if (map.putIfAbsent(req.getSourceName(),
                    new ConcurrentHashMap<>()) == null) {
                map.get(req.getSourceName()).put(req.getParam(), new ConcurrentLinkedQueue<>());
                result = new Resp("", "207");

            } else {
                String res = map.getOrDefault(req.getSourceName(),
                        new ConcurrentHashMap<>()).getOrDefault(req.getParam(),
                        new ConcurrentLinkedQueue<>()).poll();
                result = res == null ? new Resp("", "404") : new Resp(res, "200");
            }
        }
        return result;
    }
}