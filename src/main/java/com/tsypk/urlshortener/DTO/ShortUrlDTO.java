package com.tsypk.urlshortener.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ShortUrlDTO {
    private String originalUrl;
    private String shortUrlCode;
    private Date createdAt;
    private Date destroyedAt;
}
