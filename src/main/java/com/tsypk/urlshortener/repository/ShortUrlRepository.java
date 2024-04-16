package com.tsypk.urlshortener.repository;

import com.tsypk.urlshortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    ShortUrl findByShortUrlCode(String shortUrlCode);
}
