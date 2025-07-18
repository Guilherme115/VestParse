package com.example.vestparse.service;

import com.example.vestparse.model.*;
import com.example.vestparse.util.GeminiClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContentImpl implements ContentService {

    private final ExamParserService parserService;
    private final ContentGrouperService grouperService;
    private final GeminiClient geminiClient;
    private final QuestionsRepository questionRelationRepository;

    @Autowired
    public ContentImpl(ExamParserService parserService, ContentGrouperService grouperService, GeminiClient geminiClient, QuestionsRepository questionRelationRepository) {
        this.parserService = parserService;
        this.grouperService = grouperService;
        this.geminiClient = geminiClient;
        this.questionRelationRepository = questionRelationRepository;
    }

    @Transactional
    public List<QuestionRelation> processarProvaCompleta(String rawFullText) throws Exception {

        List<QuestionRelation> atomicRelations = parserService.parseText(rawFullText);

        List<QuestionRelation> groupedRelations = grouperService.groupContent(atomicRelations);

        for (QuestionRelation relation : groupedRelations) {
            if (relation.getBaseText() != null) {
                String title = geminiClient.setThemeBasic(relation.getBaseText().getContent());
                relation.getBaseText().setTitle(title);
            }

            for (Question question : relation.getQuestions()) {
                String contextText = (relation.getBaseText() != null) ? relation.getBaseText().getContent() : "";
                Category category = geminiClient.classificationTypeCategory(question.getContent());
                question.setCategory(category);
            }
        }

        List<QuestionRelation> savedRelations = questionRelationRepository.saveAll(groupedRelations);

        return savedRelations;
    }
}

