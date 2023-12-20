package hrms.employee.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hrms.employee.dto.EmployeeDto;
import hrms.employee.exception.ExceptionResponse;
import hrms.employee.response.GenericResponse;
import hrms.employee.services.EmployeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

}
