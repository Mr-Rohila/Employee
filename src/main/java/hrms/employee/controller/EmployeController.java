package hrms.employee.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hrms.employee.dto.EmployeeDto;
import hrms.employee.exception.ExceptionResponse;
import hrms.employee.response.GenericResponse;
import hrms.employee.services.EmployeService;
import hrms.employee.utils.CSVHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("employee")
@RequiredArgsConstructor
public class EmployeController {

	private final EmployeService employeService;

	@GetMapping
	public GenericResponse employeList() {
		return GenericResponse.builder().data(employeService.listOfAllEmployee()).build();
	}

	@PostMapping
	public GenericResponse saveEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws ExceptionResponse {
		employeeDto = employeService.saveEmployee(employeeDto);
		return GenericResponse.builder().status(HttpStatus.CREATED.value()).message("Employee Add Successfully")
				.data(employeeDto).build();
	}

	@GetMapping("/{employeeId}")
	public GenericResponse employeeById(@PathVariable Long employeeId) throws ExceptionResponse {
		return GenericResponse.builder().data(employeService.employeeById(employeeId)).build();
	}

	@GetMapping("test")
	public GenericResponse test() {
		return GenericResponse.builder().message("working successfully").build();
	}

	@PostMapping("csv/upload")
	public GenericResponse csvUpload(@RequestParam MultipartFile file) throws IOException, ExceptionResponse {
		if (CSVHelper.hasCSVFormat(file)) {
			employeService.csvToEmployeeDto(file.getInputStream());
			return GenericResponse.builder().message("CSV Data Upload Successfully").build();
		}
		return GenericResponse.builder().message("Invalid CSV Format").build();
	}

	@GetMapping("csv/header")
	public ResponseEntity<byte[]> downloadCSVHeader() {
		// Create CSV content with headers
		List<String> employeeUploadCSVHeader = employeService.employeeUploadCSVHeader();

		// Convert the content to bytes
		byte[] csvBytes = employeeUploadCSVHeader.toString().getBytes(StandardCharsets.UTF_8);

		// Set headers for the response
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "Employe_CSV_Template.csv");

		// Return the ResponseEntity with the CSV content and headers
		return ResponseEntity.ok().headers(headers).body(csvBytes);
	}
}
