package com.example.vestparse.service;

import com.example.vestparse.excpetion.ContentEmptyException;
import com.example.vestparse.excpetion.InvalidDocumentException;
import com.example.vestparse.model.AvaliableExam;
import com.example.vestparse.model.Proof;
import com.example.vestparse.model.ProofRepository;
import com.example.vestparse.model.QuestionRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.InterruptedNamingException;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessQuestions {

    private static final Logger logger = LoggerFactory.getLogger(ProcessQuestions.class);

    // O serviço principal só precisa conhecer quem lê o PDF e quem processa o conteúdo.
    private final ReaderPdfImpl readerPdf;
    private final ContentImpl content;
    private final ProofRepository repository;

    @Autowired
    public ProcessQuestions(ReaderPdfImpl readerPdf, ContentImpl content, ProofRepository repository) {
        this.readerPdf = readerPdf;
        this.content = content;
        this.repository = repository;
    }

    /**
     * Orquestra a extração de texto de um arquivo PDF e seu processamento completo.
     * Este é o método que o Controller vai chamar.
     *
     * @param arquivoPdf  O arquivo PDF enviado na requisição.
     * @param examName O nome da prova (pode ser usado futuramente).
     * @return A lista de blocos de questões (QuestionRelation) processados e prontos.
     */
    public Proof processarProvaPdf(MultipartFile arquivoPdf, String examName, Integer examYear) {
        if (arquivoPdf.isEmpty()) {
            throw new ContentEmptyException("O arquivo PDF não pode estar vazio.");
        }

        String key = (examName + String.valueOf(examYear));

        Optional<Proof> byKey = repository.findByKeyExam(key);

        if (byKey.isPresent()) {

            return byKey.get();
        }


        logger.info("Iniciando processamento da prova: {}", examName);

        try {
            String rawData = this.readerPdf.extractByPdf(arquivoPdf);
            if (rawData == null || rawData.isBlank()) {
                logger.warn("Nenhum texto foi extraído do PDF: {}", arquivoPdf.getOriginalFilename());
                throw new RuntimeException("Falha ao extrair texto do PDF.");
            }

            List<QuestionRelation> questionRelations = content.processarProvaCompleta(rawData);

            logger.info("Prova '{}' processada com sucesso. {} blocos de conteúdo foram criados.", examName, questionRelations.size());
            Proof proof = new Proof();
            proof.setQuestions(questionRelations);
            proof.setYear(examYear);
            AvaliableExam avaliableExam = selectExam(examName);
            proof.setAvaliableExam(avaliableExam);
            proof.setKeyExam(avaliableExam.name() + examYear);


            return proof;

        } catch (Exception e) {
            logger.error("Erro catastrófico ao processar a prova: {}", examName, e);
            throw new RuntimeException("Erro ao processar a prova: " + e.getMessage(), e);
        }
    }


    private AvaliableExam selectExam(String examName) {
        if (examName == null || examName.isBlank()) {
            throw new IllegalArgumentException("Nome da prova não pode ser vazio.");
        }

        String normalized = examName.trim().replaceAll("\\s+", "").toUpperCase();

        try {
            return AvaliableExam.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de prova inválido: " + examName);
        }
    }
}