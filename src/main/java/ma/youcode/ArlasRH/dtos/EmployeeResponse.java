package ma.youcode.ArlasRH.dtos;

import ma.youcode.ArlasRH.models.enums.Role;

import java.time.LocalDate;

public class EmployeeResponse {

    private Long id;
    private String matricule;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private LocalDate dateDeRejoindre;
    private Role role;
    private Long departmentId;

    public EmployeeResponse() {
    }

    public EmployeeResponse(
            Long id,
            String matricule,
            String firstName,
            String lastName,
            String email,
            String position,
            LocalDate dateDeRejoindre,
            Role role,
            Long departmentId) {

        this.id = id;
        this.matricule = matricule;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.dateDeRejoindre = dateDeRejoindre;
        this.role = role;
        this.departmentId = departmentId;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}