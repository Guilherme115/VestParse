package com.example.vestparse.dto;

import com.example.vestparse.model.AvaliableExam;
import com.example.vestparse.model.QuestionRelation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamDTO {

    private Long id;
    private AvaliableExam avaliableExam;
    private int year;
    private List<QuestionRelation> questions;
}

