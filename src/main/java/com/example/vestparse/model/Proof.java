package com.example.vestparse.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Proof {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nameProof;

    @OneToMany(cascade = CascadeType.ALL)
    private List<QuestionRelation> questions;
}
