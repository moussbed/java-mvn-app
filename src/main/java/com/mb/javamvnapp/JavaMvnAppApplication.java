package com.mb.javamvnapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.StringJoiner;

@SpringBootApplication
public class JavaMvnAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaMvnAppApplication.class, args);
    }

}

@Service
class Calculation {

    public int add(int i, int i1) {
        return i+i1;
    }
}

@RestController
class HelloController{

    @GetMapping("/")
    public String sayHello(){

        return "Hello world ! ";
    }

}

@JsonRootName("User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
class User{

    public int id;
    public String name;
    public Gender gender;
}

enum Gender {
    MALE,
    FEMALE
}