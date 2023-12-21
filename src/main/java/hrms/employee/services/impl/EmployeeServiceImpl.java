package hrms.employee.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
import hrms.employee.utils.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeService {

	private final EmployeeRepository employeeRepository;
	/*
	 * private static final String[] EXPECTED_HEADERS = { "Sno", "District",
	 * "SubDistrict", "FromYear", "ToYear", "FromVolume", "ToVolume", "FromDoc",
	 * "ToDoc" };
	 */

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
	public List<EmployeeDto> csvToEmployeeDto(InputStream inputStream) throws IOException, ExceptionResponse {

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

			String[] headers = Arrays.stream(keys.getCsvHeader()).map(String::trim).toArray(String[]::new);

			CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(headers).setIgnoreHeaderCase(true).setTrim(true)
					.build();
			CSVParser csvParser = csvFormat.parse(bufferedReader);

			// Validate headers
			// validateHeaders(csvParser);

			List<EmployeeDto> list = new ArrayList<>();
			Iterable<CSVRecord> iterable = csvParser.getRecords();

			boolean isHeaderRead = false;
			for (CSVRecord record : iterable) {
				if (!isHeaderRead) {
					isHeaderRead = true;
					validateHeaders(record.toMap());
					continue;
				}
				Map<String, String> map = record.toMap();
				System.out.println(map);
				String string = record.get("District");
				System.out.println(string);
				EmployeeDto csvRecordToEmployeeDto = csvRecordToEmployeeDto(record);

				// validate employee data

				// add to list
				list.add(csvRecordToEmployeeDto);
			}
			csvParser.close();
			bufferedReader.close();
		} catch (IOException exception) {
			throw exception;
		}
		return null;
	}

	private void validateHeaders(Map<String, String> actualHeaders) throws ExceptionResponse {

		// Check if headers are present and compare them with expected headers
		if (actualHeaders == null || !actualHeaders.keySet()
				.equals(actualHeaders.values().stream().map(String::trim).collect(Collectors.toSet()))) {
			throw ExceptionResponse.builder()
					.message("CSV file headers do not match the expected headers " + actualHeaders.keySet()).build();
		}
	}

	private EmployeeDto csvRecordToEmployeeDto(CSVRecord csvRecord) {
		return new EmployeeDto();
	}

	@Override
	public List<String> employeeUploadCSVHeader() {
		return List.of(keys.getCsvHeader()).stream().map(String::trim).toList();
	}

}
