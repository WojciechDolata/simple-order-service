package pl.wdolata.simpleorderservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.wdolata.simpleorderservice.api.model.ErrorResponse;
import pl.wdolata.simpleorderservice.domain.exception.CouponExpiredException;
import pl.wdolata.simpleorderservice.domain.exception.CouponNotFoundException;
import pl.wdolata.simpleorderservice.domain.exception.CouponServiceUnavailableException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final String INVALID_REQUEST_MESSAGE = "Invalid request";

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    ResponseEntity<ErrorResponse> invalidRequest() {
        return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", INVALID_REQUEST_MESSAGE);
    }

    @ExceptionHandler(CouponExpiredException.class)
    ResponseEntity<ErrorResponse> couponExpired(CouponExpiredException e) {
        return error(HttpStatus.UNPROCESSABLE_CONTENT, "COUPON_EXPIRED", e.getMessage());
    }

    @ExceptionHandler(CouponNotFoundException.class)
    ResponseEntity<ErrorResponse> couponNotFound(CouponNotFoundException e) {
        return error(HttpStatus.UNPROCESSABLE_CONTENT, "COUPON_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(CouponServiceUnavailableException.class)
    ResponseEntity<ErrorResponse> couponServiceUnavailable(CouponServiceUnavailableException e) {
        return error(HttpStatus.BAD_GATEWAY, "COUPON_SERVICE_UNAVAILABLE", e.getMessage());
    }

    private static ResponseEntity<ErrorResponse> error(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message));
    }
}
