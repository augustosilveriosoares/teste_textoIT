package com.textoit.raspberry.controllers;

import com.textoit.raspberry.models.Award;
import com.textoit.raspberry.models.MovieList;
import com.textoit.raspberry.services.AwardService;
import com.textoit.raspberry.services.MovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

@RestController
public class MovieListController {

    @Autowired
    private MovieListService ms;
    @Autowired
    private AwardService as;

    @GetMapping("/list")
    public Object getAll() throws Exception{

        List<Award> max = as.findMax();
        Collections.sort(max, Comparator.comparingLong(Award::getInterval).reversed());
        List<Award> min = as.findMin();
        Collections.sort(min, Comparator.comparingLong(Award::getInterval));
        Map<String, List<Award>> response = new HashMap<>();
        response.put("max", max);
        response.put("min", min);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/list")
    public ResponseEntity<List<MovieList>> save(@RequestParam("file") MultipartFile file) throws IOException {
       List<MovieList> movieList = ms.persistCsv(file);
       return ResponseEntity.status(HttpStatus.OK).body(movieList);
    }

    @DeleteMapping("list")
    public ResponseEntity<Object> deleteAll(){
        ms.truncate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("A Lista foi limpa");

    }


}