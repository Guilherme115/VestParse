package com.example.vestparse.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProofRepository extends JpaRepository<Proof,Long> {

    Optional<Proof> findByKeyExam(String key);

}
