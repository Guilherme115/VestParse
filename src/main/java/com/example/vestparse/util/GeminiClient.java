package com.example.vestparse.util;

import com.example.vestparse.model.Category;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GeminiClient {

    @Value("${api.secret.key}")
    private String apiKey;

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    private String getApiUrl() {
        return BASE_URL + apiKey;
    }

    private String sendPromptAndGetResponseText(String prompt) throws Exception {
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonObject content = new JsonObject();
        JsonArray partsArray = new JsonArray();
        partsArray.add(textPart);
        content.add("parts", partsArray);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getApiUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject respostaJson = JsonParser.parseString(response.body()).getAsJsonObject();
        return respostaJson
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString()
                .trim();
    }

    public Category classificationTypeCategory(String question) throws Exception {
        String prompt = "Classifique a seguinte questão em uma das categorias a seguir:\n" +
                "[MATEMATICA, PORTUGUES, HISTORIA, GEOGRAFIA, CIENCIAS, REDACAO]\n\n" +
                "Questão: \"" + question + "\"\n\n" +
                "Responda apenas com o nome da categoria. Nada mais.";

        String resposta = sendPromptAndGetResponseText(prompt).toUpperCase();

        try {
            return Category.valueOf(resposta);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoria inválida recebida da IA: " + resposta);
        }
    }

    public String setThemeBasic(String textoBase) throws Exception {
        String prompt = "Baseado no texto abaixo, defina o tema principal em uma palavra ou frase curta.\n\n" +
                "Texto: \"" + textoBase + "\"\n\n" +
                "Responda apenas com o tema.";

        return sendPromptAndGetResponseText(prompt);
    }
}