package com.textoit.raspberry.services;

import com.textoit.raspberry.models.MovieList;
import com.textoit.raspberry.repositories.IMovieListRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class CsvReaderService implements CommandLineRunner {

    private static final String CSV_FILE_PATH = "src/main/resources/data/movielist.csv";
    @Autowired
    private IMovieListRepository mr;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("teste");
            InputStream inputStream = new FileInputStream(CSV_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            CSVParser p = CSVFormat.RFC4180.withDelimiter(';').builder().setHeader().setSkipHeaderRecord(true).build().parse(br);
            for (CSVRecord record : p) {
                MovieList m = new MovieList();
                m.setYear(Long.valueOf(record.get("year")));
                m.setTitle(record.get("title"));
                m.setStudios(record.get("studios"));
                m.setProducers(record.get("producers"));
                m.setWinner((record.get("winner") != null && record.get("winner").equalsIgnoreCase("yes") ? true : false));
                mr.save(m);
            }
        } catch (IOException e) {e.printStackTrace();}

    }
}
