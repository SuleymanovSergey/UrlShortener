package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortCode;
import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortCodeRepository;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

        // Получение следующего короткого кода из базы данных
        String shortCode = getNextShortCode();
        shortUrl.setShortUrlCode(shortCode);

        return shortUrlRepository.save(shortUrl);
    }

    private String getNextShortCode() {
        // Получаем следующий доступный короткий код из базы данных
        ShortCode shortCode = shortCodeRepository.findFirstByOrderByCodeAsc();

        if (shortCode != null) {
            // Удаляем использованный короткий код из базы данных
            shortCodeRepository.delete(shortCode);
            return shortCode.getCode();
        } else {
            // Если короткий код не найден, вы можете выбрать, что делать дальше.
            // Например, выбросить исключение или вернуть значение по умолчанию.
            throw new IllegalStateException("No available short code found.");
        }
    }
}

