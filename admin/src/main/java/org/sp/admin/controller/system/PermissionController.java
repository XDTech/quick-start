package org.sp.admin.controller.system;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import org.sp.admin.beans.system.PermissionBean;

import org.sp.admin.model.system.PermissionModel;
import org.sp.admin.service.system.PermissionService;
import org.sp.admin.utils.BeanConverterUtil;
import org.sp.admin.validation.system.PermissionVal;
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
 * (Permission)表控制层
 *
 * @author Tobin
 * @since 2025-01-20 10:12:44
 */
@RestController
@RequestMapping("/permission")
@Validated  //单参数校验时我们需要，在方法的类上加上@Validated注解，否则校验不生效。
public class PermissionController {
    /**
     * 服务对象
     */
    @Resource
    private PermissionService permissionService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping("/page/list")
    public ResponseEntity<?> getPermissionPageList(@RequestParam Integer pi, @RequestParam Integer ps, @RequestParam(required = false) String name) {


        Page<PermissionModel> permissionList = this.permissionService.getPermissionPageList(pi, ps, name);


        List<PermissionBean> permissionBeans = BeanConverterUtil.convertList(permissionList.getContent(), PermissionBean.class);


        return ResponseEntity.ok(ResponseBean.success(permissionList.getTotalElements(), permissionBeans));


    }

    @GetMapping("/list")
    public ResponseEntity<?> getPermissionList(@RequestParam(required = false) String name) {


        List<PermissionModel> permissionList = this.permissionService.getPermissionList(name);


        List<PermissionBean> permissionBeans = BeanConverterUtil.convertList(permissionList, PermissionBean.class);


        return ResponseEntity.ok(ResponseBean.success(Convert.toLong(permissionBeans.size()), permissionBeans));


    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPermission(@PathVariable("id") Long id) {

        Optional<PermissionModel> permissionModel = this.permissionService.getPermission(id);
        if (permissionModel.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }


        PermissionBean permissionBean = new PermissionBean();

        BeanUtil.copyProperties(permissionModel.get(), permissionBean);

        return ResponseEntity.ok(ResponseBean.success(permissionBean));

    }

    /**
     * 新增数据
     *
     * @param permissionVal, 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody @Validated(PermissionVal.Create.class) PermissionVal permissionVal) {


        PermissionModel permission = this.permissionService.getPermissionModel(permissionVal.getIdentity());

        if (ObjectUtil.isNotEmpty(permission)) {
            return ResponseEntity.ok(ResponseBean.fail("权限标识已存在"));
        }

        PermissionModel permissionModel = new PermissionModel();
        BeanUtil.copyProperties(permissionVal, permissionModel);

        this.permissionService.createPermission(permissionModel);


        PermissionBean permissionBean = new PermissionBean();

        BeanUtil.copyProperties(permissionModel, permissionBean);

        return ResponseEntity.ok(ResponseBean.success(permissionBean));
    }

    /**
     * 编辑数据
     *
     * @param permissionVal 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<?> updatePermission(@RequestBody @Validated(PermissionVal.Update.class) PermissionVal permissionVal) {

        Optional<PermissionModel> permissionOptional = this.permissionService.getPermission(permissionVal.getId());
        if (permissionOptional.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }


        PermissionModel permission = this.permissionService.getPermissionModel(permissionVal.getIdentity());

        if (ObjectUtil.isNotEmpty(permission) && !permission.getId().equals(permissionVal.getId())) {
            return ResponseEntity.ok(ResponseBean.fail("权限标识已存在"));
        }


        PermissionModel permissionModel = permissionOptional.get();
        BeanUtil.copyProperties(permissionVal, permissionModel, "createdAt", "updatedAt", "status", "deleted");

        this.permissionService.updatePermission(permissionModel);


        PermissionBean permissionBean = new PermissionBean();

        BeanUtil.copyProperties(permissionModel, permissionBean);

        return ResponseEntity.ok(ResponseBean.success(permissionBean));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermissionById(@PathVariable("id") Long id) {

        Optional<PermissionModel> permissionModel = this.permissionService.getPermission(id);
        if (permissionModel.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }

        this.permissionService.deletePermission(permissionModel.get());
        return ResponseEntity.ok(ResponseBean.success());
    }

}

