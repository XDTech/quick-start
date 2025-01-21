package org.sp.admin.repository.system;

import org.sp.admin.model.system.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Permission)
 *
 * @author Tobin
 * @since 2025-01-20 09:53:44
 */
@Repository
public interface PermissionRepo extends JpaRepository<PermissionModel, Long>, JpaSpecificationExecutor<PermissionModel> {


    PermissionModel findByIdentity(String identity);
}
