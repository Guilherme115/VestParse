package com.example.vestparse.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Question {
    private int number;
    private String statement;
}
