package com.textoit.raspberry.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVUtils {

    public static CSVParser getRecordsFromFile(MultipartFile file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser p = CSVFormat.RFC4180.withDelimiter(';').builder().setHeader().setSkipHeaderRecord(true).build().parse(br);
        return p;

    }
}
