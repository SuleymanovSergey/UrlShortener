package com.tsypk.urlshortener.repository;

import com.tsypk.urlshortener.entity.ShortCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortCodeRepository extends JpaRepository<ShortCode, Long> {
    ShortCode findFirstByOrderByCodeAsc();

    void delete(ShortCode shortCode);
}

