package ma.youcode.ArlasRH.models;

import jakarta.persistence.*;
import ma.youcode.ArlasRH.models.enums.Role;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employes")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String position;

    private LocalDate dateDeRejoindre;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department departement;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequest;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contract> contracts;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    public Employee() {
    }

    public Employee(String matricule, String firstName, String lastName, String email, String position, LocalDate dateDeRejoindre, Role role, Department departement, List<LeaveRequest> leaveRequest, List<Contract> contracts, List<Employee> employees, Employee manager) {
        this.matricule = matricule;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.dateDeRejoindre = dateDeRejoindre;
        this.role = role;
        this.departement = departement;
        this.leaveRequest = leaveRequest;
        this.contracts = contracts;
        this.employees = employees;
        this.manager = manager;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getDateDeRejoindre() {
        return dateDeRejoindre;
    }

    public void setDateDeRejoindre(LocalDate dateDeRejoindre) {
        this.dateDeRejoindre = dateDeRejoindre;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartement() {
        return departement;
    }

    public void setDepartement(Department departement) {
        this.departement = departement;
    }

    public List<LeaveRequest> getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(List<LeaveRequest> leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}