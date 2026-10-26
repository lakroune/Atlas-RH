package ma.youcode.ArlasRH.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;

import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
@Setter
@Getter
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer workingDay;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;

    private String managerComment;

    private LocalDate decisionDate;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    public LeaveRequest() {
    }

    public LeaveRequest(LocalDate startDate, LocalDate endDate, Integer workingDay, String reason, LeaveStatus status, String managerComment, LocalDate decisionDate, Employee employee, LeaveType leaveType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.workingDay = workingDay;
        this.reason = reason;
        this.status = status;
        this.managerComment = managerComment;
        this.decisionDate = decisionDate;
        this.employee = employee;
        this.leaveType = leaveType;
    }

}