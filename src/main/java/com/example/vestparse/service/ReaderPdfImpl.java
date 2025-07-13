package com.example.vestparse.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ReaderPdfImpl implements ReaderPdfService {

    @Override
    public String extractByPdf(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();

            if (!document.isEncrypted()) {
                return stripper.getText(document);
            } else {
                throw new IOException("Documento criptografado");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String extractByPdfForAskingKey(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();

            if (document.isEncrypted()) {
                throw new IOException("Documento criptografado");
            }

            String fullText = stripper.getText(document);
            String[] lines = fullText.split("\\r?\\n");

            StringBuilder filteredText = new StringBuilder();

            for (int i = 10; i < lines.length; i++) { // Ignora as 10 primeiras
                filteredText.append(lines[i]).append("\n");
            }

            return filteredText.toString().trim();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}