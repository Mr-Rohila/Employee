package hrms.employee.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse implements Serializable {

	private static final long serialVersionUID = -6458332027183028308L;
	@Builder.Default
	private Date time = Calendar.getInstance().getTime();
	@Builder.Default
	private int status = 200;
	private String message;
	private Object data;

}
