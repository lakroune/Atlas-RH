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

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contract contract;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    public Employee() {
    }

    public Employee(String matricule, String firstName, String lastName, String email, String position, Role role, LocalDate dateDeRejoindre, Department departement) {
        this.matricule = matricule;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.role = role;
        this.dateDeRejoindre = dateDeRejoindre;
        this.departement = departement;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}