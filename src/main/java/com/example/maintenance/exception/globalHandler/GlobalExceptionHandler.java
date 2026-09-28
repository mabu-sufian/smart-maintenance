package com.example.maintenance.exception.globalHandler;

        import com.example.maintenance.exception.AlreadyRegisteredException;
        import com.example.maintenance.exception.EmailNotExists;
        import com.example.maintenance.exception.InvalidCredentialException;
        import com.example.maintenance.exception.IssueNotFoundException;
        import com.example.maintenance.exception.response.ErrorResponse;
        import jakarta.servlet.http.HttpServletRequest;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.bind.annotation.RestControllerAdvice;

        import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(IssueNotFoundException.class)
        public ResponseEntity<ErrorResponse>IssueNotFoundHandler(IssueNotFoundException exception, HttpServletRequest request)
        {
                ErrorResponse errorResponse=new ErrorResponse(
                        LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Not Found", exception.getMessage(), request.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
//
//        @ExceptionHandler(IssueNotFoundException.class)
//        public ResponseEntity<String>IssueHandler(IssueNotFoundException ex)
//        {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//        }


        @ExceptionHandler(InvalidCredentialException.class)
        public ResponseEntity<String> handleInvalidCredentia(InvalidCredentialException ex)
        {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ex.getMessage());
        }

        @ExceptionHandler(EmailNotExists.class)
        public ResponseEntity<String>HandleEmailNotExists(EmailNotExists ex)
        {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ex.getMessage());
        }

        @ExceptionHandler(AlreadyRegisteredException.class)
        public ResponseEntity<String>HandleAlreadyRegister(AlreadyRegisteredException ex)
        {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED )
                        .body(ex.getMessage());
        }
}
