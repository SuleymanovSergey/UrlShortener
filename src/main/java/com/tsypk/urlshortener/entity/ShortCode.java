package com.tsypk.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ShortCodes")
@Getter
@Setter
public class ShortCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "short_code_sequence")
    @SequenceGenerator(name = "short_code_sequence", sequenceName = "short_code_sequence", allocationSize = 1)
    private Long id;

    private String code;
    public ShortCode (){}
}

