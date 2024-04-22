package com.textoit.raspberry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textoit.raspberry.models.Award;
import com.textoit.raspberry.repositories.IMovieListRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RaspberryApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IMovieListRepository mr;

    @Test
    void contextLoads() {
    }

    @BeforeAll
    public static void testCsvFormatAndPersist() throws Exception {
        // Carrega o arquivo CSV
        ClassPathResource resource = new ClassPathResource("data/testmovielist.csv");
        BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));

        String header = reader.readLine();
        String[] columns = header.split(";");

        // Verifica se as colunas esperadas estão presentes
        List<String> expectedColumns = Arrays.asList("year", "title", "producers", "studios", "winner");
        Assert.assertTrue("As colunas do arquivo CSV não correspondem ao esperado.",
                Arrays.asList(columns).containsAll(expectedColumns));
        reader.close();


    }

    @Test
    public void testGetProducersInterval() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/list", String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        Assert.assertTrue(jsonNode.has("min"));
        Assert.assertTrue(jsonNode.has("max"));

        JsonNode minNode = jsonNode.get("min");
        Assert.assertTrue(minNode.isArray());
        Assert.assertEquals(2, minNode.size());

        JsonNode maxNode = jsonNode.get("max");
        Assert.assertTrue(maxNode.isArray());
        Assert.assertEquals(2, maxNode.size());

        Award firstMinAward = objectMapper.treeToValue(minNode.get(0), Award.class);
        Assert.assertEquals("Bo Derek", firstMinAward.getProducer());
        Assert.assertEquals(6, (firstMinAward.getInterval().longValue()));
        Assert.assertEquals(1984, firstMinAward.getPreviousWin().longValue());
        Assert.assertEquals(1990, firstMinAward.getFollowingWin().longValue());

        Award secondMinAward = objectMapper.treeToValue(minNode.get(1), Award.class);
        Assert.assertEquals("Irwin Winkler", secondMinAward.getProducer());
        Assert.assertEquals(6, secondMinAward.getInterval().longValue());
        Assert.assertEquals(1985, secondMinAward.getPreviousWin().longValue());
        Assert.assertEquals(1991, secondMinAward.getFollowingWin().longValue());

        Award firstMaxAward = objectMapper.treeToValue(maxNode.get(0), Award.class);
        Assert.assertEquals("Allan Carr", firstMaxAward.getProducer());
        Assert.assertEquals(20, firstMaxAward.getInterval().longValue());
        Assert.assertEquals(1999, firstMaxAward.getPreviousWin().longValue());
        Assert.assertEquals(2019, firstMaxAward.getFollowingWin().longValue());

        Award secondMaxAward = objectMapper.treeToValue(maxNode.get(1), Award.class);
        Assert.assertEquals("Buzz Feitshans", secondMaxAward.getProducer());
        Assert.assertEquals(20, (secondMaxAward.getInterval().longValue()));
        Assert.assertEquals(1985, secondMaxAward.getPreviousWin().longValue());
        Assert.assertEquals(2005, secondMaxAward.getFollowingWin().longValue());


    }

}
