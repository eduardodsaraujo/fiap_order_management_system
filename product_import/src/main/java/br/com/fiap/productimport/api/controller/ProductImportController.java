package br.com.fiap.productimport.api.controller;

import br.com.fiap.productimport.application.service.ProductImportService;
import br.com.fiap.productimport.domain.model.ImportFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/api/product-import")
@RequiredArgsConstructor
public class ProductImportController {

    private final ProductImportService productImportService;

    @Value("${import-file.input-path}")
    private String directory;

    @PostMapping
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            ImportFile importFile = ImportFile.builder()
                    .name("data.csv")
                    .directory(directory)
                    .bytes(file.getBytes())
                    .build();
            productImportService.execute(importFile);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
