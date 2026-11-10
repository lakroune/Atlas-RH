package ma.youcode.ArlasRH.services.impls;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.exceptions.EmployeeAlreadyExistsException;
import ma.youcode.ArlasRH.mappers.EmployeeCreateMapper;
import ma.youcode.ArlasRH.mappers.LeaveRequestCreateMapper;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeCreateMapper employeeCreateMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    @Mock
    private LeaveRequestCreateMapper leaveRequestCreateMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testEmployeeCreationAvecMatriculeExistant() {

        Long departmentId = 1L;
        Department department = new Department("IT", "Information Technology");
        Employee employee = new Employee();

        employee.setMatricule("EMP-001");
        employee.setEmail("employee@gmail.com");

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(department));

        when(employeeRepository.existsByMatricule("EMP-001"))
                .thenReturn(true);

        assertThrows(
                EmployeeAlreadyExistsException.class,
                () -> employeeService.createEmployee(departmentId, employee));

        verify(employeeRepository, never()).save(employee);
    }

    @Test
    void testEmployeeCreationAvecMatriculeNonExistant() {

        Long departmentId = 1L;
        Department department = new Department("IT", "Information Technology");
        Employee employee = new Employee();

        employee.setMatricule("EMP-001");
        employee.setEmail("employee@gmail.com");

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(department));

        when(employeeRepository.existsByMatricule("EMP-001"))
                .thenReturn(false);

        
                

    }

    @Test
    void testEmployeeCreationAvecEmailExistant() {

    }

    @Test
    void testManagerSoiMeme() {

    }

}
