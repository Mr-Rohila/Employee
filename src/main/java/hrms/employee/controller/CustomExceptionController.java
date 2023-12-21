package hrms.employee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import hrms.employee.exception.CSVErrorException;
import hrms.employee.exception.ExceptionResponse;
import hrms.employee.response.GenericErrorResponse;

@RestControllerAdvice
public class CustomExceptionController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public GenericErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return GenericErrorResponse.builder().error("Data Validation Error").status(HttpStatus.BAD_REQUEST.value())
				.data(errors).build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class,
			MultipartException.class, MethodArgumentTypeMismatchException.class })
	public GenericErrorResponse ex(Exception exception) {
		return GenericErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).error(exception.getMessage())
				.build();
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ExceptionResponse.class)
	public GenericErrorResponse runtimeException(ExceptionResponse exception) {
		return GenericErrorResponse.builder().status(HttpStatus.CONFLICT.value()).error(exception.getMessage()).build();
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(CSVErrorException.class)
	public ResponseEntity<byte[]> downloadErrorFile(CSVErrorException exception) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.setContentDispositionFormData("attachment", "textfile.txt");
		return ResponseEntity.status(406).headers(headers).body(exception.getMessage().getBytes());
	}

}