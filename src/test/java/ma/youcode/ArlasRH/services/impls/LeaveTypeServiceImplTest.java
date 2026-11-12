package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.exceptions.LeaveTypeNotFoundException;
import ma.youcode.ArlasRH.models.LeaveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveTypeServiceImplTest {

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    private LeaveTypeServiceImpl leaveTypeService;

    @BeforeEach
    void setUp() {
        leaveTypeService = new LeaveTypeServiceImpl(leaveTypeRepository);
    }

    @Test
    @Description("Créer un type de congé")
    void testCreateLeaveType() {
        LeaveType leaveType = leaveType(1L, "Annual", 22);
        when(leaveTypeRepository.save(leaveType)).thenReturn(leaveType);

        LeaveTypeResponse response = leaveTypeService.createLeaveType(leaveType);

        assertEquals("Annual", response.getName());
        assertEquals(22, response.getDaysPerYear());
    }

    @Test
    @Description("Retourner tous les types de congé")
    void testGetAllLeaveTypes() {
        when(leaveTypeRepository.findAll()).thenReturn(List.of(
                leaveType(1L, "Annual", 22),
                leaveType(2L, "Sick", 10)));

        List<LeaveTypeResponse> responses = leaveTypeService.getAllLeaveTypes();

        assertEquals(2, responses.size());
        assertEquals("Annual", responses.get(0).getName());
        assertEquals("Sick", responses.get(1).getName());
    }

    @Test
    @Description("Retourner une liste vide sans types de congé")
    void testGetAllLeaveTypesWhenEmpty() {
        when(leaveTypeRepository.findAll()).thenReturn(List.of());

        assertTrue(leaveTypeService.getAllLeaveTypes().isEmpty());
    }

    @Test
    @Description("Récupérer un type de congé par son identifiant")
    void testGetLeaveTypeById() {
        LeaveType leaveType = leaveType(1L, "Annual", 22);
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(leaveType));

        LeaveTypeResponse response = leaveTypeService.getLeaveTypeById(1L);

        assertEquals("Annual", response.getName());
    }

    @Test
    @Description("Refuser la recherche d'un type de congé inexistant")
    void testGetLeaveTypeByIdNotFound() {
        when(leaveTypeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                LeaveTypeNotFoundException.class,
                () -> leaveTypeService.getLeaveTypeById(99L)
        );
    }

    @Test
    @Description("Supprimer un type de congé existant")
    void testDeleteLeaveType() {
        when(leaveTypeRepository.existsById(1L)).thenReturn(true);

        leaveTypeService.deleteLeaveType(1L);

        verify(leaveTypeRepository).deleteById(1L);
    }

    @Test
    @Description("Refuser la suppression d'un type de congé inexistant")
    void testDeleteLeaveTypeNotFound() {
        when(leaveTypeRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                LeaveTypeNotFoundException.class,
                () -> leaveTypeService.deleteLeaveType(99L)
        );
        verify(leaveTypeRepository, never()).deleteById(99L);
    }

    @Test
    @Description("Documenter la mise à jour non implémentée")
    void testUpdateLeaveTypeCurrentlyReturnsNull() {
        assertNull(leaveTypeService.updateLeaveType(1L, new LeaveType("Annual", 25)));
    }

    private LeaveType leaveType(Long id, String name, int daysPerYear) {
        LeaveType leaveType = new LeaveType(name, daysPerYear);
        leaveType.setId(id);
        return leaveType;
    }
}
