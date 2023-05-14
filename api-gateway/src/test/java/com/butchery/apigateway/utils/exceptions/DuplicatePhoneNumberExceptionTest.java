package com.butchery.apigateway.utils.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicatePhoneNumberExceptionTest {

    @Test
    public void testDefaultConstructor() {
        DuplicatePhoneNumberException exception = new DuplicatePhoneNumberException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Duplicate ID found!";
        DuplicatePhoneNumberException exception = new DuplicatePhoneNumberException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        DuplicatePhoneNumberException exception = new DuplicatePhoneNumberException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Duplicate ID found!";
        Throwable cause = new IllegalArgumentException("Invalid ID");
        DuplicatePhoneNumberException exception = new DuplicatePhoneNumberException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}