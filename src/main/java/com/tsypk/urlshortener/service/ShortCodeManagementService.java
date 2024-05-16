package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.repository.ShortCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ShortCodeManagementService {

    private final ShortCodeRepository shortCodeRepository;

    private final ShortCodeGeneratorService shortCodeGeneratorService;

    public ShortCodeManagementService(ShortCodeRepository shortCodeRepository, ShortCodeGeneratorService shortCodeGeneratorService) {
        this.shortCodeRepository = shortCodeRepository;
        this.shortCodeGeneratorService = shortCodeGeneratorService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndGenerateShortCodesIfNeeded() { // Метод для проверки и генерации коротких кодов при необходимости
        long currentSize = shortCodeRepository.count();
        // Желаемое количество коротких кодов
        int desiredShortCodes = 100;
        int codesToGenerate = desiredShortCodes - (int) currentSize;
        if (codesToGenerate > 0) {
            shortCodeGeneratorService.generateShortCodes((long) codesToGenerate);
        }
    }
}



