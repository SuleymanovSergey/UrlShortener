package com.tsypk.urlshortener.controller;

import com.tsypk.urlshortener.DTO.CreateShortUrlDTO;
import com.tsypk.urlshortener.DTO.ShortUrlDTO;
import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import com.tsypk.urlshortener.service.KafkaProducerService;
import com.tsypk.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.tsypk.urlshortener.service.UrlCacheService;
import java.util.Date;

@Tag(name = "main_methods")
@Slf4j
@RestController
@RequestMapping("/api")
public class ShortUrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlDTO> shortenUrl(@RequestBody CreateShortUrlDTO createShortUrlDTO) {
//        ShortUrl shortUrl = urlShortenerService.createShortUrl(createShortUrlDTO.getOriginalUrl());// Создание короткого URL с помощью сервиса UrlShortenerService

        // Отправка оригинального URL в Kafka
        kafkaProducerService.sendMessage("long-url-topic", createShortUrlDTO.getOriginalUrl());
        ShortUrlDTO shortUrlDTO = new ShortUrlDTO(); // Формирование DTO для возвращаемого значения
        shortUrlDTO.setOriginalUrl(createShortUrlDTO.getOriginalUrl());
        shortUrlDTO.setShortUrlCode("processing"); // Или другой индикатор

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shortUrlDTO);

//        ShortUrlDTO shortUrlDTO = new ShortUrlDTO(); // Формирование DTO для возвращаемого значения
//        shortUrlDTO.setOriginalUrl(shortUrl.getOriginalUrl());
//        shortUrlDTO.setShortUrlCode(shortUrl.getShortUrlCode());
//        shortUrlDTO.setCreatedAt(shortUrl.getCreatedAt());
//        shortUrlDTO.setDestroyedAt(shortUrl.getDestroyedAt());
//        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlDTO);  // Возврат сокращенной ссылки и статуса 201 Created
    }

    @PostMapping("/shortenWithDate")
    public ResponseEntity shortenUrlWithDate(@RequestBody CreateShortUrlDTO createShortUrlDTO, @RequestParam("destroyedAt") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") Date destroyedAt) {
        ShortUrl shortUrl = urlShortenerService.createShortUrl(createShortUrlDTO.getOriginalUrl());

        // Присваиваем значение даты из параметра destroyedAt
        shortUrl.setDestroyedAt(destroyedAt);

        ShortUrlDTO shortUrlDTO = new ShortUrlDTO(); // Формирование DTO для возвращаемого значения
        shortUrlDTO.setOriginalUrl(shortUrl.getOriginalUrl());
        shortUrlDTO.setShortUrlCode(shortUrl.getShortUrlCode());
        shortUrlDTO.setCreatedAt(shortUrl.getCreatedAt());
        shortUrlDTO.setDestroyedAt(shortUrl.getDestroyedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlDTO);  // Возврат сокращенной ссылки и статуса 201 Created
    }

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private UrlCacheService urlCacheService;

    @GetMapping("/redirect/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        String originalUrl = urlCacheService.getOriginalUrl(shortCode);

        if (originalUrl == null) {
            ShortUrl shortUrl = shortUrlRepository.findByShortUrlCode(shortCode);

            if (shortUrl == null) {
                return new RedirectView("/error");
            }

            Date now = new Date();
            Date destroyedAt = shortUrl.getDestroyedAt();

            if (destroyedAt != null && now.after(destroyedAt)) {
                // Если TTL истек, выполняем перенаправление на страницу с сообщением о истекшей ссылке
                return new RedirectView("/expired");
            }

            originalUrl = shortUrl.getOriginalUrl();
            urlCacheService.cacheUrl(shortCode, originalUrl);
        }

        return new RedirectView(originalUrl);
    }
}

