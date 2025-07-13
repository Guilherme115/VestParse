package com.example.vestparse.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BaseText {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;
}
