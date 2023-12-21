package hrms.employee.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionResponse extends Exception {
	private static final long serialVersionUID = -5365791206958726782L;

	private String message;

}
