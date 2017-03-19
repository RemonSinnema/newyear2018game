package name.sinnema.game.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionToHttpStatus {

  @ExceptionHandler
  public ResponseEntity<String> unsupportedOperation(UnsupportedOperationException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .contentType(MediaType.TEXT_PLAIN)
        .body(e.getMessage());
  }

}
