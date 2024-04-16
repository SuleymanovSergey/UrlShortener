package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UrlShortenerService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    public ShortUrl createShortUrl(String originalUrl) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);

        // Заглушка для генерации короткого кода URL
        String shortCode = generateShortCode();
        shortUrl.setShortUrlCode(shortCode);

        return shortUrlRepository.save(shortUrl);
    }

    public ShortUrl getOriginalUrl(String shortCode) {
        return shortUrlRepository.findByShortUrlCode(shortCode);
    }

    private String generateShortCode() {
        // Генерация простой уникальной строки (например, UUID) в качестве заглушки
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
