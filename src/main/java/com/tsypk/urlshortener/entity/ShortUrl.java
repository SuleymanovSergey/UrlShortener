package com.tsypk.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;


@Entity
@Table(name = "ShortUrls")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ShortUrl {

    @Id
    @Column(name = "short_url_code", unique = true, length = 10)
    private String shortUrlCode;

    @Column(name = "original_url", length = 2048)
    private String originalUrl;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "destroyed_at")
    private Date destroyedAt;

    // Конструктор для создания объекта с установкой времени уничтожения через неделю
    public ShortUrl() {
        // Установка даты уничтожения через неделю после создания
        Date now = new Date();
        long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000; // 1 неделя в миллисекундах
        this.destroyedAt = new Date(now.getTime() + oneWeekInMillis);
    }
}
