package hrms.employee.dto;

import org.hibernate.validator.constraints.Length;

import hrms.employee.entity.AddressDetails;
import hrms.employee.entity.BankDetails;
import hrms.employee.entity.ContactDetails;
import hrms.employee.entity.Employee;
import hrms.employee.entity.LeaveDetails;
import hrms.employee.entity.PersonalDetails;
import hrms.employee.enums.EmployeeTitle;
import hrms.employee.enums.Gender;
import hrms.employee.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeDto {

	@NotEmpty(message = "Logon Id can not be empty")
	@Length(min = 5, max = 15, message = "Logon Id should be minimum 5 characters and max 15 character")
	private String logonId;

	@NotEmpty(message = "Password can not be empty")
	@Length(min = 5, max = 15, message = "Password should be minimum 5 characters and max 15 character")
	private String password;

	@NotNull(message = "Role must be assigned to new added Employee")
	private Role role;

	/* PersonalDetails */
	private EmployeeTitle title;

	@NotBlank(message = "Full Name can not be empty")
	private String fullName;

	@NotNull(message = "Gender can not be empty")
	private Gender gender;

	private String dateOfJoining;

	private boolean enable;
	/* LeaveDetails */
	private int alowedLeavesInMonth;

	private boolean carryForward;

	/* ContactDetails */

	@NotBlank(message = "Email Id can not be empty")
	@Email(message = "Invalid Email Id")
	private String emailId;

	@Length(min = 10, max = 10, message = "Invalid Mobile No")
	private String mobileNo;

	@NotBlank(message = "Parmanent Address can not be empty")
	private String parmanentAddress;

	private String communicationAddress;
	private boolean bothAddressSame;

	@NotBlank(message = "Employee name in bank can not be empty")
	private String nameInBank;

	@NotBlank(message = "Bank name can not empty")
	private String bankName;

	private String branchName;

	@NotBlank(message = "IFSC code can not be empty")
	private String ifscCode;

	@NotBlank(message = "Account Number can not be empty")
	private String accountNumber;

	public Employee createEmployee() {
		return new Employee(this);
	}

	public PersonalDetails createPersonalDetail(Employee employee) {
		return new PersonalDetails(this, employee);
	}

	public LeaveDetails createLeaveDetail(Employee employee) {
		return new LeaveDetails(this, employee);
	}

	public ContactDetails createContactDetail(Employee employee) {
		return new ContactDetails(this, employee);
	}

	public AddressDetails createAddressDetail(Employee employee) {
		return new AddressDetails(this, employee);
	}

	public BankDetails createBankDetail(Employee employee) {
		return new BankDetails(this, employee);
	}

}
