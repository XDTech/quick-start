package org.sp.admin.controller.system;


import cn.hutool.core.convert.Convert;
import org.sp.admin.beans.DepartmentBean;

import org.sp.admin.model.system.DepartmentModel;
import org.sp.admin.service.system.DepartmentService;
import org.sp.admin.validation.system.DepartmentVal;
import org.sp.admin.beans.ResponseBean;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.bean.BeanUtil;

import java.util.Optional;

import org.sp.admin.utils.BeanConverterUtil;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import jakarta.annotation.Resource;

/**
 * (Department)表控制层
 *
 * @author Tobin
 * @since 2025-01-22 21:32:55
 */
@RestController
@RequestMapping("/department")
@Validated  //单参数校验时我们需要，在方法的类上加上@Validated注解，否则校验不生效。
public class DepartmentController {
    /**
     * 服务对象
     */
    @Resource
    private DepartmentService departmentService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping("/page/list")
    public ResponseEntity<?> getDepartmentPageList(@RequestParam Integer pi, @RequestParam Integer ps) {


        Page<DepartmentModel> departmentList = this.departmentService.getDepartmentPageList(pi, ps, null);


        List<DepartmentBean> departmentBeans = BeanConverterUtil.convertList(departmentList.getContent(), DepartmentBean.class);


        return ResponseEntity.ok(ResponseBean.success(departmentList.getTotalElements(), departmentBeans));


    }

    @GetMapping("/list")
    public ResponseEntity<?> getDepartmentList(@RequestParam(required = false) String name) {


        List<DepartmentModel> departmentList = this.departmentService.getDepartmentList(name);


        List<DepartmentBean> departmentBeans = BeanConverterUtil.convertList(departmentList, DepartmentBean.class);


        return ResponseEntity.ok(ResponseBean.success(Convert.toLong(departmentBeans.size()), departmentBeans));


    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartment(@PathVariable("id") Long id) {

        Optional<DepartmentModel> departmentModel = this.departmentService.getDepartment(id);
        if (departmentModel.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }


        DepartmentBean departmentBean = new DepartmentBean();

        BeanUtil.copyProperties(departmentModel.get(), departmentBean);

        return ResponseEntity.ok(ResponseBean.success(departmentBean));

    }

    /**
     * 新增数据
     *
     * @param departmentVal, 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<?> createDepartment(
            @RequestBody @Validated(DepartmentVal.Create.class) DepartmentVal departmentVal
    ) {


        DepartmentModel departmentModel = new DepartmentModel();
        BeanUtil.copyProperties(departmentVal, departmentModel);

        this.departmentService.createDepartment(departmentModel);


        DepartmentBean departmentBean = new DepartmentBean();

        BeanUtil.copyProperties(departmentModel, departmentBean);

        return ResponseEntity.ok(ResponseBean.success(departmentBean));
    }

    /**
     * 编辑数据
     *
     * @param departmentVal 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<?> updateDepartment(
            @RequestBody @Validated(DepartmentVal.Update.class) DepartmentVal departmentVal
    ) {

        Optional<DepartmentModel> departmentOptional = this.departmentService.getDepartment(departmentVal.getId());
        if (departmentOptional.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }

        DepartmentModel departmentModel = departmentOptional.get();
        BeanUtil.copyProperties(departmentVal, departmentModel, "createdAt", "updatedAt", "deleted");

        this.departmentService.updateDepartment(departmentModel);


        DepartmentBean departmentBean = new DepartmentBean();

        BeanUtil.copyProperties(departmentModel, departmentBean);

        return ResponseEntity.ok(ResponseBean.success(departmentBean));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartmentById(@PathVariable("id") Long id) {

        Optional<DepartmentModel> departmentModel = this.departmentService.getDepartment(id);
        if (departmentModel.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("数据不存在"));
        }

        this.departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ResponseBean.success());
    }

}

