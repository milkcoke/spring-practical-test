package kioskapp.common;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiControllerAdvice {
  // 이거는 암기사항
  // @Valid 에서 터지면 BindException 발생

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public ApiResponse<Object> bindException(BindException bindException) {
    var errMessage = bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    return ApiResponse.of(
        HttpStatus.BAD_REQUEST,
        errMessage,
        null
      );
  }
}
