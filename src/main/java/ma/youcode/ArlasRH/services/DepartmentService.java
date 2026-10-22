
package ma.youcode.ArlasRH.services;

import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.models.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    /**
     *
     * @param department
     * @return
     */
    DepartmentResponse createDepartement(Department department);

    /**
     *
     * @return
     */
    List<DepartmentResponse> getAllDepartements();

    /**
     *
     * @param id
     * @return
     */
    DepartmentResponse getDepartementById(Long id);

    /**
     *
     * @param id
     * @param updatedDepartment
     * @return
     */
    DepartmentResponse updateDepartement(Long id, Department updatedDepartment);

    /**
     *
     * @param id
     */
    void deleteDepartement(Long id);
}
