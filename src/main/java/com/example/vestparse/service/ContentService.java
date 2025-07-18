package com.example.vestparse.service;

import com.example.vestparse.model.BaseText;
import com.example.vestparse.model.Question;
import com.example.vestparse.model.QuestionRelation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContentService {

 public List<QuestionRelation> processarProvaCompleta(String rawFullText) throws Exception;
}
