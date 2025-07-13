package com.example.vestparse.service;

import org.springframework.web.multipart.MultipartFile;

public interface ReaderPdfService {

    String extractByPdf(MultipartFile file);
    String extractByPdfForAskingKey(MultipartFile file);
}
