package org.sp.admin.service.system;

import cn.hutool.core.util.StrUtil;
import org.sp.admin.model.system.PermissionModel;
import org.sp.admin.model.system.RoleModel;
import org.sp.admin.repository.system.PermissionRepo;
import org.sp.admin.utils.BaseUtil;
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
 * (Permission)服务
 *
 * @author Tobin
 * @since 2025-01-20 09:53:44
 */
@Service
public class PermissionService {

    @Resource
    private PermissionRepo permissionRepo;


    @Transactional
    public PermissionModel createPermission(PermissionModel permissionModel) {
        return this.permissionRepo.save(permissionModel);
    }

    @Transactional
    public PermissionModel updatePermission(PermissionModel permissionModel) {
        return this.permissionRepo.save(permissionModel);
    }

    @Transactional
    public PermissionModel getPermissionModel(String identity) {
        return this.permissionRepo.findByIdentity(identity);
    }

    @Transactional
    public void deletePermission(PermissionModel permissionModel) {

        permissionModel.setIdentity(BaseUtil.genDeleteName(permissionModel.getIdentity()));
        permissionModel.setDeleted(true);
        this.permissionRepo.save(permissionModel);
    }


    public Optional<PermissionModel> getPermission(Long id) {
        return this.permissionRepo.findById(id);
    }

    // 分页查询
    public Page<PermissionModel> getPermissionPageList(Integer pi, Integer ps, String name) {
        // Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pi - 1, ps);

        return this.permissionRepo.findAll(this.genSpecification(name), pageRequest);

    }

    public List<PermissionModel> getPermissionList(String name) {
        // Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        return this.permissionRepo.findAll(this.genSpecification(name));

    }


    public List<PermissionModel> getPermissionList(List<Long> permissionIds) {

        return this.permissionRepo.findByIdIn(permissionIds);

    }


    public List<PermissionModel> getPermissionListAll(){
        return this.permissionRepo.findAll();
    }


    private Specification<PermissionModel> genSpecification(String name) {
        return (Root<PermissionModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
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
            Predicate[] p = new Predicate[predicatesList.size()];
            query.where(predicatesList.toArray(p));
            query.orderBy(cb.desc(root.get("createdAt")));
            query.orderBy(cb.asc(root.get("sort")));
            return query.getGroupRestriction();

        };
    }
}
