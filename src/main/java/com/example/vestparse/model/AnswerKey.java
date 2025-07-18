package com.example.vestparse.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class AnswerKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int questionNumber;

    private String answer;

    @OneToOne
    @JoinColumn(name = "question_id", unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Question question;
}