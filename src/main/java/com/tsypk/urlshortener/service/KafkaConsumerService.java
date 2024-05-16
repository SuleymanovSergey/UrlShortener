package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final UrlShortenerService urlShortenerService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaConsumerService(UrlShortenerService urlShortenerService, KafkaTemplate<String, String> kafkaTemplate) {
        this.urlShortenerService = urlShortenerService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "long-url-topic", groupId = "group_id")
    public void listen(String url) {
        // Логика сокращения URL
        ShortUrl shortUrl = urlShortenerService.createShortUrl(url);

        // Отправка сокращенного URL в другой топик
        kafkaTemplate.send("short-url-topic", shortUrl.getShortUrlCode());
    }
}
