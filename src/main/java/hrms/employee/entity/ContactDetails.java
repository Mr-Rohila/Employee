package hrms.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hrms.employee.dto.EmployeeDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ContactDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String emailId;

	private String mobileNo;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public ContactDetails() {
	}

	public ContactDetails(EmployeeDto dto, Employee employee) {
		this.emailId = dto.getEmailId();
		this.mobileNo = dto.getMobileNo();
		this.employee = employee;
	}
}
