package hrms.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hrms.employee.dto.EmployeeDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LeaveDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int alowedLeaves;

	private boolean carryForward;

	private int takeLeave;

	private int carryForwardLeave;

	private int totalRemaining;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public LeaveDetails() {
	}

	public LeaveDetails(EmployeeDto dto, Employee employee) {
		this.alowedLeaves = dto.getAlowedLeavesInMonth();
		this.carryForward = dto.isCarryForward();
		this.employee = employee;

	}

	@PrePersist
	public void prePersist() {
		if (carryForward) {
			carryForwardLeave = alowedLeaves - takeLeave;
			carryForwardLeave = Integer.signum(carryForwardLeave) == -1 ? 0 : carryForwardLeave;
		}

	}

	@PreUpdate
	public void preUpdate() {
		if (carryForward) {
			carryForwardLeave = alowedLeaves - takeLeave;
			carryForwardLeave = Integer.signum(carryForwardLeave) == -1 ? 0 : carryForwardLeave;
		}

	}
}
