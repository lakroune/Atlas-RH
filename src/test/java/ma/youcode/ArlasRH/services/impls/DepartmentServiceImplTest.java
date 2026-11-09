package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.exceptions.DepartmentNotFoundException;
import ma.youcode.ArlasRH.mappers.DepartmentMapper;
import ma.youcode.ArlasRH.models.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    private DepartmentServiceImpl departmentService;
    private List<Department> listDepartments;
    private Department department;

    @BeforeEach
    void setUp() {
        departmentService = new DepartmentServiceImpl(
                departmentRepository,
                new DepartmentMapper());

        listDepartments = List.of(
                new Department("HR", "Human Resources"),
                new Department("IT", "Information Technology"),
                new Department("FIN", "Finance"));

        department = new Department("HR", "Human Resources");
        department.setId(1L);
    }

    @Test
    @Description("Test de la récupération de tous les départements")
    void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(listDepartments);

        List<DepartmentResponse> actualDepartments = departmentService.getAllDepartements();

        assertEquals(3, actualDepartments.size());
        assertEquals("HR", actualDepartments.get(0).name());
        assertEquals("Human Resources", actualDepartments.get(0).description());
    }

    @Test
    @Description("Test de la récupération d'un département par son ID")
    void testGetDepartmentById() {
        when(departmentRepository.findById(1L)).thenReturn(java.util.Optional.of(department));
        DepartmentResponse actualDepartment = departmentService.getDepartementById(1L);
        assertEquals("HR", actualDepartment.name());
        assertEquals("Human Resources", actualDepartment.description());
    }

    @Test
    @Description("Test de la récupération d'un département par un ID inexistant")
    void testGetDepartmentByIdNotFound() {
        when(departmentRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentService.getDepartementById(2L)
        );
    }

}
