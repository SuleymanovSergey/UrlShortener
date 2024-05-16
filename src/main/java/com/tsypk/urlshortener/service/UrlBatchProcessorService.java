package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UrlBatchProcessorService {

    private final List<String> urlBatch = new ArrayList<>();
    private final int BATCH_SIZE = 10;

    @Autowired
    private UrlShortenerService urlShortenerService;

    @KafkaListener(topics = "long-url-topic", groupId = "url-group")
    public void listen(String originalUrl) {
        synchronized (urlBatch) {
            urlBatch.add(originalUrl);
            if (urlBatch.size() >= BATCH_SIZE) {
                processBatch();
                urlBatch.clear();
            }
        }
    }

    private void processBatch() {
        List<ShortUrl> shortUrls = new ArrayList<>();
        for (String url : urlBatch) {
            ShortUrl shortUrl = urlShortenerService.createShortUrl(url);
            shortUrls.add(shortUrl);
        }
        // Обновите статусы коротких URL в базе данных или кэше
        // Например, сохраняя их в базе данных
        urlShortenerService.saveAll(shortUrls);
    }
}

