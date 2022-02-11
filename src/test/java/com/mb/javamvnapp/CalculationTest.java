package com.mb.javamvnapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class CalculationTest {

    @Test
    void additionTest(){
        final Calculation calculation = new Calculation();
        final int actualValue = calculation.add(1, 3);
        final int expectedValue = 4;
        assertThat(actualValue).isEqualTo(expectedValue);
    }
    
    @Test
    void additionWithNeutralElementTest(){
        final Calculation calculation = new Calculation();
        final int actualValue = calculation.add(1, 0);
        final int expectedValue = 1;
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test()
    public void givenJsonStringWithExtra_WhenDeserializing_thenException() throws JsonProcessingException {
        String json = "{\"id\":1,\"name\":\"John\",\"checked\":true,\"gender\":\"MALE\"}";

        final ObjectMapper objectMapper = new ObjectMapper();
        final User user = objectMapper.readValue(json, User.class);
        System.out.println(user);

        Set<User> users1 = new HashSet<>();
        users1.add(new User(1,"John", Gender.MALE));

        Set<User> users2 = new HashSet<>();
        users2.add(new User(1,"John", Gender.MALE));

        assertThat(users1).isEqualTo(users2);
        assertThat(users1).contains(new User(1,"John", Gender.MALE));

    }
}
