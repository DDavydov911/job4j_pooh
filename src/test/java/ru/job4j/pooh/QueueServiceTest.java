package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenPost2QueueAndGet() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result3 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result4 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertNull(result1.text());
        assertThat(result1.status(), is("404"));
        assertThat(result2.text(), is(""));
        assertThat(result2.status(), is("200"));
        assertThat(result3.text(), is("temperature=18"));
        assertThat(result3.status(), is("200"));
        assertNull(result4.text());
        assertThat(result4.status(), is("404"));
    }
}