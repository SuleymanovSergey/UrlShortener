package com.tsypk.urlshortener.service;

import com.tsypk.urlshortener.entity.ShortCode;
import com.tsypk.urlshortener.repository.ShortCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ShortCodeGeneratorService {

    @Autowired
    private ShortCodeRepository shortCodeRepository;

    public void generateShortCodes(Long count) {
        for (int i = 0; i < count; i++) {
            ShortCode newShortCode = new ShortCode();
            shortCodeRepository.save(newShortCode);
            String generatedCode = generateShortCode(newShortCode.getId()); // Генерируем код на основе идентификатора
            newShortCode.setCode(generatedCode);
            shortCodeRepository.save(newShortCode); // Сохраняем сущность с сгенерированным кодом
        }
    }

    String generateShortCode(Long id) {
        String hexId = Long.toHexString(id).toUpperCase(); // Получаем шестнадцатеричное представление идентификатора и приводим к верхнему регистру
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmss")); // Используем только последние два символа года
        return formattedDateTime + hexId; // Объединяем время и идентификатор
    }
}

