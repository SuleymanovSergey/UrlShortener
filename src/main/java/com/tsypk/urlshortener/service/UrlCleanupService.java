package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UrlCleanupService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Scheduled(fixedDelay = 3600000) // Проверка каждый час
    public void cleanupExpiredUrls() {
        Date now = new Date();
        List<ShortUrl> expiredUrls = shortUrlRepository.findByDestroyedAtBefore(now);
        for (ShortUrl url : expiredUrls) {
            shortUrlRepository.delete(url);
        }
    }
}
