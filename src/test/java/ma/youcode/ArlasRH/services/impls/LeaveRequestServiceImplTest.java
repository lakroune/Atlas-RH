package ma.youcode.ArlasRH.services.impls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.exceptions.LeaveRequestAlreadyCancelledException;
import ma.youcode.ArlasRH.exceptions.LeaveRequestAlreadyExistException;
import ma.youcode.ArlasRH.exceptions.LeaveRequestCannotBeCancelledException;
import ma.youcode.ArlasRH.exceptions.LeaveRequestNotFoundException;
import ma.youcode.ArlasRH.exceptions.LeaveTypeNotFoundException;
import ma.youcode.ArlasRH.mappers.LeaveRequestCreateMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceImplTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private LeaveTypeRepository leaveTypeRepository;
    @Mock
    private LeaveRequestCreateMapper leaveRequestCreateMapper;

    @InjectMocks
    private LeaveRequestServiceImpl leaveRequestService;

    @Test
    void testCalculJoursOuvresAvecSucces() {
        Long employeeId = 1L;
        Long leaveTypeId = 2L;
        Employee employee = new Employee();
        LeaveType leaveType = new LeaveType("Congé annuel", 25);
        LeaveRequesCreatetRequest request = new LeaveRequesCreatetRequest(
                LocalDate.of(2026, 11, 9), LocalDate.of(2026, 11, 15),
                "Vacances", leaveTypeId);
        LeaveRequest entity = new LeaveRequest();
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        LeaveRequestRespense expectedResponse = new LeaveRequestRespense();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(leaveTypeRepository.findById(leaveTypeId)).thenReturn(Optional.of(leaveType));
        when(leaveRequestCreateMapper.toEntity(request)).thenReturn(entity);
        when(leaveRequestRepository.save(entity)).thenReturn(entity);
        when(leaveRequestCreateMapper.toResponse(entity)).thenReturn(expectedResponse);

        LeaveRequestRespense actual = leaveRequestService.createLeaveRequest(employeeId, request);

        assertSame(expectedResponse, actual);
        assertEquals(5, entity.getWorkingDay());
        assertEquals(LeaveStatus.EN_ATTENTE, entity.getStatus());
        verify(leaveRequestRepository).save(entity);
    }

    @Test
    void testCalculJoursOuvresAvecPeriodeInvalide() {
        Long employeeId = 1L;
        Long leaveTypeId = 2L;
        LeaveRequesCreatetRequest request = new LeaveRequesCreatetRequest(
                LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 9),
                "Période invalide", leaveTypeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));
        when(leaveTypeRepository.findById(leaveTypeId))
                .thenReturn(Optional.of(new LeaveType("Congé annuel", 25)));

        assertThrows(IllegalArgumentException.class,
                () -> leaveRequestService.createLeaveRequest(employeeId, request));

        verify(leaveRequestRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void testCreationDemandeEmployeIntrouvable() {
        LeaveRequesCreatetRequest request = request(2L);
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> leaveRequestService.createLeaveRequest(99L, request));
        verify(leaveRequestRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void testCreationDemandeEnAttenteDejaExistante() {
        LeaveRequesCreatetRequest request = request(2L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
        when(leaveRequestRepository.existsByEmployee_IdAndStatus(1L, LeaveStatus.EN_ATTENTE))
                .thenReturn(true);

        assertThrows(LeaveRequestAlreadyExistException.class,
                () -> leaveRequestService.createLeaveRequest(1L, request));
        verify(leaveTypeRepository, never()).findById(2L);
    }

    @Test
    void testCreationDemandeTypeCongeIntrouvable() {
        LeaveRequesCreatetRequest request = request(99L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
        when(leaveTypeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LeaveTypeNotFoundException.class,
                () -> leaveRequestService.createLeaveRequest(1L, request));
        verify(leaveRequestRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void testCreationConserveUnStatutDejaDefini() {
        LeaveRequesCreatetRequest request = request(2L);
        LeaveRequest entity = new LeaveRequest();
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setStatus(LeaveStatus.APPROUVEE);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
        when(leaveTypeRepository.findById(2L)).thenReturn(Optional.of(new LeaveType("Annual", 22)));
        when(leaveRequestCreateMapper.toEntity(request)).thenReturn(entity);
        when(leaveRequestRepository.save(entity)).thenReturn(entity);

        leaveRequestService.createLeaveRequest(1L, request);

        assertEquals(LeaveStatus.APPROUVEE, entity.getStatus());
    }

    @Test
    void testAnnulationDemandeAvecSucces() {
        Long employeeId = 1L;
        Long requestId = 10L;
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStatus(LeaveStatus.EN_ATTENTE);
        LeaveRequestRespense expectedResponse = new LeaveRequestRespense();

        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(leaveRequestRepository.findById(requestId)).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);
        when(leaveRequestCreateMapper.toResponse(leaveRequest)).thenReturn(expectedResponse);

        LeaveRequestRespense actual = leaveRequestService.cancelLeaveRequest(requestId, employeeId);

        assertSame(expectedResponse, actual);
        assertEquals(LeaveStatus.ANNULEE, leaveRequest.getStatus());
        verify(leaveRequestRepository).save(leaveRequest);
    }

    @Test
    void testAnnulationDemandeApprouveeInterdite() {
        Long employeeId = 1L;
        Long requestId = 10L;
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStatus(LeaveStatus.APPROUVEE);

        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(leaveRequestRepository.findById(requestId)).thenReturn(Optional.of(leaveRequest));

        assertThrows(LeaveRequestCannotBeCancelledException.class,
                () -> leaveRequestService.cancelLeaveRequest(requestId, employeeId));

        verify(leaveRequestRepository, never()).save(leaveRequest);
    }

    @Test
    void testAnnulationDemandeRefuseeInterdite() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStatus(LeaveStatus.REFUSEE);
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(leaveRequestRepository.findById(10L)).thenReturn(Optional.of(leaveRequest));

        assertThrows(LeaveRequestCannotBeCancelledException.class,
                () -> leaveRequestService.cancelLeaveRequest(10L, 1L));
    }

    @Test
    void testAnnulationDemandeDejaAnnulee() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStatus(LeaveStatus.ANNULEE);
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(leaveRequestRepository.findById(10L)).thenReturn(Optional.of(leaveRequest));

        assertThrows(LeaveRequestAlreadyCancelledException.class,
                () -> leaveRequestService.cancelLeaveRequest(10L, 1L));
    }

    @Test
    void testAnnulationEmployeIntrouvable() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class,
                () -> leaveRequestService.cancelLeaveRequest(10L, 99L));
        verify(leaveRequestRepository, never()).findById(10L);
    }

    @Test
    void testAnnulationDemandeIntrouvable() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(leaveRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LeaveRequestNotFoundException.class,
                () -> leaveRequestService.cancelLeaveRequest(99L, 1L));
    }

    @Test
    void testGetAllLeaveRequests() {
        LeaveRequest first = new LeaveRequest();
        LeaveRequest second = new LeaveRequest();
        LeaveRequestRespense firstResponse = new LeaveRequestRespense();
        LeaveRequestRespense secondResponse = new LeaveRequestRespense();
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(leaveRequestRepository.findByEmployee_Id(1L)).thenReturn(List.of(first, second));
        when(leaveRequestCreateMapper.toResponse(first)).thenReturn(firstResponse);
        when(leaveRequestCreateMapper.toResponse(second)).thenReturn(secondResponse);

        assertEquals(List.of(firstResponse, secondResponse), leaveRequestService.getAllLeaveRequest(1L));
    }

    @Test
    void testGetAllLeaveRequestsEmployeIntrouvable() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class,
                () -> leaveRequestService.getAllLeaveRequest(99L));
        verify(leaveRequestRepository, never()).findByEmployee_Id(99L);
    }

    @Test
    void testGetLeaveRequestsByEmployeeCurrentlyReturnsEmptyList() {
        assertTrue(leaveRequestService.getLeaveRequestsByEmployee(1L, LeaveStatus.EN_ATTENTE).isEmpty());
    }

    @Test
    void testGetLeaveRequestByIdCurrentlyReturnsEmptyOptional() {
        assertTrue(leaveRequestService.getLeaveRequestById(10L).isEmpty());
    }

    private LeaveRequesCreatetRequest request(Long leaveTypeId) {
        return new LeaveRequesCreatetRequest(
                LocalDate.of(2026, 11, 9),
                LocalDate.of(2026, 11, 10),
                "Vacation",
                leaveTypeId);
    }
}
