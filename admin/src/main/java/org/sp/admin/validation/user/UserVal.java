package org.sp.admin.validation.user;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.sp.admin.enums.StatusEnum;
import org.sp.admin.validation.validator.EnumValidatorInterface;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVal {


    @NotNull(message = "id can not be null", groups = Update.class)
    private Long id;

    @NotBlank(message = "username can not be null", groups = Create.class)
    private String username;


    @NotBlank(message = "password can not be null", groups = Create.class)
    private String password;


    private String name;// 姓名

    private String avatar; // 头像

    private String email;// 邮箱

    private Long[] roles;

    private String phone;// 电话

    private Long departmentId;

    private String postName;

    @NotBlank(message = "status is required")
    @EnumValidatorInterface(enumClass = StatusEnum.class, message = "Invalid status type")
    private String status;

    public interface Create extends Default {

    }

    public interface Update extends Default {

    }

}
