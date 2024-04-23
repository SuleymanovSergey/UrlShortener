package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.repository.ShortCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ShortCodeManagementService {

    @Autowired
    private ShortCodeRepository shortCodeRepository;

    @Autowired
    private ShortCodeGeneratorService shortCodeGeneratorService;

    private int desiredShortCodes = 100;  // Желаемое количество коротких кодов

    @Scheduled(fixedRate = 60000)
    public void checkAndGenerateShortCodesIfNeeded() { // Метод для проверки и генерации коротких кодов при необходимости
        long currentSize = shortCodeRepository.count();
        int codesToGenerate = desiredShortCodes - (int) currentSize;
        if (codesToGenerate > 0) {
            shortCodeGeneratorService.generateShortCodes((long) codesToGenerate);
        }
    }
}



