package com.example.vestparse.service;

import com.example.vestparse.model.AnswerKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AnswerKeyService {

    private static final Pattern IGNORE_LINE_PATTERN = Pattern.compile(
            "^(?:\\d+\\s*)*$|" + "(?i).*item.*|(?i).*gabarito.*|(?i).*universidade.*|(?i).*cebraspe.*|(?i).*programa.*|(?i).*aplicacao.*|(?i).*lingua.*|(?i).*brasileiro.*|(?i).*pesquisa.*|(?i).*selecao.*|(?i).*eventos.*|(?i).*subprograma.*|(?i).*pas.*|(?i).*edital.*|(?i).*agosto.*|(?i).*oficiais.*|(?i).*definitivos.*|(?i).*francesa.*|(?i).*espanhola.*|" +
                    "\\d+_PAS\\d+_\\d+_\\d+.*|" +
                    "OBS\\.: ITEM ANULADO\\."

    );

    public List<AnswerKey> parseAnswerKeyText(String rawText) {
        List<AnswerKey> result = new ArrayList<>();

        String cleanedText = rawText
                .toUpperCase()
                .replaceAll("\\(.*?\\)", "")
                .replaceAll("(C|E)(TIPOD)", "$1 $2")
                .replaceAll("TIPOD", "TIPO D")
                .replaceAll("DE", "D E")
                .replaceAll("EE", "E E")
                .replaceAll("EC", "E C")
                .replaceAll("CC", "C C")
                .replaceAll("[\\t ]{2,}", " ")
                .replaceAll("(?m)^\\s+|\\s+$", "");

        String[] lines = cleanedText.split("\\n");
        int questionNumber = 1;

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty() || IGNORE_LINE_PATTERN.matcher(line).matches()) {
                continue;
            }

            String[] tokens = line.split("\\s+");
            List<String> validTokensInLine = new ArrayList<>();

            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i].trim();

                if (token.isEmpty()) {
                    continue;
                }

                if (token.equals("TIPO") && i + 1 < tokens.length) {
                    String nextToken = tokens[i + 1].trim();
                    // Permitimos D ou qualquer outra letra após TIPO
                    if (nextToken.matches("[A-Z]")) {
                        validTokensInLine.add("TIPO " + nextToken);
                        i++;
                        continue;
                    }
                }

                if (token.equals("X") || token.matches("\\d+") || token.matches("[A-E]")) {
                    validTokensInLine.add(token);
                }
            }

            if (validTokensInLine.isEmpty()) {
                continue;
            }

            for (String token : validTokensInLine) {
                result.add(createAnswerKey(questionNumber++, token));
            }
        }
        return result;
    }

    private AnswerKey createAnswerKey(int number, String answer) {
        AnswerKey key = new AnswerKey();
        key.setQuestionNumber(number);
        key.setAnswer(answer);
        return key;
    }
}