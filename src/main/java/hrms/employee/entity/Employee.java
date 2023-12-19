package hrms.employee.entity;

import java.util.List;

import hrms.employee.dto.EmployeeDto;
import hrms.employee.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, updatable = false)
	private String logonId;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	private boolean enable;

	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private PersonalDetails personalDetails;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LeaveDetails> leaveDetails;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContactDetails> contactDetails;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AddressDetails> addressDetails;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BankDetails> bankDetails;

	public Employee() {
	}

	public Employee(EmployeeDto dto) {
		this.setLogonId(dto.getLogonId());
		this.setPassword(dto.getPassword());
		this.setRole(dto.getRole());
		this.setEnable(dto.isEnable());
	}
}
