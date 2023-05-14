package com.butchery.apigateway.utils;

import com.butchery.apigateway.utils.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalControllerExceptionHandlerTest {

    private final GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();

    @Test
    public void testHandleNotFoundException() {
        // Mock the WebRequest and Exception objects
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        NotFoundException ex = new NotFoundException("Not found");

        // Call the method being tested
        HttpErrorInfo errorInfo = handler.handleNotFoundException(request, ex);

        // Check the result
        assertEquals(HttpStatus.NOT_FOUND, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Not found", errorInfo.getMessage());
    }

    @Test
    public void testHandleInvalidEmailAddressException() {
        // Set up a mock WebRequest
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        InvalidEmailAddressException ex = new InvalidEmailAddressException("Invalid email address");

        // Call the method being tested
        HttpErrorInfo errorInfo = handler.handleInvalidEmailAddressException(request, ex);

        // Check the result
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Invalid email address", errorInfo.getMessage());
    }

    @Test
    public void testHandleDuplicatePhoneNumberException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        DuplicatePhoneNumberException ex = new DuplicatePhoneNumberException("Duplicate Phone Number");

        HttpErrorInfo errorInfo = handler.handleDuplicatePhoneNumberException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Duplicate Phone Number", errorInfo.getMessage());
    }

    @Test
    public void testHandleButcherIsTooYoungException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        ButcherIsTooYoungException ex = new ButcherIsTooYoungException("Butcher is too young. Must be above or equal to the age of 16");

        HttpErrorInfo errorInfo = handler.handleButcherIsTooYoungException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Butcher is too young. Must be above or equal to the age of 16", errorInfo.getMessage());
    }

    @Test
    public void testHandlePriceLessOrEqualToZeroException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        PriceLessOrEqualToZeroException ex = new PriceLessOrEqualToZeroException("Price is less or equal to 0");

        HttpErrorInfo errorInfo = handler.handlePriceIsLessOrEqualToZeroException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Price is less or equal to 0", errorInfo.getMessage());
    }

    @Test
    public void testHandleThisFieldIsRequiredException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        ThisFieldIsRequiredException ex = new ThisFieldIsRequiredException("Price is less or equal to 0");

        HttpErrorInfo errorInfo = handler.handleThisFieldIsRequiredException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Price is less or equal to 0", errorInfo.getMessage());
    }

}