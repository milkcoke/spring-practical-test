package kioskapp.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T>{
  private final HttpStatus status;
  private final int code;
  private final String message;
  private final T data;

  public ApiResponse(HttpStatus status, String message, T data) {
    this.status = status;
    this.code = status.value();
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
    return new ApiResponse<>(httpStatus, message, data);
  }
  public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
    return of(httpStatus, httpStatus.name(), data);
  }

  // 자주 사용하는 메소드는 팩토리 메소드 구성
  public static <T> ApiResponse<T> ok(T data) {
    return of(HttpStatus.OK, data);
  }

  public static <T> ApiResponse<T> created(T data) {
    return of(HttpStatus.CREATED, data);
  }
}
