package com.example.vestparse.service;

import com.example.vestparse.model.BaseText;
import com.example.vestparse.model.Question;
import com.example.vestparse.model.QuestionRelation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContentService {

 List<QuestionRelation> contentList (String[] text) throws Exception;

}
