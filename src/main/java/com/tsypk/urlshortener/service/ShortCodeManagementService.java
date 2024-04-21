package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tsypk.urlshortener.repository.ShortCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ShortCodeManagementService {

    @Autowired
    private ShortCodeRepository shortCodeRepository;

    @Autowired
    private ShortCodeGeneratorService shortCodeGeneratorService;

    // Желаемое количество коротких кодов
    private int desiredShortCodes = 100;

    // Метод для проверки и генерации коротких кодов при необходимости
    @Scheduled(fixedRate = 6000)
    public void checkAndGenerateShortCodesIfNeeded() {
        long currentSize = shortCodeRepository.count();
        int codesToGenerate = desiredShortCodes - (int) currentSize;
        if (codesToGenerate > 0) {
            ShortCode newShortCode = new ShortCode();
            shortCodeRepository.save(newShortCode); // Сохраняем сущность перед вызовом генерации кодов
            shortCodeGeneratorService.generateShortCodes((long) codesToGenerate);
        }
    }

    @Scheduled(fixedRate = 6000)
    public void penis() {
        System.out.println("HUI_PIZDA");
    }
}



