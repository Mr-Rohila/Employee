package hrms.employee.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import hrms.employee.dto.EmployeeDto;
import hrms.employee.entity.Employee;
import hrms.employee.exception.ExceptionResponse;

@Service
public interface EmployeService {

	public EmployeeDto saveEmployee(EmployeeDto employeeDto) throws ExceptionResponse;

	List<Employee> listOfAllEmployee();

	Employee employeeById(Long employeeId) throws ExceptionResponse;

	String csvToEmployeeDto(InputStream inputStream) throws IOException, ExceptionResponse;

	List<String> employeeUploadCSVHeader();
}
