package org.sp.admin.validation;

import java.util.Date;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import org.sp.admin.enums.StatusEnum;
import org.sp.admin.validation.validator.EnumValidatorInterface;

/**
 * (Role)表验证类
 *
 * @author Tobin
 * @since 2025-01-19 11:30:15
 */
@Data
public class RoleVal {

    //@NotBlank(message = "username can not be null")
    //private String username;

    @NotNull(message = "id can not be null", groups = Update.class)
    private Long id;


    @NotBlank(message = "identity can not be null")
    private String identity;

    @NotBlank(message = "name can not be null")
    private String name;

    private Integer sort;

    private String remarks;

    private Long[] permissionIds;


    @NotBlank(message = "status is required")
    @EnumValidatorInterface(enumClass = StatusEnum.class, message = "Invalid status type")
    private String status;

    public interface Create extends Default {

    }

    public interface Update extends Default {

    }

}

