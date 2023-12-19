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
public class BankDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fullName;

	private String bankName;

	private String branchName;

	private String ifscCode;

	private String accountNumber;

	private boolean accountStatus;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public BankDetails() {
	}

	public BankDetails(EmployeeDto dto, Employee employee) {
		this.fullName = dto.getNameInBank();
		this.bankName = dto.getBankName();
		this.branchName = dto.getBranchName();
		this.ifscCode = dto.getIfscCode();
		this.accountNumber = dto.getAccountNumber();
		accountStatus = true;
		this.employee = employee;
	}

}
