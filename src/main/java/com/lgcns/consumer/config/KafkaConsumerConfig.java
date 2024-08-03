package com.lgcns.consumer.config;

import com.lgcns.consumer.dto.TopicDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${cloudaa.kafka.consumer.bootstrap-servers-config}")
    String bootstrapServersConfig;

    @Value("${cloudaa.kafka.consumer.key-deserializer-class-config}")
    String KeyDeserializerClassConfig;

    @Value("${cloudaa.kafka.consumer.value-deserializer-class-config}")
    String ValueDeserialzierClassConfig;

    @Value("${cloudaa.kafka.consumer.group-id-config}")
    String GroupIdConfig;

    @Bean
    public ConsumerFactory<String, TopicDto> consumerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KeyDeserializerClassConfig);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ValueDeserialzierClassConfig);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GroupIdConfig);

        // 들어오는 record 를 객체로 받기 위한 deserializer
        JsonDeserializer<TopicDto> deserializer = new JsonDeserializer<>(TopicDto.class, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TopicDto>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TopicDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // 수신하는 consumer 에서 record를 필터링할 수 있음
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TopicDto>
    filterListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TopicDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
//        factory.setRecordFilterStrategy(
//                record -> Integer.parseInt(record.value().getAge()) > 30);
        return factory;
    }

}
