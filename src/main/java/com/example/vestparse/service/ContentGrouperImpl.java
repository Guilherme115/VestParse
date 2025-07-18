package com.example.vestparse.service;

import com.example.vestparse.model.QuestionRelation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class ContentGrouperImpl implements ContentGrouperService {

    @Override

    public List<QuestionRelation> groupContent(List<QuestionRelation> atomicRelations) {
        if (atomicRelations == null || atomicRelations.isEmpty()) {
            return new ArrayList<>();
        }

        List<QuestionRelation> finalGroupedList = new ArrayList<>();

        QuestionRelation currentGroup = new QuestionRelation();
        currentGroup.setQuestions(new ArrayList<>());

        for (QuestionRelation atom : atomicRelations) {

            if (atom.getBaseText() != null) {
                if (!currentGroup.getQuestions().isEmpty()) {
                    finalGroupedList.add(currentGroup);
                }

                currentGroup = new QuestionRelation();
                currentGroup.setQuestions(new ArrayList<>());
                currentGroup.setBaseText(atom.getBaseText());

            } else if (atom.getQuestions() != null && !atom.getQuestions().isEmpty()) {
                currentGroup.getQuestions().add(atom.getQuestions().get(0));
            }
        }

        if (currentGroup.getBaseText() != null || !currentGroup.getQuestions().isEmpty()) {
            finalGroupedList.add(currentGroup);
        }

        return finalGroupedList;
    }
}

