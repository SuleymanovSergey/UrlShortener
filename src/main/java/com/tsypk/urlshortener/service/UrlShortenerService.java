package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortCode;
import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortCodeRepository;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeRepository shortCodeRepository;

    public UrlShortenerService(ShortUrlRepository shortUrlRepository, ShortCodeRepository shortCodeRepository) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortCodeRepository = shortCodeRepository;
    }

    public ShortUrl createShortUrl(String originalUrl) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);

        String shortCode = getNextShortCode(); // Получение следующего короткого кода из базы данных
        shortUrl.setShortUrlCode(shortCode);

        return shortUrlRepository.save(shortUrl);
    }

    private String getNextShortCode() {
        ShortCode shortCode = shortCodeRepository.findFirstByOrderByCodeAsc(); // Получаем следующий доступный короткий код из базы данных

        if (shortCode != null) {
            shortCodeRepository.delete(shortCode); // Удаляем использованный короткий код из базы данных
            return shortCode.getCode();
        } else {
            throw new IllegalStateException("No available short code found.");
        }
    }

    public void saveAll(List<ShortUrl> shortUrls) {
        shortUrlRepository.saveAll(shortUrls); // Сохраняем все ShortUrl объекты в базе данных
    }
}

