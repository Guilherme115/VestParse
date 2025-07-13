package com.example.vestparse.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestionRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    private BaseText baseText;

    @ManyToOne(cascade = CascadeType.ALL)
    private Question question;
}
