package org.sp.admin.repository.system;

import org.sp.admin.model.system.DepartmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Department)
 *
 * @author Tobin
 * @since 2025-01-22 21:32:55
 */
@Repository
public interface DepartmentRepo extends JpaRepository<DepartmentModel, Long>, JpaSpecificationExecutor<DepartmentModel> {

}
