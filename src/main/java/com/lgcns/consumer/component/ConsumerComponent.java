package com.lgcns.consumer.component;

import com.lgcns.consumer.dto.TopicDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
@Data
public class ConsumerComponent {
    private CountDownLatch latch = new CountDownLatch(10);
    private List<TopicDto> payloads = new ArrayList<>();
    private TopicDto payload;

    // record 를 수신하기 위한 consumer 설정
    @KafkaListener(topics = "testing0801",
            containerFactory = "filterListenerContainerFactory")
    public void receive(ConsumerRecord<String, TopicDto> consumerRecord) {
        payload = consumerRecord.value();
        log.info("received payload = {}", payload.toString());
        payloads.add(payload);
        latch.countDown();
    }

    public List<TopicDto> getPayloads() {
        return payloads;
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
