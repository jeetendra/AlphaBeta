package com.jeet.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void add() {
        //arrange

        //act
        Double sum = calculator.add(10.0, 20.0);

        //assert
        assertEquals(30.0, sum);
        assertNotEquals(31, sum);
    }

    @Test
    void divide() {
        Double divided = calculator.divide(10.0, 20.0);

        assertEquals(0.5, divided);
    }

    @Test
    void divide_withZero() {
        Double divided = calculator.divide(30.0, 0.0);

        assertEquals(Double.POSITIVE_INFINITY, divided);
    }

    @Test
    void multiply() {
        Double multiply = calculator.multiply(10.0, 20.0);

        assertEquals(200.0, multiply);
    }
}