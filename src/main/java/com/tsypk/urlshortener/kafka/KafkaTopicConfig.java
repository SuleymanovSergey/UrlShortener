package com.tsypk.urlshortener.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic longUrlTopic() {
        return TopicBuilder.name("long-url-topic").partitions(3).build();
    }

    @Bean
    public NewTopic shortUrlTopic() {
        return TopicBuilder.name("short-url-topic").partitions(3).build();
    }
}
