package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.mappers.DepartmentMapper;
import ma.youcode.ArlasRH.models.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    private DepartmentServiceImpl departmentService;
    private List<Department> listDepartments;

    @BeforeEach
    void setUp() {
        departmentService = new DepartmentServiceImpl(
                departmentRepository,
                new DepartmentMapper()
        );
        listDepartments = List.of(
                new Department("HR", "Human Resources"),
                new Department("IT", "Information Technology"),
                new Department("FIN", "Finance")
        );
    }

    @Test
    void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(listDepartments);

        List<DepartmentResponse> actualDepartments =
                departmentService.getAllDepartements();

        assertEquals(3, actualDepartments.size());
        assertEquals("HR", actualDepartments.get(0).name());
        assertEquals("Human Resources", actualDepartments.get(0).description());
    }

}
