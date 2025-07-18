package com.example.vestparse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface ReaderPdfService {

    String extractByPdf(MultipartFile file);
    String extractByPdfForAskingKey(MultipartFile file);
}
