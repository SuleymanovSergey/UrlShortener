package com.tsypk.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsypk.urlshortener.entity.ShortUrl;
import com.tsypk.urlshortener.repository.ShortUrlRepository;
import com.tsypk.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class ShortUrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ShortUrl shortenUrl(@RequestParam String originalUrl) {
        return urlShortenerService.createShortUrl(originalUrl);
    }

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @GetMapping("/redirect/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortUrlCode(shortCode);
        String originalUrl = shortUrl.getOriginalUrl();
        return new RedirectView(originalUrl);
    }

}
