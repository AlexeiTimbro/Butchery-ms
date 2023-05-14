package com.butchery.apigateway.utils.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceLessOrEqualToZeroExceptionTest {

    @Test
    public void testDefaultConstructor() {
        PriceLessOrEqualToZeroException exception = new PriceLessOrEqualToZeroException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Duplicate ID found!";
        PriceLessOrEqualToZeroException exception = new PriceLessOrEqualToZeroException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        PriceLessOrEqualToZeroException exception = new PriceLessOrEqualToZeroException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Duplicate ID found!";
        Throwable cause = new IllegalArgumentException("Invalid ID");
        PriceLessOrEqualToZeroException exception = new PriceLessOrEqualToZeroException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}