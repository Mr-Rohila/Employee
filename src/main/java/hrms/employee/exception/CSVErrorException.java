package hrms.employee.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class CSVErrorException extends Exception {
	private static final long serialVersionUID = 1406775031721122258L;
	private String message;

}
