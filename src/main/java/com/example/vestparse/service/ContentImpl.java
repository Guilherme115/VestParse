package com.example.vestparse.service;

import com.example.vestparse.model.*;
import com.example.vestparse.util.GeminiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContentImpl implements ContentService {

    private final QuestionImpl question;
    private final GeminiClient client;

    @Autowired
    public ContentImpl(QuestionImpl question, GeminiClient client) {
        this.question = question;
        this.client = client;
    }

    @Override
    public List<QuestionRelation> contentList(String[] texts) throws Exception {
        List<QuestionRelation> listRelation = new ArrayList<>();

        for (String text : texts) {
            if (text == null || text.isBlank()) {
                continue;
            }

            QuestionRelation relation = new QuestionRelation();

            if (question.isQuestion(text)) {
                Pattern pattern = Pattern.compile("^(\\d+)\\s+(.+)$");
                Matcher matcher = pattern.matcher(text);

                if (matcher.matches()) {
                    Question model = new Question();

                    int number = Integer.parseInt(matcher.group(1));
                    String content = matcher.group(2);

                    model.setContent(content);
                    model.setNumber(number);

                    relation.setQuestion(model);

                    Category classification = client.classificationTypeCategory(content);
                    relation.setCategory(classification);
                }

            } else {
                BaseText baseText = new BaseText();
                baseText.setContent(text);
                String title = client.setThemeBasic(baseText.getContent());
                baseText.setTitle(title);

                relation.setBaseText(baseText);
            }

            listRelation.add(relation);
        }

        return listRelation;
    }
}


