package com.anonymousibex.Agents.of.Revature.exception;

import com.anonymousibex.Agents.of.Revature.dto.ResponseDto;
import com.anonymousibex.Agents.of.Revature.util.ExceptionResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ResponseDto> handleUsernameTaken(UsernameAlreadyExistsException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseDto> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnauthorizedUser(UnauthorizedException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseDto> handleUnkownUser(UsernameNotFoundException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<ResponseDto> handleShortUsername(InvalidUsernameException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ResponseDto> handleInvalidPassword(InvalidPasswordException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionAlreadyExistsException.class)
    public ResponseEntity<ResponseDto> handleExistingSession(SessionAlreadyExistsException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoActiveSessionException.class)
    public ResponseEntity<ResponseDto> handleNoActiveSession(NoActiveSessionException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto> handleAccessDenied(AccessDeniedException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ItemNameExistsException.class)
    public ResponseEntity<ResponseDto> handleItemNameExists(ItemNameExistsException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseDto> handleItemNotFound(NoSuchElementException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoUserResultsFoundException.class)
    public ResponseEntity<ResponseDto> handleNoUserResultsFound(NoUserResultsFoundException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CalamityNotFoundException.class)
    public ResponseEntity<ResponseDto> CalamityNotFoundException(CalamityNotFoundException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScenarioNotFoundException.class)
    public ResponseEntity<ResponseDto> ScenarioNotFoundHandler(ScenarioNotFoundException ex) {
        return ExceptionResponseUtils.buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}