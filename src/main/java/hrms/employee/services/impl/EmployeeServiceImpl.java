package hrms.employee.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import hrms.employee.dto.EmployeeDto;
import hrms.employee.entity.AddressDetails;
import hrms.employee.entity.BankDetails;
import hrms.employee.entity.ContactDetails;
import hrms.employee.entity.Employee;
import hrms.employee.entity.LeaveDetails;
import hrms.employee.entity.PersonalDetails;
import hrms.employee.enums.EmployeeTitle;
import hrms.employee.enums.Gender;
import hrms.employee.enums.Role;
import hrms.employee.exception.CSVErrorException;
import hrms.employee.exception.ExceptionResponse;
import hrms.employee.repository.EmployeeRepository;
import hrms.employee.services.EmployeService;
import hrms.employee.utils.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeService {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private final EmployeeRepository employeeRepository;
	private final Validator validator;

	private final Keys keys;

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

	@Override
	public String csvToEmployeeDto(InputStream inputStream) throws IOException {

		StringBuffer errorTxt = new StringBuffer();

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

			String[] headers = Arrays.stream(keys.getCsvHeader()).map(String::trim).toArray(String[]::new);

			CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(headers).setIgnoreHeaderCase(true).setTrim(true)
					.build();
			CSVParser csvParser = csvFormat.parse(bufferedReader);

			Iterable<CSVRecord> iterable = csvParser.getRecords();

			int rowCount = 0;
			for (CSVRecord record : iterable) {
				rowCount++;
				if (rowCount == 1) {
					// Validate headers
					try {
						validateHeaders(record.toMap());
						continue;
					} catch (CSVErrorException e) {
						errorTxt.append("\t" + e.getMessage());
						csvParser.close();
						bufferedReader.close();
						return errorTxt.toString();
					}
				}

				try {
					EmployeeDto csvRecordToEmployeeDto = csvRecordToEmployeeDto(record);
					// validate data
					Errors errors = new BeanPropertyBindingResult(csvRecordToEmployeeDto, "employeeDto");
					ValidationUtils.invokeValidator(validator, csvRecordToEmployeeDto, errors);
					if (errors.hasErrors()) {
						final int row = rowCount;
						// get all error and add to error file
						errors.getAllErrors().forEach((error) -> {
							String errorMessage = error.getDefaultMessage();
							errorTxt.append("\t Data Error, row = " + (row - 1) + " : " + errorMessage + " \n");
						});
					} else {
						saveEmployee(csvRecordToEmployeeDto);
					}
				} catch (Exception ex) {
					errorTxt.append("\t Data  Error, row = " + (rowCount - 1) + " : " + ex.getMessage() + "\n");
				}
			} // loop closed
			csvParser.close();
			bufferedReader.close();
		} catch (IOException exception) {
			errorTxt.append("\t" + exception.getMessage()
					+ " :  If you are face same issue multiple time contact Administrator");
		}
		return errorTxt.toString();
	}

	private void validateHeaders(Map<String, String> actualHeaders) throws CSVErrorException {
		// Check if headers are present and compare them with expected headers
		if (actualHeaders == null || !actualHeaders.keySet()
				.equals(actualHeaders.values().stream().map(String::trim).collect(Collectors.toSet()))) {
			throw CSVErrorException.builder()
					.message("CSV file headers do not match the expected headers " + actualHeaders.keySet()).build();
		}
	}

	private EmployeeDto csvRecordToEmployeeDto(CSVRecord csvRecord) {

		EmployeeDto dto = new EmployeeDto();

		// title field
		EmployeeTitle title = getEmployeeTitleFromCSV(csvRecord.get("Title"));
		dto.setTitle(title);

		// name field
		String fullName = csvRecord.get("Full-Name");
		dto.setFullName(fullName);

		// gender field
		Gender gender = getEmployeeGenderFromCsv(csvRecord.get("Gender"));
		dto.setGender(gender);

		// date of joining field
		String dateOfJoining = csvRecord.get("Date-Of-Joining");
		try {
			LocalDate parse = LocalDate.parse(dateOfJoining, DATE_TIME_FORMATTER);
			dto.setDateOfJoining(DATE_TIME_FORMATTER.format(parse));
		} catch (Exception ex) {
			dto.setDateOfJoining("");
		}

		// logon id field
		String logonId = csvRecord.get("LogonId");
		dto.setLogonId(logonId);

		// password field
		String password = csvRecord.get("Password");
		dto.setPassword(password);

		// role field
		Role role = getEmployeeRoleFromCsv(csvRecord.get("Role"));
		dto.setRole(role);

		// email field
		String emailId = csvRecord.get("EmailId");
		dto.setEmailId(emailId);

		// mobile number field
		String mobileNo = csvRecord.get("MobileNo");
		dto.setMobileNo(mobileNo);

		// permanent address field
		String permanentAddress = csvRecord.get("Permanent-Address");
		dto.setPermanentAddress(permanentAddress);

		// communication address field
		String communicationAddress = csvRecord.get("Communication-Address");
		dto.setCommunicationAddress(communicationAddress);

		// allowed leaves in month field
		String alowedLeavesInMonth = csvRecord.get("Alowed-Leaves-In-Month");
		dto.setAlowedLeavesInMonth(Integer.parseInt(alowedLeavesInMonth));

		// carry forward flag
		boolean carryForward = getEmployeeCarryForwardFlgFromCsv(csvRecord.get("Carry-Forward(Yes/No)"));
		dto.setCarryForward(carryForward);

		// Employee name in Bank
		String nameInBank = csvRecord.get("Name-In-Bank");
		dto.setNameInBank(nameInBank);

		// Bank name
		String bankName = csvRecord.get("Bank-Name");
		dto.setBankName(bankName);

		// Bank Branch name
		String branchName = csvRecord.get("Branch-Name");
		dto.setBranchName(branchName);

		// Bank IFSC Code
		String ifscCode = csvRecord.get("Ifsc-Code");
		dto.setIfscCode(ifscCode);

		// Employee Account Number
		String accountNumber = csvRecord.get("Account-Number");
		dto.setAccountNumber(accountNumber);

		return dto;
	}

	private boolean getEmployeeCarryForwardFlgFromCsv(String carryForward) {
		carryForward = carryForward != null ? carryForward.trim() : "No";
		return carryForward.equalsIgnoreCase("YES") || carryForward.startsWith("Y");
	}

	private Role getEmployeeRoleFromCsv(String role) {
		role = role != null ? role.trim() : "Employee";
		if (role.equalsIgnoreCase("ADMIN") || role.startsWith("A"))
			return Role.ADMIN;
		else if (role.equalsIgnoreCase("HR") || role.startsWith("H"))
			return Role.HR;
		else
			return Role.EMPLOYEE;
	}

	private Gender getEmployeeGenderFromCsv(String gender) {
		gender = gender != null ? gender.trim() : "MALE";
		if (gender.equalsIgnoreCase("MALE") || gender.startsWith("M"))
			return Gender.MALE;
		else if (gender.equalsIgnoreCase("FEMALE") || gender.startsWith("F"))
			return Gender.FEMALE;
		else
			return Gender.OTHER;
	}

	private EmployeeTitle getEmployeeTitleFromCSV(String title) {
		title = (title != null) ? title.trim() : "MR";
		if (title.equalsIgnoreCase("MISS"))
			return EmployeeTitle.MISS;
		else if (title.equalsIgnoreCase("MRS"))
			return EmployeeTitle.MRS;
		else
			return EmployeeTitle.MR;
	}

	@Override
	public List<String> employeeUploadCSVHeader() {
		return List.of(keys.getCsvHeader()).stream().map(String::trim).toList();
	}

}
