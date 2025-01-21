package org.sp.admin.repository.system;

import org.sp.admin.model.system.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * (Role)
 *
 * @author Tobin
 * @since 2025-01-19 11:30:15
 */
@Repository
public interface RoleRepo extends JpaRepository<RoleModel, Long>, JpaSpecificationExecutor<RoleModel> {


    RoleModel findByIdentity(String identity);
}
