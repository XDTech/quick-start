package org.sp.admin.beans.system;

import java.util.Date;

import lombok.Data;

/**
 * (RolePermission)表实体类
 *
 * @author Tobin
 * @since 2025-01-20 10:25:31
 */
@Data
public class RolePermissionBean {


    private Long id;

    private Long permissionId;

    private Long roleId;


}

