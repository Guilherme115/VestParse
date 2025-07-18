package com.example.vestparse.controller;

import com.example.vestparse.model.Proof;
import com.example.vestparse.service.ProcessQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/process-exam")
public class ExamController {

    private final ProcessQuestions processQuestionsService;

    @Autowired
    public ExamController(ProcessQuestions processQuestionsService) {
        this.processQuestionsService = processQuestionsService;
    }

    @PostMapping("/processar")
    public ResponseEntity<?> uploadEProcessarProva(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("nomeProva") String nomeProva,
            @RequestParam("ano") Integer anoDaProva) {

        try {
            // Validações básicas pra não dar merda
            if (arquivo.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(criarErroResponse("Arquivo não pode estar vazio, porra!"));
            }

            if (!arquivo.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest()
                        .body(criarErroResponse("Só aceito PDF, caralho! Arquivo: " + arquivo.getOriginalFilename()));
            }

            if (nomeProva == null || nomeProva.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(criarErroResponse("Nome da prova é obrigatório!"));
            }

            if (anoDaProva == null || anoDaProva < 1900 || anoDaProva > 2030) {
                return ResponseEntity.badRequest()
                        .body(criarErroResponse("Ano da prova tá fudido! Deve ser entre 1900 e 2030"));
            }

            // Log pra debug
            System.out.println("Processando arquivo: " + arquivo.getOriginalFilename());
            System.out.println("Tamanho: " + arquivo.getSize() + " bytes");
            System.out.println("Nome da prova: " + nomeProva);
            System.out.println("Ano: " + anoDaProva);

            // Chama o service
            Proof resultado = processQuestionsService.processarProvaPdf(arquivo, nomeProva, anoDaProva);

            if (resultado == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(criarErroResponse("Deu merda no processamento, resultado veio null"));
            }

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            // Log do erro completo
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarErroResponse("Erro interno: " + e.getMessage()));
        }
    }

    private Map<String, String> criarErroResponse(String mensagem) {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", mensagem);
        erro.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return erro;
    }

    // Endpoint de teste pra ver se tá funcionando
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Controller funcionando, porra!");
    }
}