package org.sp.admin.service.system;

import org.sp.admin.model.system.RoleModel;
import org.sp.admin.repository.system.RoleRepo;
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
 * (Role)服务
 *
 * @author Tobin
 * @since 2025-01-19 11:30:15
 */
@Service
public class RoleService {

    @Resource
    private RoleRepo roleRepo;


    @Transactional
    public RoleModel createRole(RoleModel roleModel) {
        return this.roleRepo.save(roleModel);
    }

    @Transactional
    public RoleModel updateRole(RoleModel roleModel) {
        return this.roleRepo.save(roleModel);
    }

    public RoleModel getRole(String identity) {
        return this.roleRepo.findByIdentity(identity);
    }

    @Transactional
    public void deleteRole(RoleModel roleModel) {

        roleModel.setIdentity(BaseUtil.genDeleteName(roleModel.getIdentity()));
        roleModel.setDeleted(true);
        this.roleRepo.save(roleModel);
    }

//    @Transactional
//    public void deleteRole(Long id) {
//        this.roleRepo.deleteById(id);
//    }


    public Optional<RoleModel> getRole(Long id) {
        return this.roleRepo.findById(id);
    }

    // 分页查询
    public Page<RoleModel> getRolePageList(Integer pi, Integer ps) {
        // Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pi - 1, ps);
        Specification<RoleModel> specification = (Root<RoleModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            /**
             *
             */


            // 用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();
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
            return query.getGroupRestriction();

        };
        return this.roleRepo.findAll(specification, pageRequest);

    }
}
