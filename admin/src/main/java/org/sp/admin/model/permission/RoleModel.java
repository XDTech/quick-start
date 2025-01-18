package org.sp.admin.model.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.*;

import jakarta.persistence.*;
import org.sp.admin.utils.SnowflakeId;

import java.io.Serializable;
import java.util.Date;

//角色 组表
// 内置一个root角色账户
@Data
@Entity
@Table(name = "role", schema = "public")
@DynamicInsert // 不然@Column 不起作用
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})

public class RoleModel implements Serializable {


    @Id
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", type = SnowflakeId.class)
    private long id;

    @NotBlank
    private String name;

    private int sort;


    @Column(unique = true)
    @NotBlank
    private String identity; // hasRole 标识


    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}