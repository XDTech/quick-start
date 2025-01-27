package org.sp.admin.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.sp.admin.model.system.PermissionModel;
import org.sp.admin.model.system.RoleModel;
import org.sp.admin.model.system.RolePermissionModel;
import org.sp.admin.repository.system.RolePermissionRepo;
import org.sp.admin.repository.system.RoleRepo;
import org.sp.admin.utils.BaseUtil;
import org.sp.admin.validation.system.RoleVal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * (Role)服务
 *
 * @author Tobin
 * @since 2025-01-19 11:30:15
 */
@Service
public class RoleService {

    @Resource
    private RoleRepo roleRepo;

    @Resource
    private RolePermissionRepo rolePermissionRepo;


    public List<RolePermissionModel> getRolePermissions(Long roleId) {
        return this.rolePermissionRepo.findByRoleId(roleId);
    }


    @Transactional
    public RoleModel createRole(RoleModel roleModel) {
        return this.roleRepo.save(roleModel);
    }


    @Transactional
    public void createRole(RoleVal roleVal) {
        RoleModel roleModel = new RoleModel();
        BeanUtil.copyProperties(roleVal, roleModel);
        this.createRole(roleModel);
        // 写入对照表

        List<RolePermissionModel> permissionModels = new ArrayList<>();
        for (Long permissionId : roleVal.getPermissionIds()) {
            RolePermissionModel rolePermissionModel = new RolePermissionModel();
            rolePermissionModel.setRoleId(roleModel.getId());
            rolePermissionModel.setPermissionId(permissionId);
            permissionModels.add(rolePermissionModel);
        }

        this.rolePermissionRepo.saveAll(permissionModels);
    }


    @Transactional
    public RoleModel updateRole(RoleModel roleModel, Long[] permissions) {

        // 先把对照表的permission删除
        this.rolePermissionRepo.deleteByRoleId(roleModel.getId());
        this.roleRepo.save(roleModel);


        List<RolePermissionModel> permissionModels = new ArrayList<>();
        for (Long permissionId : permissions) {
            RolePermissionModel rolePermissionModel = new RolePermissionModel();
            rolePermissionModel.setRoleId(roleModel.getId());
            rolePermissionModel.setPermissionId(permissionId);
            permissionModels.add(rolePermissionModel);
        }

        this.rolePermissionRepo.saveAll(permissionModels);

        return roleModel;
    }

    public RoleModel getRole(String identity) {
        return this.roleRepo.findByIdentity(identity);
    }

    @Transactional
    public void deleteRole(RoleModel roleModel) {

        roleModel.setIdentity(BaseUtil.genDeleteName(roleModel.getIdentity()));
        roleModel.setDeleted(true);
        this.roleRepo.save(roleModel);

        // 删除对照表
        this.rolePermissionRepo.deleteByRoleId(roleModel.getId());
    }

//    @Transactional
//    public void deleteRole(Long id) {
//        this.roleRepo.deleteById(id);
//    }


    public Optional<RoleModel> getRole(Long id) {
        return this.roleRepo.findById(id);
    }

    // 分页查询
    public Page<RoleModel> getRolePageList(Integer pi, Integer ps, String name) {
        // Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pi - 1, ps);
        Specification<RoleModel> specification = this.genSpecification(name);
        return this.roleRepo.findAll(specification, pageRequest);

    }
    public List<RoleModel> getRoleList(String name) {

        Specification<RoleModel> specification = this.genSpecification(name);
        return this.roleRepo.findAll(specification);

    }
    public List<RoleModel> getRoleListIn(Long[] roleIds) {

        return this.roleRepo.findByIdIn(roleIds);

    }


    public List<RoleModel> getRoleList(){

        // 查询没有删除的角色
       return this.roleRepo.findByDeleted(false);
    }


    private Specification<RoleModel> genSpecification(String name) {
        return (Root<RoleModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            /**
             *
             */

            // 用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();

            if (!StrUtil.isEmpty(name)) {
                predicatesList.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            // --------------------------------------------
            // 模糊查询
            /**
             if (!StrUtil.isEmpty(username)) {
             predicatesList.add(cb.like(root.get("username"), "%" + username + "%"));
             }
             if (!StrUtil.isEmpty(status)) {
             predicatesList.add(cb.equal(root.get("status"), UserStatusEnum.valueOf(status)));
             }
             **/
            predicatesList.add(cb.equal(root.get("deleted"), false));// 查询没有删除的
            predicatesList.add(cb.notEqual(root.get("identity"), "root"));
            Predicate[] p = new Predicate[predicatesList.size()];
            query.where(predicatesList.toArray(p));
            query.orderBy(cb.desc(root.get("createdAt")));
            query.orderBy(cb.asc(root.get("sort")));
            return query.getGroupRestriction();

        };
    }
}
