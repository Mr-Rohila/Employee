package hrms.employee.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Keys {

	@Value("#{'${employee.csv.header}'.split(',')}")
	private String[] csvHeader;
}
