package com.tsypk.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;


@Entity
@Table(name = "ShortUrls")
@Getter
@Setter
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "short_url_sequence")
    @SequenceGenerator(name = "short_url_sequence", sequenceName = "short_url_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;

    @Column(name = "short_url_code", unique = true, nullable = false, length = 10)
    private String shortUrlCode;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
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
