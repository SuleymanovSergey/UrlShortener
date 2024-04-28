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

import java.util.Date;

@Tag(name = "main_methods")
@Slf4j
@RestController
@RequestMapping("/api")
public class ShortUrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlDTO> shortenUrl(@RequestBody CreateShortUrlDTO createShortUrlDTO) {
        ShortUrl shortUrl = urlShortenerService.createShortUrl(createShortUrlDTO.getOriginalUrl());// Создание короткого URL с помощью сервиса UrlShortenerService

        ShortUrlDTO shortUrlDTO = new ShortUrlDTO(); // Формирование DTO для возвращаемого значения
        shortUrlDTO.setOriginalUrl(shortUrl.getOriginalUrl());
        shortUrlDTO.setShortUrlCode(shortUrl.getShortUrlCode());
        shortUrlDTO.setCreatedAt(shortUrl.getCreatedAt());
        shortUrlDTO.setDestroyedAt(shortUrl.getDestroyedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlDTO);  // Возврат сокращенной ссылки и статуса 201 Created
    }

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @GetMapping("/redirect/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
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

        String originalUrl = shortUrl.getOriginalUrl();
        return new RedirectView(originalUrl);
    }
}

