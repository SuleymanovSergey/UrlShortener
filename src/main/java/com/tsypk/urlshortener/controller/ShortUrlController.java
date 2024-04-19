package com.tsypk.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsypk.urlshortener.DTO.CreateShortUrlDTO;
import com.tsypk.urlshortener.DTO.ShortUrlDTO;
import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import com.tsypk.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "main_methods")
@Slf4j
@RestController
@RequestMapping("/api")
public class ShortUrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlDTO> shortenUrl(@RequestBody CreateShortUrlDTO createShortUrlDTO) {
        // Создание короткого URL с помощью сервиса UrlShortenerService
        ShortUrl shortUrl = urlShortenerService.createShortUrl(createShortUrlDTO.getOriginalUrl());

        // Формирование DTO для возвращаемого значения
        ShortUrlDTO shortUrlDTO = new ShortUrlDTO();
        shortUrlDTO.setOriginalUrl(shortUrl.getOriginalUrl());
        shortUrlDTO.setShortUrlCode(shortUrl.getShortUrlCode());
        shortUrlDTO.setCreatedAt(shortUrl.getCreatedAt());
        shortUrlDTO.setDestroyedAt(shortUrl.getDestroyedAt());

        // Возврат сокращенной ссылки и статуса 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlDTO);
    }

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @GetMapping("/redirect/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        // Получаем ShortUrl по короткому коду из репозитория
        ShortUrl shortUrl = shortUrlRepository.findByShortUrlCode(shortCode);

        // Проверяем, был ли найден ShortUrl
        if (shortUrl == null) {
            // Если ShortUrl не найден, выполняем перенаправление на страницу с ошибкой или другую альтернативную страницу
            return new RedirectView("/error");
        }

        // Получаем оригинальный URL из ShortUrl и выполняем перенаправление
        String originalUrl = shortUrl.getOriginalUrl();
        return new RedirectView(originalUrl);
    }
}
