package com.textoit.raspberry.services;

import com.textoit.raspberry.exceptions.CsvHeaderException;
import com.textoit.raspberry.exceptions.InvalidFileFormatException;
import com.textoit.raspberry.models.Movie;
import com.textoit.raspberry.models.Producer;
import com.textoit.raspberry.models.Recommendation;
import com.textoit.raspberry.repositories.IMovieRepository;
import com.textoit.raspberry.repositories.IProducerRepository;
import com.textoit.raspberry.repositories.IRecommendationRepository;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvPersistService {

    @Autowired
    private IProducerRepository producerRepository;
    @Autowired
    private IMovieRepository movieRepository;
    @Autowired
    private IRecommendationRepository recommendationRepository;

    private CSVParser parser;

    public List<Recommendation> persistCsv(MultipartFile file) throws InvalidFileFormatException, IOException {
        truncate();
        CSVValidator.validate(file);
        persistProducers(file);
        persistMovies(file);
        persistRecommendations(file);
        return recommendationRepository.findAll();
    }

    public void persistProducers(MultipartFile file) throws CsvHeaderException, IOException {
        List<Producer> producerList = getProducersFromCsv(file);
        producerRepository.saveAll(producerList);
    }

    public void persistMovies(MultipartFile file) throws CsvHeaderException, IOException {
        List<Movie> movieList = getMoviesFromCsv(file);
        movieRepository.saveAll(movieList);
    }

    public void persistRecommendations(MultipartFile file) throws CsvHeaderException, IOException {
        List<Recommendation> recommendationList = getRecommendationsFromCsv(file);
        recommendationRepository.saveAll(recommendationList);

    }

    private List<Recommendation> getRecommendationsFromCsv(MultipartFile file) throws IOException {
        List<Recommendation> recommendationList = new ArrayList<>();

        for (CSVRecord record : CSVUtils.getRecordsFromFile(file)) {
            Recommendation recommendation = new Recommendation();
            recommendation.setYear(Long.valueOf(record.get(CSVValidator.YEAR)));
            recommendation.setMovie(movieRepository.findByTitle(record.get(CSVValidator.TITLE)));
            recommendation.setWinner((record.get(CSVValidator.WINNER) != null && record.get(CSVValidator.WINNER).equalsIgnoreCase("yes") ? true : false));
            recommendationList.add(recommendation);

        }
        return recommendationList;

    }

    public List<Producer> getProducersFromCsv(MultipartFile file) throws CsvHeaderException, IOException {

        List<Producer> producersList = new ArrayList<>();
        Set<String> producers = new HashSet<>();

        for (CSVRecord record : CSVUtils.getRecordsFromFile(file)) {

            String producerColumn = record.get(CSVValidator.PRODUCERS);
            for (String producer : getProducerNameFromColumn(producerColumn)) {
                producers.add(producer);
            }
        }
        for (String p: producers) {
            Producer producer = new Producer();
            producer.setName(p);
            producersList.add(producer);
        }
        return producersList;
    }

    private List<Movie> getMoviesFromCsv(MultipartFile file) throws IOException {
        List<Movie> movieList = new ArrayList<>();

        for (CSVRecord record : CSVUtils.getRecordsFromFile(file)) {
            Movie movie = new Movie();
            movie.setTitle(record.get(CSVValidator.TITLE));
            movie.setStudio(record.get(CSVValidator.STUDIOS));
            movie.setProducers(getProducersFromColumn(record.get(CSVValidator.PRODUCERS)));
            movieList.add(movie);
        }
        return movieList;
    }

    private List<Producer> getProducersFromColumn(String producerColumn) {

        List<Producer> producersList = new ArrayList<>();
        for (String name : getProducerNameFromColumn(producerColumn)) {
            producersList.add(producerRepository.findByName(name.trim()));
        }
        return producersList;
    }

    public Set<String> getProducerNameFromColumn(String producerColumn) {

        Set<String> producers = new HashSet<>();
        producerColumn = producerColumn.replace(" and ", ",");

        if (producerColumn.contains(",")) {
            String[] producersMovie = producerColumn.split(",");
            for (String producer : producersMovie) {
                if (!producer.trim().isEmpty()) {
                    producers.add(producer.trim());

                }
            }
        } else {
            if (!producerColumn.trim().isEmpty()) {
                producers.add(producerColumn.trim());
            }
        }
        return producers;
    }

    public List<Recommendation> persistCsvOnLoad(MultipartFile file) throws InvalidFileFormatException, IOException {

        persistProducers(file);
        persistMovies(file);
        persistRecommendations(file);
        return recommendationRepository.findAll();
    }


    public void truncate() {
        recommendationRepository.truncateTable();
        movieRepository.truncateTable();
        producerRepository.truncateTable();
    }
}
