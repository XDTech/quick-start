package org.sp.admin.controller.system;


import cn.hutool.core.util.ObjectUtil;
import org.sp.admin.beans.system.RoleBean;

import org.sp.admin.model.system.RoleModel;
import org.sp.admin.service.system.RoleService;
import org.sp.admin.utils.BeanConverterUtil;
import org.sp.admin.validation.RoleVal;
import org.sp.admin.beans.ResponseBean;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

/**
 * (Role)表控制层
 *
 * @author Tobin
 * @since 2025-01-19 12:11:20
 */
@RestController
@RequestMapping("/role")
@Validated  //单参数校验时我们需要，在方法的类上加上@Validated注解，否则校验不生效。
public class RoleController {
    /**
     * 服务对象
     */
    @Resource
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping("/page/list")
    public ResponseEntity<?> getRolePageList(@RequestParam Integer pi, @RequestParam Integer ps) {


        Page<RoleModel> roleList = this.roleService.getRolePageList(pi, ps);


        List<RoleBean> roleBeans = BeanConverterUtil.convertList(roleList.getContent(), RoleBean.class);


        return ResponseEntity.ok(ResponseBean.success(roleList.getTotalElements(), roleBeans));


    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable("id") Long id) {

        Optional<RoleModel> roleModel = this.roleService.getRole(id);
        if (roleModel.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }

        RoleBean roleBean = new RoleBean();

        BeanUtil.copyProperties(roleModel.get(), roleBean);

        return ResponseEntity.ok(ResponseBean.success(roleBean));

    }

    /**
     * 新增数据
     *
     * @param roleVal, 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody @Validated(RoleVal.Create.class) RoleVal roleVal) {


        RoleModel role = this.roleService.getRole(roleVal.getIdentity());
        if (ObjectUtil.isNotEmpty(role)) {
            return ResponseEntity.ok(ResponseBean.fail("标识已经存在"));
        }


        RoleModel roleModel = new RoleModel();
        BeanUtil.copyProperties(roleVal, roleModel);

        this.roleService.createRole(roleModel);


        RoleBean roleBean = new RoleBean();

        BeanUtil.copyProperties(roleBean, roleBean);

        return ResponseEntity.ok(ResponseBean.success(roleBean));
    }

    /**
     * 编辑数据
     *
     * @param roleVal 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<?> updateRole(@RequestBody @Validated(RoleVal.Update.class) RoleVal roleVal) {

        Optional<RoleModel> roleOptional = this.roleService.getRole(roleVal.getId());
        if (roleOptional.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }

        RoleModel role = this.roleService.getRole(roleVal.getIdentity());
        if (ObjectUtil.isNotEmpty(role) && !role.getId().equals(roleVal.getId())) {
            return ResponseEntity.ok(ResponseBean.fail("标识已经存在"));
        }


        RoleModel roleModel = roleOptional.get();
        BeanUtil.copyProperties(roleVal, roleModel, "createdAt", "updatedAt", "status", "deleted");

        this.roleService.updateRole(roleModel);

        RoleBean roleBean = new RoleBean();

        BeanUtil.copyProperties(roleBean, roleBean);

        return ResponseEntity.ok(ResponseBean.success(roleBean));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("id") Long id) {

        Optional<RoleModel> roleModel = this.roleService.getRole(id);
        if (roleModel.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }

        this.roleService.deleteRole(roleModel.get());
        return ResponseEntity.ok(ResponseBean.success());
    }

}

