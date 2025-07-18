package com.example.vestparse.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class QuestionRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "base_text_id")
    private BaseText baseText;

    @OneToMany(mappedBy = "questionRelation", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Question> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "proof_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Proof proof;
}