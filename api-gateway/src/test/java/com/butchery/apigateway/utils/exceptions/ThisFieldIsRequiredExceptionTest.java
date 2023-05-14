package com.butchery.apigateway.utils.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThisFieldIsRequiredExceptionTest {

    @Test
    public void testDefaultConstructor() {
        ThisFieldIsRequiredException exception = new ThisFieldIsRequiredException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Duplicate ID found!";
        ThisFieldIsRequiredException exception = new ThisFieldIsRequiredException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        ThisFieldIsRequiredException exception = new ThisFieldIsRequiredException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Duplicate ID found!";
        Throwable cause = new IllegalArgumentException("Invalid ID");
        ThisFieldIsRequiredException exception = new ThisFieldIsRequiredException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}