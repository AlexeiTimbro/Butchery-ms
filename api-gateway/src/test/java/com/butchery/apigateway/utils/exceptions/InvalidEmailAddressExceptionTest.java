package com.butchery.apigateway.utils.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidEmailAddressExceptionTest {

    @Test
    public void testDefaultConstructor() {
        InvalidEmailAddressException exception = new InvalidEmailAddressException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Duplicate ID found!";
        InvalidEmailAddressException exception = new InvalidEmailAddressException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        InvalidEmailAddressException exception = new InvalidEmailAddressException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Duplicate ID found!";
        Throwable cause = new IllegalArgumentException("Invalid ID");
        InvalidEmailAddressException exception = new InvalidEmailAddressException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}