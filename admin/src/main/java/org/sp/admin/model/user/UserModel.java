package org.sp.admin.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.LongArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.*;
import org.sp.admin.enums.StatusEnum;
import org.sp.admin.utils.SnowflakeId;

import java.util.Date;


@Data
@Entity
@Table(name = "user", schema = "public")
//, uniqueConstraints = @UniqueConstraint(name = "unique_username_status", columnNames = {"username", "status"}), indexes = {@Index(name = "idx_status", columnList = "status")}
// 1.表名 2.模式
@SQLDelete(sql = "update \"user\"  set deleted = true where id = ?")
//@Where(clause = "deleted != true")
@DynamicInsert
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 忽略  lazy 层级/为空 时候的引用
public class UserModel {

    @Id
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", type = SnowflakeId.class)
    private Long id;

    @NotNull
    private String username;

    @JsonIgnore
    @NotNull
    @Column(nullable = false)
    private String password;


    @JsonIgnore
    @NotNull
    @Column(nullable = false)
    private String salt;// 加密盐

    private String name;// 姓名

    private String avatar; // 头像

    private String email;// 邮箱

    private String phone;// 电话

    private Long departmentId;


    // @see https://www.baeldung.com/java-hibernate-map-postgresql-array
    @Type(value = LongArrayType.class)
    @Column(columnDefinition = "int8[]")
    private Long[] roles;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'normal'")
    private StatusEnum status = StatusEnum.normal;// 状态


    // 是否删除
    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}