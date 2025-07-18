package com.example.vestparse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Proof {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AvaliableExam avaliableExam;

    @Min(2008)
    @Max(2030)
    private int year;

    @Column(name = "proof_key")
    private String keyExam;

    @OneToMany(mappedBy = "proof", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<QuestionRelation> questions = new ArrayList<>();
}