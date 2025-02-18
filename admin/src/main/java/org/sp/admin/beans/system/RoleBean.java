package org.sp.admin.beans.system;

import java.util.Date;

import lombok.Data;
import org.sp.admin.enums.StatusEnum;

/**
 * (Role)表实体类
 *
 * @author Tobin
 * @since 2025-01-19 11:30:15
 */
@Data
public class RoleBean {


    private Long id;

    private String identity;

    private String name;

    private Integer sort;

    private Long[] permissions;

    private String remarks;

    private StatusEnum status;


}

