package com.example.vestparse.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ReaderPdfImpl implements ReaderPdfService {


    @Override
    public String getoN(String setfile) {
        File file = new File(setfile);
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();


            if (document != null && document.isEncrypted()) {
                String text = stripper.getText(document);

                return text;

            } else {
                System.out.println("Parece que não está se extraindo nada. ");

                return "The document not can be open";
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //Is now, we'll developer a boolean method what gonna to identify if a section is a question or not.

}
