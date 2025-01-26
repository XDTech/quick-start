package org.sp.admin.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (MUser)表实体类
 *
 * @author Tobin
 * @since 2024-11-10 10:05:25
 */
@Data
public class UserBean implements Serializable {

    private static final long serialVersionUID = -62648420112215297L;


    private Long id;

    private String avatar;

    private String email;

    private String name;


    private String phone;


    private String username;

    private Long[] roles;

    private List<String> roleNames = new ArrayList<>();

    private Long departmentId;

    private String departmentName;

    private String status;

    private String postName;


}

