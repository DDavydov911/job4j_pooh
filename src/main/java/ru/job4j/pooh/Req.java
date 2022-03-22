package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] arr = content.split("[ /\n\r]");
        String httpRequestType = arr[0];
        String poohMode = arr[2];
        String sourceName = arr[3];
        String param = "GET".equals(httpRequestType) && "topic".equals(poohMode) ? arr[4]
                : "GET".equals(httpRequestType) && "queue".equals(poohMode) ? "" : arr[arr.length - 1];
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}