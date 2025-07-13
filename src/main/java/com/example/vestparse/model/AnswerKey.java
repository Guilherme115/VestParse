package com.example.vestparse.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AnswerKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int questionNumber;
/*
- Vai re
 */
    private String Answer;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;


}
