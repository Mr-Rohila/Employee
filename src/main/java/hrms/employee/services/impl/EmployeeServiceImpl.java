package hrms.employee.services.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import hrms.employee.dto.EmployeeDto;
import hrms.employee.entity.AddressDetails;
import hrms.employee.entity.BankDetails;
import hrms.employee.entity.ContactDetails;
import hrms.employee.entity.Employee;
import hrms.employee.entity.LeaveDetails;
import hrms.employee.entity.PersonalDetails;
import hrms.employee.exception.ExceptionResponse;
import hrms.employee.repository.EmployeeRepository;
import hrms.employee.services.EmployeService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeService {

	private final EmployeeRepository employeeRepository;

	@Override
	public EmployeeDto saveEmployee(EmployeeDto employeeDto) throws ExceptionResponse {
		employeeDto.setLogonId(employeeDto.getLogonId().trim());
		// check duplicate logon id
		List<Employee> findByLogonId = this.employeeRepository.findByLogonId(employeeDto.getLogonId());
		if (!findByLogonId.isEmpty())
			throw ExceptionResponse.builder().message("Duplicate Logon Id").build();

		// create employee
		Employee employee = employeeDto.createEmployee();
		PersonalDetails createPersonalDetail = employeeDto.createPersonalDetail(employee);
		employee.setPersonalDetails(createPersonalDetail);

		LeaveDetails createLeaveDetail = employeeDto.createLeaveDetail(employee);
		employee.setLeaveDetails(List.of(createLeaveDetail));

		ContactDetails createContactDetail = employeeDto.createContactDetail(employee);
		employee.setContactDetails(List.of(createContactDetail));

		AddressDetails createAddressDetail = employeeDto.createAddressDetail(employee);
		employee.setAddressDetails(List.of(createAddressDetail));

		BankDetails createBankDetail = employeeDto.createBankDetail(employee);
		employee.setBankDetails(List.of(createBankDetail));

		employeeRepository.save(employee);
		return employeeDto;
	}

	@Override
	public Employee employeeById(Long employeeId) throws ExceptionResponse {
		return employeeRepository.findById(employeeId).orElseThrow(
				() -> ExceptionResponse.builder().message("Employee not found with id " + employeeId).build());
	}

	@Override
	public List<Employee> listOfAllEmployee() {
		return employeeRepository.findAll();
	}

}
