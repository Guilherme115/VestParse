package com.example.vestparse.service;

import com.example.vestparse.model.BaseText;
import com.example.vestparse.model.Question;
import com.example.vestparse.model.QuestionRelation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExamParserService {

    private static final Pattern QUESTION_PATTERN = Pattern.compile("^\\s*(\\d+)\\s*[.)]?\\s+(.*)");

    public List<QuestionRelation> parseText(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            return new ArrayList<>();
        }

        List<QuestionRelation> relations = new ArrayList<>();
        String[] lines = rawText.split("\\R");
        StringBuilder currentTextBuffer = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (line.isBlank()) {
                continue;
            }

            Matcher matcher = QUESTION_PATTERN.matcher(line);

            if (matcher.matches()) {

                if (!currentTextBuffer.isEmpty()) {
                    relations.add(createBaseTextRelation(currentTextBuffer.toString()));
                    currentTextBuffer.setLength(0);
                }

                ParseResult result = parseMultiLineQuestion(matcher, lines, i);
                relations.add(createQuestionRelation(result.getQuestion()));

                i = result.getNewIndex();

            } else {
                if (!currentTextBuffer.isEmpty()) {
                    currentTextBuffer.append("\n");
                }
                currentTextBuffer.append(line);
            }
        }

        if (!currentTextBuffer.isEmpty()) {
            relations.add(createBaseTextRelation(currentTextBuffer.toString()));
        }

        return relations;
    }

    /**
     * Método auxiliar para ler uma questão que pode ter múltiplas linhas.
     * Ele lê as linhas seguintes até encontrar uma nova questão ou uma linha em branco.
     */
    private ParseResult parseMultiLineQuestion(Matcher firstLineMatcher, String[] allLines, int currentIndex) {
        Question question = new Question();
        question.setNumber(Integer.parseInt(firstLineMatcher.group(1)));

        StringBuilder questionContent = new StringBuilder(firstLineMatcher.group(2).trim());

        int i = currentIndex + 1;
        while (i < allLines.length) {
            String nextLine = allLines[i];

            if (QUESTION_PATTERN.matcher(nextLine).matches()) {
                break; // Encontramos o fim da questão atual.
            }

            if (nextLine.isBlank()) {
                break;
            }

            questionContent.append("\n").append(nextLine.trim());
            i++;
        }

        question.setContent(questionContent.toString());

        return new ParseResult(question, i - 1);
    }

    private QuestionRelation createQuestionRelation(Question question) {
        QuestionRelation relation = new QuestionRelation();
        relation.setQuestions(Collections.singletonList(question));
        return relation;
    }

    private QuestionRelation createBaseTextRelation(String content) {
        BaseText baseText = new BaseText();
        baseText.setContent(content.trim());
        QuestionRelation relation = new QuestionRelation();
        relation.setBaseText(baseText);
        return relation;
    }

    /**
     * Classe auxiliar interna para retornar múltiplos valores do método de parse.
     */
    private static class ParseResult {
        private final Question question;
        private final int newIndex;

        public ParseResult(Question question, int newIndex) {
            this.question = question;
            this.newIndex = newIndex;
        }

        public Question getQuestion() {
            return question;
        }

        public int getNewIndex() {
            return newIndex;
        }
    }
}