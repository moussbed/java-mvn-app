package com.mb.javamvnapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

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