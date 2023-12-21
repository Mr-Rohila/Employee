package hrms.employee.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hrms.employee.dto.EmployeeDto;
import hrms.employee.enums.EmployeeTitle;
import hrms.employee.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PersonalDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private EmployeeTitle title;

	private String fullName;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dateOfJoining;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public PersonalDetails() {
	}

	public PersonalDetails(EmployeeDto dto, Employee employee) {
		this.setTitle(dto.getTitle());
		this.setFullName(dto.getFullName());
		this.setGender(dto.getGender());

		if (StringUtils.hasLength(dto.getDateOfJoining()))
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				this.dateOfJoining = dateFormat.parse(dto.getDateOfJoining());
			} catch (ParseException e) {
				this.dateOfJoining = null;
			}
		this.setEmployee(employee);
	}
}
