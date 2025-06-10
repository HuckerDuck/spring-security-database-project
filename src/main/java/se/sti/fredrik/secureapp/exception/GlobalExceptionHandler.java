package se.sti.fredrik.secureapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global hanterare för applikationens undantag.
 * <p>
 * Används för att fånga specifika och generella fel och returnera
 * meningsfulla svar till klienten i ett enhetligt format.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Hanterar undantag för när validering misslyckas
     * @param ex undantaget som kastas vid misslyckanden
     * @return en ResponseEntity mappat med ett message och validation error med HTTP Status 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            errors.put("message", fieldError.getDefaultMessage());
        } else {
            errors.put("message", "Validation failed");
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Hanterar undantag när användaren inte hittas i databasen.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Hanterar undantag när ett användarnamn redan existerar.
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleDuplicateUser(UsernameAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Hanterar undantag när användaren redan har rollen som försöker sättas.
     */
    @ExceptionHandler(RoleAlreadyAssignedException.class)
    public ResponseEntity<Object> handleRoleAlreadyAssigned(RoleAlreadyAssignedException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    /**
     * Hanterar undantag vid ogiltig roll.
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Object> handleInvalidRole(InvalidRoleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    /**
     * Fallback för oväntade fel i systemet.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Oväntat fel: " + ex.getMessage());
    }

    /**
     * Bygger ett enhetligt felmeddelande med HTTP-status, feltyp och tidsstämpel.
     *
     * @param status  HTTP-statuskod
     * @param message Felmeddelande
     * @return ResponseEntity med JSON-innehåll
     */
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> response = Map.of("error", "Unauthorized: Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
