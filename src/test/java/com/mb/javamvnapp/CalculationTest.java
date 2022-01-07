package com.mb.javamvnapp;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class CalculationTest {

    @Test
    void additionTest(){
        final Calculation calculation = new Calculation();
        final int actualValue = calculation.add(1, 3);
        final int expectedValue = 4;
        assertThat(actualValue).isEqualTo(expectedValue);
    }
}
