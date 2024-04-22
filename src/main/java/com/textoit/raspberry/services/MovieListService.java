package com.textoit.raspberry.services;


import com.textoit.raspberry.exceptions.CsvHeaderException;
import com.textoit.raspberry.models.MovieList;
import com.textoit.raspberry.repositories.IMovieListRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MovieListService {

    @Autowired
    private IMovieListRepository r;

    public List<MovieList> persistCsv(MultipartFile file) throws CsvHeaderException{

        ArrayList<MovieList> movieList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser p = CSVFormat.RFC4180.withDelimiter(';').builder().setHeader().setSkipHeaderRecord(true).build().parse(br);

            List<String> expectedColumns = Arrays.asList("year", "title", "producers", "studios", "winner");
            String[] header = p.getHeaderNames().toArray(new String[0]);
            if(!Arrays.asList(header).containsAll(expectedColumns)){
                throw new CsvHeaderException("O Cabeçalho do CSV não está de acordo com o padrão");
            }

            r.truncateTable();
            for (CSVRecord record : p) {
                MovieList m = new MovieList();
                m.setYear(Long.valueOf(record.get("year")));
                m.setTitle(record.get("title"));
                m.setStudios(record.get("studios"));
                m.setProducers(record.get("producers"));
                m.setWinner((record.get("winner") != null && record.get("winner").equalsIgnoreCase("yes") ? true : false));
                movieList.add(m);
                r.save(m);
            }
        } catch (IOException e) {e.printStackTrace();}
        return movieList;
    }

    public void truncate(){
        r.truncateTable();
    }

}
