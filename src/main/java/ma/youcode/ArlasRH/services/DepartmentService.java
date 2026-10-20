package ma.youcode.ArlasRH.services;


import ma.youcode.ArlasRH.models.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    /**
     *
     * @param department
     * @return
     */
    Department createDepartement(Department department);

    /**
     *
     * @return
     */
    List<Department> getAllDepartements();

    /**
     *
     * @param id
     * @return
     */
    Optional<Department> getDepartementById(Long id);

    /**
     *
     * @param id
     * @param updatedDepartment
     * @return
     */
    Department updateDepartement(Long id, Department updatedDepartment);

    /**
     *
     * @param id
     */
    void deleteDepartement(Long id);
}

