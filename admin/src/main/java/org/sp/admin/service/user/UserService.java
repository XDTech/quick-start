package org.sp.admin.service.user;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.sp.admin.enums.StatusEnum;
import org.sp.admin.model.system.RoleModel;
import org.sp.admin.model.user.UserModel;
import org.sp.admin.repository.user.UserRepo;
import org.sp.admin.service.system.RoleService;
import org.sp.admin.utils.BaseUtil;
import org.sp.admin.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * (UserModel)服务
 *
 * @author Tobin
 * @since 2024-11-10 10:05:25
 */
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private final SecurityUtils securityUtils = new SecurityUtils();


    @Resource
    private RoleService roleService;


    // 查询账户是否存在

    public UserModel getUser(String username) {
        return this.userRepo.findByUsername(username);
    }

    @Transactional
    public UserModel createMUser(UserModel mUser) {
        return this.userRepo.save(mUser);
    }

    @Transactional
    public UserModel updateMUser(UserModel mUser) {
        return this.userRepo.save(mUser);
    }

    @Transactional
    public void deleteMUser(UserModel mUser) {
        mUser.setUsername(BaseUtil.genDeleteName(mUser.getUsername()));
        mUser.setDeleted(true);
        this.userRepo.save(mUser);
    }


    public Optional<UserModel> getMUser(Long id) {
        return this.userRepo.findById(id);
    }

    // 分页查询
    public Page<UserModel> getMUserPageList(Integer pi, Integer ps, String name) {
        // Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pi - 1, ps);
        Specification<UserModel> specification = (Root<UserModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // 用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();
            // --------------------------------------------
            // 模糊查询
            if (!StrUtil.isEmpty(name)) {
                predicatesList.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            /**
             if (!StrUtil.isEmpty(username)) {
             predicatesList.add(cb.like(root.get("username"), "%" + username + "%"));
             }
             if (!StrUtil.isEmpty(status)) {
             predicatesList.add(cb.equal(root.get("status"), UserStatusEnum.valueOf(status)));
             }
             **/

            predicatesList.add(cb.equal(root.get("deleted"), false));// 查询没有删除的
            predicatesList.add(cb.notEqual(root.get("username"), "root"));
            Predicate[] p = new Predicate[predicatesList.size()];
            query.where(predicatesList.toArray(p));
            query.orderBy(cb.desc(root.get("createdAt")));
            return query.getGroupRestriction();


        };
        return this.userRepo.findAll(specification, pageRequest);

    }

    public UserModel genPwd(UserModel userModel, String newPassword) {
        // 生成加密盐
        String salt = this.securityUtils.createSecuritySalt();

        userModel.setSalt(salt);
        // 生成密码

        String pwd = this.securityUtils.shaEncode(newPassword + salt);

        userModel.setPassword(pwd);

        return userModel;
    }


    public List<String> getUserRoles(UserModel userModel) {

        List<String> roleList = new ArrayList<>();
        if (userModel.getStatus().equals(StatusEnum.locked)) return roleList;

        // 如果是超级管理员账号，默认返回root角色

        if (userModel.getUsername().equals("root")) {
            roleList.add("root");
            return roleList;
        }


        List<RoleModel> roles = this.roleService.getRoleListIn(userModel.getRoles());


        /**
         * toList() 方法在 Java 16 及更高版本中返回的是一个 不可变的列表，
         * 如果你尝试修改这个列表（例如，调用 add()、remove() 等方法），就会抛出 UnsupportedOperationException 异常。
         */

        //   roleList = roles.stream().map(RoleModel::getIdentity).toList();

        roleList = roles.stream().map(RoleModel::getIdentity).collect(Collectors.toList());

        return roleList;
    }


    public void createRootAccount() {
        String accountName = "root";

        RoleModel role = this.roleService.getRole(accountName);

        if (ObjectUtil.isEmpty(role)) {
            role = new RoleModel();

            role.setName(accountName);
            role.setIdentity(accountName);

            this.roleService.createRole(role);

        }
        UserModel root = this.userRepo.findByUsername(accountName);

        if (ObjectUtil.isEmpty(root)) {
            UserModel userModel = new UserModel();

            userModel.setName(accountName);
            userModel.setUsername(accountName);
            // 生成加密盐
            String salt = this.securityUtils.createSecuritySalt();
            userModel.setSalt(salt);
            // 生成密码
            String pwd = this.securityUtils.shaEncode(accountName + salt);

            userModel.setPassword(pwd);
            userModel.setRoles(new Long[]{role.getId()});

            this.userRepo.save(userModel);

        }

    }
}
