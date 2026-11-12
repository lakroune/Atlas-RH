package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.exceptions.DepartmentAlreadyExistsException;
import ma.youcode.ArlasRH.exceptions.DepartmentDeleteException;
import ma.youcode.ArlasRH.exceptions.DepartmentNotFoundException;
import ma.youcode.ArlasRH.mappers.DepartmentMapper;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    @Description("Retourne une liste vide lorsqu'aucun département n'existe")
    void testGetAllDepartmentsWhenRepositoryIsEmpty() {
        when(departmentRepository.findAll()).thenReturn(List.of());

        List<DepartmentResponse> actualDepartments = departmentService.getAllDepartements();

        assertTrue(actualDepartments.isEmpty());
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

    @Test
    @Description("Crée un département lorsque son nom est disponible")
    void testCreateDepartment() {
        Department departmentToCreate = new Department("Sales", "Sales department");
        Department savedDepartment = new Department("Sales", "Sales department");
        savedDepartment.setId(4L);
        when(departmentRepository.existsByName("Sales")).thenReturn(false);
        when(departmentRepository.save(departmentToCreate)).thenReturn(savedDepartment);

        DepartmentResponse response = departmentService.createDepartement(departmentToCreate);

        assertEquals(4L, response.id());
        assertEquals("Sales", response.name());
        verify(departmentRepository).save(departmentToCreate);
    }

    @Test
    @Description("Refuse la création lorsque le nom du département existe déjà")
    void testCreateDepartmentWithExistingName() {
        Department departmentToCreate = new Department("HR", "Another HR department");
        when(departmentRepository.existsByName("HR")).thenReturn(true);

        assertThrows(
                DepartmentAlreadyExistsException.class,
                () -> departmentService.createDepartement(departmentToCreate)
        );
        verify(departmentRepository, never()).save(departmentToCreate);
    }

    @Test
    @Description("Met à jour le nom et la description d'un département")
    void testUpdateDepartment() {
        Department changes = new Department("People", "People operations");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);

        DepartmentResponse response = departmentService.updateDepartement(1L, changes);

        assertEquals("People", response.name());
        assertEquals("People operations", response.description());
        verify(departmentRepository).save(department);
    }

    @Test
    @Description("Refuse la mise à jour d'un département inexistant")
    void testUpdateDepartmentNotFound() {
        Department changes = new Department("People", "People operations");
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentService.updateDepartement(99L, changes)
        );
        verify(departmentRepository, never()).save(changes);
    }

    @Test
    @Description("Supprime un département sans employés")
    void testDeleteDepartment() {
        department.setEmployees(List.of());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        departmentService.deleteDepartement(1L);

        verify(departmentRepository).delete(department);
    }

    @Test
    @Description("Refuse la suppression d'un département contenant des employés")
    void testDeleteDepartmentWithEmployees() {
        department.setEmployees(List.of(new Employee()));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        assertThrows(
                DepartmentDeleteException.class,
                () -> departmentService.deleteDepartement(1L)
        );
        verify(departmentRepository, never()).delete(department);
    }

    @Test
    @Description("Refuse la suppression d'un département inexistant")
    void testDeleteDepartmentNotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentService.deleteDepartement(99L)
        );
        verify(departmentRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

}
