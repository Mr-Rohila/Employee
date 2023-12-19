package hrms.employee.response;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericErrorResponse {
	@Builder.Default
	private Date time = Calendar.getInstance().getTime();
	private int status;
	private String error;
	private Object data;

}
