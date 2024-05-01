package com.textoit.raspberry.services;


import com.textoit.raspberry.exceptions.CsvHeaderException;
import com.textoit.raspberry.exceptions.InvalidFileFormatException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Service
public  class CSVValidator {

    public static final String YEAR = "year";
    public static final String TITLE = "title";
    public static final String PRODUCERS = "producers";
    public static final String STUDIOS = "studios";
    public static final String WINNER = "winner";

    public static void validate(MultipartFile file) throws InvalidFileFormatException, CsvHeaderException ,  IOException {
        if (!file.getContentType().equals("text/csv") && !file.getContentType().equals("application/vnd.ms-excel")) {
            throw new InvalidFileFormatException("O arquivo fornecido não é CSV.");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser p = CSVFormat.RFC4180.withDelimiter(';').builder().setHeader().setSkipHeaderRecord(true).build().parse(br);
        String[] header = p.getHeaderNames().toArray(new String[0]);

        if(!Arrays.asList(header).containsAll(getExpectedHeaders())){
            throw new CsvHeaderException("O Cabeçalho do CSV não está de acordo com o padrão, o padrão esperado é: "+getExpectedHeaders().toString());
        }
    }
    public static List<String> getExpectedHeaders() {
        return Arrays.asList(YEAR, TITLE, PRODUCERS,STUDIOS,WINNER);
    }
}
