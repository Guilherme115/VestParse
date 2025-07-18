package com.example.vestparse.service;

import com.example.vestparse.model.QuestionRelation;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ContentGrouperService {


    public List<QuestionRelation> groupContent(List<QuestionRelation> atomicRelations);

}
