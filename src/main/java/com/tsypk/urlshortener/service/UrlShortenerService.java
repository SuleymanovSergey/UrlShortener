package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortCode;
import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortCodeRepository;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.UUID;

@Service
public class UrlShortenerService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private ShortCodeRepository shortCodeRepository;

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
}

