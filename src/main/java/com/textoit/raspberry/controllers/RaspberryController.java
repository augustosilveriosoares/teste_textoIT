package com.textoit.raspberry.controllers;

import com.textoit.raspberry.exceptions.CsvHeaderException;
import com.textoit.raspberry.exceptions.InvalidFileFormatException;
import com.textoit.raspberry.exceptions.MovieListNotFoundException;
import com.textoit.raspberry.models.Award;
import com.textoit.raspberry.services.AwardService;
import com.textoit.raspberry.services.CsvPersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class RaspberryController {

    @Autowired
    private CsvPersistService csvPersistService;
    @Autowired
    private AwardService as;


    @GetMapping("/list")
    public Object getAll() {

        List<Award> max = as.findMax();
        Collections.sort(max, Comparator.comparingLong(Award::getInterval).reversed());
        try {
            if (max.isEmpty()) {
                throw new MovieListNotFoundException("Nenhum dado salvo , por favor faça upload da lista");
            }
        } catch (MovieListNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        List<Award> min = as.findMin();
        Collections.sort(min, Comparator.comparingLong(Award::getInterval));
        Map<String, List<Award>> response = new HashMap<>();
        response.put("max", max);
        response.put("min", min);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/list")

    public Object save(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(csvPersistService.persistCsv(file));
        } catch (InvalidFileFormatException | CsvHeaderException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("list")
    public ResponseEntity<Object> deleteAll() {
        csvPersistService.truncate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("A Lista foi limpa");

    }


}
