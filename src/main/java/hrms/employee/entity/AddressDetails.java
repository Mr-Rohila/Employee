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
public class AddressDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String permanentAddress;

	private String communicationAddress;

	private boolean bothSame;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public AddressDetails() {
	}

	public AddressDetails(EmployeeDto dto, Employee employee) {
		this.permanentAddress = dto.getPermanentAddress();
		this.bothSame = dto.isBothAddressSame();
		if (!bothSame)
			this.communicationAddress = dto.getCommunicationAddress();
		this.employee = employee;
	}
}
