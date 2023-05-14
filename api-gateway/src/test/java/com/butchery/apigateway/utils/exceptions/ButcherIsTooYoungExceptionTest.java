package com.butchery.apigateway.utils.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ButcherIsTooYoungExceptionTest {

    @Test
    public void testDefaultConstructor() {
        ButcherIsTooYoungException exception = new ButcherIsTooYoungException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Duplicate ID found!";
        ButcherIsTooYoungException exception = new ButcherIsTooYoungException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        ButcherIsTooYoungException exception = new ButcherIsTooYoungException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Duplicate ID found!";
        Throwable cause = new IllegalArgumentException("Invalid ID");
        ButcherIsTooYoungException exception = new ButcherIsTooYoungException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}