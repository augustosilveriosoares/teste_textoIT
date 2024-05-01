package com.textoit.raspberry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textoit.raspberry.models.Award;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RaspberryApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

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
        Assert.assertEquals(1, minNode.size());

        JsonNode maxNode = jsonNode.get("max");
        Assert.assertTrue(maxNode.isArray());
        Assert.assertEquals(1, maxNode.size());

        Award firstMinAward = objectMapper.treeToValue(minNode.get(0), Award.class);
        Assert.assertEquals("Joel Silver", firstMinAward.getProducer());
        Assert.assertEquals(1, (firstMinAward.getInterval().longValue()));
        Assert.assertEquals(1990, firstMinAward.getPreviousWin().longValue());
        Assert.assertEquals(1991, firstMinAward.getFollowingWin().longValue());



        Award firstMaxAward = objectMapper.treeToValue(maxNode.get(0), Award.class);
        Assert.assertEquals("Matthew Vaughn", firstMaxAward.getProducer());
        Assert.assertEquals(13, firstMaxAward.getInterval().longValue());
        Assert.assertEquals(2002, firstMaxAward.getPreviousWin().longValue());
        Assert.assertEquals(2015, firstMaxAward.getFollowingWin().longValue());




    }

}
