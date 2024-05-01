package com.textoit.raspberry.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileInputStream;
import java.io.InputStream;
import java.net.http.HttpHeaders;

@Component
public class CsvReaderService implements CommandLineRunner {

    private static final String CSV_FILE_PATH = "src/main/resources/data/movielist.csv";

    @Autowired
    CsvPersistService csvPersistService;

    @Override
    public void run(String... args) throws Exception {
            InputStream inputStream = new FileInputStream(CSV_FILE_PATH);

            MultipartFile file = new MockMultipartFile("movielist.csv",inputStream);
            csvPersistService.persistCsvOnLoad(file);

    }
}
