package org.sp.admin.repository.system;


import org.sp.admin.model.system.RolePermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (RolePermission)
 *
 * @author Tobin
 * @since 2025-01-20 10:25:31
 */
@Repository
public interface RolePermissionRepo extends JpaRepository<RolePermissionModel, Long>, JpaSpecificationExecutor<RolePermissionModel> {


    void deleteByRoleId(Long roleId);

    List<RolePermissionModel> findByRoleId(Long roleId);




}
