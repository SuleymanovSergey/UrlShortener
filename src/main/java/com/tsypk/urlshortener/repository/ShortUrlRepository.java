package com.tsypk.urlshortener.repository;

import com.tsypk.urlshortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    ShortUrl findByShortUrlCode(String shortUrlCode);

    List<ShortUrl> findByDestroyedAtBefore(Date now);
}
