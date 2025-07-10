package com.example.vestparse.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BaseText {
    private String title;
    private String content;

}
