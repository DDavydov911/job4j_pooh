package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void when2TopicAndBack() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        Resp result1 = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        Resp result3 = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result4 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result5 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertNull(result1.text());
        assertThat(result1.status(), is("404"));
        assertThat(result2.text(), is(""));
        assertThat(result2.status(), is("207"));
        assertThat(result3.text(), is(""));
        assertThat(result3.status(), is("200"));
        assertThat(result4.text(), is("temperature=18"));
        assertThat(result4.status(), is("200"));
        assertThat(result5.text(), is(""));
//        assertThat(result5.status(), is("404"));
    }
}